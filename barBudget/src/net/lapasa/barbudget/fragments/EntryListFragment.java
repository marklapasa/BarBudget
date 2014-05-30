package net.lapasa.barbudget.fragments;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.lapasa.barbudget.MainActivity;
import net.lapasa.barbudget.R;
import net.lapasa.barbudget.dto.EntryDTO;
import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.Entry;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import dagger.Lazy;

public class EntryListFragment extends DaggerListFragment implements OnItemLongClickListener, OnItemClickListener, IRefreshable
{
	@Inject
	protected Lazy<EntryDTO> lazyEntryDTO;	
	
	private Category existingCategory;

	private ArrayList<Entry> entries;

	private EntryListAdapter adapter;

	private EntryDTO dto;


	/**
	 * Factory method
	 * 
	 * @param category
	 * @return
	 */
	public static EntryListFragment create(Category category)
	{
		EntryListFragment frag = new EntryListFragment();
		frag.existingCategory = category;
		return frag;
	}
	
	/**
	 * Constructor
	 */
	public EntryListFragment()
	{
		super();
		setHasOptionsMenu(true);
		entries = new ArrayList<Entry>();
		
		// This is the default adapter - show only the records for today
		// Assumption: entries collection only represents records querried where date = today
		adapter = new EntryListAdapter(entries);
		setListAdapter(adapter);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		menu.clear();
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.entry, menu);
		super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		getActivity().setTitle(existingCategory.getName());
//		Drawable d = new ColorDrawable(existingCategory.getColor());
//		getActivity().getActionBar().setBackgroundDrawable(d);
	}

	/**
	 * Launch edit entry workflow
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3)
	{
		final Entry selectedEntry = entries.get(index);
		
		EntryFormFragment frag = EntryFormFragment.create(existingCategory, selectedEntry);
		((MainActivity) getActivity()).showFragment(frag);
		
	}

	/**
	 * Launch actions menu to delete 
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int index, long arg3)
	{
		final Entry selectedEntry = entries.get(index);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Choose Action");
		final CharSequence[] items 
			= new CharSequence[]
					{getString(R.string.action_delete_entry)};

		builder.setItems(items, new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String label = (String) items[which];
				if (label.equalsIgnoreCase(getString(R.string.action_delete_entry)))
				{
					EntryFormDialogs.deleteEntry(EntryListFragment.this, selectedEntry, lazyEntryDTO.get(), false);
				}
			}
		});
		builder.show();		
				
				
		return true;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		refresh();
	}

	public void refresh()
	{
		dto = lazyEntryDTO.get();
		// TODO: Will need to define date range somehow, maybe through options menu
		List<Entry> entries = dto.getEntries(existingCategory, null, null);
		
		int size = entries.size();
		this.entries.clear();
		if (size > 0)
		{
			this.entries.addAll(entries);
		}
		adapter.notifyDataSetChanged();
		if (size == 0)
		{
			setEmptyText("There are no transactions for this category");
		}
		
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
				if (entries.size() == 0)
				{
					addNewEntry();
				}
			}
		});	
		
		
		/* Configure long item press on list */
		getListView().setOnItemLongClickListener(this);
		
		/* Configure short item press on list */
		getListView().setOnItemClickListener(this);
	}
	
	
	private void addNewEntry()
	{
		EntryFormFragment frag = EntryFormFragment.create(existingCategory);
		((MainActivity) getActivity()).showFragment(frag);
	}
	

}
