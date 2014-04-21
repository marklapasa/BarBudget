package test.net.lapasa.barbduget.dto;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

import net.lapasa.barbudget.dto.CategoryDTO;
import net.lapasa.barbudget.dto.EntryDTO;
import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.Entry;

import java.util.Date;

/**
 * Created by mlapasa on 4/4/14.
 */
public class EntryDTOTest extends AndroidTestCase
{
    private static final String TAG = EntryDTOTest.class.getName();
    private Context context;
    private CategoryDTO categoryDTO;
    private Category category;
    private EntryDTO entryDTO;

    public void setUp() throws Exception
    {
        super.setUp();

        context =  getContext(); //getInstrumentation().getContext();
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
//        assertTrue(entry.getId() >= 0);

        entryDTO.create(timestamp, testVal, memo, category);

    }

    public void testUpdate() throws Exception
    {
        fail();
    }

    public void testDelete() throws Exception
    {
        fail();
    }

    public void testDeleteUnderCategory() throws Exception
    {
        fail();
    }

    public void testGetEntries() throws Exception
    {
        fail();
    }
}
