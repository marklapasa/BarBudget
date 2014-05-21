package net.lapasa.barbudget.fragments;

import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class CategoryActionsDialogFragment extends AlertDialog
{

	private ListAdapter adapter;

	protected CategoryActionsDialogFragment(Context context)
	{
		super(context);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		List<String> values = Arrays.asList(new String[]{"View Entries", "Update Category", "Delete Category", "Set Budget"});
//		adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, values);
//		getListView().setAdapter(adapter);
	}
}
