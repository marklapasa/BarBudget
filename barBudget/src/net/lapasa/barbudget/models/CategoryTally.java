package net.lapasa.barbudget.models;

import java.util.Calendar;

import com.orm.SugarRecord;

public class CategoryTally extends SugarRecord<CategoryTally>
{
	private int periodType;
	private Calendar startDate;
	private Calendar endDate;
	private Category category;
	private double value;
	
	/**
     * Empty constructor needed for SugarORM 1.3
     */
	public CategoryTally(){}
	
	public CategoryTally(int periodType, Calendar startDate, Calendar endDate, Category category, double value)
	{
		this.periodType = periodType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.category = category;
		this.value = value;
	}

	public int getPeriodType()
	{
		return periodType;
	}

	public void setPeriodType(int periodType)
	{
		this.periodType = periodType;
	}

	public Calendar getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Calendar startDate)
	{
		this.startDate = startDate;
	}

	public Calendar getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Calendar endDate)
	{
		this.endDate = endDate;
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	public double getValue()
	{
		return value;
	}

	public void setValue(double value)
	{
		this.value = value;
	}
	
}