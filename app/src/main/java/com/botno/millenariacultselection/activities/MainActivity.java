package com.botno.millenariacultselection.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;

import com.botno.millenariacultselection.R;


public class MainActivity extends AppCompatActivity {
    public final static String NUMBER_OF_PLAYERS = "com.botno.millenariacultselection.PLAYERNUMBER";
    private NumberPicker np;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        np = (NumberPicker) findViewById(R.id.numberOfPlayersPicker);
        String[] nums = new String[20];
        for(int i=0; i<nums.length; i++)
            nums[i] = Integer.toString(i+1);

        np.setMinValue(1);
        np.setMaxValue(20);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(nums);
        np.setValue(4);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Select button */
    public void selectPlayers(View view) {
        Intent intent = new Intent(this, IncludeCultsActivity.class);
        intent.putExtra("numberOfPlayers", np.getValue());
        startActivity(intent);
    }

    //Initiates database with existing cults
    private void initiateCults()
    {

    }

//    public void writeDatabase() {
//        //openDatabase();
//        Log.d("Create Database", "Creating database...");
//        //Create a new map of values, where column names are the keys
//        ContentValues values = new ContentValues();
//        values.put(FactionReaderContract.PremadeCult.CULT_COLUMN_NAME, name);
//        values.put(FactionReaderContract.PremadeCult.CULT_COLUMN_EXPANSION, expansion);
//        values.put(FactionReaderContract.PremadeCult.CULT_COLUMN_DIFFICULTY, difficulty);
//        Log.d("Create Database", "Inserting " + name + ", " + expansion + ", " + difficulty + "...");
//        //Insert drawable to database
//
//        if (cultUri == null) {
//            // New cult
//            cultUri = getActivity().getContentResolver().insert(FactionContentProvider.OTHER_CONTENT_URI, values);
//            Log.d("Create Database", "Creating cult uri: " + cultUri);
//        } else {
//            // Update cult
//            getActivity().getContentResolver().update(cultUri, values, null, null);
//        }
//        Log.d("Create Database", "Database creation complete.");
//    }
}
