package net.lapasa.barbudget.dto;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.Entry;
import net.lapasa.barbudget.models.PeriodModel;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * Created by mlapasa on 3/31/14.
 */
public class EntryDTO
{
    private static final String TAG = EntryDTO.class.getName();

    public Entry create(Date timestamp, double value, String memo, Category category)
    {
        Entry entry = new Entry(timestamp, value, memo, category);
        entry.save();

        Log.i(TAG, "Entry created: " + value);
        return entry;
    }

    public void update(Entry entry)
    {
        if (entry.getId() != null)
        {
            entry.save();
            Log.i(TAG, "Entry updated successfully");
        }
        else
        {
            Log.e(TAG, "Entry update failed. This record has not been persisted yet");
        }
    }

    public void delete(Entry entry)
    {
        if (entry.getId() != null)
        {
            entry.delete();
            Log.i(TAG, "Entry delete successful");
        }
        else
        {
            Log.e(TAG, "Entry delete failed. This record was not persisted to begin with");
        }
    }

    /**
     * Delete all entries that belong to target category
     *
     * @param category
     */
    public static void deleteUnderCategory(Category category)
    {
        // find(Class<T> type, String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit)

        long categoryId = category.getId();
        String whereClause = "id = " + categoryId;
        String[] whereArgs = null;
        String groupBy = null;
        String orderBy = null;
        String limit = null;

        List<Entry> entries = null;
        try
        {
            entries = Entry.find(Entry.class, whereClause, whereArgs, groupBy, orderBy, limit);
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
            return;
        }

        if (entries.size() > 0)
        {
            for(Entry e : entries)
            {
                Log.i(TAG, "Deleting Entry id = " + e.getId());
                e.delete(); // This is gonna be slow, ideally we want a bulk delete
            }
        }
        else
        {
            Log.e(TAG, "There are no entries that exist for Category.id = " + categoryId);
        }
    }

    public List<Entry> getEntries(Category category, Date periodStart, Date periodEnd)
    {
        // Select date FROM table WHERE date BETWEEN 2014/01/01 AND 2014/01/30

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        String startTimeStr = null;
        if (periodStart != null)
        {
        	startTimeStr = String.valueOf(periodStart.getTime());
        }
        
        	
        
        String endTimeStr = null;
        if (periodEnd != null)
        {
        	endTimeStr = String.valueOf(periodEnd.getTime());
        }
        
        String whereClause = "category = " + category.getId();
        
        if (startTimeStr != null && endTimeStr != null)
        {
        	if (isSameDay(periodStart, periodEnd))
        	{
        		endTimeStr = String.valueOf(periodEnd.getTime() + PeriodModel.DAILY); // Tomorrow
        	}
	        whereClause += " AND (timestamp BETWEEN " + startTimeStr + " AND " + endTimeStr + ")";
        }
        else if (startTimeStr == null && endTimeStr != null)
        {
        	// Get records from 0000-00-00 to endTimeStr
        	startTimeStr = "date('0000-01-01')";
        	whereClause += " AND (timestamp BETWEEN " + startTimeStr + " AND " + endTimeStr + ")";
        }
        else if (startTimeStr != null && endTimeStr == null)
        {
        	// Get records between startTimeStr and today
        	
        	Calendar tomorrow = Calendar.getInstance();
        	tomorrow.set(Calendar.DAY_OF_MONTH, tomorrow.get(Calendar.DAY_OF_MONTH) + 1);
        	
        	endTimeStr = sdf.format(tomorrow.getTime());
        	whereClause += " AND (timestamp BETWEEN date(" + startTimeStr + ") AND date(" + endTimeStr + "))";
        }
        
        String[] whereArgs = null;
        String groupBy = null;
        String orderBy = "timestamp";
        String limit = null;
        List<Entry> entries = Entry.find(Entry.class, whereClause, whereArgs, groupBy, orderBy, limit);

        return entries;
    }

	private boolean isSameDay(Date periodStart, Date periodEnd)
	{
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(periodStart);
		cal2.setTime(periodEnd);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
		                  cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);		
	}
}