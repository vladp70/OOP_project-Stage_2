package children;

public interface AverageScoreStrategy {
    /**
     * Calculates the average nice score of the child according to the strategy type
     * @param child whose nice score average is wanted
     * @return nice score average
     */
    Double getAverageScore(Child child);
}
