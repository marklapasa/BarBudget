package net.lapasa.barbudget;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import de.keyboardsurfer.android.widget.crouton.Crouton;

public class MainActivity extends Activity implements OnBackStackChangedListener
{

    private static final String TAG = MainActivity.class.getName();
	private FragmentManager fm;

	private ViewPager viewPager = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);
	}
	
	/*
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fm = getFragmentManager();
        fm.addOnBackStackChangedListener(this);
        ((BarBudgetApplication) getApplication()).inject(this);;
        setContentView(R.layout.activity_main);
//        viewPager = (ViewPager) findViewById(R.id.root)
        
        
        // If there are no categories, display the default fragment, i.e. fragment_no_categories
        showFragment(new CategoryListFragment());
//        showFragment(CategoryListFragment.create(PeriodModel.RANGE_TODAY));
        
        getActionBar().setSubtitle("SubTitle");
    }
    */






	public void showFragment(Fragment targetFragment)
	{
		FragmentTransaction ft = fm.beginTransaction();
		
		
		ft.replace(R.id.root, targetFragment).addToBackStack(null);
		
		ft.commit();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Crouton.cancelAllCroutons();
	}






	@Override
	public void onBackStackChanged()
	{
		if (fm.getBackStackEntryCount() == 1)
		{
			setTitle(getString(R.string.app_name));
		}
	}
}

