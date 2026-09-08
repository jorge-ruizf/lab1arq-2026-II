
CustomerController

Método	Ruta								Body			Respuesta							Descripción
GET		/api/customers						—				200 List<CustomerDTO>				Lista todos los clientes
GET		/api/customers/{id}					—				200 CustomerDTO						Cliente por ID
POST	/api/customers						CustomerDTO		200 CustomerDTO						Crea un cliente (requiere balance)


TransactionController


Método	Ruta								Body			Respuesta							Descripción
POST	/api/transactions					TransactionDTO	200 TransactionDTO / 400 string		Transfiere dinero entre cuentas
GET		/api/transactions/{accountNumber}	—				200 List<TransactionDTO>			Transacciones de una cuenta