package org.bilanzius.persistence.models;

public class Category {

    public static Category create(User user, String name, Double budget) {
        return new Category(
                0, user.getId(), name, budget, 0.00
        );
    }

    private final int categoryId;
    private final int userId;
    private String name;
    private Double budget;
    private Double amountSpent;

    public Category(int categoryId, int userId, String name, Double budget, Double amountSpent) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.name = name;
        this.budget = budget;
        this.amountSpent = amountSpent;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(Double amountSpent) {
        this.amountSpent = amountSpent;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", budget='" + budget + '\'' +
                ", amountSpent='" + amountSpent + '\'' +
                '}';
    }
}
