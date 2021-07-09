package com.company.algorithm;

import com.company.utils.ArgumentsUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * simple implementation of genetic algorithm
 * <p>
 * 1. generate first generation
 * 2. select best 1% gens for mutation
 * 3. mutate it and find best gens to check
 * 4. if gens aren't good enough to stop evolution, do again 2-4 points
 */
public class GeneticAlgorithm {

    final static Logger logger = Logger.getLogger(GeneticAlgorithm.class);

    private List<Integer> tax;
    /*
     * select 1% of all
     */
    private double SELECTION_RATE = 0.01;
    /*
     * generate 100 persons
     */
    private int DEFAULT_POPULATION = 100;

    private int sumOfTax;

    private int perfectDebt;

    private Driver bestDriver;
    /**
     * cycle counter for each mutation
     */
    private int cycle = 0;

    /**
     * initialize tax list, and variables for evolution: sumOfTax and perfectDebt
     *
     * @param tax args from console
     */
    public GeneticAlgorithm(List<Integer> tax) {
        this.tax = tax;
        int sum = ArgumentsUtils.getSumOfIntegers(tax);
        this.sumOfTax = sum;
        int debt = sum - 55;
        this.perfectDebt = debt;
    }

    /**
     * start total evolution till find solution of task
     */
    public void generateBestWayToPay() {
        logger.info("input taxes: " + tax);
        logger.info("sum of tax " + sumOfTax);
        logger.info("perfect debt " + perfectDebt);
        List<Driver> firstGeneration = generateFirstGeneration();
        findBestDriver(firstGeneration);
        doLifeCycle(firstGeneration);
        logger.info("best way to pay for tax: " + bestDriver.getPocket() + " company needs to pay doubt: " + bestDriver.getDebt());
    }

    /**
     * generate first generation
     *
     * @return list of population
     */
    private List<Driver> generateFirstGeneration() {
        logger.info("generate first generation");
        List<Driver> drivers = new ArrayList<>();
        for (int i = 0; i < DEFAULT_POPULATION; i++) {
            Driver driver = driverFabric();
            findBestGenes(driver);
            drivers.add(driver);
        }
        return drivers;
    }

    /**
     * making new population by selecting best and do mutations
     * do recursion if solution wasn't founded
     * or done 10 cycles of evolution
     *
     * @param allPopulation population of gens before evolution
     */
    private void doLifeCycle(List<Driver> allPopulation) {
        logger.info("doing mutation");
        List<Driver> bestDrivers = selectBest(allPopulation); //методом отсеиваю первых 1% лучших
        List<Driver> mutatedDrivers = new ArrayList<>();
        for (int i = 0; i < bestDrivers.size(); i++) {
            mutatedDrivers.addAll(mutation(bestDrivers.get(i), bestDrivers.size()));
        }
        findBestDriver(mutatedDrivers); //проитерировал весь список уже мутированных водителей, и если там был лучше вариант, то переменная bestDriver изменилась
        boolean isPerfect = bestDriver.getDebt() == perfectDebt;
        logger.info("perfect gens atm: " + bestDriver.getPocket() + " doubt of it = " + bestDriver.getDebt());
        logger.info("is perfect doubt?: " + isPerfect);
        cycle++;
        if (!isPerfect && cycle != 10) { //производим мутацию, пока не найдем самый подходящий вариант
            doLifeCycle(mutatedDrivers);
        }
    }

    /**
     * iterate all population to find best gen from input
     *
     * @param drivers input population
     */
    private void findBestDriver(List<Driver> drivers) {
        if (bestDriver == null) {
            bestDriver = drivers.get(0);
        }
        for (Driver driver : drivers) {
            if (bestDriver.getDebt() > driver.getDebt()) {
                bestDriver = driver;
            }
        }
    }

    /**
     * making mutation from selected gen
     * better way to change number of new mutated gens, but this sample shows how it works on low
     * number of population
     *
     * @param driver 1 of the best gens for mutation
     * @return list of mutated population
     */
    private List<Driver> mutation(Driver driver, int iterations) {
        List<Coin> pocket = driver.getPocket();
        List<Integer> gensForMutation = new ArrayList<>();
        for (Coin coin : pocket) {
            if (!coin.isLocked()) {
                gensForMutation.add(coin.getValue());
            }
        }
        List<Driver> mutatedDrivers = new ArrayList<>();
        for (int i = 0; i < DEFAULT_POPULATION * iterations; i++) {
            Driver freshDriver = new Driver();
            Collections.shuffle(gensForMutation);
            int index = 0;
            List<Coin> coinz = new ArrayList<>();
            for (Coin coin : pocket) {
                Coin freshCoin = new Coin();
                if (!coin.isLocked()) {
                    freshCoin.setValue(gensForMutation.get(index));
                    freshCoin.setLocked(false);
                    index++;
                } else {
                    freshCoin.setValue(coin.getValue());
                    freshCoin.setLocked(coin.isLocked());
                }
                coinz.add(freshCoin);
            }
            freshDriver.setPocket(coinz);
            freshDriver.setDebt(calculateDebt(freshDriver));
            findBestGenes(freshDriver);
            mutatedDrivers.add(freshDriver);
        }
        return mutatedDrivers;
    }

    /**
     * selecting 1% the best gens
     *
     * @param drivers input population
     * @return list top 1% of input population
     */
    private List<Driver> selectBest(List<Driver> drivers) {
        long limit = (long) (DEFAULT_POPULATION * SELECTION_RATE);
        return drivers.stream().sorted(Comparator.comparing(Driver::getDebt)).limit(limit).collect(Collectors.toList());
    }

    /**
     * fabric to initialize first generation in evolution
     *
     * @return new person in first generation
     */
    private Driver driverFabric() {
        List<Integer> coins = IntStream.rangeClosed(1, tax.size())
                .boxed().collect(Collectors.toList());
        Collections.shuffle(coins);
        List<Coin> money = new ArrayList<>();
        for (Integer integer : coins) {
            Coin coin = new Coin(integer, false);
            money.add(coin);
        }
        Driver driver = new Driver(money);
        driver.setDebt(calculateDebt(driver));
        return driver;
    }

    /**
     * find best genes in person, if it match with input conditions, it become true and won't be changed later
     *
     * @param driver input person
     */
    private void findBestGenes(Driver driver) {
        List<Coin> pocket = driver.getPocket();
        for (int i = 0; i < pocket.size(); i++) {
            if (pocket.get(i).getValue().equals(tax.get(i))) {
                pocket.get(i).setLocked(true);
            }
        }
    }

    /**
     * calculation debt of person
     *
     * @param driver input person
     * @return debt after paying
     */
    private int calculateDebt(Driver driver) {
        List<Coin> pocket = driver.getPocket();
        int debt = 0;
        for (int i = 0; i < tax.size(); i++) {
            int coin = pocket.get(i).getValue();
            int paying = tax.get(i);
            if (paying > coin) {
                debt += (paying - coin);
            }
        }
        return debt;
    }

}
