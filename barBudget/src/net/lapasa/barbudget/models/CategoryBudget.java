package net.lapasa.barbudget.models;

import com.orm.SugarRecord;

/**
 * Created by mlapasa on 3/31/14.
 */
public class CategoryBudget extends SugarRecord<CategoryBudget>
{
    private int periodType;
    private int value;
    
    /**
     * Empty constructor needed for SugarORM 1.3
     */
    public CategoryBudget(){}


    public CategoryBudget(int periodType, int value)
    {
        this.periodType = periodType;
        this.value = value;
    }

    public int getPeriodModel()
    {
        return periodType;
    }

    public void setPeriodModel(int periodType)
    {
        this.periodType = periodType;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }
}
