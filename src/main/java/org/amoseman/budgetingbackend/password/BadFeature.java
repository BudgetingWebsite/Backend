package org.amoseman.budgetingbackend.password;

public class BadFeature {
    public double score(String password) {
        double score = 0;
        if (containsRepetition(password)) {
            score += 1;
        }
        if (containsSequence(password)) {
            score += 1;
        }
        if (containsPlainWord(password)) {
            score += 1;
        }
        return score;
    }

    private boolean containsRepetition(String password) {
        // check for repetition using regex
        // likely faster to do manually, as then it could fail fast
        return password.matches("^(.+)(?:\\1)+$");
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

    private boolean containsPlainWord(String password) {
        // todo: check if password contains plain word (would require dictionary)
        return false;
    }
}
