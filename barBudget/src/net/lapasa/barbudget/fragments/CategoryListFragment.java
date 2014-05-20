package net.lapasa.barbudget.fragments;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.lapasa.barbudget.BarBudgetApplication;
import net.lapasa.barbudget.MainActivity;
import net.lapasa.barbudget.R;
import net.lapasa.barbudget.R.id;
import net.lapasa.barbudget.dto.CategoryDTO;
import net.lapasa.barbudget.fragments.adapters.CategoryListAdapter;
import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.SortRule;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.Toast;
import dagger.Lazy;

public class CategoryListFragment extends ListFragment
{
	private static final String TAG = null;

	@Inject
	Lazy<CategoryDTO> lazyCAtegoryDTO;

	protected CategoryDTO dto;
	private CategoryListAdapter adapter;
	private List<Category> categories;

	private MainActivity activity;

	/*
	 * public static CategoryListFragment create(List<Object> list) {
	 * CategoryListFragment instance = new CategoryListFragment();
	 * 
	 * CategoryListAdapter adapter = new CategoryListAdapter(new
	 * ArrayList<Category>()); instance.setListAdapter(adapter);
	 * 
	 * CategoryDTO dto = new CategoryDTO(); instance.setCategoryDTO(dto);
	 * 
	 * return instance; }
	 */
	public CategoryListFragment()
	{
		super();
		setHasOptionsMenu(true);
		categories = new ArrayList<Category>();
		adapter = new CategoryListAdapter(categories);
	}

	public CategoryDTO getCategoryDTO()
	{
		return dto;
	}

	public void setCategoryDTO(CategoryDTO dto)
	{
		this.dto = dto;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		((BarBudgetApplication) getActivity().getApplication()).inject(this);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		dto = lazyCAtegoryDTO.get();
		refresh();
	}

	private void refresh()
	{
		List<Category> categories = dto.getCategories(SortRule.SORT_HIGH_TO_LOW);
		int size = categories.size();
		if (size > 0)
		{
			this.categories.clear();
			this.categories.addAll(categories);
		}
		else if (size == 0)
		{
			setListShown(true);
			setEmptyText("Please create a category");
		}

	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		menu.clear();
		MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate(R.menu.category, menu);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_add_category:
			activity.showFragment(CategoryFormFragment.create(true));
			return true;
		case R.id.action_delete_all_categories:
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Are you sure you want to delete all categories?");
			builder.setPositiveButton("OK", new OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					dto.deleteAll();
				}
			});
			builder.setNegativeButton("Cancel", null);
			builder.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
		this.activity = (MainActivity) getActivity();
	}
}
