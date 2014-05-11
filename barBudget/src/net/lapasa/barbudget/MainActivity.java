package net.lapasa.barbudget;

import net.lapasa.barbudget.fragments.CategoryListFragment;
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
        setContentView(R.layout.activity_main);
        
        
        /* If there are no categories, display the default fragment, i.e. fragment_no_categories */
//        showFragment(new EmptyCategoryFragment());
        showFragment(new CategoryListFragment());


        
//        Category c = new Category("TEST CATEGORY", 0xff0000);
//        c.save();
        
//        Entry e = new Entry(new Date(), 0.01, "Test Memo", c);
//        e.save();
		
        
//        Book book = new Book("Algorithms", "2nd edition");
//        book.save();
//        Log.i(TAG, "Done");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_category)
        {
//        	showFragment(targetFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	private void showFragment(Fragment targetFragment)
	{
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.root, targetFragment);
		ft.commit();
	}

    
    /*
     
     */
}
