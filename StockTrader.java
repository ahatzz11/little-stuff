package com.codility;

import java.util.*;

class Stock {

    // AAPL or TSLA
    private final String tickerSymbol;

    public Stock(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

}

// 3 shares of AAPL
class Position {

    private final Stock stock;
    private int quantity;

    public Position(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

class Portfolio {

    // uninvested
    private double cash;
    private Map<String, Position> positions;

    public Portfolio(double cash, List<Position> positions) {
        this.cash = cash;

        Map<String, Position> positionsMap = new HashMap<String, Position>();

        positions.stream().forEach( position -> {
            positionsMap.put(position.getStock().getTickerSymbol(), position);
        });

        this.positions = positionsMap;
    }

    public double getCash() {
        return cash;
    }

    // return map as collections
    public Collection<Position> getPositions() {
        return positions.values();
    }

    /*
    If the buy is viable (sufficient cash), executes it and returns true,
    else returns false

        boolean result = portfolio.buy(new Stock("MSFT"), 6, 100.00);

    */
    public boolean buy(Stock stock, int quantity, double price) {

        double totalCost = quantity * price;

        // not enough money, return false
        if(this.getCash() < totalCost) {
            return false;
        }

        // we have enough money, add to portfolio
        // if it exists, update quantity
        // if it doesn't, add
        if(this.positions.containsKey(stock.getTickerSymbol())) {
            // update quantity
            Position currentPosition = this.positions.get(stock.getTickerSymbol());
            int currentQuantity = currentPosition.getQuantity();

            currentPosition.setQuantity(currentQuantity + quantity);
        } else {
            this.positions.put(stock.getTickerSymbol(), new Position(stock, quantity));
        }

        // decrement cash on hand for the purchase
        this.cash = this.cash - totalCost;

        return true;

    }

    /*
    If the sell is viable (sufficiently large existing position), executes
    it and returns true, else returns false
    */
    public boolean sell(Stock stock, int quantity, double price) {

        // get the position we are trying to sell
        Position currentPosition = this.positions.get(stock.getTickerSymbol());

        // do not have the position, return false
        if(currentPosition == null) {
            return false;
        }

        // not enough shares, return false
        if(currentPosition.getQuantity() < quantity) {
            return false;
        }

        // we have enough shares, remove from portfolio and get cash :money:
        currentPosition.setQuantity(currentPosition.getQuantity() - quantity);

        double totalSellCash = quantity * price;
        this.cash += totalSellCash;

        // remove from position list if we have nothing left
        if(currentPosition.getQuantity() == 0) {
            this.positions.remove(currentPosition.getStock().getTickerSymbol());
        }

        return true;
    }

    /*
    Adds all cash and positions of Portfolio 'other' into this portfolio
    and always returns true
    */
    public boolean transferFrom(Portfolio other) {

        // add all the cash from other portfolio to ours
        this.cash += other.cash;

        // transfer all of the stocks over
        other.positions.forEach( (ticker, position) -> {

            Position currentPosition = this.positions.get(ticker);

            // check if we have the position, if yes, add quantity
            if(currentPosition != null) {
                currentPosition.setQuantity(currentPosition.getQuantity() + position.getQuantity());
            } else { // if we don't have it, add directly
                this.positions.put(ticker, position);
            }
        });

        return true;
    }
}

class Solution {

    public static void main(String[] args) {
        // testBuy();
        // testSell();
        // testTransferFrom();
    }

    // success
    public static void testBuy() {
        Portfolio portfolio = new Portfolio(1000.00, Arrays.asList(
            new Position(new Stock("MSFT"), 20)
        ));

        boolean result = portfolio.buy(new Stock("MSFT"), 6, 1000.00);
        printPortfolio(portfolio, result);
    }

    public static void testSell() {
        Portfolio portfolio = new Portfolio(1000.00, Arrays.asList(
            new Position(new Stock("AAPL"), 50),
            new Position(new Stock("MSFT"), 20)
        ));

        boolean result = portfolio.sell(new Stock("AAPL"), 50, 100.00);
        printPortfolio(portfolio, result);
    }

    public static void testTransferFrom() {
        Portfolio portfolio = new Portfolio(750.00, Arrays.asList(
            new Position(new Stock("MSFT"), 20)
        ));
        Portfolio other = new Portfolio(0, Arrays.asList(
            new Position(new Stock("MSFT"), 30),
            new Position(new Stock("TSLA"), 30),
            new Position(new Stock("NFLX"), 30)
        ));

        boolean result = portfolio.transferFrom(other);
        printPortfolio(portfolio, result);
    }

    public static void printPortfolio(Portfolio portfolio, boolean result) {
        System.out.println();
        System.out.println("Result: " + result);
        System.out.println("Cash: $" + portfolio.getCash());
        System.out.println("Positions:");
        for (Position p : portfolio.getPositions()) {
            System.out.println("  " + p.getQuantity() + " shares of " + p.getStock().getTickerSymbol());
        }
    }
}
