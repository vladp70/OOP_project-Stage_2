package santareplacer;

import children.Child;
import elves.BlackElf;
import elves.PinkElf;
import elves.WhiteElf;
import elves.YellowElf;
import enums.AgeGroup;
import enums.Category;
import enums.CityStrategyEnum;
import fileoutput.AnnualChildReport;
import fileinput.Input;
import gifts.Gift;

import java.util.*;

public final class Database {
    private static Database instance = null;
    private Integer numberOfYears;
    private Double santaBudget;
    private List<Child> children;
    private List<Gift> gifts;
    private List<AnnualChange> annualChanges;
    private Double budgetUnit;
    private CityStrategyEnum strategy = CityStrategyEnum.ID;
    private Map<Child, Double> childrenBudgets = new HashMap<>();

    private Database() { }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Integer getNumberOfYears() {
        return numberOfYears;
    }

    public Double getSantaBudget() {
        return santaBudget;
    }

    public List<Child> getChildren() {
        return children;
    }

    public List<Gift> getGifts() {
        return gifts;
    }

    public List<AnnualChange> getAnnualChanges() {
        return annualChanges;
    }

    public Double getBudgetUnit() {
        return budgetUnit;
    }

    /**
     * Copies the input data to this database object and initializes the children in the database
     * @param input that contains the initial state of the Santa database
     */
    public void initDatabase(final Input input) {
        if (input == null) {
            return;
        }
        numberOfYears = input.getNumberOfYears();
        santaBudget = input.getSantaBudget();
        children = input.getInitialData().getChildren();
        gifts = input.getInitialData().getSantaGiftsList();
        annualChanges = input.getAnnualChanges();
        initChildrenList(children);
    }

    public void initChildren(final List<Child> childrenList) {
        childrenList.forEach(Child::init);
    }

    /**
     * Initializes every child in the list, removes young adults and sorts the list by child id
     * @param childrenList to be initialized as a list to comply with the database rules
     */
    public void initChildrenList(final List<Child> childrenList) {
        initChildren(childrenList);
        removeAdults();
        Collections.sort(childrenList);
    }

    public void incrementAges() {
        children.forEach(Child::incrementAge);
    }

    public void removeAdults() {
        children.removeIf(child -> (child.getAgeGroup().equals(AgeGroup.YOUNG_ADULT)));
    }

    public void clearChildrenGifts() {
        children.forEach(Child::clearGifts);
    }

    public void calculateBudgetUnit() {
        Double averageSum = 0.0;
        for (var child : children) {
            averageSum += child.getAverageScore();
        }
        budgetUnit = santaBudget / averageSum;
    }

    //TODO use boolean inStock
    public Gift findCheapestInStockGiftByCategory(final Category category) {
        if (category == null) {
            return null;
        }

        Gift cheapestGift = null;

        for (var gift : gifts) {
            if (gift.getCategory().equals(category)
                    && gift.getQuantity() > 0) {
                if (cheapestGift == null) {
                    cheapestGift = gift;
                } else if (cheapestGift.getPrice() > gift.getPrice()) {
                    cheapestGift = gift;
                }
            }
        }

        return cheapestGift;
    }

    public Gift findCheapestGiftByCategory(final Category category) {
        if (category == null) {
            return null;
        }

        Gift cheapestGift = null;

        for (var gift : gifts) {
            if (gift.getCategory().equals(category)) {
                if (cheapestGift == null) {
                    cheapestGift = gift;
                } else if (cheapestGift.getPrice() > gift.getPrice()) {
                    cheapestGift = gift;
                }
            }
        }

        return cheapestGift;
    }

    public void assignGiftsToChild(final Child child, Double childBudget) {
        for (var preference : child.getGiftsPreferences()) {
            if (childBudget <= 0.0) {
                return;
            }

            Gift gift = findCheapestInStockGiftByCategory(preference);
            if (gift == null) {
                continue;
            }
            if (childBudget >= gift.getPrice()) {
                childBudget -= gift.getPrice();
                child.receiveGift(gift);
                gift.decQuantity();
            }
        }
    }

    public Child findChildByID(final int id) {
        for (var child : children) {
            if (child.getId().equals(id)) {
                return child;
            }
        }
        return null;
    }

    public void calculateChildrenBudgets() {
        var whiteElf = new WhiteElf(this);
        var blackElf = new BlackElf(this);
        var pinkElf = new PinkElf(this);
        Double childBudget;

        calculateBudgetUnit();
        for (var child : children) {
            childBudget = whiteElf.visit(child);
            if (childBudget != null) {
                childrenBudgets.put(child, childBudget);
                continue;
            }

            childBudget = pinkElf.visit(child);
            if (childBudget != null) {
                childrenBudgets.put(child, childBudget);
                continue;
            }

            childBudget = blackElf.visit(child);
            if (childBudget != null) {
                childrenBudgets.put(child, childBudget);
                continue;
            }

            childrenBudgets.put(child, budgetUnit * child.getAverageScore());
        }
    }

    /**
     * Simulates the children growing up, resets their received gifts,
     * adds new data and initializes it, removes adults, sorts lists,
     * then implements children updates by finding the child in the database,
     * adding the new nice score (if existing) and adds their new preferences
     * @param changes to be implemented this year/round
     */
    public void implementAnnualChange(final AnnualChange changes) {
        incrementAges();
        clearChildrenGifts();
        children.addAll(changes.getNewChildren());
        initChildren(changes.getNewChildren());
        removeAdults();
        Collections.sort(children);
        gifts.addAll(changes.getNewGifts());
        santaBudget = changes.getNewSantaBudget();
        strategy = changes.getStrategy();

        for (var update : changes.getChildrenUpdates()) {
            Child updatedChild = findChildByID(update.getId());
            if (updatedChild == null) {
                continue;
            }

            if (update.getNiceScore() != null) {
                updatedChild.addNiceScore(update.getNiceScore());
            }
            updatedChild.addPreferences(update.getGiftsPreferences());
        }
    }

    /**
     * If the round index is valid and not 0 (when the annual changes stage is skipped),
     * the budget unit is calculated and every child receives the gifts Santa can provide
     * based on their preferences
     * @param round index starting from 0 (initial round)
     * @return the generated annual report of the children states
     */
    public List<AnnualChildReport> simulateRound(final int round) {
        List<AnnualChildReport> report = new ArrayList<>();

        if (round > numberOfYears || round < 0) {
            return report;
        } else if (round != 0) {
            implementAnnualChange(annualChanges.get(round - 1));
        }

        //Calculate budgets
        childrenBudgets.clear();
        calculateChildrenBudgets();

        //Assign gifts
        for (var child : children) {
            assignGiftsToChild(child, childrenBudgets.get(child));
        }

        //Apply yellow elf and create annual report
        var yellowElf = new YellowElf(this);
        for (var child : children) {
            child.accept(yellowElf);
            report.add(new AnnualChildReport(child, childrenBudgets.get(child)));
        }

        return report;
    }

    @Override
    public String toString() {
        return "Database{"
                + "numberOfYears=" + numberOfYears
                + ", santaBudget=" + santaBudget
                + ",\nchildren=" + children
                + ",\ngifts=" + gifts
                + ",\nannualChanges=" + annualChanges + '}';
    }
}
