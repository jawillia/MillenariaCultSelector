package com.botno.millenariacultselection.activities;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.botno.millenariacultselection.fragments.PremadeCultCreatorDialogFragment;
import com.botno.millenariacultselection.fragments.PremadeCultPickerDialogFragment;
import com.botno.millenariacultselection.R;
import com.botno.millenariacultselection.adapters.CultAdapter;
import com.botno.millenariacultselection.data.FactionContentProvider;
import com.botno.millenariacultselection.data.FactionReaderContract;
import com.botno.millenariacultselection.data.FactionReaderDbHelper;
import com.botno.millenariacultselection.models.Cult;
import com.botno.millenariacultselection.models.Player;

import java.util.ArrayList;


public class IncludeCultsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, PremadeCultPickerDialogFragment.onCultsPickedListener {
    private int numberOfPlayers;
    private CultAdapter cultAdapter;
    private Cursor cultCursor;
    private FactionReaderDbHelper dbHelper;
    private SQLiteDatabase database;
    private String[] PROJECTION = {
            FactionReaderContract.PremadeCult._ID,
            FactionReaderContract.PremadeCult.CULT_COLUMN_NAME,
            FactionReaderContract.PremadeCult.CULT_COLUMN_DIFFICULTY,
            FactionReaderContract.PremadeCult.CULT_COLUMN_EXPANSION
            //FactionReader.Contract.PremadeCult.CULT_COLUMN_ICON
            //FactionReaderContract.PremadeCult.CULT_COLUMN_ACTIVE
    };
    private ListView listView;

    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        numberOfPlayers = bundle.getInt("numberOfPlayers");

        setContentView(R.layout.activity_include_cults);
        listView = (ListView) findViewById(R.id.active_cult_listview);

        fillData();
        listView.setAdapter(cultAdapter);

        // Adjusts the sub-title based on number of players
        insertMinCultNumberText();

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void fillData()
    {
        dbHelper = new FactionReaderDbHelper(this);
        cultCursor = dbHelper.getAllActiveCults();

        String[] fromColumns = {FactionReaderContract.PremadeCult.CULT_COLUMN_NAME, FactionReaderContract.PremadeCult.CULT_COLUMN_EXPANSION,
                FactionReaderContract.PremadeCult.CULT_COLUMN_DIFFICULTY};
        int[] toViews = {R.id.name, R.id.expansion, R.id.difficulty};

        cultAdapter = new CultAdapter(this, R.layout.include_cult_row, cultCursor, fromColumns, toViews);


    }

    private void insertMinCultNumberText()
    {
        TextView adjustedText = (TextView) findViewById(R.id.cultsIncludedTextView);
        adjustedText.append(" (Choose at least " + numberOfPlayers + ")");
    }

    private ArrayList<Cult> makeCultList()
    {
        ArrayList<Cult> cultList = new ArrayList<Cult>();
        Cursor newCursor = cultAdapter.getCursor();
        newCursor.moveToFirst();
        for (int i = 0; i < cultAdapter.getCount(); i++)
        {
            int cultNameIndex = newCursor.getColumnIndexOrThrow(FactionReaderContract.PremadeCult.CULT_COLUMN_NAME);
            int cultExpansionIndex = newCursor.getColumnIndexOrThrow(FactionReaderContract.PremadeCult.CULT_COLUMN_EXPANSION);
            int cultDifficultyIndex = newCursor.getColumnIndexOrThrow(FactionReaderContract.PremadeCult.CULT_COLUMN_DIFFICULTY);
            String newCultName = newCursor.getString(cultNameIndex);
            String newExpansion = newCursor.getString(cultExpansionIndex);
            String newDifficulty = newCursor.getString(cultDifficultyIndex);
            Cult newCult = new Cult(newCultName, newDifficulty, newExpansion);
            cultList.add(newCult);
            newCursor.moveToNext();
        }
        return cultList;
    }

    @Override
    public void onCultsChangedListener() {
        // to refresh activity
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        fillData();
//
//        final View includeCultView = getLayoutInflater().inflate(R.layout.activity_include_cults, null);
//        setContentView(R.layout.activity_include_cults);
//        final ListView listView = (ListView) includeCultView.findViewById(R.id.listView);
//
//        listView.setAdapter(cultAdapter);
//    }

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

    /** Called when the user clicks the Add Cult button */
    public void pickCult(View view) {
        DialogFragment pickCultFragment = new PremadeCultPickerDialogFragment();
        pickCultFragment.show(getFragmentManager(), "pick_cult");
    }

    /** Called when the user clicks the Select button */
    public void makeCult(View view) {
        DialogFragment makeCultFragment = new PremadeCultCreatorDialogFragment();
        makeCultFragment.show(getFragmentManager(), "make_cult");
    }

    public void prioritizeCults(View view) {
        Intent intent = new Intent(this, PrioritizeCultsActivity.class);
        intent.putExtra("numberOfPlayers", numberOfPlayers);
        ArrayList<Cult> listOfCults = makeCultList();
        intent.putParcelableArrayListExtra("listOfCults", listOfCults);
        ArrayList<Player> listOfPlayers = new ArrayList<Player>();
        intent.putParcelableArrayListExtra("listOfPlayers", listOfPlayers);
        intent.putExtra("currentPlayerNumber", 1);
        startActivity(intent);
    }

    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, FactionContentProvider.CONTENT_URI,
                PROJECTION, null, null, null);
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        //Log.d("Include Adapter", "onLoadFinished: " + cultCursor.getCount());
        // A switch-case is useful when dealing with multiple Loaders/IDs
        switch (loader.getId()) {
            case LOADER_ID:
                // The asynchronous load is complete and the data
                // is now available for use. Only now can we associate
                // the queried Cursor with the SimpleCursorAdapter.
                // Swap the new cursor in.  (The framework will take care of closing the
                // old cursor once we return.)
                cultAdapter.swapCursor(cultCursor);
                break;
        }

        //listView.setAdapter(cultAdapter);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        cultAdapter.swapCursor(null);
    }
}
