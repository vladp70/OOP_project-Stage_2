package children;

public final class KidAverageScoreStrategy implements AverageScoreStrategy {
    @Override
    public Double getAverageScore(final Child child) {
        Double res = 0.0;
        for (var score : child.getNiceScoreHistory()) {
            res += score;
        }
        return res / child.getNiceScoreHistory().size();
    }
}
