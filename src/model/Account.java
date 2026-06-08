package model;

public class Account {
    // Приватные поля по ТЗ
    private String accountNumber;
    private int clientId;
    private double balance;
    private String type; // "DEBIT" или "CREDIT"

    // Конструктор
    public Account(String accountNumber, int clientId, double balance, String type) {
        this.accountNumber = accountNumber;
        this.clientId = clientId;
        this.balance = balance;
        this.type = type;
    }

    // Геттеры и сеттеры
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    // Метод toString
    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", clientId=" + clientId +
                ", balance=" + balance +
                ", type='" + type + '\'' +
                '}';
    }
}
