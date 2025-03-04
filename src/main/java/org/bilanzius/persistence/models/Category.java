package org.bilanzius.persistence.models;

import java.math.BigDecimal;

public class Category {

    public static Category create(User user, String name, BigDecimal budget) {
        return new Category(
                0, user.getId(), name, budget, new BigDecimal("0.0")
        );
    }

    private final int categoryId;
    private final int userId;
    private String name;
    private BigDecimal budget;
    private BigDecimal amountSpent;

    public Category(int categoryId, int userId, String name, BigDecimal budget, BigDecimal amountSpent) {
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

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(BigDecimal amountSpent) {
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
