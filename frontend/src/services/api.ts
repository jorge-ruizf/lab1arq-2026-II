const API_BASE = import.meta.env.PUBLIC_API_URL ?? '';

export interface Customer {
	id?: number;
	firstName: string;
	lastName: string;
	accountNumber: string;
	balance: number;
}

export interface Transaction {
	id?: number;
	senderAccountNumber: string;
	receiverAccountNumber: string;
	amount: number;
	timestamp?: string;
}

export interface TransferInput {
	senderAccountNumber: string;
	receiverAccountNumber: string;
	amount: number;
	timestamp: string;
}

export class ApiError extends Error {
	readonly status: number;

	constructor(status: number, message: string) {
		super(message);
		this.name = 'ApiError';
		this.status = status;
	}
}

async function request(path: string, init?: RequestInit): Promise<Response> {
	return fetch(`${API_BASE}${path}`, init);
}

async function responseError(res: Response, fallback: string): Promise<ApiError> {
	let message = fallback;
	try {
		const text = await res.text();
		if (text) message = text;
	} catch (_) {}
	return new ApiError(res.status, message);
}

export async function listCustomers(): Promise<Customer[]> {
	const res = await request('/customers');
	if (!res.ok) throw new ApiError(res.status, `Error ${res.status}`);
	return res.json();
}

export async function getCustomer(id: string): Promise<Customer> {
	const res = await request(`/customers/${id}`);
	if (!res.ok) throw await responseError(res, `No existe un cliente con ID ${id}.`);
	return res.json();
}

export async function createCustomer(input: Omit<Customer, 'id'>): Promise<Customer> {
	const res = await request('/customers', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(input),
	});

	if (!res.ok) {
		const fallback = `Error ${res.status} al crear el cliente.`;
		let message = fallback;
		try {
			const body = await res.json();
			message = body?.error || body?.message || fallback;
		} catch (_) {}
		throw new ApiError(res.status, message);
	}

	return res.json();
}

export async function transfer(input: TransferInput): Promise<Transaction> {
	const res = await request('/transactions', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(input),
	});
	if (!res.ok) throw await responseError(res, `Error ${res.status}`);
	return res.json();
}

export async function getTransactionsByAccount(account: string): Promise<Transaction[]> {
	const res = await request(`/transactions/${account}`);
	if (!res.ok) throw await responseError(res, `Error ${res.status}`);
	return res.json();
}
