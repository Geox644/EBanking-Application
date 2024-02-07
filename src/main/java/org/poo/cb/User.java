package org.poo.cb;

import java.util.ArrayList;
import java.util.List;

public class User implements ObserverUser{
    String email;
    String firstname; // prenume
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

    public void adaugaBani(String currency, double amount) {
        Account account = portofoliu.getConturi().get(currency);

        if (account != null) {
            account.deposit(amount);
        }
    }
//    public void buyPremium() {
//        // Se presupune că utilizatorul are deja un cont în dolari
//        if (portofoliu.getConturi().containsKey("USD") && portofoliu.getConturi().get("USD").getBalance() >= 100) {
//            portofoliu.getConturi().get("USD").withdrawAccount(100);
//            premiumOption = true;
//        }
//    }
//    public void buyPremium() {
//        if (!premiumOption) {
//            // Verificați dacă utilizatorul are suficienți bani în cont
//            if (portofoliu.getConturi().containsKey("USD") &&
//                    portofoliu.getConturi().get("USD").getBalance() >= 100) {
//                // Retrage 100 USD din cont
//                portofoliu.getConturi().get("USD").withdrawAccount(100);
//
//                // Acordați opțiunea premium
//                premiumOption = true;
//
//                // Adăugați beneficiile opțiunii premium (de exemplu, reducerea la schimbul valutar și la cumpărarea acțiunilor)
//                // Adăugați codul corespunzător pentru beneficiile opțiunii premium
//            } else {
//                System.out.println("Insufficient funds to purchase premium option.");
//            }
//        } else {
//            System.out.println("Premium option already purchased.");
//        }
//    }
}
