package test.net.lapasa.barbduget.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.lapasa.barbudget.dto.CategoryDTO;
import net.lapasa.barbudget.dto.EntryDTO;
import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.Entry;

import org.junit.Ignore;
import org.junit.Test;

import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by mlapasa on 4/4/14.
 */
public class EntryDTOTest extends AndroidTestCase
{
    private static final String TAG = EntryDTOTest.class.getName();
	private static final double DUMMY_VALUE = 2014.04;
	private static final String DUMMY_MEMO = "Dummy Memo";
    private CategoryDTO categoryDTO;
    private Category category;
    private EntryDTO entryDTO;

    public void setUp() throws Exception
    {
        super.setUp();

        this.categoryDTO = new CategoryDTO();

        // Create default category
        this.category = categoryDTO.create(CategoryDTOTest.DUMMY, CategoryDTOTest.COLOR_RED);

        this.entryDTO = new EntryDTO();

    }

    public void tearDown() throws Exception
    {
        try
        {
            Category.deleteAll(Category.class);
            Entry.deleteAll(Entry.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void testCreate() throws Exception
    {
        Entry entry = null;

        Date timestamp = new Date();
        double testVal = 2014.04;
        String memo = "Hello World";
        entry = new Entry(timestamp, testVal, memo, category);
        assertNotNull(entry);

        assertNull(entry.getId());

        entry = null;

        entry = entryDTO.create(timestamp, testVal, memo, category);
        assertNotNull(entry.getId());

        Log.i(TAG, "Entry ID = " + entry.getId());

        entryDTO.create(timestamp, testVal, memo, category);

    }

    /**
     * Create a record, modify fields, save the record, verify if the changes were persisted
     * @throws Exception
     */
    public void testUpdate() throws Exception
    {
        Entry entry = entryDTO.create(new Date(), DUMMY_VALUE, DUMMY_MEMO, category);
        assertNotNull(entry);
        assertEquals(DUMMY_VALUE, entry.getValue());
        
        entry.setValue(entry.getValue() * 2);
        entry.save();
        
        entry = null;
        assertNull(entry);
        
        entry = entryDTO.getEntries(category, null, null).get(0);
        assertNotNull(entry);
        assertEquals(DUMMY_VALUE * 2, entry.getValue());
        
    }

    
    /*
     * Create Entry record, delete Entry record
     */
    public void testDelete() throws Exception
    {
        Entry entry = new Entry(new Date(), DUMMY_VALUE, DUMMY_MEMO, category);
        entry.save();
        assertNotNull(entry);
        entry = null;
        assertNull(entry);
        
        List<Entry> entries = Entry.find(Entry.class, "value = ?", String.valueOf(DUMMY_VALUE));
        assertEquals(1, entries.size());
        
        entry = entries.get(0);
        assertNotNull(entry);
        
        assertNotNull(entry.getId());
        entry.delete();
        
        // Very that it is not in the DB anymore
        entries = Entry.find(Entry.class, "value = ?", String.valueOf(DUMMY_VALUE));
        assertEquals(0, entries.size());
        
        
        
        
        
    }

    /**
     * Create Entry records for a category, then delete the category
     * The records associated to that category should no longer exist
     * @throws Exception
     */
    public void testDeleteUnderCategory() throws Exception
    {
//        fail();
    }

    
    /**
     * Create entries, retrive entries
     * @throws Exception
     */
    public void testGetEntries() throws Exception
    {
        Entry entry = new Entry(new Date(), DUMMY_VALUE, null, category);
        entry.save();
        
        entry = new Entry(new Date(), DUMMY_VALUE, null, category);
        entry.save();
        
        entry = new Entry(new Date(), DUMMY_VALUE, null, category);
        entry.save();
        
        List<Entry> entries = entryDTO.getEntries(category, null, null);
        assertEquals(3, entries.size());
        
        
        
        Entry.deleteAll(Entry.class);
        
        
        // Test for start date only query
        
        /*
        entries = entryDTO.getEntries(category, null, null);
        assertEquals(0, entries.size());
        
        Calendar c0 = Calendar.getInstance();
        c0.set(Calendar.DAY_OF_MONTH, c0.get(Calendar.DAY_OF_MONTH) - 1);
        entry = new Entry(c0.getTime(), DUMMY_VALUE, null, category);
        entry.save();
        
        
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
        Date A_YEAR_AGO = c.getTime();
		entry = new Entry(A_YEAR_AGO, DUMMY_VALUE, null, category);
        entry.save();

        
        // Get a date from a month ago
        Calendar ONE_MONTH_AGO = Calendar.getInstance();
        ONE_MONTH_AGO.set(Calendar.MONTH, ONE_MONTH_AGO.get(Calendar.MONTH) - 1);
          
        entries = entryDTO.getEntries(category, ONE_MONTH_AGO.getTime(), null);
        assertEquals(1, entries.size());
        
        */
    }
}
