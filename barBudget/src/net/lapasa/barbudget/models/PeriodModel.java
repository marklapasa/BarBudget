package net.lapasa.barbudget.models;

import java.util.Calendar;
import java.util.Date;

public class PeriodModel
{
	public static final int DAILY = 86400000;
	public static final int WEEKLY = DAILY * 7;
	public static final int MONTHLY = DAILY * 30;
	public static final int QUARTERLY = DAILY * 90;
	public static final int ANNUALLY = DAILY * 356;

	public static final int RANGE_TODAY = 0;
	public static final int RANGE_LAST_7_DAYS = 1;
	public static final int RANGE_CURRENT_WEEK = 2;
	public static final int RANGE_LAST_30_DAYS = 3;
	public static final int RANGE_CURRENT_MONTH = 4;
	public static final int RANGE_CURRENT_QUARTER = 5;
	public static final int RANGE_LAST_90_DAYS = 6;
	public static final int RANGE_CURRENT_YEAR = 7;
	public static final int RANGE_LAST_365_DAYS = 8;

	public static final int[] RANGE_TYPES =
	{ 
		RANGE_TODAY, 
		RANGE_CURRENT_MONTH, 
		RANGE_CURRENT_QUARTER, 
		RANGE_CURRENT_WEEK, 
		RANGE_CURRENT_YEAR, 
		RANGE_LAST_30_DAYS, 
		RANGE_LAST_365_DAYS, 
		RANGE_LAST_7_DAYS, 
		RANGE_LAST_90_DAYS };

	private Calendar periodStart;
	private Calendar periodEnd;
	private int type;

	public static PeriodModel getModelByPeriodType(int range)
	{

		Calendar start = Calendar.getInstance();
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		Calendar end = Calendar.getInstance();
		// Tomorrow at midnight
		end.set(Calendar.DAY_OF_MONTH, end.get(Calendar.DAY_OF_MONTH) + 1); 
		end.set(Calendar.HOUR_OF_DAY, 0);
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.SECOND, 0);
		
		String s = start.getTime().toString() + " | " + end.getTime().toString();
		String s1 = start.getTimeInMillis() + " | " + end.getTimeInMillis();
		switch (range)
		{
		case RANGE_LAST_7_DAYS:
			// End is today
			// Start is 7 days ago
			start.set(Calendar.DAY_OF_MONTH, start.get(Calendar.DAY_OF_MONTH) - 7);
			break;
		case RANGE_LAST_30_DAYS:
			// End is today
			// Start is 30 days ago
			start.set(Calendar.DAY_OF_MONTH, start.get(Calendar.DAY_OF_MONTH) - 30);
			break;
		case RANGE_LAST_90_DAYS:
			// End is today
			// Start is 90 days ago
			start.set(Calendar.DAY_OF_MONTH, start.get(Calendar.DAY_OF_MONTH) - 90);
		case RANGE_LAST_365_DAYS:
			// End is today
			// Start is 365 days ago
			start.set(Calendar.DAY_OF_MONTH, start.get(Calendar.DAY_OF_MONTH) - 365);
			break;
		case RANGE_CURRENT_WEEK:
			// End is today
			// Start is on Sunday of this week

			// What day is Sunday? - http://stackoverflow.com/a/1900666/855984
			final int currentDayOfWeek = (start.get(Calendar.DAY_OF_WEEK) + 7 - start.getFirstDayOfWeek()) % 7;
			start.add(Calendar.DAY_OF_YEAR, -currentDayOfWeek);
			break;
		case RANGE_CURRENT_MONTH:
			// End is today
			// Start is the first day of the month
			start.set(Calendar.DAY_OF_MONTH, 1);
		case RANGE_CURRENT_QUARTER:
			// End is today

			// 1) Get current month in relation to quarter
			int currentMonth = start.get(Calendar.MONTH);
			if (currentMonth >= Calendar.JANUARY && currentMonth <= Calendar.MARCH)
			{
				// Q1
				start.set(start.get(Calendar.YEAR), Calendar.JANUARY, 1);
			}
			else if (currentMonth >= Calendar.APRIL && currentMonth <= Calendar.JUNE)
			{
				// Q2
				start.set(start.get(Calendar.YEAR), Calendar.APRIL, 1);
			}
			else if (currentMonth >= Calendar.JULY && currentMonth <= Calendar.SEPTEMBER)
			{
				// Q3
				start.set(start.get(Calendar.YEAR), Calendar.JULY, 1);
			}
			else if (currentMonth >= Calendar.OCTOBER && currentMonth <= Calendar.DECEMBER)
			{
				// Q4
				start.set(start.get(Calendar.YEAR), Calendar.OCTOBER, 1);
			}

			break;
		case RANGE_CURRENT_YEAR:
			// End is today

			// Start is first day of current year
			start.set(start.get(Calendar.YEAR), Calendar.JANUARY, 1);
			break;
		case RANGE_TODAY:
		default:
			// Default to today
			break;
		}
		return new PeriodModel(start, end);
	}

	public PeriodModel(Calendar periodStart, Calendar periodEnd)
	{
		this.periodStart = periodStart;
		this.periodEnd = periodEnd;

		setPeriodType();
	}

	private void setPeriodType()
	{
		long diff = periodEnd.getTimeInMillis() - periodStart.getTimeInMillis();

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

	public Calendar getPeriodEnd()
	{
		return periodEnd;
	}

	public Calendar getPeriodStart()
	{
		return periodStart;
	}
}
