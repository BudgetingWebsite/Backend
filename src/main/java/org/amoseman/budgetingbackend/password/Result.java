package org.amoseman.budgetingbackend.password;

public class Result {
    public final ResultType type;
    public final double entropyScore;
    public final double featureScore;

    public Result(ResultType type, double entropyScore, double featureScore) {
        this.type = type;
        this.entropyScore = entropyScore;
        this.featureScore = featureScore;
    }

    @Override
    public String toString() {
        return String.format("%s: %.4f, %.4f, %.4f", type, entropyScore, featureScore, entropyScore + featureScore);
    }
}
