package com.udea.lab1arq.service;

import com.udea.lab1arq.DTO.TransactionDTO;
import com.udea.lab1arq.entity.Customer;
import com.udea.lab1arq.entity.Transaction;
import com.udea.lab1arq.repository.CustomerRepository;
import com.udea.lab1arq.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private TransactionService transactionService;

    private static final String SENDER_ACCOUNT = "1001";
    private static final String RECEIVER_ACCOUNT = "1002";
    private static final LocalDateTime TIMESTAMP = LocalDateTime.of(2026, 10, 12, 10, 30, 0);

    private Customer buildCustomer(Long id, String accountNumber, Double balance) {
        return new Customer(id, accountNumber, "Nombre", "Apellido", balance);
    }

    // ---------------------------------------------------------------
    // transferMoney - happy path
    // ---------------------------------------------------------------

    @Test
    void transferMoney_withValidAccountsAndSufficientBalance_updatesBalancesAndReturnsSavedTransaction() {
        Customer sender = buildCustomer(1L, SENDER_ACCOUNT, 500.0);
        Customer receiver = buildCustomer(2L, RECEIVER_ACCOUNT, 100.0);
        TransactionDTO request = new TransactionDTO(null, SENDER_ACCOUNT, RECEIVER_ACCOUNT, 200.0, TIMESTAMP);

        Transaction persisted = new Transaction(7L, SENDER_ACCOUNT, RECEIVER_ACCOUNT, 200.0, TIMESTAMP);

        when(customerRepository.findByAccountNumber(SENDER_ACCOUNT)).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber(RECEIVER_ACCOUNT)).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(persisted);

        TransactionDTO result = transactionService.transferMoney(request);

        assertEquals(Double.valueOf(300.0), sender.getBalance());
        assertEquals(Double.valueOf(300.0), receiver.getBalance());

        verify(customerRepository).save(sender);
        verify(customerRepository).save(receiver);
        verify(transactionRepository).save(any(Transaction.class));

        assertNotNull(result);
        assertEquals(Long.valueOf(7L), result.getId());
        assertEquals(SENDER_ACCOUNT, result.getSenderAccountNumber());
        assertEquals(RECEIVER_ACCOUNT, result.getReceiverAccountNumber());
        assertEquals(Double.valueOf(200.0), result.getAmount());
        assertEquals(TIMESTAMP, result.getTimestamp());
    }

    // ---------------------------------------------------------------
    // transferMoney - null account numbers
    // ---------------------------------------------------------------

    @Test
    void transferMoney_withNullSenderAccountNumber_throwsIllegalArgumentException() {
        TransactionDTO request = new TransactionDTO(null, null, RECEIVER_ACCOUNT, 100.0, TIMESTAMP);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request));

        assertNotNull(exception.getMessage());
        verify(customerRepository, never()).findByAccountNumber(any());
        verify(customerRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferMoney_withNullReceiverAccountNumber_throwsIllegalArgumentException() {
        TransactionDTO request = new TransactionDTO(null, SENDER_ACCOUNT, null, 100.0, TIMESTAMP);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request));

        assertNotNull(exception.getMessage());
        verify(customerRepository, never()).findByAccountNumber(any());
        verify(customerRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    // ---------------------------------------------------------------
    // transferMoney - missing accounts
    // ---------------------------------------------------------------

    @Test
    void transferMoney_whenSenderAccountDoesNotExist_throwsIllegalArgumentException() {
        TransactionDTO request = new TransactionDTO(null, SENDER_ACCOUNT, RECEIVER_ACCOUNT, 100.0, TIMESTAMP);

        when(customerRepository.findByAccountNumber(SENDER_ACCOUNT)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request));

        assertNotNull(exception.getMessage());
        verify(customerRepository, never()).findByAccountNumber(RECEIVER_ACCOUNT);
        verify(customerRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferMoney_whenReceiverAccountDoesNotExist_throwsIllegalArgumentException() {
        Customer sender = buildCustomer(1L, SENDER_ACCOUNT, 500.0);
        TransactionDTO request = new TransactionDTO(null, SENDER_ACCOUNT, RECEIVER_ACCOUNT, 100.0, TIMESTAMP);

        when(customerRepository.findByAccountNumber(SENDER_ACCOUNT)).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber(RECEIVER_ACCOUNT)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request));

        assertNotNull(exception.getMessage());
        verify(customerRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    // ---------------------------------------------------------------
    // transferMoney - insufficient balance
    // ---------------------------------------------------------------

    @Test
    void transferMoney_whenSenderHasInsufficientBalance_throwsIllegalArgumentException() {
        Customer sender = buildCustomer(1L, SENDER_ACCOUNT, 50.0);
        Customer receiver = buildCustomer(2L, RECEIVER_ACCOUNT, 100.0);
        TransactionDTO request = new TransactionDTO(null, SENDER_ACCOUNT, RECEIVER_ACCOUNT, 100.0, TIMESTAMP);

        when(customerRepository.findByAccountNumber(SENDER_ACCOUNT)).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber(RECEIVER_ACCOUNT)).thenReturn(Optional.of(receiver));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request));

        assertNotNull(exception.getMessage());
        assertEquals(Double.valueOf(50.0), sender.getBalance());
        assertEquals(Double.valueOf(100.0), receiver.getBalance());
        verify(customerRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    // ---------------------------------------------------------------
    // getTransactionsForAccount
    // ---------------------------------------------------------------

    @Test
    void getTransactionsForAccount_withPopulatedList_returnsMappedTransactionDTOs() {
        Transaction first = new Transaction(1L, SENDER_ACCOUNT, RECEIVER_ACCOUNT, 200.0, TIMESTAMP);
        Transaction second = new Transaction(2L, RECEIVER_ACCOUNT, SENDER_ACCOUNT, 50.0, TIMESTAMP.plusDays(1));
        List<Transaction> transactions = List.of(first, second);

        when(transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber(SENDER_ACCOUNT, SENDER_ACCOUNT))
                .thenReturn(transactions);

        List<TransactionDTO> result = transactionService.getTransactionsForAccount(SENDER_ACCOUNT);

        verify(transactionRepository).findBySenderAccountNumberOrReceiverAccountNumber(SENDER_ACCOUNT, SENDER_ACCOUNT);

        assertNotNull(result);
        assertEquals(2, result.size());

        TransactionDTO dto = result.get(0);
        assertEquals(Long.valueOf(1L), dto.getId());
        assertEquals(SENDER_ACCOUNT, dto.getSenderAccountNumber());
        assertEquals(RECEIVER_ACCOUNT, dto.getReceiverAccountNumber());
        assertEquals(Double.valueOf(200.0), dto.getAmount());
        assertEquals(TIMESTAMP, dto.getTimestamp());

        TransactionDTO otherDto = result.get(1);
        assertEquals(Long.valueOf(2L), otherDto.getId());
        assertEquals(RECEIVER_ACCOUNT, otherDto.getSenderAccountNumber());
        assertEquals(SENDER_ACCOUNT, otherDto.getReceiverAccountNumber());
        assertEquals(Double.valueOf(50.0), otherDto.getAmount());
        assertEquals(TIMESTAMP.plusDays(1), otherDto.getTimestamp());
    }

    @Test
    void getTransactionsForAccount_withEmptyList_returnsEmptyList() {
        when(transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber(RECEIVER_ACCOUNT, RECEIVER_ACCOUNT))
                .thenReturn(List.of());

        List<TransactionDTO> result = transactionService.getTransactionsForAccount(RECEIVER_ACCOUNT);

        verify(transactionRepository).findBySenderAccountNumberOrReceiverAccountNumber(RECEIVER_ACCOUNT, RECEIVER_ACCOUNT);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
