package org.amoseman.budgetingbackend.password;

import java.util.Optional;

public class Entropy {
    public Optional<Double> entropy(String password) {
        int l = password.length();
        Optional<Integer> pool = new PoolCalculator().pool(password);
        if (pool.isEmpty()) {
            return Optional.empty();
        }
        int r = pool.get();
        return Optional.of(log2(Math.pow(r, l)));
    }

    private double log2(double x) {
        return Math.log(x) / Math.log(2);
    }
}
