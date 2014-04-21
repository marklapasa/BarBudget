package net.lapasa.barbudget.models;

import java.util.Date;

/**
 * Created by mlapasa on 3/31/14.
 */
public class PeriodModel
{
    public static final int WEEK = 7;
    public static final int MONTH = 30;
    public static final int QUARTER = 90;
    public static final int ANNUAL = 356;

    private Date periodStart;
    private Date periodEnd;
    private int type;

    public PeriodModel(Date periodStart, Date periodEnd)
    {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;

        setPeriodType();
    }

    private void setPeriodType()
    {
        long diff = periodEnd.getTime() - periodStart.getTime();

        if (diff < WEEK)
        {
            this.type = WEEK;
        }
        else if (diff < MONTH)
        {
            this.type = MONTH;
        }
        else if (diff < QUARTER)
        {
            this.type = QUARTER;
        }
        else
        {
            this.type = ANNUAL;
        }
    }

    public Date getPeriodEnd()
    {
        return periodEnd;
    }

    public Date getPeriodStart()
    {
        return periodStart;
    }
}
