package net.lapasa.barbudget.fragments;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import net.lapasa.barbudget.R;
import net.lapasa.barbudget.dto.CategoryDTO;
import net.lapasa.barbudget.dto.EntryDTO;
import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.Entry;
import net.lapasa.barbudget.models.SortRule;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import dagger.Lazy;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class EntryFormFragment extends DaggerFragment implements ValidationListener, IRefreshable 
{
	@Inject
	protected Lazy<EntryDTO> lazyEntryDTO;

	@Inject
	protected Lazy<CategoryDTO> lazyCategoryDTO;
	
	public static final String SHOW_CATEGORY = "SHOW_CATEGORY";
	
	@Required(order = 1)
	@TextRule(order = 2, message="You must enter a dollar amount")
	private EditText amountField;
	private Spinner categorySpinner;
	private EditText dateField;
	private EditText memoField;
	private Calendar entryDate;

	private SimpleDateFormat f = new SimpleDateFormat("MMMM d, yyyy");

	private boolean isVisible;

	private Category existingCategory;

	private boolean isValid;

	private Validator validator;

	private Entry existingEntry;



	/**
	 * Factory method for creating new entries for an existing category OR
	 * 
	 * @param selectedCategory
	 * @return
	 */
	public static EntryFormFragment create(Category selectedCategory, Entry selectedEntry)
	{
		EntryFormFragment frag = new EntryFormFragment();
		
		if (selectedEntry != null || selectedCategory != null)
		{
			frag.existingCategory = selectedCategory;
			frag.existingEntry = selectedEntry;
			frag.isVisible = true;
			frag.setHasOptionsMenu(true);
		}
		else
		{
			frag.isVisible = false;
			frag.setHasOptionsMenu(false);			
		}
		return frag;
	}
	
	public static EntryFormFragment create(Category selectedCategory)
	{
		return EntryFormFragment.create(selectedCategory, null);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		validator = new Validator(this);
		validator.setValidationListener(this);
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.form_quick_entry, null);
		configAmountField(v);
		configCategorySpinner(v);

		configCurrentDateOnDatePicker(v);
		memoField = (EditText) v.findViewById(R.id.memo);

		if (getTag() != null)
		{
			if (getTag().equals(SHOW_CATEGORY))
			{
				categorySpinner.setVisibility(View.GONE);
			}
			else
			{
				categorySpinner.setVisibility(View.VISIBLE);
			}
		}

		if (!isVisible)
		{
			v.setVisibility(View.GONE);
		}

		return v;
	}

	private void configCategorySpinner(View v)
	{
		View categorySection = v.findViewById(R.id.dropDownSection);
		categorySpinner = (Spinner) v.findViewById(R.id.categorySpinner);
		
		
		if(existingCategory != null)
		{
			// Get list of existing category
			CategoryDTO categoryDTO = lazyCategoryDTO.get();
			List<Category> categories = categoryDTO.getCategories(SortRule.SORT_ALPHABETICALLY);
			
			// Set drop down to selected category
			categorySection.setVisibility(View.VISIBLE);
			SpinnerAdapter adapter = new CategorySpinnerAdapter(categories);
			categorySpinner.setAdapter(adapter);
			setCategory(existingCategory, categories);			
		}
		
	}
	
		 
	 /**
	  * Set the spinner to the index that matches the target category 
	  * @param targetCategory
	  * @param categories
	  */
	public void setCategory(Category targetCategory, List<Category> categories)
	{
		for (int i = 0; i < categories.size(); i ++)
		{
			String cName = categories.get(i).getName();
			if (targetCategory.getName().equalsIgnoreCase(cName))
			{
				categorySpinner.setSelection(i);
				return;
			}
		}
	}	

	private void configAmountField(View v)
	{
		final NumberFormat formatter = NumberFormat.getCurrencyInstance();
		amountField = (EditText) v.findViewById(R.id.amount);
		amountField.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if (!s.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$"))
				{
					String userInput = "" + s.toString().replaceAll("[^\\d]", "");
					if (userInput.length() > 0)
					{
						Float in = Float.parseFloat(userInput);
						float percen = in / 100;
						amountField.setText(formatter.format(percen));
						amountField.setSelection(amountField.getText().length());
					}
				}
				validator.validate();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				// Do nothing

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				// Do nothing
			}
		});
		
		if(existingEntry != null)
		{
			amountField.setText(formatter.format(existingEntry.getValue()));
		}
		
		if (existingCategory != null)
		{
			amountField.requestFocus();
		}
	}

	private void configCurrentDateOnDatePicker(View v)
	{
		dateField = (EditText) v.findViewById(R.id.date);
		entryDate = Calendar.getInstance();
		refreshDateField();

		dateField.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new DatePickerDialog(getActivity(), datePickerListener, entryDate.get(Calendar.YEAR), Calendar.MONTH, Calendar.DAY_OF_MONTH).show();
			}
		});
	}

	private void refreshDateField()
	{
		dateField.setText(f.format(entryDate.getTime()));
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		menu.clear();
		MenuInflater inflater = getActivity().getMenuInflater();
		
		inflater.inflate(R.menu.form_quick_entry, menu);
		menu.getItem(0).setEnabled(isValid);
		
		if (existingEntry == null)
		{
			menu.removeItem(R.id.action_delete_entry);
		}
	
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_save_entry:
			if (isValid())
			{
				// If category spinner is available, use that selected category
				if (categorySpinner.getVisibility() == View.VISIBLE)
				{
					existingCategory = (Category) categorySpinner.getSelectedItem();
				}
				
				save(existingCategory);
				// Return to previous screen
				getActivity().getFragmentManager().popBackStack();
				return true;
			}
			else
			{
				return super.onOptionsItemSelected(item);
			}
		case R.id.action_delete_entry:
			EntryFormDialogs.deleteEntry(this, existingEntry, lazyEntryDTO.get(), true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private boolean isValid()
	{
		if (amountField.getText().length() == 0)
		{
			return false;
		}

		return true;
	}

	/**
	 * Collect the data from the fields and let the DTO do it's work
	 */
	public void save(Category category)
	{
		double amt = 0;
		String amtStr = amountField.getText().toString();
		Crouton.makeText(getActivity(), amtStr + " recorded under " + category.getName(), Style.CONFIRM).show();
		amtStr = amtStr.substring(1);
		amt = Double.valueOf(amtStr);

		lazyEntryDTO.get().create(entryDate.getTime(), amt, memoField.getText().toString(), category);
	}

	private OnDateSetListener datePickerListener = new OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			EntryFormFragment.this.entryDate.set(Calendar.YEAR, year);
			EntryFormFragment.this.entryDate.set(Calendar.MONTH, monthOfYear);
			EntryFormFragment.this.entryDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			EntryFormFragment.this.refreshDateField();
		}
	};

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		String msg = null;
		if (existingEntry != null)
		{
			msg = "Update Entry";
		}
		else if (existingCategory != null)
		{
			msg = "Create New Entry";
		}
		
		if (msg != null)
		{
			activity.setTitle(msg);
		}
	}

	@Override
	public void onValidationSucceeded()
	{
		isValid = true;
		getActivity().invalidateOptionsMenu();
	}

	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule)
	{
		isValid = false;
		String msg = failedRule.getFailureMessage();
		if (failedView instanceof EditText)
		{
			failedView.requestFocus();
			((EditText) failedView).setError(msg);
		}
		else
		{
			Crouton.makeText(getActivity(), msg, Style.ALERT).show();
		}
		getActivity().invalidateOptionsMenu();
	}
	
	public void refresh()
	{
		// Do nothing; this view cannot be refreshed
	}

}