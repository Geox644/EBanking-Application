package org.poo.cb;

import java.util.ArrayList;
import java.util.List;

// Observer
public class StocksManager {
    private List<ObserverUser> observers = new ArrayList<>();

    public void addObserver(ObserverUser observer) {
        observers.add(observer);
    }

    public void notifyObservers(List<String> recommendedStocks) {
        for (ObserverUser observer : observers) {
            observer.update(recommendedStocks);
        }
    }

    public void recommendStocks(List<String[]> stocksValues) {
        List<String> recommendedStocks = new ArrayList<>();

        // parcurg incepand cu a doua sublista
        for (int i = 1; i < stocksValues.size(); i++) {
            String companyName = stocksValues.get(i)[0];
            List<Double> stockValues = new ArrayList<>();

            for (int j = 1; j < stocksValues.get(i).length; j++) {
                stockValues.add(Double.parseDouble(stocksValues.get(i)[j]));
            }

            double shortTermSMA = calculateSMA(stockValues, 5);

            double longTermSMA = calculateSMA(stockValues, 10);

            if (shortTermSMA > longTermSMA) {
                recommendedStocks.add(companyName);
                notifyObservers(recommendedStocks);
            }
        }

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
}
