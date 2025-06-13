import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

abstract class Account {
    protected int accountNumber;
    protected String accountHolderName;
    protected double balance;
    protected String password;
    protected List<String> transactionHistory;

    public Account(int accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.password = generatePassword();
        this.transactionHistory = new ArrayList<>();
        recordTransaction("Account created", initialBalance);
    }

    private String generatePassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-_+=<>?";
        Random random = new Random();
        StringBuilder password = new StringBuilder(8);
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));
        String allChars = upperCase + lowerCase + digits + specialChars;
        for (int i = 4; i < 8; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        return password.toString();
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getPassword() {
        return password;
    }

    private void recordTransaction(String type, double amount) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String transaction = String.format("%s | %s: $%.2f | Balance: $%.2f", timestamp, type, amount, balance);
        transactionHistory.add(transaction);
    }

    public void deposit(double amount) {
        balance += amount;
        recordTransaction("Deposit", amount);
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            recordTransaction("Withdrawal", amount);
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    public abstract void displayAccountInfo();
}

class SavingsAccount extends Account {
    public SavingsAccount(int accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
    }

    @Override
    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder Name: " + accountHolderName);
        System.out.println("Balance: " + balance);
        System.out.println("Password: " + password);
    }
}

class CurrentAccount extends Account {
    public CurrentAccount(int accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
    }

    @Override
    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder Name: " + accountHolderName);
        System.out.println("Balance: " + balance);
        System.out.println("Password: " + password);
    }
}

class Bank {
    private List<Account> accounts;
    private int nextAccountNumber;

    public Bank() {
        accounts = new ArrayList<>();
        nextAccountNumber = 1001;
    }

    public void createAccount(String type, String name, double initialBalance) {
        Account account;
        if (type.equalsIgnoreCase("Savings")) {
            account = new SavingsAccount(nextAccountNumber++, name, initialBalance);
        } else if (type.equalsIgnoreCase("Current")) {
            account = new CurrentAccount(nextAccountNumber++, name, initialBalance);
        } else {
            System.out.println("Invalid account type.");
            return;
        }
        accounts.add(account);
        System.out.println("Account created successfully. Account Number: " + account.getAccountNumber());
        System.out.println("Generated Password: " + account.getPassword());
    }

    public void displayAccountInfo(int accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                account.displayAccountInfo();
                displayTransactionHistory(account);
                return;
            }
        }
        System.out.println("Account not found.");
    }

    public void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }
        for (Account account : accounts) {
            account.displayAccountInfo();
            displayTransactionHistory(account);
            System.out.println("-------------------");
        }
    }

    public void deposit(int accountNumber, double amount, String password) {
        for (Account account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                if (account.getPassword().equals(password)) {
                    account.deposit(amount);
                    System.out.println("Deposited $" + amount + " into account number " + accountNumber);
                    return;
                } else {
                    System.out.println("Invalid password. Deposit failed.");
                    return;
                }
            }
        }
        System.out.println("Account not found.");
    }

    public void withdraw(int accountNumber, double amount, String password) {
        for (Account account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                if (account.getPassword().equals(password)) {
                    account.withdraw(amount);
                    return;
                } else {
                    System.out.println("Invalid password. Withdrawal failed.");
                    return;
                }
            }
        }
        System.out.println("Account not found.");
    }

    private void displayTransactionHistory(Account account) {
        System.out.println("Transaction History:");
        for (String transaction : account.getTransactionHistory()) {
            System.out.println(transaction);
        }
    }

    public void displayBankStatement(int accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                System.out.println("Bank Statement for Account Number: " + accountNumber);
                displayTransactionHistory(account);
                return;
            }
        }
        System.out.println("Account not found.");
    }
}

public class BankManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();

        while (true) {
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Display Account Information");
            System.out.println("5. Display All Accounts");
            System.out.println("6. Display Bank Statement");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter account type (Savings/Current): ");
                    String type = sc.next();
                    System.out.print("Enter account holder name: ");
                    String name = sc.next();
                    System.out.print("Enter initial balance: ");
                    double initialBalance = sc.nextDouble();
                    bank.createAccount(type, name, initialBalance);
                    break;
                case 2:
                    System.out.print("Enter account number to deposit: ");
                    int depositAccountNumber = sc.nextInt();
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = sc.nextDouble();
                    System.out.print("Enter password: ");
                    String depositPassword = sc.next();
                    bank.deposit(depositAccountNumber, depositAmount, depositPassword);
                    break;
                case 3:
                    System.out.print("Enter account number to withdraw from: ");
                    int withdrawAccountNumber = sc.nextInt();
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = sc.nextDouble();
                    System.out.print("Enter password: ");
                    String withdrawPassword = sc.next();
                    bank.withdraw(withdrawAccountNumber, withdrawAmount, withdrawPassword);
                    break;
                case 4:
                    System.out.print("Enter account number to display: ");
                    int displayAccountNumber = sc.nextInt();
                    bank.displayAccountInfo(displayAccountNumber);
                    break;
                case 5:
                    bank.displayAllAccounts();
                    break;
                case 6:
                    System.out.print("Enter account number to display bank statement: ");
                    int statementAccountNumber = sc.nextInt();
                    bank.displayBankStatement(statementAccountNumber);
                    break;
                case 7:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
}