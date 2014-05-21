package net.lapasa.barbudget.models;

import com.orm.SugarRecord;

/**
 * Created by mlapasa on 3/31/14.
 */
public class CategoryBudget extends SugarRecord<CategoryBudget>
{
    private int periodType;
    private double value;
    private Category category;
    
    /**
     * Empty constructor needed for SugarORM 1.3
     */
    public CategoryBudget(){}


    public CategoryBudget(int periodType, double value, Category category)
    {
        this.periodType = periodType;
        this.value = value;
        this.category = category;
    }

    public int getPeriodType()
    {
        return periodType;
    }

    public void setPeriodType(int periodType)
    {
        this.periodType = periodType;
    }

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }
    
    public void setCategory(Category category)
    {
    	this.category = category;
    }
    
    public Category getCategory()
    {
    	return category;
    }
}
