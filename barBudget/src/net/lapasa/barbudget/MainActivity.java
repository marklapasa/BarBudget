package net.lapasa.barbudget;

import net.lapasa.barbudget.fragments.CategoryListFragment;
import net.lapasa.barbudget.BarBudgetApplication;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
{

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ((BarBudgetApplication) getApplication()).inject(this);;
        setContentView(R.layout.activity_main);
        
        
        /* If there are no categories, display the default fragment, i.e. fragment_no_categories */
        showFragment(new CategoryListFragment());
    }






	public void showFragment(Fragment targetFragment)
	{
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		
		
		ft.replace(R.id.root, targetFragment).addToBackStack(null);
		
		ft.commit();
	}

    
    /*
     
     */
}
