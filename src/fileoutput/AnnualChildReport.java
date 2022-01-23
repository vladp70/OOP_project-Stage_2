package fileoutput;

import children.Child;
import enums.Category;
import enums.Cities;

import java.util.ArrayList;
import java.util.List;

public final class AnnualChildReport {
    private Integer id;
    private String lastName;
    private String firstName;
    private Cities city;
    private Integer age;
    private List<Category> giftsPreferences;
    private Double averageScore;
    private List<Double> niceScoreHistory;
    private Double assignedBudget;
    private List<GiftDataOutput> receivedGifts;

    /**
     * Creates the report based on a semi-deep copy of the child data and the budget.
     * It needs to contain deep copies of the child's lists because those fields can
     * change in ulterior rounds, but the report needs to be independent of those updates.
     * @param child whose annual situation needs to be reported
     * @param assignedBudget by Santa
     */
    public AnnualChildReport(final Child child, final Double assignedBudget) {
        this.id = child.getId();
        this.lastName = child.getLastName();
        this.firstName = child.getFirstName();
        this.city = child.getCity();
        this.age = child.getAge();
        this.giftsPreferences = new ArrayList<>(child.getGiftsPreferences());
        this.averageScore = child.getAverageScore();
        this.niceScoreHistory = new ArrayList<>(child.getNiceScoreHistory());
        this.receivedGifts = new ArrayList<>();
        for (var gift : child.getReceivedGifts()) {
            receivedGifts.add(new GiftDataOutput(gift));
        }
        this.assignedBudget = assignedBudget;
    }

    public Integer getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Cities getCity() {
        return city;
    }

    public Integer getAge() {
        return age;
    }

    public List<Category> getGiftsPreferences() {
        return giftsPreferences;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public List<Double> getNiceScoreHistory() {
        return niceScoreHistory;
    }

    public Double getAssignedBudget() {
        return assignedBudget;
    }

    public List<GiftDataOutput> getReceivedGifts() {
        return receivedGifts;
    }
}
