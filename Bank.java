import java.sql.*;

public class Bank {

    public Account createAccount(String name, int pin) {
        String accNumber = "ACC" + System.currentTimeMillis();

        try {
            Connection con = DBConnection.getConnection();

            double initialBalance = 500;

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO accounts VALUES (?, ?, ?, ?)");

            ps.setString(1, accNumber);
            ps.setString(2, name);
            ps.setDouble(3, initialBalance);
            ps.setInt(4, pin);

            ps.executeUpdate();

            System.out.println("Account created: " + accNumber);
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Account findAccount(String accNumber) {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM accounts WHERE account_number = ?");

            ps.setString(1, accNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getString("account_number"),
                        rs.getString("account_holder"),
                        rs.getInt("pin"));
            } else {
                System.out.println("No such account");
                return null;

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}