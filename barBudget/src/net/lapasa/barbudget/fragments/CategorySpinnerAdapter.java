package net.lapasa.barbudget.fragments;

import java.util.List;

import net.lapasa.barbudget.BarBudgetApplication;
import net.lapasa.barbudget.models.Category;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategorySpinnerAdapter extends BaseAdapter
{
	private List<Category> categories;
	private LayoutInflater inflater;

	public CategorySpinnerAdapter(List<Category> categories)
	{
		super();
		this.categories = categories;
		Context applicationContext = BarBudgetApplication.getSugarContext().getApplicationContext();
		inflater = (LayoutInflater) applicationContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	}
	
	@Override
	public int getCount()
	{
		return categories.size();
	}

	@Override
	public Object getItem(int position)
	{
		return categories.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Category category = this.categories.get(position);
		
		View v = inflater.inflate(android.R.layout.simple_spinner_item, null);
		
		// Set Category Name for @android:id/text1
		TextView tv = (TextView) v.findViewById(android.R.id.text1);
		tv.setGravity(Gravity.RIGHT);
		tv.setText(category.getName());
		tv.setPadding(20, 20, 20, 20);
		
		// Set Color
		tv.setBackgroundColor(category.getColor());
		
		return v;
	}
	


}
