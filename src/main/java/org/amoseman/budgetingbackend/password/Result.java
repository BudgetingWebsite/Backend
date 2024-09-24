package org.amoseman.budgetingbackend.password;

public class Result {
    public final ResultType type;
    public final double score;

    public Result(ResultType type, double score) {
        this.type = type;
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("%s: %.4f", type, score);
    }
}
