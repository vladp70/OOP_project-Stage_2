package santareplacer;

import children.Child;
import elves.BlackElf;
import elves.PinkElf;
import elves.WhiteElf;
import elves.YellowElf;
import enums.AgeGroup;
import enums.Category;
import enums.CityStrategyEnum;
import fileinput.AnnualChange;
import fileoutput.AnnualChildReport;
import fileinput.Input;
import gifts.Gift;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class Database {
    private static Database instance = null;
    private Integer numberOfYears;
    private Double santaBudget;
    private List<Child> children;
    private List<Gift> gifts;
    private List<AnnualChange> annualChanges;
    private Double budgetUnit;
    private CityStrategyEnum strategy = null;
    private GiftingStrategy giftingStrategy = null;
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

    public Map<Child, Double> getChildrenBudgets() {
        return childrenBudgets;
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
        setStrategy(CityStrategyEnum.ID);
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

    public void setStrategy(final CityStrategyEnum newStrategy) {
        strategy = newStrategy;
        giftingStrategy = GiftingStrategyFactory
                .getInstance()
                .createStrategy(strategy);
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

    /**
     * Finds the gift with the smallest price in Santa's inventory
     * from a specific category (in stock or independent of stock)
     * @param category cheapest gift should be searched in
     * @param inStock is true if stock should be taken in consideration
     *                and false if found gift doesn't need to be in stock
     * @return found gift or null if there is none available
     */
    public Gift findCheapestGiftByCategory(final Category category, final boolean inStock) {
        if (category == null) {
            return null;
        }

        Gift cheapestGift = null;

        for (var gift : gifts) {
            if (gift.getCategory().equals(category)
                    && (gift.getQuantity() > 0 || !inStock)) {
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

            Gift gift = findCheapestGiftByCategory(preference, true);
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

    /**
     * Calculates budget unit then each child's assigned budget (stored in a map)
     * and sets in motion the actions of the white, pink and black elf that influence
     * children's budgets
     */
    public void calculateChildrenBudgets() {
        childrenBudgets.clear();

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
     * updates gifting strategy then implements children updates by
     * finding the child in the database, adding the new nice score
     * (if existing) and adds their new preferences
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
        setStrategy(changes.getStrategy());

        for (var update : changes.getChildrenUpdates()) {
            Child updatedChild = findChildByID(update.getId());
            if (updatedChild == null) {
                continue;
            }

            if (update.getNiceScore() != null) {
                updatedChild.addNiceScore(update.getNiceScore());
            }
            updatedChild.addPreferences(update.getGiftsPreferences());
            updatedChild.setElf(update.getElf());
        }
    }

    /**
     * If the round index is valid and not 0 (when the annual changes stage is skipped),
     * the budget unit is calculated and every child receives the gifts Santa can provide
     * based on their preferences and the elves' effects
     * @param round index starting from 0 (initial round)
     * @return the generated annual report of the children states
     */
    public List<AnnualChildReport> simulateRound(final int round) {
        List<AnnualChildReport> report = new ArrayList<>();

        if (round > numberOfYears || round < 0) {
            return report;
        } else if (round == 0) {
            setStrategy(CityStrategyEnum.ID);
        } else {
            implementAnnualChange(annualChanges.get(round - 1));
        }

        //Calculate budgets
        calculateChildrenBudgets();

        //Apply gifting strategy and assign gifts
        giftingStrategy.apply(this);

        //Apply yellow elf and create annual report
        var yellowElf = new YellowElf(this);
        Collections.sort(children);
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
