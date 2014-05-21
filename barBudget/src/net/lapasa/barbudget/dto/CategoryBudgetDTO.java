package net.lapasa.barbudget.dto;

import java.util.List;

import javax.inject.Singleton;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.CategoryBudget;

@Singleton
public class CategoryBudgetDTO
{

	/**
	 * Return budget for target period type and category
	 * 
	 * @param periodModel
	 * @param existingCategory
	 * @return
	 */
	public CategoryBudget getBudget(int periodType, Category existingCategory)
	{
		String whereClause = "period_type = " + periodType + " AND category = " + existingCategory.getId();
        String[] whereArgs = null;
        String orderBy = null;
		List<CategoryBudget> results = CategoryBudget.find(CategoryBudget.class, whereClause, whereArgs, null, orderBy, null);
		
		if (results.size() > 0)
		{
			return results.get(0);
		}
		else
		{
			return null;
		}	
	}
	
	/**
	 * Persist target budget by deleting an existing one with matching period type and category
	 * 
	 * @param budget
	 */
	public void setBudget(CategoryBudget budget)
	{
		String whereClause = "category = " + budget.getCategory().getId() + " AND period_type = " + budget.getPeriodType();
        String[] whereArgs = null;
        String orderBy = null;

		List<CategoryBudget> budgets = CategoryBudget.find(CategoryBudget.class, whereClause, whereArgs, null, orderBy, null);
		
		if (budgets.size() == 0)
		{
			budget.save();
		}
		else
		{
			// Remove existing budget
			for (CategoryBudget cBudget: budgets)
			{
				cBudget.delete();
			}
			budget.save();
			return;
		}
	}	

}
