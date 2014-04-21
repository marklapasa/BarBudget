package test.net.lapasa.barbduget.dto;

import net.lapasa.barbudget.dto.CategoryDTO;
import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.Entry;
import android.content.Context;
import android.test.AndroidTestCase;

import com.orm.SugarApp;

/**
 * 1) Delete no records [OK]
 * 2) Create record, delete record [OK]
 * 3) Create, update, read, Delete record [OK]
 *
 * Created by mlapasa on 4/4/14.
 */
public class CategoryDTOTest extends AndroidTestCase
{
    public static final String DUMMY = "DUMMY";
    public static final int COLOR_RED = 0xFF0000;
    private static final String TAG = CategoryDTO.class.getName();
    private CategoryDTO dto;
    private Category targetCategory0;


    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        this.dto = new CategoryDTO();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();

        if (targetCategory0 != null)
        {
            dto.deleteCategory(targetCategory0, true);
        }

        dto = null;



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

    public void testDeleteNothing() throws Exception
    {
        SugarApp sugarApp = SugarApp.getSugarContext();
        assertNotNull(sugarApp);


        dto.deleteCategory(null, false);
        dto.deleteCategory(null, true);
    }

    public void testCreateRecordDeleteRecord() throws Exception
    {
        // Create record
        createNonPersistedRecord();
        createPersistedRecord();

    }

    /**
     * Use CategoryDTO to save a record for real
     */
    private void createPersistedRecord()
    {
        targetCategory0 = dto.create(DUMMY, COLOR_RED);
        assertNotNull(targetCategory0);
        assertTrue(targetCategory0.getId() >= 0);
    }

    private void createNonPersistedRecord()
    {
        Category targetCategory0 = new Category(DUMMY, COLOR_RED);
        assertNull(targetCategory0.getId());
    }

    public void testCreateUpdateDeleteRecord() throws Exception
    {
        createPersistedRecord();

        assertEquals(targetCategory0.getSum(), 0, 0);

        double dummyVal = 99999.99;
        targetCategory0.setSum(dummyVal);
        dto.update(targetCategory0);
        long tmpID = targetCategory0.getId();
        targetCategory0 = null;
        assertNull(targetCategory0);

        targetCategory0 = dto.getCategory(tmpID);
        assertNotNull(targetCategory0);
        assertEquals(targetCategory0.getSum(),dummyVal);

        dto.deleteCategory(targetCategory0, false);

        targetCategory0 = dto.getCategory(tmpID);
        assertNull(targetCategory0);
    }
}
