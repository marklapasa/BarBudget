package net.lapasa.barbudget.fragments;

import java.util.Random;

import javax.inject.Inject;

import net.lapasa.barbudget.R;
import net.lapasa.barbudget.dto.CategoryDTO;
import net.lapasa.barbudget.models.Category;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorSelectedListener;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Required;

import dagger.Lazy;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class CategoryFormFragment extends DaggerFragment implements ValidationListener
{

	private Category existingCategory;

	/**
	 * Factory method
	 * 
	 * @return
	 */
	public static Fragment create(Category existingCategory)
	{
		CategoryFormFragment f = new CategoryFormFragment();
		f.existingCategory = existingCategory;
		f.isValid = (existingCategory != null); 
		return f;
	}

	@Required(order = 1)
	private EditText categoryField;
	private Button colorPickerBtn;
	private int color;
	private CheckBox quickEntryToggle;
	private View quickEntryView;
	private Validator validator;
	private boolean isValid;

	@Inject
	Lazy<CategoryDTO> lazyCategoryDTO;

	private EntryFormFragment quickEntryFrag;

	@Override
	public void onStart()
	{
		super.onStart();
		setHasOptionsMenu(true);
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
		View v = inflater.inflate(R.layout.form_category, null);

		/* Category Name */
		configCategoryName(v);

		/* Color Picker */
		configColorPicker(v);


		/* Toggle Quick Entry */
		configQuickEntryToggle(v);
		
		/* Quick Entry Fragment */
		configQuickEntryFrag();
		

		return v;
	}

	private void configQuickEntryToggle(View v)
	{
		quickEntryToggle = (CheckBox) v.findViewById(R.id.toggleQuickEntry);
		if (existingCategory == null)
		{
			quickEntryToggle.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{

				@Override
				public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked)
				{
					quickEntryView = quickEntryFrag.getView();
					if (isChecked)
					{
						quickEntryView.setVisibility(View.VISIBLE);
					}
					else
					{
						quickEntryView.setVisibility(View.GONE);
					}
				}
			});
		}
		else
		{
			quickEntryToggle.setVisibility(View.GONE);
		}
	}

	private void configQuickEntryFrag()
	{
		quickEntryFrag = EntryFormFragment.create(null);//(EntryFormFragment) getActivity().getFragmentManager().findFragmentById(R.id.quickEntryFragment);
		FragmentTransaction txn = getFragmentManager().beginTransaction();
		txn.replace(R.id.quickEntryContainer, quickEntryFrag);
		txn.commit();
		
	}

	private void configColorPicker(View v)
	{
		colorPickerBtn = (Button) v.findViewById(R.id.colorPickerBtn);
		colorPickerBtn.setOnClickListener(new OnClickListener()
		{

			private AlertDialog dialog;

			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				ColorPicker picker = new ColorPicker(getActivity());
				picker.setOnColorSelectedListener(new OnColorSelectedListener()
				{
					@Override
					public void onColorSelected(int color)
					{
						CategoryFormFragment.this.setColor(color);
						dialog.dismiss();
					}
				});
				builder.setView(picker);
				builder.setTitle("Choose Category Color");

				dialog = builder.create();
				dialog.show();
			}
		});

		if (existingCategory == null)
		{
			setColor(getRandomColor());
		}
		else
		{
			setColor(existingCategory.getColor());
		}
	}

	private void configCategoryName(View v)
	{
		categoryField = (EditText) v.findViewById(R.id.category);
		
		if (existingCategory != null)
		{
			categoryField.setText(existingCategory.getName());
		}
		
		categoryField.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				validator.validate();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {};
			
			@Override
			public void afterTextChanged(Editable s){}
		});
	}

	public void setColor(int color)
	{
		this.color = color;
		colorPickerBtn.setBackgroundColor(color);
	}

	public int getRandomColor()
	{
		Color c = new Color();
		Random rand = new Random();
		return Color.rgb(rand.nextInt(), rand.nextInt(), rand.nextInt());
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		menu.clear();
		getActivity().getMenuInflater().inflate(R.menu.form_category, menu);
		menu.getItem(0).setEnabled(isValid);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_save_category:
			save();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void save()
	{
		CategoryDTO categoryDTO = lazyCategoryDTO.get();
		String categoryName = categoryField.getText().toString();
		
		if (existingCategory == null)
		{
			Category createdCategory = categoryDTO.create(categoryName, color);

			/*
			 * Delegate saving the entry associated to this category by letting the
			 * entry form save it's own data
			 */
			if (quickEntryToggle.isChecked())
			{
				quickEntryFrag.save(createdCategory);
			}

			Crouton.makeText(getActivity(), "Category " + categoryName + " created!", Style.CONFIRM).show();			
		}
		else
		{
			existingCategory.setName(categoryName);
			existingCategory.setColor(color);
			categoryDTO.update(existingCategory);
			Crouton.makeText(getActivity(), "Category " + categoryName + " updated", Style.CONFIRM).show();
		}

		getActivity().getFragmentManager().popBackStack();
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

	
	@Override
	public void onResume()
	{
		super.onResume();
		String title = (existingCategory != null) ? "Edit Category" : "Create New Category"; 
		getActivity().setTitle(title);
	}
}
