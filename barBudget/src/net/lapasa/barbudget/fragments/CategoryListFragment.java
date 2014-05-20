package net.lapasa.barbudget.fragments;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.lapasa.barbudget.BarBudgetApplication;
import net.lapasa.barbudget.MainActivity;
import net.lapasa.barbudget.R;
import net.lapasa.barbudget.dto.CategoryDTO;
import net.lapasa.barbudget.dto.EntryDTO;
import net.lapasa.barbudget.fragments.adapters.CategoryListAdapter;
import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.Entry;
import net.lapasa.barbudget.models.SortRule;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import dagger.Lazy;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class CategoryListFragment extends ListFragment implements OnItemLongClickListener
{
	private static final String TAG = CategoryFormFragment.class.getName();

	protected static final int ACTION_VIEW_ENTRIES = 0;

	@Inject
	protected Lazy<CategoryDTO> lazyCategoryDTO;

	@Inject
	protected Lazy<EntryDTO> lazyEntryDTO;

	
	protected CategoryDTO dto;
	private CategoryListAdapter adapter;
	private List<Category> categories;

	private ListView listView;

	private View actionsList;

	public CategoryListFragment()
	{
		super();
		setHasOptionsMenu(true);
		categories = new ArrayList<Category>();
		adapter = new CategoryListAdapter(categories);
		setListAdapter(adapter);
		
	
	}


	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		// This is normally done in DaggerFragment but this doesn't extend from that class
		((BarBudgetApplication) getActivity().getApplication()).inject(this);
		
		
	}

	@Override
	public void onResume()
	{
		super.onResume();
		refresh();
	}

	private void refresh()
	{
		dto = lazyCategoryDTO.get();
		List<Category> categories = dto.getCategories(SortRule.SORT_HIGH_TO_LOW);
		int size = categories.size();
		this.categories.clear();
		if (size > 0)
		{
			this.categories.addAll(categories);
		}
		else if (size == 0)
		{
			setEmptyText("Please create a category");
		}
		adapter.notifyDataSetChanged();

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
	public void setListAdapter(ListAdapter adapter)
	{
		super.setListAdapter(adapter);

		// Obtain reference to list of categories
		this.categories = ((CategoryListAdapter) adapter).getCategories();
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
		
		
		listView = getListView();
		
		/* Configure long item press on list */
		listView.setOnItemLongClickListener(this);
		
		/* Configure short item press on list */
	}

	/**
	 * Display options for View Entries, Update Category, Delete Category, and Set Budget
	 */
	@Override
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
				if (label.equalsIgnoreCase(getString(R.string.action_view_entries)))
				{
					
				}
				else if (label.equalsIgnoreCase(getString(R.string.action_update_category)))
				{
					((MainActivity)getActivity()).showFragment(CategoryFormFragment.create(selectedCategory));			
				}
				else if (label.equalsIgnoreCase(getString(R.string.action_delete_category)))
				{
					deleteCategory(selectedCategory);
				}
				else if (label.equalsIgnoreCase(getString(R.string.action_set_budget)))
				{
					
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
}
