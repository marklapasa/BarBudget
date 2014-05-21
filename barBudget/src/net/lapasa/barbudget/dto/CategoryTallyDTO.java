package net.lapasa.barbudget.dto;

import java.util.List;

import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.CategoryTally;
import net.lapasa.barbudget.models.Entry;
import net.lapasa.barbudget.models.PeriodModel;
import android.util.SparseArray;


public class CategoryTallyDTO
{
	protected EntryDTO entryDTO;
	
	public CategoryTallyDTO(EntryDTO entryDTO)
	{
		this.entryDTO = entryDTO;
	}
	
	
	public SparseArray<CategoryTally> getTallyForCategory(Category existingCategory)
	{
		SparseArray<CategoryTally> map = new SparseArray<CategoryTally>();
		List<CategoryTally> tallies = getTalliesByCategory(existingCategory);
		if (tallies != null && !tallies.isEmpty())
		{
			for (CategoryTally tally : tallies)
			{
				map.put(tally.getPeriodType(), tally);
			}
		}
		return map; 
	}
	
	public CategoryTally getTally(int targetRange, Category existingCategory)
	{
		String whereClause = "category = " + existingCategory.getId() + " AND period_type = " + targetRange;
		String[] whereArgs = null;
        String orderBy = null;
		List<CategoryTally> results = CategoryTally.find(CategoryTally.class, whereClause, whereArgs, null, orderBy, null);
		if (results.size() == 0)
		{
			return null;
		}
		else
		{
			return results.get(0);
		}		
	}
	
	/**
	 * This could potentially be a long running operation. Might need to refactor this out into a dedicated
	 * service in the future
	 * 
	 * @param existingCategory
	 */
	public void updateTallies(Category existingCategory)
	{
		// Recalculate each one
		for (int i = 0; i < PeriodModel.RANGE_TYPES.length; i++)
		{
			int targetRange = PeriodModel.RANGE_TYPES[i];
			PeriodModel periodModel = PeriodModel.getModelByPeriodType(targetRange);
			
			double tallyValue = 0;
			List<Entry> entries = entryDTO.getEntries(existingCategory, periodModel.getPeriodStart().getTime(), periodModel.getPeriodEnd().getTime());
			for (Entry entry : entries)
			{
				tallyValue += entry.getValue();
			}
			
			// Persist values
			CategoryTally tally = getTally(targetRange, existingCategory);
			if (tally == null)
			{
				// Create a new one in lieu of an existing one
				tally = new CategoryTally(targetRange, periodModel.getPeriodStart(), periodModel.getPeriodEnd(), existingCategory, tallyValue);
			}
			else
			{
				// Update existing tally
				tally.setStartDate(periodModel.getPeriodStart());
				tally.setEndDate(periodModel.getPeriodEnd());
				tally.setValue(tallyValue);
			}
			tally.save();
		}
	}
	
	
	/**
	 * Delete existing tally records that match this category
	 * 
	 * @param existingCategory
	 */
	public void deleteCategoryTallies(Category existingCategory)
	{
		List<CategoryTally> tallies = getTalliesByCategory(existingCategory);
		if (tallies != null && !tallies.isEmpty())
		{
			for (CategoryTally tally : tallies)
			{
				tally.delete();
			}
		}
	}
	
	/**
	 * Query for matching tally records who belong to target category
	 * 
	 * @param existingCategory
	 * @return
	 */
	private List<CategoryTally> getTalliesByCategory(Category existingCategory)
	{
		String whereClause = "category = " + existingCategory.getId();
		String[] whereArgs = null;
        String orderBy = null;
		List<CategoryTally> results = CategoryTally.find(CategoryTally.class, whereClause, whereArgs, null, orderBy, null);
		if (results.size() == 0)
		{
			return null;
		}
		else
		{
			return results;
		}
	}
}
