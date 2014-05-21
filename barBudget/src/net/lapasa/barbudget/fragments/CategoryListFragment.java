package net.lapasa.barbudget.fragments;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.lapasa.barbudget.MainActivity;
import net.lapasa.barbudget.R;
import net.lapasa.barbudget.dto.CategoryBudgetDTO;
import net.lapasa.barbudget.dto.CategoryDTO;
import net.lapasa.barbudget.dto.CategoryTallyDTO;
import net.lapasa.barbudget.dto.EntryDTO;
import net.lapasa.barbudget.fragments.adapters.CategoryListAdapter;
import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.CategoryBudget;
import net.lapasa.barbudget.models.CategoryTally;
import net.lapasa.barbudget.models.Entry;
import net.lapasa.barbudget.models.PeriodModel;
import net.lapasa.barbudget.models.SortRule;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import dagger.Lazy;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class CategoryListFragment extends DaggerListFragment implements OnItemLongClickListener, OnItemClickListener
{
	private static final String TAG = CategoryFormFragment.class.getName();

	protected static final int ACTION_VIEW_ENTRIES = 0;

	@Inject
	protected Lazy<CategoryDTO> lazyCategoryDTO;

	@Inject
	protected Lazy<EntryDTO> lazyEntryDTO;
	
	@Inject
	protected Lazy<CategoryTallyDTO> lazyCategoryTallyDTO;
	
	protected CategoryDTO dto;
	private CategoryListAdapter adapter;
	private List<Category> categories;


	private Category categoryWithHighestTally;

	@Inject
	protected Lazy<CategoryBudgetDTO> lazyCategoryBudgetDTO;

	/**
	 * Constructor
	 */
	public CategoryListFragment()
	{
		super();
		setHasOptionsMenu(true);
		categories = new ArrayList<Category>();
		adapter = new CategoryListAdapter(categories);
		setListAdapter(adapter);
	}



	@Override
	public void onResume()
	{
		super.onResume();
		Drawable d = new ColorDrawable(Color.BLACK); // Default to black
		getActivity().getActionBar().setBackgroundDrawable(d);
		
		
		
		refresh();
	}

	private void refresh()
	{
		dto = lazyCategoryDTO.get();
		
		/* This will fetch all possible Categories */
		List<Category> categories = dto.getCategories(SortRule.SORT_ALPHABETICALLY);
		int size = categories.size();
		this.categories.clear();
		if (size > 0)
		{
			this.categories.addAll(categories);

			/* For each category, get the CategoryTally for target periodType and store it in the Category's Sum*/
			injectCategoryTallyForPeriod(PeriodModel.RANGE_TODAY);
			injectCategoryBudgetForPeriod(PeriodModel.RANGE_TODAY);
			
		}
		adapter.notifyDataSetChanged();

		if (size == 0)
		{
			setEmptyText("Please create a category");
		}

	}

	/**
	 * Provide category instances with enough information to draw a budget bar graph
	 * @param periodType
	 */	
	private void injectCategoryBudgetForPeriod(int periodType)
	{
		CategoryBudgetDTO categoryBudgetDTO = lazyCategoryBudgetDTO.get();
		for (Category category : categories)
		{
			// 1) Get Budget Object for each category
			CategoryBudget budget = categoryBudgetDTO.getBudget(periodType, category);
			
			// 2) Get budget value for target period type
			if (budget != null)
			{
				double value = budget.getValue();
				
				// 3) Store that value into the Category instance
				category.setBudget(value);
			}
		}
	}



	/**
	 * Provide category instances with enough information to draw a bar graph
	 * @param periodType
	 */
	private void injectCategoryTallyForPeriod(int periodType)
	{
		categoryWithHighestTally = null;
		
		CategoryTallyDTO categoryTallyDTO = lazyCategoryTallyDTO.get();
		for (Category category : categories)
		{
			// 1) Get Tally Object For Category
			CategoryTally tally = categoryTallyDTO.getTally(periodType, category);
			
			// 2) Get tally Value for target period type
			if (tally != null)
			{
				double value = tally.getValue();
				// 3) Store that type into the Category instance
				category.setSum(value);

				// 4) If this sum is greater than the highest category's sum,
				// remember this category
				if (categoryWithHighestTally == null)
				{
					categoryWithHighestTally = category;
				}
				else
				{
					if (tally.getValue() > categoryWithHighestTally.getSum())
					{
						categoryWithHighestTally = category;
					}
				}
			}
		}
		
		if (categoryWithHighestTally != null)
		{
			for (Category category : categories)
			{
				category.setHighestSum(categoryWithHighestTally.getSum());
			}

			Crouton.makeText(getActivity(), "HIGHEST: " + categoryWithHighestTally.getName() + " @ " + categoryWithHighestTally.getSum(), Style.INFO).show();
		}
	}



	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		menu.clear();
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.category, menu);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_add_category:
			addNewCategory();
			return true;
		case R.id.action_delete_all_categories:
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Are you sure you want to delete all categories?");
			builder.setPositiveButton("OK", new OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					dto.deleteAll();
					refresh();
				}
			});
			builder.setNegativeButton("Cancel", null);
			builder.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	private void addNewCategory()
	{
		((MainActivity)getActivity()).showFragment(CategoryFormFragment.create(null));
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	
		/* If there are no categories shown, lure user to create one */
		View v = getView();
		v.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (categories.size() == 0)
				{
					addNewCategory();
				}
			}
		});		
		
		/* Configure long item press on list */
		getListView().setOnItemLongClickListener(this);
		
		/* Configure short item press on list */
		getListView().setOnItemClickListener(this);
		
		getListView().setDivider(null);
	}

	
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int index, long arg3)
	{		
		final Category selectedCategory = categories.get(index);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Choose Action");
		final CharSequence[] items 
			= new CharSequence[]
					{getString(R.string.action_view_entries),
					 getString(R.string.action_update_category),
					 getString(R.string.action_delete_category),
					 getString(R.string.action_set_budget)};
		
		builder.setItems(items, new OnClickListener()
		{			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String label = (String) items[which];
				Fragment targetFrag = null;
				if (label.equalsIgnoreCase(getString(R.string.action_view_entries)))
				{
					targetFrag = EntryListFragment.create(selectedCategory);
				}
				else if (label.equalsIgnoreCase(getString(R.string.action_update_category)))
				{
					targetFrag = CategoryFormFragment.create(selectedCategory);
				}
				else if (label.equalsIgnoreCase(getString(R.string.action_delete_category)))
				{
					deleteCategory(selectedCategory);
				}
				else if (label.equalsIgnoreCase(getString(R.string.action_set_budget)))
				{
					targetFrag = SetBudgetFragment.create(selectedCategory);
				}
				
				if (targetFrag != null)
				{
					((MainActivity)getActivity()).showFragment(targetFrag);
				}
			}
		});
		builder.show();
		return true;
	}
	
	
	/**
	 * The first time this dialog is shown, give the user the choice whether or not to see it again.
	 * @param selectedCategory
	 */
	private void deleteCategory(final Category selectedCategory)
	{
		// TODO: Skip the dialog if the related preferences says so
		final String categoryName = selectedCategory.getName();
		
		AlertDialog.Builder builderDelete = new AlertDialog.Builder(getActivity());
		builderDelete.setTitle("Delete " + selectedCategory.getName() + "?");
		LayoutInflater inflator = getActivity().getLayoutInflater();
		View deleteCategoryView = inflator.inflate(R.layout.alert_delete_category, null);
		
		// Get count for how many entries are under this category
		EntryDTO entryDTO = lazyEntryDTO.get();
		List<Entry> entries = entryDTO.getEntries(selectedCategory, null, null);
		final String entriesSizeStr = String.valueOf(entries.size());
		
		String msg = "There are " + entriesSizeStr + " entries under this category.";
		TextView tv = (TextView) deleteCategoryView.findViewById(R.id.alertTextView);
		tv.setText(msg);
		builderDelete.setView(deleteCategoryView);
		builderDelete.setNegativeButton("Cancel", null);
		builderDelete.setPositiveButton("OK", new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dto.deleteCategory(selectedCategory, true);
				Crouton.makeText(getActivity(), "'" + categoryName + "' deleted. " + entriesSizeStr + " deleted." , Style.CONFIRM).show();
				refresh();
			}
		});
		builderDelete.show();
	}


	/**
	 * When a category is selected, the user will be prompted to enter an entry
	 * associated to that category
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3)
	{
		final Category selectedCategory = categories.get(index);
		
		EntryFormFragment entryFormFrag = EntryFormFragment.create(selectedCategory);
		
		((MainActivity)getActivity()).showFragment(entryFormFrag);
	}	
}
