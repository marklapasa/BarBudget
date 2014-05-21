package net.lapasa.barbudget.fragments;

import java.text.NumberFormat;

import net.lapasa.barbudget.dto.EntryDTO;
import net.lapasa.barbudget.models.Entry;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class EntryFormDialogs
{
	/**
	 * Display to the user an alert dialog confirming if they should proceed or not
	 * @param activity
	 * @param existingEntry
	 * @param entryDTO
	 */
	public static void deleteEntry(final IRefreshable client, final Entry existingEntry, final EntryDTO entryDTO, final boolean goToPrevScreen)
	{
		String valStr = NumberFormat.getCurrencyInstance().format(existingEntry.getValue());
		final Activity activity = client.getActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Confirm delete record");
		builder.setMessage("Are you sure you want to delete this record for "+ valStr +"?");
		builder.setPositiveButton("OK", new Dialog.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				entryDTO.delete(existingEntry);
				Crouton.makeText(client.getActivity(), "Record Deleted", Style.CONFIRM).show();
				if (goToPrevScreen)
				{
					activity.getFragmentManager().popBackStack();	
				}
				else
				{
					client.refresh();
				}
			}
		});
		builder.setNegativeButton("Cancel", null);
		builder.show();		
	}
}
