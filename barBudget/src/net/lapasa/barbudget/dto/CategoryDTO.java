package net.lapasa.barbudget.dto;

import android.content.Context;
import android.util.Log;

import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.Entry;
import net.lapasa.barbudget.models.SortRule;

import java.util.Date;
import java.util.List;

/**
 * Created by mlapasa on 3/31/14.
 */
public class CategoryDTO
{
    private static final String TAG = CategoryDTO.class.getName();
    private Context ctx;
    private EntryDTO entryDTO;

    /**
     * Constructor
     */
    public CategoryDTO()
    {
        this.entryDTO = new EntryDTO();
    }


    /**
     * Persist a Category to the DB
     * @param name
     * @param color
     * @return
     */
    public Category create(String name, int color)
    {
        Category category = new Category(name, color);
        category.save();

        return category;
    }

    /**
     * Save changes to the Category record
     *
     * @param category
     */
    public void update(Category category)
    {
        category.save();
    }

    /**
     * Get list of categories sorted alphabetically, lowest to highest or highest to lowest
     * @param sortRule
     * @return
     */
    public List<Category> getCategories(int sortRule)
    {
        String whereClause = null;
        String[] whereArgs = null;
        String orderBy = null;

        switch(sortRule)
        {
            case SortRule.SORT_ALPHABETICALLY:
                orderBy = "name ASC";
                break;
            case SortRule.SORT_LOW_TO_HIGH:
                orderBy = "sum ASC";
                break;
            case SortRule.SORT_HIGH_TO_LOW:
                orderBy = "sum DESC";
                break;
        }

        List<Category> categories = Category.find(Category.class, whereClause, whereArgs, null, orderBy, null);
        return categories;
    }


    public void deleteCategory(Category category, boolean cascade)
    {
        if(category == null)
        {
            Log.e(TAG, "You cannot delete a null Category");
            return;
        }
        if (cascade)
        {
            // Delete all entries associated to this category before deleting the category itself
            entryDTO.deleteUnderCategory(category);
        }
        category.delete();
    }

    public int getCategorySum(Category category, Date periodStart, Date periodEnd)
    {
        int categorySum = 0;

        // Get all entries for the category, between start and end dates
        List<Entry> entries = entryDTO.getEntries(category, periodStart, periodEnd);


        for(Entry entry : entries)
        {
            categorySum += entry.getValue();
        }


        // The reason why the sum is saved is to aid sorting
        category.setSum(categorySum);
        category.save();

        return categorySum;
    }

    public Category getCategory(Long id)
    {
        List<Category> category = Category.findWithQuery(Category.class, "SELECT * from Category WHERE id = ?", String.valueOf(id));
        if (category != null && category.size() > 0)
        {
            Log.i(TAG, "Found " + category.size() + " matches.");
            return category.get(0);
        }
        else
        {
            Log.e(TAG, "No categories found that match id = " + String.valueOf(id));
            return null;
        }
    }
}