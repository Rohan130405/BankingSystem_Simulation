import java.sql.*;
import java.time.LocalDateTime;

public class Account {

    private String accountNumber;
    private String accountHolder;
    private int pin;

    public Account(String accountNumber, String accountHolder, int pin) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.pin = pin;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public boolean validatePin(int enteredPin) {
        return this.pin == enteredPin;
    }

    // deposit
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE account_number = ?");

            ps.setDouble(1, amount);
            ps.setString(2, accountNumber);
            ps.executeUpdate();

            addTransaction("Deposit", amount, "Self deposit");

            System.out.println("Deposit successful");
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // withdraw
    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount <= 0) {
            System.out.println("Invalid amount");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps1 = con.prepareStatement(
                    "SELECT balance FROM accounts WHERE account_number = ?");

            ps1.setString(1, accountNumber);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");

                if (balance < amount) {
                    throw new InsufficientBalanceException("Insufficient balance");
                }
            }

            PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE account_number = ?");

            ps2.setDouble(1, amount);
            ps2.setString(2, accountNumber);
            ps2.executeUpdate();

            addTransaction("Withdraw", amount, "Cash withdraw");

            System.out.println("Withdraw successful");
            con.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // transfer
    public void transfer(Account receiver, double amount) throws InsufficientBalanceException {
        if (amount <= 0) {
            System.out.println("Invalid amount");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            // check balance
            PreparedStatement ps1 = con.prepareStatement(
                    "SELECT balance FROM accounts WHERE account_number = ?");

            ps1.setString(1, accountNumber);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");

                if (balance < amount) {
                    throw new InsufficientBalanceException("Insufficient balance");
                }
            }

            // deduct sender
            PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE account_number = ?");
            ps2.setDouble(1, amount);
            ps2.setString(2, accountNumber);
            ps2.executeUpdate();

            // add receiver
            PreparedStatement ps3 = con.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE account_number = ?");
            ps3.setDouble(1, amount);
            ps3.setString(2, receiver.accountNumber);
            ps3.executeUpdate();

            addTransaction("Transfer", amount, "To " + receiver.accountNumber);
            receiver.addTransaction("Received", amount, "From " + this.accountNumber);

            System.out.println("Transfer successful");
            con.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // check balance
    public void getBalance() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT balance FROM accounts WHERE account_number = ?");

            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Balance: " + rs.getDouble("balance"));
            }
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // add transaction
    public void addTransaction(String type, double amount, String details) {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO transactions (account_number, type, amount, details, date_time) VALUES (?, ?, ?, ?, ?)");

            ps.setString(1, accountNumber);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setString(4, details);
            ps.setString(5, LocalDateTime.now().toString());

            ps.executeUpdate();
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // show history
    public void printTransactionHistory() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM transactions WHERE account_number = ? ORDER BY id DESC LIMIT 5");

            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                        rs.getString("type") + " | " +
                                rs.getDouble("amount") + " | " +
                                rs.getString("details") + " | " +
                                rs.getString("date_time"));
            }
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}