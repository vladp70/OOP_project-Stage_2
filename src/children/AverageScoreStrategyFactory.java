package children;

import enums.AgeGroup;

public final class AverageScoreStrategyFactory {
    private static AverageScoreStrategyFactory instance = null;

    private AverageScoreStrategyFactory() {
    }

    public static AverageScoreStrategyFactory getInstance() {
        if (instance == null) {
            instance = new AverageScoreStrategyFactory();
        }
        return instance;
    }

    /**
     * Factory pattern that creates a strategy based on the age group it needs to operate on
     * @param ageGroup that dictates how the strategy calculates the nice score
     * @return the newly created strategy (to be assigned to a child)
     */
    public AverageScoreStrategy createStrategy(final AgeGroup ageGroup) {
        if (ageGroup.equals(AgeGroup.BABY)) {
            return new BabyAverageScoreStrategy();
        } else if (ageGroup.equals(AgeGroup.KID)) {
            return new KidAverageScoreStrategy();
        } else if (ageGroup.equals(AgeGroup.TEEN)) {
            return new TeenAverageScoreStrategy();
        }
        return null;
    }
}
