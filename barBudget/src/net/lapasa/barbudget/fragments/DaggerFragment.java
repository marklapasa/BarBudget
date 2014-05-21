package net.lapasa.barbudget.fragments;

import net.lapasa.barbudget.BarBudgetApplication;
import android.app.Activity;
import android.app.Fragment;

public class DaggerFragment extends Fragment
{
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		((BarBudgetApplication) activity.getApplication()).inject(this);		
	}
}
