package service;

import model.Account;
import model.Client;
import model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankService {
    // Хранилища данных в памяти
    private List<Client> clients = new ArrayList<>();
    private Map<String, Account> accounts = new HashMap<>();
    private List<Transaction> transactions = new ArrayList<>();

    // Счетчики для генерации уникальных ID и номеров счетов
    private int transactionCounter = 1;
    private int accountCounter = 1001;

    // --- ТАСК 2: БИЗНЕС-ЛОГИКА ---

    // 1. Зарегистрировать клиента
    public void addClient(Client client) {
        clients.add(client);
        System.out.println("Клиент успешно добавлен: " + client.getFullName());
    }

    // 2. Открыть счёт клиенту с автогенерацией номера "ACC-1001"
    public void createAccount(int clientId, String type) {
        // Проверяем, существует ли клиент с таким ID
        boolean clientExists = false;
        for (Client c : clients) {
            if (c.getId() == clientId) {
                clientExists = true;
                break;
            }
        }

        if (!clientExists) {
            System.out.println("Ошибка: Клиент с ID " + clientId + " не найден!");
            return;
        }

        String accountNumber = "ACC-" + accountCounter++;
        Account newAccount = new Account(accountNumber, clientId, 0.0, type.toUpperCase());
        accounts.put(accountNumber, newAccount);
        System.out.println("Счет успешно открыт! Номер счета: " + accountNumber);
    }

    // 3. Пополнить счёт
    public void deposit(String accountNumber, double amount) {
        if (amount <= 0) {
            System.out.println("Ошибка: Сумма пополнения должна быть больше нуля!");
            return;
        }
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("Ошибка: Счёт " + accountNumber + " не найден!");
            return;
        }

        account.setBalance(account.getBalance() + amount);

        // Фиксируем транзакцию
        Transaction tx = new Transaction(transactionCounter++, accountNumber, "DEPOSIT", amount, "Пополнение счета");
        transactions.add(tx);
        System.out.println("Счет " + accountNumber + " успешно пополнен на " + amount);
    }

    // 4. Снять деньги
    public void withdraw(String accountNumber, double amount) {
        if (amount <= 0) {
            System.out.println("Ошибка: Сумма снятия должна быть больше нуля!");
            return;
        }
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("Ошибка: Счёт " + accountNumber + " не найден!");
            return;
        }
        if (account.getBalance() < amount) {
            System.out.println("Ошибка: Недостаточно средств на счёте! Текущий баланс: " + account.getBalance());
            return;
        }

        account.setBalance(account.getBalance() - amount);

        // Фиксируем транзакцию
        Transaction tx = new Transaction(transactionCounter++, accountNumber, "WITHDRAW", amount, "Снятие наличных");
        transactions.add(tx);
        System.out.println("Со счета " + accountNumber + " успешно снято " + amount);
    }

    // 5. Перевод между счетами
    public void transfer(String fromAccount, String toAccount, double amount) {
        if (amount <= 0) {
            System.out.println("Ошибка: Сумма перевода должна быть больше нуля!");
            return;
        }
        Account source = accounts.get(fromAccount);
        Account destination = accounts.get(toAccount);

        if (source == null) {
            System.out.println("Ошибка: Счёт отправителя " + fromAccount + " не найден!");
            return;
        }
        if (destination == null) {
            System.out.println("Ошибка: Счёт получателя " + toAccount + " не найден!");
            return;
        }
        if (source.getBalance() < amount) {
            System.out.println("Ошибка: Недостаточно средств для перевода! Баланс: " + source.getBalance());
            return;
        }

        // Списываем у одного, добавляем другому
        source.setBalance(source.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);

        // Фиксируем транзакции для обоих счетов
        Transaction txFrom = new Transaction(transactionCounter++, fromAccount, "TRANSFER", amount, "Перевод на счёт " + toAccount);
        Transaction txTo = new Transaction(transactionCounter++, toAccount, "TRANSFER", amount, "Перевод со счёта " + fromAccount);
        transactions.add(txFrom);
        transactions.add(txTo);

        System.out.println("Перевод " + amount + " со счета " + fromAccount + " на счет " + toAccount + " выполнен успешно!");
    }

    // 6. Показать текущий баланс
    public double getBalance(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("Ошибка: Счёт " + accountNumber + " не найден!");
            return -1;
        }
        return account.getBalance();
    }


    // --- ТАСК 3: АНАЛИТИКА И ПОИСК ---

    // 1. История всех транзакций по номеру счёта
    public List<Transaction> getTransactionHistory(String accountNumber) {
        List<Transaction> history = new ArrayList<>();
        for (Transaction tx : transactions) {
            if (tx.getAccountNumber().equals(accountNumber)) {
                history.add(tx);
            }
        }
        return history;
    }

    // 2. Общий баланс всех счетов клиента по его ID
    public double getTotalBalanceByClientId(int clientId) {
        double total = 0;
        for (Account acc : accounts.values()) {
            if (acc.getClientId() == clientId) {
                total += acc.getBalance();
            }
        }
        return total;
    }

    // 3. Список клиентов с балансом выше заданной суммы
    public List<Client> getClientsWithBalanceAbove(double targetBalance) {
        List<Client> result = new ArrayList<>();
        for (Client client : clients) {
            if (getTotalBalanceByClientId(client.getId()) > targetBalance) {
                result.add(client);
            }
        }
        return result;
    }

    // 4. Клиент с наибольшим суммарным балансом
    public Client getTopClient() {
        if (clients.isEmpty()) return null;

        Client topClient = null;
        double maxBalance = -1;

        for (Client client : clients) {
            double currentBalance = getTotalBalanceByClientId(client.getId());
            if (currentBalance > maxBalance) {
                maxBalance = currentBalance;
                topClient = client;
            }
        }
        return topClient;
    }
}
