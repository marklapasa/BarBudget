package net.lapasa.barbudget.fragments.adapters;

import java.util.List;

import net.lapasa.barbudget.R;
import net.lapasa.barbudget.models.Category;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.orm.SugarApp;

public class CategoryListAdapter extends BaseAdapter
{
	private List<Category> list;
	private LayoutInflater inflater;

	public CategoryListAdapter(List<Category> list)
	{
		this.list = list;
		Context applicationContext = SugarApp.getSugarContext().getApplicationContext();
		inflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Category getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		if (v == null)
		{
			v = inflater.inflate(R.layout.default_category_row, null);
		}
		
		Category cat = list.get(position);
		TextView tv = (TextView) v.findViewById(R.id.name);
		tv.setText(cat.getName());
		v.setBackgroundColor(cat.getColor());
		return v;
	}

	public void add(List<Category> categories)
	{
		if (categories.size() > 0)
		{
			list.clear();
			list.addAll(categories);
		}
	}
	
	public List<Category> getCategories()
	{
		return list;
	}

}
