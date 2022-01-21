package children;

import enums.AgeGroup;
import enums.Category;
import enums.Cities;
import enums.ElvesType;
import gifts.Gift;
import utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Child implements Comparable<Child> {
    private Integer id;
    private String lastName;
    private String firstName;
    private Integer age;
    private Cities city;
    private Double niceScore;
    private LinkedList<Category> giftsPreferences;
    private List<Double> niceScoreHistory = new ArrayList<>();
    private Double averageScore = 0.0;
    private List<Gift> receivedGifts = new ArrayList<>();
    private AgeGroup ageGroup = AgeGroup.UNKNOWN;
    private AverageScoreStrategy averageScoreStrategy = null;
    private Double niceScoreBonus = 0.0;
    private ElvesType elf;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    public Cities getCity() {
        return city;
    }

    public void setCity(final Cities city) {
        this.city = city;
    }

    public List<Double> getNiceScoreHistory() {
        return niceScoreHistory;
    }

    public void setNiceScoreHistory(final List<Double> niceScoreHistory) {
        this.niceScoreHistory = niceScoreHistory;
    }

    public void setAverageScore(final Double averageScore) {
        this.averageScore = averageScore;
    }

    public List<Category> getGiftsPreferences() {
        return giftsPreferences;
    }

    public void setGiftsPreferences(final LinkedList<Category> giftsPreferences) {
        this.giftsPreferences = giftsPreferences;
    }

    public Double getNiceScore() {
        return niceScore;
    }

    public void setNiceScore(final Double niceScore) {
        this.niceScore = niceScore;
    }

    public List<Gift> getReceivedGifts() {
        return receivedGifts;
    }

    public Double getNiceScoreBonus() {
        return niceScoreBonus;
    }

    public void setNiceScoreBonus(Double niceScoreBonus) {
        this.niceScoreBonus = niceScoreBonus;
    }

    public ElvesType getElf() {
        return elf;
    }

    public void setElf(ElvesType elf) {
        this.elf = elf;
    }

    public void receiveGift(final Gift receivedGift) {
        if (receivedGift != null) {
            this.receivedGifts.add(receivedGift);
        }
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(final AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public void addNiceScore(final Double newNiceScore) {
        niceScoreHistory.add(newNiceScore);
    }

    public void setStrategy() {
        averageScoreStrategy = AverageScoreStrategyFactory.getInstance()
                .createStrategy(ageGroup);
    }

    public Double getAverageScore() {
        averageScore = averageScoreStrategy.getAverageScore(this);
        averageScore += averageScore * niceScoreBonus / 100;
        if (averageScore > 10.0) {
            averageScore = 10.0;
        }
        return averageScore;
    }

    public void initAgeGroup() {
        if (ageGroup == AgeGroup.UNKNOWN) {
            ageGroup = Utils.ageToAgeGroup(age);
        }
    }

    /**
     * Initializes the age group based on the child's age, adds the initial nice score
     * (from the round 0) to the nice score history, and creates a strategy based on
     * the age group and assigns it to the child
     */
    public void init() {
        initAgeGroup();
        addNiceScore(niceScore);
        setStrategy();
    }

    /**
     * Converts the new preferences to a linked list (used as a reversed stack) and,
     * for every category, deletes if it's already existing among the preferences
     * and adds it at the beginning of that list
     * @param newGiftsPreferences preferences to be added to the start of the already existing list
     */
    public void addPreferences(final List<Category> newGiftsPreferences) {
        LinkedList<Category> aux = new LinkedList<>(newGiftsPreferences);
        while (!(aux.isEmpty())) {
            var newPreference = aux.removeLast();
            if (giftsPreferences.contains(newPreference)) {
                giftsPreferences.remove(newPreference);
            }
            giftsPreferences.addFirst(newPreference);
        }
    }

    /**
     * Increments the child's age by 1 and if it crosses the current age group threshold,
     * the age group is also "incremented" to the next one chronologically
     */
    public void incrementAge() {
        age++;
        if (age > Utils.upperLimitAgeGroup(ageGroup)) {
            ageGroup = Utils.nextAgeGroup(ageGroup);
            setStrategy();
        }
    }

    public void clearGifts() {
        receivedGifts.clear();
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " ("
                + id + ") {"
                + age + " yo"
                + ", " + city
                + ", niceScoreHistory: " + niceScoreHistory
                + ", averageScore: " + averageScore
                + ", giftsPreferences: " + giftsPreferences + "}";
    }

    @Override
    public int compareTo(final Child o) {
        return this.id.compareTo(o.getId());
    }
}
