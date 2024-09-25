package org.amoseman.budgetingbackend.password;

/**
 * Class for checking if a password contains bad features.
 */
public class BadFeature {
    private final int MIN_BAD_FEATURE_SCORE = 3;

    /**
     * Calculate the feature score of a password.
     * The higher the number, the better.
     * Points are lost if it contains a sequence or repetition.
     * @param password the password to check.
     * @return the feature score.
     */
    public double score(String password) {
        double score = 0;
        if (containsRepetition(password)) {
            score += 1;
        }
        if (containsSequence(password)) {
            score += 1;
        }
        return (MIN_BAD_FEATURE_SCORE - score) / MIN_BAD_FEATURE_SCORE;
    }

    private boolean containsRepetition(String password) {
        // check for repetition using regex
        // likely faster to do manually, as then it could fail fast
        if (password.length() < 3) {
            return false;
        }
        for (int i = 0; i < password.length() - 3; i++) {
            String substring = password.substring(i, i + 3);
            if (substring.matches("^(.+)(?:\\1)+$")) {
                return true;
            }
        }
        return false;
    }

    private boolean containsSequence(String password) {
        if (password.length() < 4) {
            return false;
        }
        for (int i = 0; i < password.length() - 4; i++) {
            String substring = password.substring(i, i + 4);
            if (isSequence(substring)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSequence(String string) {
        char[] characters = string.toCharArray();
        if (!(Character.isAlphabetic(characters[0]) || Character.isDigit(characters[0]))) {
            return false;
        }
        int[] deltas = new int[characters.length - 1];
        for (int i = 0; i < characters.length - 1; i++) {
            deltas[i] = characters[i] - characters[i + 1];
        }
        for (int i = 0; i < deltas.length - 1; i++) {
            if (deltas[i] == deltas[i + 1]) {
                return true;
            }
        }
        return false;
    }
}
