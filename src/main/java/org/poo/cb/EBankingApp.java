package org.poo.cb;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

// Singleton
public class EBankingApp {
    private static EBankingApp instance;
    private Map<String, User> users;
    private StocksManager stocksRecommended;
    private boolean premiumOption = false;


    public void clearUserData() {
        users.clear();
    }

    private EBankingApp() {
        users = new HashMap<>();
        stocksRecommended = new StocksManager();
    }

    public static EBankingApp getInstance() {
        if (instance == null) {
            instance = new EBankingApp();
        }
        return instance;
    }

    public UserBuilder createUserBuilder() {
        return new UserBuilder();
    }

    // adaug user in lista observer pt a fi notificati
    public void createUser(User user) throws UserAlreadyExistsException {
        if (!users.containsKey(user.getEmail())) {
            users.put(user.getEmail(), user);
            stocksRecommended.addObserver(user);
        } else {
            throw new UserAlreadyExistsException("User with " + user.getEmail() + " already exists");
        }
    }

    public StocksManager getRecomandareManager() {
        return stocksRecommended;
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
            user.addMoney(currency, amount);
        }
    }

    public void listPortfolio(String userEmail) {
        User user = users.get(userEmail);

        if (user != null) {
            System.out.print("{\"stocks\":[");

            // parcurg lista de actiuni a utilizatorului
            List<String> stocksList = user.getPortofoliu().getStocks();
            for (int i = 0; i < stocksList.size(); i++) {
                String stock = stocksList.get(i);
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

    public void exchangeMoney(String userEmail, String source, String destination, double amount, List<String[]> exchangeRates) throws InsufficientAmountExchangeException {
        User user = users.get(userEmail);

        if (user != null) {
            int sourceIndex = -1;
            int destinationIndex = -1;

            // caut indexul valutelor in lista
            for (int i = 1; i < exchangeRates.get(0).length; i++) {
                if (exchangeRates.get(0)[i].equals(source)) {
                    sourceIndex = i;
                } else if (exchangeRates.get(0)[i].equals(destination)) {
                    destinationIndex = i;
                }
            }

            if (sourceIndex != -1 && destinationIndex != -1) {
                double sourceToDestinationRate = Double.parseDouble(exchangeRates.get(destinationIndex)[sourceIndex]);

                double amountDestination = amount * sourceToDestinationRate;


                // verifica daca user ul are suficiente fonduri in contul sursa
                if (user.getPortofoliu().getConturi().get(source).getBalance() >= amount) {
                    // retrage suma din contul sursa
                    if (!premiumOption) {
                        user.getPortofoliu().getConturi().get(source).withdrawAccount(amountDestination);
                    } else {
                        user.getPortofoliu().getConturi().get(source).withdrawAccountPremium(amountDestination);
                    }
                    user.getPortofoliu().getConturi().get(destination).deposit(amount);

                } else {
                    throw new InsufficientAmountExchangeException("insufficient amount in account " + source + " for exchange");
                }
            }
        }
    }

    public void transferMoney(String userEmail, String friendEmail, String currency, double amount) throws InsufficientAmountTransferException {
        User user = users.get(userEmail);
        User friend = users.get(friendEmail);

        if (user != null && friend != null) {
            if (user.getFriends().contains(friendEmail)) {
                if (user.getPortofoliu().getConturi().containsKey(currency) &&
                        user.getPortofoliu().getConturi().get(currency).getBalance() >= amount) {

                    user.getPortofoliu().getConturi().get(currency).withdrawAccount(amount);

                    friend.getPortofoliu().getConturi().get(currency).deposit(amount);

                } else {
                    throw new InsufficientAmountTransferException("Insufficient amount in account " + currency + " for transfer");
                }
            }
        }
    }

    public void buyStocks(String userEmail, String companyName, int noOfStocks, List<String[]> stocksValues) throws InsufficientAmountStocks {
        User user = users.get(userEmail);

        if (user != null) {
            for (int i = 1; i < stocksValues.size(); i++) {
                if (stocksValues.get(i)[0].equals(companyName)) {
                    double stockPrice = Double.parseDouble(stocksValues.get(i)[stocksValues.get(i).length - 1]);
                    // cost normal
                    double totalCost = noOfStocks * stockPrice;
                    // cost cont premium
                    double totalCostPremium = noOfStocks * stockPrice - 0.05 * noOfStocks * stockPrice;

                    if (user.getPortofoliu().getConturi().containsKey("USD") &&
                            user.getPortofoliu().getConturi().get("USD").getBalance() >= totalCost) {

                        if (!premiumOption) {
                            user.getPortofoliu().getConturi().get("USD").withdrawStocks(totalCost);
                        } else {
                            user.getPortofoliu().getConturi().get("USD").withdrawStocks(totalCostPremium);
                        }
                        user.getPortofoliu().addStock(companyName, noOfStocks);

                    } else {
                        throw new InsufficientAmountStocks("Insufficient amount in account for buying stock");
                    }
                    break;
                }
            }
        }
    }

    public void buyPremium(String email) throws InsufficientAmountTransferException, UserDoestExistException {
        User user = users.get(email);
        if (!premiumOption) {

            if (user.portofoliu.getConturi().containsKey("USD") &&
                    user.getPortofoliu().getConturi().get("USD").getBalance() >= 100) {
                user.portofoliu.getConturi().get("USD").withdrawStocks(100);

                premiumOption = true;

            } else if (user == null) {
                throw new UserDoestExistException("User with " + email + " doesn't exist");
            } else {
                throw new InsufficientAmountTransferException("Insufficient amount in account for buying premium option");

            }
        }
    }

}
