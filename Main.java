import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();

        while (true) {

            System.out.println("\n1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Balance");
            System.out.println("6. History");
            System.out.println("7. Exit");

            int ch = sc.nextInt();
            sc.nextLine();

            if (ch == 1) {
                System.out.print("Name: ");
                String name = sc.nextLine();

                System.out.print("PIN: ");
                int pin = sc.nextInt();

                Account acc = bank.createAccount(name, pin);
                acc.deposit(500);
            }

            else if (ch == 2) {
                System.out.print("Account: ");
                String acc = sc.nextLine();

                System.out.print("Amount: ");
                double amt = sc.nextDouble();

                Account a = bank.findAccount(acc);
                if (a != null)
                    a.deposit(amt);
            }

            else if (ch == 3) {
                System.out.print("Account: ");
                String acc = sc.nextLine();

                System.out.print("Amount: ");
                double amt = sc.nextDouble();

                System.out.print("PIN: ");
                int pin = sc.nextInt();

                Account a = bank.findAccount(acc);

                if (a == null) {
                    System.out.println("Account not found");
                    continue;
                }

                if (!a.validatePin(pin)) {
                    System.out.println("Wrong PIN");
                    return;
                }

                try {
                    a.withdraw(amt);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            else if (ch == 4) {
                System.out.print("From: ");
                String from = sc.nextLine();

                System.out.print("To: ");
                String to = sc.nextLine();

                System.out.print("Amount: ");
                double amt = sc.nextDouble();

                System.out.print("PIN: ");
                int pin = sc.nextInt();

                Account sender = bank.findAccount(from);
                Account receiver = bank.findAccount(to);

                if (sender == null) {
                    System.out.println("Sender account not found");
                    continue;
                }

                if (receiver == null) {
                    System.out.println("Receiver account not found");
                    continue;
                }

                if (!sender.validatePin(pin)) {
                    System.out.println("Wrong PIN");
                    return;
                }

                try {
                    sender.transfer(receiver, amt);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            else if (ch == 5) {
                System.out.print("Account: ");
                String acc = sc.nextLine();
                Account account = bank.findAccount(acc);
                int pin = sc.nextInt();

                if (!account.validatePin(pin)) {
                    System.out.println("Wrong PIN");
                    return;
                }

                Account a = bank.findAccount(acc);
                if (a != null)
                    a.getBalance();
            }

            else if (ch == 6) {
                System.out.print("Account: ");
                String acc = sc.nextLine();

                Account a = bank.findAccount(acc);
                if (a != null)
                    a.printTransactionHistory();
            }

            else if (ch == 7) {
                break;
            }
        }

        sc.close();
    }
}