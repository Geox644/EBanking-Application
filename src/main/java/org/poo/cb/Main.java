package org.poo.cb;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class Main {
    public static void main(String[] args) {
        if (args == null) {
            System.out.println("Running Main");
            return;
        }
        if (args.length < 3) {
            System.out.println("Usage: Main <exchangeRatesFile> <stockValuesFile> <commandsFile>");
            return;
        }

        String exchangeRatesPath = "src/main/resources/" + args[0];
        String stockValuesPath = "src/main/resources/" + args[1];
        String commandsPath = "src/main/resources/" + args[2];

        List<String[]> exchangeRates = null;
        try {
            CSVReader reader1 = new CSVReaderBuilder(new FileReader(exchangeRatesPath)).build();
            // Restul codului care foloseste obiectul reader1
            exchangeRates = reader1.readAll();
//            for (String[] row : myEntries) {
//                for (String column : row) {
//                    System.out.print(column + " ");
//                }
//             //  System.out.println();  // Treci la urmatorul rand pentru afisare clara
//            }
        } catch (FileNotFoundException e) {
            // Trateaza exceptia aici sau afiseaza un mesaj de eroare
            e.printStackTrace();  // sau foloseste un alt mod de tratare a erorilor
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        List<String[]> stocksValues = null;
        try {
            CSVReader reader2 = new CSVReaderBuilder(new FileReader(stockValuesPath)).build();
            // Restul codului care foloseste obiectul reader1
            stocksValues = reader2.readAll();
        } catch (FileNotFoundException e) {
            // Trateaza exceptia aici sau afiseaza un mesaj de eroare
            e.printStackTrace();  // sau foloseste un alt mod de tratare a erorilor
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        EBankingApp app = EBankingApp.getInstance();
        UserBuilder userBuilder = app.createUserBuilder();
        app.clearUserData();


        try (BufferedReader reader = new BufferedReader(new FileReader(commandsPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] token = line.split(" ");
                String command1 = token[0];
                String command2 = token[1];

                if (command1.equals("CREATE") && command2.equals("USER")) {
                    String email = token[2];
                    String firstName = token[3];
                    String lastName = token[4];
                    // Concatenam cuvintele ramase in adresa utilizand un StringBuilder
                    StringBuilder addressBuilder = new StringBuilder();
                    for (int i = 5; i < token.length; i++) {
                        addressBuilder.append(token[i]).append(" ");
                    }
                    String address = addressBuilder.toString().trim();
                    User user = userBuilder
                            .setEmail(email)
                            .setFirstname(firstName)
                            .setLastname(lastName)
                            .setAddress(address)
                            .build();
                    try {
//                        app.createUser(email, firstName, lastName, address);
                        app.createUser(user);
                    } catch (UserAlreadyExistsException e) {
                        System.out.println(e.getMessage());
                    }

                }
                if (command1.equals("LIST") && command2.equals("USER")) {
                    String email = token[2];
                    try {
                        app.listUser(email);
                    } catch (UserDoestExistException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (command1.equals("ADD") && command2.equals("FRIEND")) {
                    String userEmail1 = token[2];
                    String userEmail2 = token[3];
                    try {
                        app.addFriend(userEmail1, userEmail2);
                    } catch (UserAlreadyFriendException e) {
                        System.out.println(e.getMessage());
                    } catch (UserDoestExistException e) {
                        System.out.println(e.getMessage());
                    }
                }

                if (command1.equals("ADD") && command2.equals("ACCOUNT")) {
                    String email = token[2];
                    String currency = token[3];
                    app.addAccount(email, currency);
                }

                if (command1.equals("ADD") && command2.equals("MONEY")) {
                    String userEmail = token[2];
                    String currency = token[3];
                    double amount = Double.parseDouble(token[4]);
                    app.addMoney(userEmail, currency, amount);
                }

                if (command1.equals("LIST") && command2.equals("PORTFOLIO")) {
                    String email = token[2];
                    app.listPortfolio(email);
                }
                if (command1.equals("EXCHANGE") && command2.equals("MONEY")) {
                    String userEmail = token[2];
                    String sourceCurrency = token[3];
                    String destinationCurrency = token[4];
                    double amount = Double.parseDouble(token[5]);

                    try {
                        app.exchangeMoney(userEmail, sourceCurrency, destinationCurrency, amount, exchangeRates);
                    } catch (InsufficientAmountExchangeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (command1.equals("TRANSFER") && command2.equals("MONEY")) {
                    String userEmail = token[2];
                    String friendEmail = token[3];
                    String currency = token[4];
                    double amount = Double.parseDouble(token[5]);

                    try {
                        app.transferMoney(userEmail, friendEmail, currency, amount);
                    } catch (InsufficientAmountTransferException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (command1.equals("RECOMMEND") && command2.equals("STOCKS")) {
                    app.recommendStocks(stocksValues);
                }

                if (command1.equals("BUY") && command2.equals("STOCKS")) {
                    String userEmail = token[2];
                    String companyName = token[3];
                    int noOfStocks = Integer.parseInt(token[4]);
                    try {
                        app.buyStocks(userEmail, companyName, noOfStocks, stocksValues);
                    } catch (InsufficientAmountStocks e) {
                        System.out.println(e.getMessage());
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
