package net.lapasa.barbudget.fragments.adapters;

import java.util.List;

import javax.inject.Inject;

import net.lapasa.barbudget.BarBudgetApplication;
import net.lapasa.barbudget.R;
import net.lapasa.barbudget.dto.CategoryTallyDTO;
import net.lapasa.barbudget.models.Category;
import net.lapasa.barbudget.models.CategoryTally;
import net.lapasa.barbudget.views.BarGraphView;
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
		
		// Set category
		Category cat = list.get(position);
		TextView tv = (TextView) v.findViewById(R.id.name);
		tv.setText(cat.getName());
		
		// Set BarGraph
		BarGraphView bgView = (BarGraphView) v.findViewById(R.id.barGraph);
		initBarGraphView(cat, bgView);
				
		/* Budget 
		View budgetGraph = v.findViewById(R.id.budget);
		float ratio = (float) (cat.getBudget() / cat.getHighestSum());
		android.widget.LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, ratio);
		budgetGraph.setLayoutParams(lp);
		*/
		return v;
	}

	private void initBarGraphView(Category cat, BarGraphView bgView)
	{
		bgView.setNumerator(cat.getSum());
		bgView.setDenominator(cat.getHighestSum());
		bgView.setColor(cat.getColor());
		bgView.refresh();
	}


}
