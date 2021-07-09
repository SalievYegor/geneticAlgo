package com.company;

import com.company.algorithm.GeneticAlgorithm;
import com.company.utils.ArgumentsUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;

public class Application {

    private static Logger logger = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        if (args.length == 10) {
            try {
                GeneticAlgorithm algorithm = new GeneticAlgorithm(ArgumentsUtils.argumentsReader(args));
                algorithm.generateBestWayToPay();
            } catch (NumberFormatException ex){
                logger.error("arguments isn't list of integers");
            } catch (Exception e){
                logger.error(e.getMessage());
            }
        } else {
            logger.error("invalid input");
        }
    }
}
