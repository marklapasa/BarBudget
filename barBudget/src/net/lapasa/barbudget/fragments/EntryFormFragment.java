package net.lapasa.barbudget.fragments;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import net.lapasa.barbudget.R;
import net.lapasa.barbudget.dto.EntryDTO;
import net.lapasa.barbudget.models.Category;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
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
import dagger.Lazy;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class EntryFormFragment extends DaggerFragment
{
	@Inject
	protected Lazy<EntryDTO> lazyEntryDTO;

	public static final String SHOW_CATEGORY = "SHOW_CATEGORY";
	private EditText amountField;
	private Spinner categorySpinner;
	private EditText dateField;
	private EditText memoField;
	private boolean displayCategoryDropDown;
	private Calendar entryDate;

	private SimpleDateFormat f = new SimpleDateFormat("MMMM d, yyyy");

	private boolean isVisible;

	/**
	 * Factory method
	 * 
	 * @param displayCategoryDropDown
	 *            Set true if you want to display the drop down of categories
	 * @return
	 */
	public static EntryFormFragment create(boolean visibleAtStart, boolean displayCategoryDropDown)
	{
		EntryFormFragment frag = new EntryFormFragment();
		frag.setDisplayCategoryDropDown(displayCategoryDropDown);
		frag.isVisible = visibleAtStart;
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.form_quick_entry, null);
		configAmountField(v);
		categorySpinner = (Spinner) v.findViewById(R.id.categorySpinner);

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

	private void configAmountField(View v)
	{
		amountField = (EditText) v.findViewById(R.id.amount);
		amountField.addTextChangedListener(new TextWatcher()
		{
			DecimalFormat dec = new DecimalFormat("0.00");

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
						amountField.setText("$" + dec.format(percen));
						amountField.setSelection(amountField.getText().length());
					}
				}
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

	private void setDisplayCategoryDropDown(boolean displayCategoryDropDown)
	{
		this.displayCategoryDropDown = displayCategoryDropDown;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		menu.clear();
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.form_quick_entry, menu);
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
				save(null);
				// Return to previous screen
				getActivity().getFragmentManager().popBackStack();
				return true;
			}
			else
			{
				return super.onOptionsItemSelected(item);
			}
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
//		activity.setTitle("Create New Entry");
	}
}