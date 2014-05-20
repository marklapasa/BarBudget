package net.lapasa.barbudget.fragments;

import java.util.Random;

import net.lapasa.barbudget.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class CategoryFormFragment extends Fragment implements IColorPickerClient
{

	/**
	 * Factory method
	 * 
	 * @return
	 */
	public static Fragment create(boolean isCreatingNewCategory)
	{
		CategoryFormFragment f = new CategoryFormFragment();
		f.isCreatingNewCategory = isCreatingNewCategory;
		return f;
	}

	private EditText categoryField;
	private Button colorPickerBtn;
	private int color;
	private CheckBox quickEntryToggle;
	private View quickEntryView;
	private boolean isCreatingNewCategory;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.form_category, null);
		
		/* Category Name */
		categoryField = (EditText) v.findViewById(R.id.category);
		
		/* Color Picker*/
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
		
		if (isCreatingNewCategory)
		{
			setColor(getRandomColor());
		}
		else
		{
//			setColor(originalColor);
		}
		
		
		/* Quick Entry Fragment */
		Fragment quickEntryFrag = getActivity().getFragmentManager().findFragmentById(R.id.quickEntryFragment);
		quickEntryView = quickEntryFrag.getView();
		quickEntryView.setVisibility(View.GONE);
		
		
		/* Toggle Quick Entry */
		quickEntryToggle = (CheckBox) v.findViewById(R.id.toggleQuickEntry);
		quickEntryToggle.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
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
		return v;
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		activity.setTitle("Create New Category");
	}

	@Override
	public void setColor(int color)
	{
		this.color = color;
		colorPickerBtn.setText("#" + Integer.toHexString(color));
		colorPickerBtn.setBackgroundColor(color);
	}
	

	@Override
	public int getColor()
	{
		return color;
	}	

	public int getRandomColor()
	{
		Color c = new Color();
		Random rand = new Random();
		return Color.rgb(rand.nextInt(), rand.nextInt(), rand.nextInt());
		
	}
}


