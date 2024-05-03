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

    public static void main(String[] args)  {
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

            File file = new File(fileName);

            // Check if the file exists. If it does not, create it.
            if (!file.exists()) {
                try {
                    file.createNewFile(); // This will create a new file if it does not exist
                } catch (IOException e) {
                    System.out.println("An error occurred while trying to create a new file: " + e.getMessage());
                    return; // Exit the method if file creation fails
                }
            }

            // Proceed to read from the file only if it exists or has been successfully created
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split("\\|");
                    if (data.length < 5) {
                        continue; // Skip the improperly formatted lines
                    }

                    String dateString = data[0].trim(); // Convert String to Date format
                    LocalDate date = LocalDate.parse(dateString, DATE_FORMATTER);

                    String timeString = data[1].trim(); // Convert String to Time format
                    LocalTime time = LocalTime.parse(timeString, TIME_FORMATTER);

                    String description = data[2].trim();
                    String vendor = data[3].trim();
                    double price = Double.parseDouble(data[4]); // Convert String to Double format

                    transactions.add(new Transaction(date, time, description, vendor, price));
                }
            } catch (FileNotFoundException e) { //throws exceptions if file not found
                System.out.println("The file was not found: " + e.getMessage());
            } catch (IOException e) { //throws exceptions when reading from the file
                System.out.println("An error occurred while reading from the file: " + e.getMessage());
            } catch (DateTimeParseException e) { //throws exception if date format is incorrect and cant be read
                System.out.println("Error parsing date or time from the file: " + e.getMessage());
            } catch (NumberFormatException e) { //throws exception if time format is incorrect and cant be read
                System.out.println("Error parsing numeric values from the file: " + e.getMessage());
            }
        }

    private static void addDeposit(Scanner scanner) {
        LocalDate date = null; //initialising variables outside the loop
        LocalTime time = null;
        String dateString, timeString;

        // Get date
        while (date == null) {
            System.out.println("Please enter the date of the Deposit in the following format: yyyy-MM-dd ");
            dateString = scanner.nextLine().trim();
            try {
                date = LocalDate.parse(dateString, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter in yyyy-MM-dd format: ");
            }
        }

        // Get time
        while (time == null) {
            System.out.println("Please enter the time of the Deposit in the following format: HH:mm:ss ");
            timeString = scanner.nextLine().trim();
            try {
                time = LocalTime.parse(timeString, TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please enter in HH:mm:ss format: ");
            }
        }

        System.out.println("Please enter the purpose of your Deposit in a few words: ");
        String purpose = scanner.nextLine();

        System.out.println("Please enter the Name of Vendor: ");
        String name = scanner.nextLine();

        double amount = 0;
        boolean validAmount = false;
        while (!validAmount) {
            System.out.print("Finally, please enter the Amount of Deposit: ");
            if (scanner.hasNextDouble()) {
                amount = scanner.nextDouble(); //checks for valid input types and also convert int to doubles
                if (amount > 0) {
                    validAmount = true; // accepts input if amount is positive
                } else {
                    System.out.println("The number is not positive. Please enter a positive number.");
                }
            } else {
                System.out.println("That's not a valid number. Please enter a number.");
                scanner.next(); // This consumes the incorrect input
            }
        }
        scanner.nextLine(); // consume the remaining newline

        // Save the transaction
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String record = date.toString() + "|" + time.toString() + "|" + purpose + "|" + name + "|" + amount;
            transactions.add(new Transaction(date, time, purpose, name, amount));
            writer.write(record);
            writer.newLine();
            System.out.println("=======THANK YOU! DEPOSIT COMPLETE.=======");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }

    private static void addPayment(Scanner scanner) {
        LocalDate date = null;
        LocalTime time = null;
        String dateString, timeString;

        // Get date
        while (date == null) {
            System.out.println("Please enter the date of the Payment in the following format: yyyy-MM-dd ");
            dateString = scanner.nextLine().trim();
            try {
                date = LocalDate.parse(dateString, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter in yyyy-MM-dd format: ");
            }
        }

        // Get time
        while (time == null) {
            System.out.println("Please enter the time of the Payment in the following format: HH:mm:ss ");
            timeString = scanner.nextLine().trim();
            try {
                time = LocalTime.parse(timeString, TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please enter in HH:mm:ss format: ");
            }
        }

        System.out.println("Please enter the purpose of your Payment in a few words: ");
        String purpose = scanner.nextLine();

        System.out.println("Please enter the Name of Vendor: ");
        String name = scanner.nextLine();

        double amount = 0;
        boolean validAmount = false;
        while (!validAmount) {
            System.out.println("Please enter the amount of Payment: ");
            if (scanner.hasNextDouble()) {
                amount = scanner.nextDouble();
                if (amount > 0) {
                    validAmount = true;
                } else {
                    System.out.println("The number is not positive. Please enter a positive number.");
                }
            } else {
                System.out.println("That's not a valid number. Please enter a number.");
                scanner.next(); // This consumes the incorrect input
            }
        }
        scanner.nextLine(); // consume the remaining newline

        // Amount must be negative for withdrawals
        amount = -amount;

        // Save the transaction
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String record = date.toString() + "|" + time.toString() + "|" + purpose + "|" + name + "|" + amount;
            transactions.add(new Transaction(date, time, purpose, name, amount));
            writer.write(record);
            writer.newLine();
            System.out.println("========= PAYMENT PROCESSED, THANK YOU! ==========");
            System.out.println();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
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
        System.out.printf("%-12s | %-10s | %-30s | %-25s | %-10s%n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        // Print each transaction in the list
        for (Transaction transac : transactions) {
            System.out.printf("%-12s | %-10s | %-30s | %-25s | $%-,10.2f%n",
                    transac.getDate(),
                    transac.getTime().toString(),
                    transac.getDescription(),
                    transac.getVendor(),
                    transac.getAmount());
        }
    }

    private static void displayDeposits() {
        // Print the header with fixed column widths
        System.out.printf("%-12s | %-10s | %-30s | %-25s | %-10s%n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        // Iterate over the transactions to print only deposits
        for (Transaction transac : transactions) {
            if (transac.getAmount() > 0) { // Check if the transaction is a deposit (amount > 0)
                System.out.printf("%-12s | %-10s | %-30s | %-25s | $%-,10.2f%n",
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
        System.out.printf("%-12s | %-10s | %-30s | %-25s | %-10s%n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");

        // Iterate over the transactions to print only payments
        for (Transaction transac : transactions) {
            if (transac.getAmount() < 0) { // Check if the transaction is a payment (amount < 0)
                System.out.printf("%-12s | %-10s | %-30s | %-25s | $%-,10.2f%n",
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
                case "2":
                    LocalDate prevMonth = now.withDayOfMonth(1).minusMonths(1);
                    filterTransactionsByDate(prevMonth, prevMonth.withDayOfMonth(prevMonth.lengthOfMonth()));
                    break;
                    // Generate a report for all transactions within the previous month,

                case "3":
                    // Generate a report for all transactions within the current year,
                    filterTransactionsByDate(now.withDayOfYear(1), now);
                    break;

                case "4":
                    // Generate a report for all transactions within the previous year,
                    LocalDate startOfLastYear = LocalDate.of(now.getYear() - 1, 1, 1);
                    LocalDate endOfLastYear = LocalDate.of(now.getYear() - 1, 12, 31);
                    filterTransactionsByDate(startOfLastYear, endOfLastYear);
                    break;
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    System.out.println("Enter vendor name:");
                    String vendor = scanner.nextLine().trim();
                    filterTransactionsByVendor(vendor);
                    break;
                case "0":
                    System.out.println("Returning ...");
                    running = false;
                default:
                    System.out.println();
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        System.out.println("Transactions from " + startDate + " to " + endDate);
        boolean found = false;
        System.out.printf("%-12s | %-10s | %-30s | %-25s | %-10s%n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT"); //Formating in columns
        for (Transaction transac : transactions) {
            if (!transac.getDate().isBefore(startDate) && !transac.getDate().isAfter(endDate)) {
                System.out.printf("%-12s | %-10s | %-30s | %-25s | $%-,10.2f%n",
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

        System.out.println("Transactions with vendor: " + vendor);
        System.out.printf("%-12s | %-10s | %-30s | %-25s | %-10s%n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");

        boolean found = false;
        for (Transaction transac : transactions) {
            if (transac.getVendor().trim().equalsIgnoreCase(vendor)) {
                System.out.printf("%-12s | %-10s | %-30s | %-25s | $%-,10.2f%n",
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

