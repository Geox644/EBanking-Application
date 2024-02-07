package org.poo.cb;

import java.util.*;

public class Portofoliu {
    private Map<String, Account> usersAccounts;
    private List<String> stocks;


    public Portofoliu() {
        this.usersAccounts = new LinkedHashMap<>();
        this.stocks = new ArrayList<>();
    }

    public Map<String, Account> getConturi() {
        return usersAccounts;
    }

    public List<String> getStocks() {
        return stocks;
    }

    public void adaugaCont(Account cont) {
        usersAccounts.put(cont.getCurrency(), cont);
    }

    public void addStock(String companyName, int noOfStocks) {
        String stock = companyName + " " + noOfStocks;
        stocks.add(stock);
    }
}
