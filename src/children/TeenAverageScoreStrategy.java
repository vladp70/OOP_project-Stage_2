package children;

public final class TeenAverageScoreStrategy implements AverageScoreStrategy {
    @Override
    public Double getAverageScore(final Child child) {
        Integer numScores = child.getNiceScoreHistory().size();
        Double res = 0.0;
        Integer iteration = 0;

        for (var score : child.getNiceScoreHistory()) {
            res += score * (iteration + 1);
            iteration++;
        }
        res /= numScores * (numScores + 1.0) / 2.0;

        return res;
    }
}
