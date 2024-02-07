package org.poo.cb;


import java.io.BufferedReader;
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

        // CSV Read
        List<String[]> exchangeRates;
        try {
            CSVReader reader1 = new CSVReaderBuilder(new FileReader(exchangeRatesPath)).build();
            exchangeRates = reader1.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        List<String[]> stocksValues;
        try {
            CSVReader reader2 = new CSVReaderBuilder(new FileReader(stockValuesPath)).build();
            stocksValues = reader2.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        EBankingApp app = EBankingApp.getInstance();
        UserBuilder userBuilder = app.createUserBuilder();
        StocksManager recommededStocks = app.getRecomandareManager();
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
                    StringBuilder addressBuilder = new StringBuilder();
                    for (int i = 5; i < token.length; i++) {
                        addressBuilder.append(token[i]).append(" ");
                    }
                    // Builder
                    String address = addressBuilder.toString().trim();
                    User user = userBuilder
                            .setEmail(email)
                            .setFirstname(firstName)
                            .setLastname(lastName)
                            .setAddress(address)
                            .build();
                    try {
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
                    String email = token[2];
                    String currency = token[3];
                    double amount = Double.parseDouble(token[4]);
                    app.addMoney(email, currency, amount);
                }

                if (command1.equals("LIST") && command2.equals("PORTFOLIO")) {
                    String email = token[2];
                    app.listPortfolio(email);
                }
                if (command1.equals("EXCHANGE") && command2.equals("MONEY")) {
                    String email = token[2];
                    String source = token[3];
                    String destination = token[4];
                    double amount = Double.parseDouble(token[5]);

                    try {
                        app.exchangeMoney(email, source, destination, amount, exchangeRates);
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
                    // observer
                    recommededStocks.recommendStocks(stocksValues);
                }

                if (command1.equals("BUY") && command2.equals("STOCKS")) {
                    String email = token[2];
                    String companyName = token[3];
                    int noOfStocks = Integer.parseInt(token[4]);
                    try {
                        app.buyStocks(email, companyName, noOfStocks, stocksValues);
                    } catch (InsufficientAmountStocks e) {
                        System.out.println(e.getMessage());
                    }

                }
                if (command1.equals("BUY") && command2.equals("PREMIUM")) {
                    String email = token[2];
                    try {
                        app.buyPremium(email);
                    } catch (InsufficientAmountTransferException e) {
                        System.out.println(e.getMessage());
                    } catch (UserDoestExistException e) {
                        System.out.println(e.getMessage());
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
