package com.company.algorithm;

import java.util.List;

public class Driver {
    private List<Coin> pocket;
    private int debt;

    public Driver() {
    }

    public Driver(List<Coin> pocket) {
        this.pocket = pocket;
    }

    public List<Coin> getPocket() {
        return pocket;
    }

    public void setPocket(List<Coin> pocket) {
        this.pocket = pocket;
    }

    public int getDebt() {
        return debt;
    }

    public void setDebt(int debt) {
        this.debt = debt;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "pocket=" + pocket +
                ", debt=" + debt +
                '}';
    }

}
