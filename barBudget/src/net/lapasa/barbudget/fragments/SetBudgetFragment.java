package net.lapasa.barbudget.fragments;

import java.text.NumberFormat;

import javax.inject.Inject;

import net.lapasa.barbudget.R;
import net.lapasa.barbudget.dto.CategoryBudgetDTO;
import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.CategoryBudget;
import net.lapasa.barbudget.models.PeriodModel;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import dagger.Lazy;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class SetBudgetFragment extends DaggerFragment
{
	@Inject
	Lazy<CategoryBudgetDTO> lazyCategoryBudgetDTO;	
	
	private Category existingCategory;
	
	private EditText dailyBudgetEditText;
	private EditText monthlyBudgetEditText;
	private EditText annualBudgetEditText;
	private EditText weeklyBudgetEditText;
	private EditText quarterlyBudgetEditText;

	private CategoryBudgetDTO categoryBudgetDTO;

	/**
	 * Factory method
	 * 
	 * @param existingCategory
	 * @return
	 */
	public static SetBudgetFragment create(Category existingCategory)
	{
		SetBudgetFragment frag = new SetBudgetFragment();
		frag.existingCategory = existingCategory;
		frag.setHasOptionsMenu(true);
		return frag;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.form_set_budget, null);
		categoryBudgetDTO = lazyCategoryBudgetDTO.get();		
		
		dailyBudgetEditText = (EditText) v.findViewById(R.id.dailyBudget);
		monthlyBudgetEditText = (EditText) v.findViewById(R.id.monthlyBudget);
		annualBudgetEditText = (EditText) v.findViewById(R.id.annualBudget);
		weeklyBudgetEditText = (EditText) v.findViewById(R.id.weeklyBudget);
		quarterlyBudgetEditText = (EditText) v.findViewById(R.id.quarterlyBudget);
		
		final NumberFormat f = NumberFormat.getCurrencyInstance();
		configField(f, dailyBudgetEditText, PeriodModel.RANGE_TODAY);
		configField(f, monthlyBudgetEditText, PeriodModel.RANGE_LAST_30_DAYS);
		configField(f, annualBudgetEditText, PeriodModel.RANGE_LAST_365_DAYS);
		configField(f, weeklyBudgetEditText, PeriodModel.RANGE_LAST_7_DAYS);
		configField(f, quarterlyBudgetEditText, PeriodModel.RANGE_LAST_90_DAYS);
		

		dailyBudgetEditText.addTextChangedListener(getTextWatcher(dailyBudgetEditText, f, new IRefreshCallback()
		{
			/*
			 * Daily changes, then
			 * 		MONTHLY 	=	DAILY * 30
			 * 		ANNUALY 	=	DAILY * 365
			 * 		WEEKLY		=	DAILY * 7
			 * 		QUARTERLY	=	DAILY * 90
			 */			
			@Override
			public void updateOtherFields(float value)
			{
				monthlyBudgetEditText.setText(f.format(value * 30));
				annualBudgetEditText.setText(f.format(value * 365));
				weeklyBudgetEditText.setText(f.format(value * 7));
				quarterlyBudgetEditText.setText(f.format(value * 90));
			}
		}));
		
		monthlyBudgetEditText.addTextChangedListener(getTextWatcher(monthlyBudgetEditText, f, new IRefreshCallback()
		{
			/*
			 * MONTHLY changes, then
			 * 		DAILY 		=	MONTLHY / 30
			 * 		ANNUALY 	=	MONTLHY * 12
			 * 		WEEKLY		=	MONTHLY / 4
			 * 		QUARTERLY	=	MONTHLY * 3
			 */						
			@Override
			public void updateOtherFields(float value)
			{
				dailyBudgetEditText.setText(f.format(value / 30));
				annualBudgetEditText.setText(f.format(value * 12));
				weeklyBudgetEditText.setText(f.format(value / 4));
				quarterlyBudgetEditText.setText(f.format(value * 3));				
			}
		}));
		
		annualBudgetEditText.addTextChangedListener(getTextWatcher(annualBudgetEditText, f, new IRefreshCallback()
		{
			/*
			 * ANNUALY changes, then
			 * 		DAILY 		=	ANNUALY / 365
			 * 		MONTHLY 	=	ANNUALY / 12
			 * 		WEEKLY		=	ANNUALY / 52
			 * 		QUARTERLY	=	ANNUALY / 4
			 */
			@Override
			public void updateOtherFields(float value)
			{
				dailyBudgetEditText.setText(f.format(value / 365));
				monthlyBudgetEditText.setText(f.format(value / 12));
				weeklyBudgetEditText.setText(f.format(value / 52));
				quarterlyBudgetEditText.setText(f.format(value * 4));				
			}
		}));
		
		weeklyBudgetEditText.addTextChangedListener(getTextWatcher(weeklyBudgetEditText, f, new IRefreshCallback()
		{
			/*
			 * WEEKLY changes, then
			 * 		DAILY 		=	WEEKLY / 7
			 * 		MONTHLY 	=	WEEKLY * 4
			 * 		ANNUALLY	=	ANNUALY * 52
			 * 		QUARTERLY	=	WEEKLY * 12
			 */
			@Override
			public void updateOtherFields(float value)
			{
				dailyBudgetEditText.setText(f.format(value / 7));
				monthlyBudgetEditText.setText(f.format(value *4));
				annualBudgetEditText.setText(f.format(value * 52));
				quarterlyBudgetEditText.setText(f.format(value * 12));
			}
		}));
		
		quarterlyBudgetEditText.addTextChangedListener(getTextWatcher(quarterlyBudgetEditText, f, new IRefreshCallback()
		{
			/*
			 * QUARTERLY changes, then
			 * 		DAILY 		=	QUARTERLY / 90
			 * 		MONTHLY 	=	QUARTERLY / 3
			 * 		ANNUALLY	=	QUARTERLY * 4
			 * 		WEEKLY		=	QUARTERLY / 12
			 */
			@Override
			public void updateOtherFields(float value)
			{
				dailyBudgetEditText.setText(f.format(value / 90));
				monthlyBudgetEditText.setText(f.format(value / 3));
				annualBudgetEditText.setText(f.format(value * 4));
				weeklyBudgetEditText.setText(f.format(value / 12));
			}
		}));
		
		
		return v;
	}
	
	interface IRefreshCallback
	{
		void updateOtherFields(float value);
	}


	private TextWatcher getTextWatcher(final EditText field, final NumberFormat formatter, final IRefreshCallback callback)
	{
		TextWatcher tw = new TextWatcher()
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
						float percen = in / 100F;
						String formattedValue = formatter.format(percen);
						field.setText(formattedValue);
						field.setSelection(field.getText().length());
						
						callback.updateOtherFields(percen);
					}
				}				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				
			}
		};
		return tw;
	}

	private void configField(NumberFormat f, EditText field, int periodType)
	{
		CategoryBudget budget = categoryBudgetDTO.getBudget(periodType, existingCategory);
		if(budget != null)
		{
			if (budget.getValue() != 0.00)
			{
				field.setText(f.format(budget.getValue()));
			}
			else
			{
				field.setText(null);
			}
		}
		else
		{
			field.setText(null);
		}
	}

	
	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		menu.clear();
		getActivity().getMenuInflater().inflate(R.menu.set_budget, menu);
		super.onPrepareOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.action_save_budget:
			save();
			Crouton.makeText(getActivity(), existingCategory.getName() + " budget updated.", Style.CONFIRM).show();
			getActivity().getFragmentManager().popBackStack();
		break;
		case R.id.action_clear_budget:
			clearFields();
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	private void save()
	{
		/* Save Daily */
		CategoryBudget budget = new CategoryBudget(PeriodModel.RANGE_TODAY, convert(dailyBudgetEditText.getText().toString()), existingCategory);
		categoryBudgetDTO.setBudget(budget);
		
		/* Save Weekly */
		budget = new CategoryBudget(PeriodModel.RANGE_LAST_7_DAYS, convert(weeklyBudgetEditText.getText().toString()), existingCategory);
		categoryBudgetDTO.setBudget(budget);
		
		/* Save Monthly */
		budget = new CategoryBudget(PeriodModel.RANGE_LAST_30_DAYS, convert(monthlyBudgetEditText.getText().toString()), existingCategory);
		categoryBudgetDTO.setBudget(budget);
		
		/* Save Quarterly */
		budget = new CategoryBudget(PeriodModel.RANGE_LAST_90_DAYS, convert(quarterlyBudgetEditText.getText().toString()), existingCategory);
		categoryBudgetDTO.setBudget(budget);
		
		/* Save Annually */
		budget = new CategoryBudget(PeriodModel.RANGE_LAST_365_DAYS, convert(annualBudgetEditText.getText().toString()), existingCategory);
		categoryBudgetDTO.setBudget(budget);		
	}
	
	private double convert(String str)
	{
		if (str != null && str.length() > 3)
		{
			str = str.replace(",", "");
			return Double.valueOf(str.substring(1));
		}
		else
		{
			return 0.00d;
		}
		
	}


	private void clearFields()
	{
		dailyBudgetEditText.setText(null);
		monthlyBudgetEditText.setText(null);
		annualBudgetEditText.setText(null);
		weeklyBudgetEditText.setText(null);
		quarterlyBudgetEditText.setText(null);
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		getActivity().setTitle(existingCategory.getName() + " Budget");
	}
	
}