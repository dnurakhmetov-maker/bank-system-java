package main;

import model.Client;
import model.Transaction;
import service.BankService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BankService bankService = new BankService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Добро пожаловать в Консольную Банковскую Систему ===");

        while (true) {
            System.out.println("\n--- ГЛАВНОЕ МЕНЮ ---");
            System.out.println("1. Добавить клиента");
            System.out.println("2. Открыть счёт");
            System.out.println("3. Пополнить счёт");
            System.out.println("4. Снять деньги");
            System.out.println("5. Перевод между счетами");
            System.out.println("6. Показать баланс счёта");
            System.out.println("7. История транзакций по счёту");
            System.out.println("8. [Аналитика] Общий баланс клиента по ID");
            System.out.println("9. [Аналитика] Топ-клиент по балансу");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Очистка буфера после nextInt()

            if (choice == 0) {
                System.out.println("Работа программы завершена. До свидания!");
                break;
            }

            switch (choice) {
                case 1:
                    System.out.print("Введите ID клиента (число): ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Введите ФИО клиента: ");
                    String name = scanner.nextLine();
                    System.out.print("Введите номер телефона: ");
                    String phone = scanner.nextLine();

                    Client client = new Client(id, name, phone);
                    bankService.addClient(client);
                    break;

                case 2:
                    System.out.print("Введите ID клиента, для которого открывается счет: ");
                    int accClientId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Введите тип счета (DEBIT или CREDIT): ");
                    String type = scanner.nextLine();

                    bankService.createAccount(accClientId, type);
                    break;

                case 3:
                    System.out.print("Введите номер счета (например, ACC-1001): ");
                    String depAcc = scanner.nextLine();
                    System.out.print("Введите сумму пополнения: ");
                    double depAmount = scanner.nextDouble();

                    bankService.deposit(depAcc, depAmount);
                    break;

                case 4:
                    System.out.print("Введите номер счета: ");
                    String withAcc = scanner.nextLine();
                    System.out.print("Введите сумму снятия: ");
                    double withAmount = scanner.nextDouble();

                    bankService.withdraw(withAcc, withAmount);
                    break;

                case 5:
                    System.out.print("Введите номер счета-отправителя: ");
                    String fromAcc = scanner.nextLine();
                    System.out.print("Введите номер счета-получателя: ");
                    String toAcc = scanner.nextLine();
                    System.out.print("Введите сумму перевода: ");
                    double transferAmount = scanner.nextDouble();

                    bankService.transfer(fromAcc, toAcc, transferAmount);
                    break;

                case 6:
                    System.out.print("Введите номер счета: ");
                    String balAcc = scanner.nextLine();
                    double balance = bankService.getBalance(balAcc);
                    if (balance != -1) {
                        System.out.println("Текущий баланс счета " + balAcc + ": " + balance);
                    }
                    break;

                case 7:
                    System.out.print("Введите номер счета для просмотра истории: ");
                    String histAcc = scanner.nextLine();
                    List<Transaction> history = bankService.getTransactionHistory(histAcc);

                    if (history.isEmpty()) {
                        System.out.println("Транзакций по этому счету пока не было.");
                    } else {
                        System.out.println("=== История транзакций для " + histAcc + " ===");
                        for (Transaction tx : history) {
                            System.out.println(tx);
                        }
                    }
                    break;

                case 8:
                    System.out.print("Введите ID клиента: ");
                    int totalClientId = scanner.nextInt();
                    double totalBalance = bankService.getTotalBalanceByClientId(totalClientId);
                    System.out.println("Общий баланс всех счетов клиента с ID " + totalClientId + ": " + totalBalance);
                    break;

                case 9:
                    Client topClient = bankService.getTopClient();
                    if (topClient == null) {
                        System.out.println("В базе пока нет клиентов.");
                    } else {
                        System.out.println("Самый богатый клиент: " + topClient.getFullName());
                        System.out.println("Его общий баланс: " + bankService.getTotalBalanceByClientId(topClient.getId()));
                    }
                    break;

                default:
                    System.out.println("Неверный пункт меню. Попробуйте еще раз.");
            }
        }
        scanner.close();
    }
}
