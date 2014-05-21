package net.lapasa.barbudget.fragments;

import java.text.SimpleDateFormat;
import java.util.List;

import com.orm.SugarApp;

import net.lapasa.barbudget.BarBudgetApplication;
import net.lapasa.barbudget.R;
import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.Entry;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EntryListAdapter extends BaseAdapter
{
	private List<Entry> list;
	private LayoutInflater inflator;
	private SimpleDateFormat f = new SimpleDateFormat("MM/d");


	public EntryListAdapter(List<Entry> list)
	{
		this.list = list;
		Context applicationContext = BarBudgetApplication.getSugarContext().getApplicationContext();
		inflator = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}
	
	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Entry getItem(int position)
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
			v = inflator.inflate(R.layout.default_category_row, null);
		}
		
		Entry entry = list.get(position);
		TextView tv = (TextView) v.findViewById(R.id.name);
		tv.setText(f.format(entry.getTimestamp()));
		
		tv = (TextView) v.findViewById(R.id.value);
		
		CharSequence value = String.format("$%.2f", entry.getValue());
		tv.setText(value);
		
		return v;
	}

}
