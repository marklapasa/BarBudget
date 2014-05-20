package net.lapasa.barbudget.fragments;

import net.lapasa.barbudget.R;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;

public class ColorPickerFragment extends DialogFragment implements OnColorChangedListener
{
	private ColorPicker colorPicker;
	private IColorPickerClient client;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.colorpicker, null);
		colorPicker = (ColorPicker) v.findViewById(R.id.picker);
		colorPicker.setOnColorChangedListener(this);
		
		return v;
	}

	@Override
	public void onColorChanged(int color)
	{
		client.setColor(color);
		dismiss();
	}

	public static ColorPickerFragment create(IColorPickerClient client)
	{
		ColorPickerFragment frag = new ColorPickerFragment();
		frag.setClient(client);
		
		return frag;
	}

	private void setClient(IColorPickerClient client)
	{
		this.client = client;
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
//		activity.setTitle(title);
	}
}
