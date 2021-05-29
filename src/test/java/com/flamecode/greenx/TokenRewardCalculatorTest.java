package com.flamecode.greenx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenRewardCalculatorTest {

    @Test
    public void givenInputWhenComputeThenReturnReward(){

        TokenRewardInput input = new TokenRewardInput();
        input.setDistance(10);
        input.setPricePerUnit(1);

        double reward = TokenRewardCalculator.compute(input);

        assertTrue(reward > 1 && reward < 100000000);
    }
}