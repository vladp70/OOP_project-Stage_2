package children;

import common.Constants;

public final class BabyAverageScoreStrategy implements AverageScoreStrategy {
    @Override
    public Double getAverageScore(final Child child) {
        return Constants.MAX_NICE_SCORE;
    }
}
