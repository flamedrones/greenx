package com.flamecode.greenx;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TokenRewardCalculator {

    public static double compute(TokenRewardInput input){

        double random = ThreadLocalRandom.current().nextDouble(0.00001, 0.00003);

        double tokenValue = 1.0d/random;
        return (tokenValue * input.getDistance() * input.getPricePerUnit())/100000;

    }

}
