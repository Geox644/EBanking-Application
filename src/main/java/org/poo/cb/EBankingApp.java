package org.poo.cb;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;



public class EBankingApp {
    private static EBankingApp instance;
    private Map<String, User> users;

    public void clearUserData() {
        users.clear();
    }

    private EBankingApp() {
        users = new HashMap<>();
    }

    public static EBankingApp getInstance() {
        if (instance == null) {
            instance = new EBankingApp();
        }
        return instance;
    }

//    public void createUser(String email, String firstName, String lastName, String address) throws UserAlreadyExistsException {
//        if (!users.containsKey(email)) {
//            User newUser = new User(email, firstName, lastName, address);
//            users.put(email, newUser);
//        } else {
//            throw new UserAlreadyExistsException("User with " + email + " already exists");
//        }
//    }
public UserBuilder createUserBuilder() {
    return new UserBuilder();
}

    public void createUser(User user) throws UserAlreadyExistsException {
        if (!users.containsKey(user.getEmail())) {
            users.put(user.getEmail(), user);
        } else {
            throw new UserAlreadyExistsException("User with " + user.getEmail() + " already exists");
        }
    }

    public void listUser(String email) throws UserDoestExistException {
        User user = users.get(email);

        if (user != null) {
            System.out.print("{");
            System.out.print("\"email\":\"" + user.getEmail() + "\",");
            System.out.print("\"firstname\":\"" + user.getFirstname() + "\",");
            System.out.print("\"lastname\":\"" + user.getLastname() + "\",");
            System.out.print("\"address\":\"" + user.getAddress() + "\",");
            System.out.print("\"friends\":[");
            List<String> friends = user.getFriends();
            for (int i = 0; i < friends.size(); i++) {
                System.out.print("\"" + friends.get(i) + "\"");
                if (i < friends.size() - 1) {
                    System.out.print(",");
                }
            }
            System.out.print("]");
            System.out.println("}");
        } else {
            throw new UserDoestExistException("User with " + email + " doesn't exist");
        }
    }

    public void addFriend(String userEmail1, String userEmail2) throws UserAlreadyFriendException, UserDoestExistException {
        User user1 = users.get(userEmail1);
        User user2 = users.get(userEmail2);

        if (user1 != null && user2 != null) {
            user1.addFriend(userEmail2);
            user2.addFriend(userEmail1);
        } else if (user1 == null) {
            throw new UserDoestExistException("User with" + userEmail1 + " doesn't exist");
        } else if (user2 == null) {
            throw new UserDoestExistException("User with" + userEmail2 + " doesn't exist");
        } else {
            throw new UserAlreadyFriendException("User with " + userEmail2 + " is already a friend");
        }
    }

    public void addAccount(String userEmail, String currency) {
        User user = users.get(userEmail);

        if (user != null) {
            switch (currency) {
                case "USD":
                    user.addAccount(new USDAccount());
                    break;
                case "EUR":
                    user.addAccount(new EURAccount());
                    break;
                case "GBP":
                    user.addAccount(new GBPAccount());
                    break;
                case "JPY":
                    user.addAccount(new JPYAccount());
                    break;
                case "CAD":
                    user.addAccount(new CADAccount());
                    break;
            }
        }
    }

    public void addMoney(String userEmail, String currency, double amount) {
        User user = users.get(userEmail);
        if (user != null) {
            user.adaugaBani(currency, amount);
        }
    }

    public void listPortfolio(String userEmail) {
        User user = users.get(userEmail);

        if (user != null) {
            System.out.print("{\"stocks\":[");

            // Parcurge lista de actiuni a utilizatorului
            List<String> stocksList = user.getPortofoliu().getStocks();
            for (int i = 0; i < stocksList.size(); i++) {
                String stock = stocksList.get(i);
//                String[] stockDetails = stock.split(" - ");
                String[] stockDetails = stock.split(" ");

                System.out.print("{");
                System.out.print("\"stockName\":\"" + stockDetails[0] + "\",");
                System.out.print("\"amount\":" + stockDetails[1]);
                System.out.print("}");

                if (i < stocksList.size() - 1) {
                    System.out.print(",");
                }
            }

            System.out.print("],");

            // Continua sa afisezi si detaliile conturilor utilizatorului
            Map<String, Account> conturi = user.getPortofoliu().getConturi();
            int index = 0;

            System.out.print("\"accounts\":[");
            for (Map.Entry<String, Account> entry : conturi.entrySet()) {
                String currencyName = entry.getKey();
                double amount = entry.getValue().getBalance();

                System.out.print("{");
                System.out.print("\"currencyName\":\"" + currencyName + "\",");
                System.out.print("\"amount\":\"" + String.format("%.2f", amount) + "\"");
                System.out.print("}");

                if ((index < conturi.size() - 1 || !stocksList.isEmpty()) && index < conturi.size() - 1) {
                    System.out.print(",");
                }

                index++;
            }

            System.out.print("]}");
            System.out.println();
        }
    }

    public void exchangeMoney(String userEmail, String sourceCurrency, String destinationCurrency, double amount, List<String[]> exchangeRates) throws InsufficientAmountExchangeException {
        User user = users.get(userEmail);

        if (user != null) {
            int sourceCurrencyIndex = -1;
            int destinationCurrencyIndex = -1;

            // Identifica indexul valutelor in lista
            for (int i = 1; i < exchangeRates.get(0).length; i++) {
                if (exchangeRates.get(0)[i].equals(sourceCurrency)) {
                    sourceCurrencyIndex = i;
                } else if (exchangeRates.get(0)[i].equals(destinationCurrency)) {
                    destinationCurrencyIndex = i;
                }
            }

            if (sourceCurrencyIndex != -1 && destinationCurrencyIndex != -1) {
                // Obtine ratele de schimb pentru conversie
                double sourceToDestinationRate = Double.parseDouble(exchangeRates.get(destinationCurrencyIndex)[sourceCurrencyIndex]);

                // Implementeaza conversia
                double amountInDestinationCurrency = amount * sourceToDestinationRate;


                // Verifica daca utilizatorul are suficiente fonduri in contul de sursa
                if (user.getPortofoliu().getConturi().get(sourceCurrency).getBalance() >= amount) {
                    // Retrage suma din contul de sursa
                    user.getPortofoliu().getConturi().get(sourceCurrency).withdrawAccount(amountInDestinationCurrency);

                    // Adauga suma in contul de destinatie
                   // user.getPortofoliu().getConturi().get(destinationCurrency).deposit(amount);
                    Command depositCommand =  new DepositCommand(user, destinationCurrency, amount);
                    TransactionInvoker invoker = new TransactionInvoker();
                    invoker.setCommand(depositCommand);
                    invoker.executeCommand();

                } else {
                    throw new InsufficientAmountExchangeException("insufficient amount in account " + sourceCurrency + " for exchange");
                }
            }
        }
    }

    public void transferMoney(String userEmail, String friendEmail, String currency, double amount) throws InsufficientAmountTransferException {
        User user = users.get(userEmail);
        User friend = users.get(friendEmail);

        if (user != null && friend != null) {
            if (user.getFriends().contains(friendEmail)) {
                // Check if the user has sufficient funds in the specified currency
                if (user.getPortofoliu().getConturi().containsKey(currency) &&
                        user.getPortofoliu().getConturi().get(currency).getBalance() >= amount) {

                    // Withdraw the amount from the user's account
                    user.getPortofoliu().getConturi().get(currency).withdrawAccount(amount);
                    // Deposit the amount into the friend's account
                //    friend.getPortofoliu().getConturi().get(currency).deposit(amount);
                    Command depositCommand =  new DepositCommand(friend, currency, amount);
                    TransactionInvoker invoker = new TransactionInvoker();
                    invoker.setCommand(depositCommand);
                    invoker.executeCommand();
                } else {
                    throw new InsufficientAmountTransferException("Insufficient amount in account " + currency + " for transfer");
                }
            }
        }
    }

    public void recommendStocks(List<String[]> stocksValues) {
        List<String> recommendedStocks = new ArrayList<>();

        // Parcurge fiecare sublista din stocksValues incepand cu a doua lista
        for (int i = 1; i < stocksValues.size(); i++) {
            String companyName = stocksValues.get(i)[0];
            List<Double> stockValues = new ArrayList<>();

            // Parcurge valorile pentru fiecare zi din sublista curenta
            for (int j = 1; j < stocksValues.get(i).length; j++) {
                stockValues.add(Double.parseDouble(stocksValues.get(i)[j]));
            }

            // Calculeaza SMA pe termen scurt (ultimele 5 zile)
            double shortTermSMA = calculateSMA(stockValues, 5);

            // Calculeaza SMA pe termen lung (ultimele 10 zile)
            double longTermSMA = calculateSMA(stockValues, 10);

            // Verifica conditia si adauga la lista de recomandari
            if (shortTermSMA > longTermSMA) {
                recommendedStocks.add(companyName);
            }
        }

        // Afiseaza lista de recomandari
        System.out.print("{");
        System.out.print("\"stockstobuy\":[");
        for (int i = 0; i < recommendedStocks.size(); i++) {
            System.out.print("\"" + recommendedStocks.get(i) + "\"");
            if (i < recommendedStocks.size() - 1) {
                System.out.print(",");
            }
        }
        System.out.println("]}");
    }

    private double calculateSMA(List<Double> values, int term) {

        double sum = 0;
        for (int i = values.size() - term; i < values.size(); i++) {
            sum += values.get(i);
        }

        return sum / term;
    }

    // Adauga aceasta metoda in clasa EBankingApp
    public void buyStocks(String userEmail, String companyName, int noOfStocks, List<String[]> stocksValues) throws InsufficientAmountStocks {
        User user = users.get(userEmail);

        if (user != null) {
            // Gaseste sublista corespunzatoare companiei in stocksValues
            for (int i = 1; i < stocksValues.size(); i++) {
                if (stocksValues.get(i)[0].equals(companyName)) {
                    // Obtine ultima valoare din sublista pentru pretul actiunii
                    double stockPrice = Double.parseDouble(stocksValues.get(i)[stocksValues.get(i).length - 1]);

                    // Calculeaza valoarea totala cheltuita pentru actiuni
                    double totalCost = noOfStocks * stockPrice;

                    // Verifica daca utilizatorul are suficiente fonduri in cont pentru a cumpara actiunile
                    if (user.getPortofoliu().getConturi().containsKey("USD") &&
                            user.getPortofoliu().getConturi().get("USD").getBalance() >= totalCost) {
                        // Retrage suma din contul de USD
                        user.getPortofoliu().getConturi().get("USD").withdrawStocks(totalCost);
//                        user.getPortofoliu().adaugaStock(companyName + " - " + noOfStocks + " stocks");
                        user.getPortofoliu().addStock(companyName, noOfStocks);

                    } else {
                        throw new InsufficientAmountStocks("Insufficient amount in account for buying stock");
                    }
                    break;
                }
            }
        }
    }
}
