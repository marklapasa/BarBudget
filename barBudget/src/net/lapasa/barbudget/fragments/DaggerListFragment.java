package net.lapasa.barbudget.fragments;

import net.lapasa.barbudget.BarBudgetApplication;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;

public class DaggerListFragment extends ListFragment
{
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		((BarBudgetApplication) activity.getApplication()).inject(this);		
	}
	
	protected AlertDialog getLongPressMenu(CharSequence[] items, DialogInterface.OnClickListener listener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Choose Action");
		builder.setItems(items, listener);
		return builder.create();
	}

}
