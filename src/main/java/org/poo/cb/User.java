package org.poo.cb;

import java.util.ArrayList;
import java.util.List;

public class User implements ObserverUser{
    String email;
    String firstname;
    String lastname;
    String address;

    Portofoliu portofoliu;

    List<String> friends;
    List<String> recommendedStocks;



    public User(String email, String firstname, String lastname, String address) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.portofoliu = new Portofoliu();
        this.friends = new ArrayList<>();
        this.recommendedStocks = new ArrayList<>();

    }
    public void update(List<String> recommendedStocks) {
        setRecommendedStocks(recommendedStocks);
    }

    private void setRecommendedStocks(List<String> recommendedStocks) {
        this.recommendedStocks = recommendedStocks;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress() {
        return address;
    }


    public List<String> getFriends() {
        return friends;
    }

    public void addFriend(String email) {
        if (!friends.contains(email)) {
            friends.add(email);
        }
    }
    public Portofoliu getPortofoliu() {
        return portofoliu;
    }

    public void addAccount(Account cont) {
        portofoliu.adaugaCont(cont);
    }

    public void addMoney(String currency, double amount) {
        Account account = portofoliu.getConturi().get(currency);

        if (account != null) {
            account.deposit(amount);
        }
    }
}
