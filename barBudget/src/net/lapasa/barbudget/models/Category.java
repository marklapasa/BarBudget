package net.lapasa.barbudget.models;

import com.orm.SugarRecord;

/**
 * Created by mlapasa on 3/31/14.
 */
public class Category extends SugarRecord<Category>
{
    private String name;
    private int color;
//    private List<CategoryBudget> budgets;
    private double sum;

    /**
     * Empty constructor needed for SugarORM 1.3
     */
    public Category(){}

    public Category(String name, int color)
    {
        this.name = name;
        this.color = color;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getColor()
    {
        return color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    /*
    public List<CategoryBudget> getBudgets()
    {
        return budgets;
    }

    public void setBudget(List<CategoryBudget> budgets)
    {
        this.budgets = budgets;
    }
    */

    public double getSum()
    {
        return sum;
    }

    public void setSum(double sum)
    {
        this.sum = sum;
    }
}
