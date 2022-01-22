package santareplacer;

import children.Child;
import enums.Cities;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class NiceScoreCityGiftingStrategy implements GiftingStrategy{
    @Override
    public void apply(Database santaDB) {
        Map<Cities, Double> cityAverages = new HashMap<>();
        Double average;
        int numChildrenInCity;
        for (var city : Cities.values()) {
            average = 0.0;
            numChildrenInCity = 0;
            for (var child : santaDB.getChildren()) {
                if (child.getCity().equals(city)) {
                    numChildrenInCity++;
                    average += child.getAverageScoreWithoutUpdate();
                }
            }

            cityAverages.put(city, average / numChildrenInCity);
        }



        Collections.sort(santaDB.getChildren(), new Comparator<Child>() {
            @Override
            public int compare(Child o1, Child o2) {
                Cities city1 = o1.getCity();
                Cities city2 = o2.getCity();
                if (cityAverages.get(city1).equals(cityAverages.get(city2))) {
                    if (city1.equals(city2)) {
                        return o1.getId().compareTo(o2.getId());
                    } else {
                        return city1.toString().compareTo(city2.toString());
                    }
                } else {
                    return cityAverages.get(city2).compareTo(cityAverages.get(city1));
                }
            }
        });

        for (var child : santaDB.getChildren()) {
            santaDB.assignGiftsToChild(child, santaDB.getChildrenBudgets().get(child));
        }
    }
}
