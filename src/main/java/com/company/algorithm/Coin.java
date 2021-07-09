package com.company.algorithm;

public class Coin {

    private Integer value;
    private boolean locked;

    public Coin() {
    }

    public Coin(Integer value, boolean locked) {
        this.value = value;
        this.locked = locked;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
