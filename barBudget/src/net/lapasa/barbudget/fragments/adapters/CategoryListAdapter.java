package net.lapasa.barbudget.fragments.adapters;

import java.text.NumberFormat;
import java.util.List;

import javax.inject.Inject;

import net.lapasa.barbudget.BarBudgetApplication;
import net.lapasa.barbudget.R;
import net.lapasa.barbudget.dto.CategoryTallyDTO;
import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.CategoryTally;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import dagger.Lazy;

public class CategoryListAdapter extends BaseAdapter
{
	@Inject
	protected Lazy<CategoryTallyDTO> lazyCategoryTallyDTO;
	
	private List<Category> list;
	private LayoutInflater inflator;

	public CategoryListAdapter(List<Category> list)
	{
		this.list = list;
		Context applicationContext = BarBudgetApplication.getSugarContext().getApplicationContext();
		inflator = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		((BarBudgetApplication) applicationContext).inject(this);		

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
			v = inflator.inflate(R.layout.category_row, null);
		}
		
		Category cat = list.get(position);
		TextView tv = (TextView) v.findViewById(R.id.name);
		tv.setText(cat.getName());
//		v.setBackgroundColor(cat.getColor());
		
		tv = (TextView) v.findViewById(R.id.value);
		tv.setText(getDebugString(cat));
		
		View barGraph = v.findViewById(R.id.barGraph);
		barGraph.setBackgroundColor(cat.getColor());
		float ratio = (float) (cat.getSum() / cat.getHighestSum());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, ratio);
		barGraph.setLayoutParams(lp);
		
		tv = (TextView) v.findViewById(R.id.value_width);
		if (cat.getSum() > 0)
		{
			tv.append(cat.getSum() + "");
		}
		else
		{
			tv.setText("Add Entry");
			tv.setVisibility(View.VISIBLE);
		}
		
		return v;
	}

	private String getDebugString(Category cat)
	{
		CategoryTallyDTO dto = lazyCategoryTallyDTO.get();
		SparseArray<CategoryTally> tallies = dto.getTallyForCategory(cat);
		
		
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
		String label = "";
		if (tallies != null)
		{
			CategoryTally obj = tallies.get(0);
			if (obj != null)
			{
				label += numberFormat.format(obj.getValue());
			}
			else
			{
				label += numberFormat.format(0);
			}
		}
		
		/*
		int key = 0;
		for(int i = 0; i < tallies.size(); i++) 
		{
		   key = tallies.keyAt(i);
		   CategoryTally obj = tallies.get(key);
		   label += key + "-" + obj.getValue() + "\n"; 
		}
		*/
		return label;
	}

}
