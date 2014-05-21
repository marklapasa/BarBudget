package net.lapasa.barbudget.models;

import java.util.Date;

public class PeriodModel
{
	public static final int DAILY = 86400000;
	public static final int WEEKLY = DAILY * 7;
	public static final int MONTHLY = DAILY * 30;
	public static final int QUARTERLY = DAILY * 90;
	public static final int ANNUALLY = DAILY * 356;

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

		if (diff < DAILY)
		{
			this.type = DAILY;
		}
		else if (diff < WEEKLY)
		{
			this.type = WEEKLY;
		}
		else if (diff < MONTHLY)
		{
			this.type = MONTHLY;
		}
		else if (diff < QUARTERLY)
		{
			this.type = QUARTERLY;
		}
		else
		{
			this.type = ANNUALLY;
		}
	}

	public int getPeriodType()
	{
		return type;
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
