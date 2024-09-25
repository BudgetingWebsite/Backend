package org.amoseman.budgetingbackend.password;

/**
 * The result of a password strength check.
 */
public class Result {
    public final ResultType type;
    public final double entropyScore;
    public final double featureScore;

    /**
     * Instantiate a password strength check result.
     * @param type the type of result.
     * @param entropyScore the entropy score of the password.
     * @param featureScore the feature score of the password.
     */
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
