package net.lapasa.barbudget;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import net.lapasa.barbudget.dto.CategoryDTO;

import com.orm.SugarApp;

import dagger.ObjectGraph;

public class BarBudgetApplication extends SugarApp
{
	private ObjectGraph oGraph;
	private DecimalFormat dec;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		Object[] array = getModules().toArray();
		oGraph = ObjectGraph.create(array);
		dec = new DecimalFormat("0.00");
	}

	private List<BarBudgetModule> getModules()
	{
		return Arrays.asList(new BarBudgetModule());
	}
	
	public void inject(Object object)
	{
		oGraph.inject(object);
	}
}