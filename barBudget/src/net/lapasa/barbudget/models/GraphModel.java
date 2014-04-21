package net.lapasa.barbudget.models;

import java.util.Date;
import java.util.List;

/**
 * Created by mlapasa on 3/31/14.
 */
public class GraphModel
{
    private Date periodStart;
    private Date periodEnd;
    private List<Category> categories;
    private int periodSum;

    public Date getPeriodStart()
    {
        return periodStart;
    }

    public void setPeriodStart(Date periodStart)
    {
        this.periodStart = periodStart;
    }

    public Date getPeriodEnd()
    {
        return periodEnd;
    }

    public void setPeriodEnd(Date periodEnd)
    {
        this.periodEnd = periodEnd;
    }

    public List<Category> getCategories()
    {
        return categories;
    }

    public void setCategories(List<Category> categories)
    {
        this.categories = categories;
    }

    public int getPeriodSum()
    {
        return periodSum;
    }

    public void setPeriodSum(int periodSum)
    {
        this.periodSum = periodSum;
    }
}
