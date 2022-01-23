package santareplacer;

import enums.CityStrategyEnum;

public final class GiftingStrategyFactory {
    private static GiftingStrategyFactory instance = null;

    private GiftingStrategyFactory() {
    }

    public static GiftingStrategyFactory getInstance() {
        if (instance == null) {
            instance = new GiftingStrategyFactory();
        }
        return instance;
    }

    /**
     * Factory pattern that creates a strategy based on the specified type
     * @param strategy enum of the required gifting strategy type
     * @return the newly created strategy (to assign gifts to children)
     */
    public GiftingStrategy createStrategy(final CityStrategyEnum strategy) {
        if (strategy.equals(CityStrategyEnum.ID)) {
            return new IdGiftingStrategy();
        } else if (strategy.equals(CityStrategyEnum.NICE_SCORE)) {
            return new NiceScoreGiftingStrategy();
        } else if (strategy.equals(CityStrategyEnum.NICE_SCORE_CITY)) {
            return new NiceScoreCityGiftingStrategy();
        }
        return null;
    }
}
