package net.lapasa.barbudget.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by mlapasa on 3/31/14.
 */
public class Category extends SugarRecord<Category>
{
    private String name;
    private int color;
    
    @Ignore
    private double sum;

    @Ignore
    private double highestSum;
    
    @Ignore
	private double budget;

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

    public double getSum()
    {
        return sum;
    }

    public void setSum(double sum)
    {
        this.sum = sum;
    }

	public double getHighestSum()
	{
		return highestSum;
	}

	public void setHighestSum(double highestSum)
	{
		this.highestSum = highestSum;
	}

	public void setBudget(double budget)
	{
		this.budget = budget;
	}
	
	public double getBudget()
	{
		return budget;
	}
}
