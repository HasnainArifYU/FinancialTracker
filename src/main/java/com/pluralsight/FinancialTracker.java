package com.pluralsight;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.*;


public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) throws IOException {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("--- WELCOME TO THE TRANSACTION APP ---");
            System.out.println("Please choose an option:");
            System.out.println();
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {

        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>,<time>,<vendor>,<type>,<amount>
        // For example: 2023-04-29,13:45:00,Amazon,PAYMENT,29.99
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while (( line = reader.readLine()) != null) {
                String[] data = line.split("\\|");

                String dateString = data[0].trim();
                LocalDate date = LocalDate.parse(dateString, DATE_FORMATTER);

                String timeString = data[1].trim();
                LocalTime time = LocalTime.parse(timeString, TIME_FORMATTER);

                String description = data[2].trim();
                String vendor = data[3].trim();
                double price = Double.parseDouble(data[4]);

                transactions.add(new Transaction(date, time, description, vendor, price));

            }
            reader.close();
        } catch (Exception e) {

        }
    }

        private static void addDeposit(Scanner scanner) throws IOException {
        // This method should prompt the user to enter the date, time, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Deposit` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
            LocalDate date = null;
            LocalTime time = null;
            do {
                // Get date
                System.out.println("Please enter the date of the Deposit in the following format : yyyy-MM-dd ");
                String dateString = scanner.nextLine().trim();

                try {
                    date = LocalDate.parse(dateString, DATE_FORMATTER);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter in yyyy-MM-dd format: ");
                }

                // Get time (only if date is valid)
                if (date != null) {
                    System.out.println("Please enter the time of the Deposit in the following format : HH:mm:ss ");
                    String timeString = scanner.nextLine().trim();

                    try {
                        time = LocalTime.parse(timeString, TIME_FORMATTER);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid time format. Please enter in HH:mm:ss format: ");
                    }
                }
            } while (date == null || time == null);

            System.out.println("Please enter the purpose of your Deposit in a few words : ");
            String purpose = scanner.nextLine();

            System.out.println("Please enter the Name of Vendor : ");
            String name = scanner.nextLine();

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));

            Double amount;
            while (true) {
                System.out.print("Finally, please enter the Amount of Deposit : ");
                if (scanner.hasNextDouble()) { // This checks for any double input, including integers
                    amount = scanner.nextDouble(); // This reads the input as a double, converting integers to double automatically
                    if (amount > 0) {
                        Transaction Deposit = new Transaction(date, time, purpose, name, amount);
                        writer.write(date.toString()+"|"+time.toString()+"|"+purpose+"|"+name+"|"+amount);
                        transactions.add(Deposit);
                        System.out.println("=======THANK YOU! DEPOSIT COMPLETE.=======\n");
                        scanner.nextLine();
                        writer.newLine();
                        break; // Break the loop if the number is positive
                    } else {
                        System.out.println("The number is not positive. Please enter a positive number.");
                    }
                } else {
                    System.out.println("That's not a valid number. Please enter a number.");
                    scanner.next(); // This consumes the incorrect input
                }
            } writer.close();
        }


    private static void addPayment(Scanner scanner) throws IOException {
        // This method should prompt the user to enter the date, time, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Payment` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.
        LocalDate date = null;
        LocalTime time = null;
        do {
            // Get date
            System.out.println("Please enter the date of the Withdrawal in the following format : yyyy-MM-dd ");
            String dateString = scanner.nextLine().trim();

            try {
                date = LocalDate.parse(dateString, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter in yyyy-MM-dd format: ");
            }

            // Get time (only if date is valid)
            if (date != null) {
                System.out.println("Please enter the time of the Withdrawal in the following format : HH:mm:ss ");
                String timeString = scanner.nextLine().trim();

                try {
                    time = LocalTime.parse(timeString, TIME_FORMATTER);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid time format. Please enter in HH:mm:ss format: ");
                }
            }
        } while (date == null || time == null);

        System.out.println("Please enter the purpose of your Withdrawal in a few words : ");
        String purpose = scanner.nextLine();

        System.out.println("Please enter the Name of Vendor : ");
        String name = scanner.nextLine();
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));

        Double amount;
        do {


            System.out.println("Please enter the amount of Withdrawal : ");
            amount = scanner.nextDouble();
            if (amount > 0) {
                amount = -amount;
                Transaction Payment = (new Transaction(date, time, purpose, name, amount));
                writer.write(date.toString()+"|"+time.toString()+"|"+purpose+"|"+name+"|"+amount);
                transactions.add(Payment);
                System.out.println("========= WITHDRAWAL PROCESSED, THANK YOU! ==========\n");
                scanner.nextLine();
                writer.newLine();
                break;
            } else {
                System.out.println("Please enter a positive number : ");
            }

        } while (true);
        writer.close();

    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Returning to Home Page ");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // Print the header with fixed column widths
        System.out.printf("%-12s | %-8s | %-20s | %-15s | %-10s%n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");

        // Print each transaction in the list
        for (Transaction transac : transactions) {
            System.out.printf("%-12s | %-8s | %-20s | %-15s | $%-10.2f%n",
                    transac.getDate(),
                    transac.getTime().toString(),
                    transac.getDescription(),
                    transac.getVendor(),
                    transac.getAmount());
        }
    }

    private static void displayDeposits() {
        // Print the header with fixed column widths
        System.out.printf("%-12s | %-8s | %-20s | %-15s | %-10s%n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");

        // Iterate over the transactions to print only deposits
        for (Transaction transac : transactions) {
            if (transac.getAmount() > 0) { // Check if the transaction is a deposit (amount > 0)
                System.out.printf("%-12s | %-8s | %-20s | %-15s | $%-,10.2f%n",
                        transac.getDate(),
                        transac.getTime().toString(),
                        transac.getDescription(),
                        transac.getVendor(),
                        transac.getAmount());
            }
        }
    }

    private static void displayPayments() {
        // Print the header with fixed column widths
        System.out.printf("%-12s | %-8s | %-20s | %-15s | %-10s%n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");

        // Iterate over the transactions to print only payments
        for (Transaction transac : transactions) {
            if (transac.getAmount() < 0) { // Check if the transaction is a payment (amount < 0)
                System.out.printf("%-12s | %-8s | %-20s | %-15s | $%-,10.2f%n",
                        transac.getDate(),
                        transac.getTime().toString(),
                        transac.getDescription(),
                        transac.getVendor(),
                        transac.getAmount()); // Display the absolute value of the amount
            }
        }
    }
    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();
            LocalDate now = LocalDate.now();

            switch (input) {
                case "1":
                    filterTransactionsByDate(now.withDayOfMonth(1), now);
                    break;
                    // Generate a report for all transactions within the current month,
                    // including the date, vendor, and amount for each transaction.
                case "2":
                    LocalDate prevMonth = now.withDayOfMonth(1).minusMonths(1);
                    filterTransactionsByDate(prevMonth, prevMonth.withDayOfMonth(prevMonth.lengthOfMonth()));
                    // Generate a report for all transactions within the previous month,
                    // including the date, vendor, and amount for each transaction.

                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, vendor, and amount for each transaction.
                    filterTransactionsByDate(now.withDayOfYear(1), now);
                    break;

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate startOfLastYear = LocalDate.of(now.getYear() - 1, 1, 1);
                    LocalDate endOfLastYear = LocalDate.of(now.getYear() - 1, 12, 31);
                    filterTransactionsByDate(startOfLastYear, endOfLastYear);
                    break;
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, vendor, and amount for each transaction.
                    System.out.println("Enter vendor name:");
                    String vendor = scanner.nextLine().trim();
                    filterTransactionsByVendor(vendor);
                    break;
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
        System.out.println("Transactions from " + startDate + " to " + endDate);
        boolean found = false;
        System.out.printf("%-12s | %-8s | %-20s | %-15s | %-10s%n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        for (Transaction transac : transactions) {
            if (!transac.getDate().isBefore(startDate) && !transac.getDate().isAfter(endDate)) {
                System.out.printf("%-12s | %-8s | %-20s | %-15s | $%-,10.2f%n",
                        transac.getDate(),
                        transac.getTime().toString(),
                        transac.getDescription(),
                        transac.getVendor(),
                        transac.getAmount()); // Display the absolute value of the amount
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found for this period.");
        }
    }


    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
        System.out.println("Transactions with vendor: " + vendor);
        System.out.printf("%-12s | %-8s | %-20s | %-15s | %-10s%n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");

        boolean found = false;
        for (Transaction transac : transactions) {
            if (transac.getVendor().trim().equalsIgnoreCase(vendor)) {
                System.out.printf("%-12s | %-8s | %-20s | %-15s | $%-,10.2f%n",
                        transac.getDate(),
                        transac.getTime().toString(),
                        transac.getDescription(),
                        transac.getVendor(),
                        transac.getAmount()); // Display the absolute value of the amount
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found for this vendor.");
        }
    }
}

