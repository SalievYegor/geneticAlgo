package com.company.utils;

import java.util.ArrayList;
import java.util.List;

public class ArgumentsUtils {

    public static List<Integer> argumentsReader(String[] args) throws Exception {
        List<Integer> inputValues = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int value = Integer.valueOf(args[i]);
            inputValues.add(value);
            isFitNumber(value);
        }
        isEnoughSum(inputValues);
        return inputValues;
    }

    /**
     * found sum of integers in list
     *
     * @param list input list
     * @return integer sum
     */
    public static int getSumOfIntegers(List<Integer> list) {
        int sum = 0;
        for (int i : list) {
            sum += i;
        }
        return sum;
    }

    /**
     * check is sum of number more than 55 to start evolution
     *
     * @param list of input numbers
     * @throws Exception
     */
    private static void isEnoughSum(List<Integer> list) throws Exception {
        int sum = getSumOfIntegers(list);
        if (sum < 55) {
            throw new Exception("sum of integers isn't enough to do evolution, please enter sum more than 55");
        }
    }

    /**
     * check is number between 1 and 10
     *
     * @param input number for check
     * @throws Exception
     */
    private static void isFitNumber(int input) throws Exception {
        if (input < 0 || input > 10) {
            throw new Exception("Number should be between 1 and 10");
        }
    }
}
