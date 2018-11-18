package com.botno.millenariacultselection.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.content.Loader;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.botno.millenariacultselection.R;
import com.botno.millenariacultselection.data.FactionContentProvider;
import com.botno.millenariacultselection.data.FactionReaderContract;
import com.botno.millenariacultselection.data.FactionReaderDbHelper;
import com.botno.millenariacultselection.models.Cult;
import com.botno.millenariacultselection.widgets.CheckableLinearLayout;

import java.util.ArrayList;

/**
 * Created by User on 1/15/2015.
 */
public class PremadeCultPickerDialogFragment extends DialogFragment
    implements LoaderManager.LoaderCallbacks<Cursor>{
    onCultsPickedListener mCallback;
    //Database fields
    private SQLiteDatabase database;
    private FactionReaderDbHelper dbHelper;
    private SimpleCursorAdapter sca;
    //all of the set cults
    private ArrayList<Cult> setCults;

    private Uri cultUri;
    // Defines a projection that specifies which columns from the database
    // will be used after this query
    String[] PROJECTION = {
            FactionReaderContract.PremadeCult._ID,
            FactionReaderContract.PremadeCult.CULT_COLUMN_NAME,
            FactionReaderContract.PremadeCult.CULT_COLUMN_DIFFICULTY,
            //FactionReaderContract.PremadeCult.CULT_COLUMN_EXPANSION
            //FactionReader.Contract.PremadeCult.CULT_COLUMN_ICON
            FactionReaderContract.PremadeCult.CULT_COLUMN_ACTIVE
    };

    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    private static final int LOADER_ID = 1;

    // How you want the results sorted in the resulting Cursor
    String sortOrder =
            FactionReaderContract.PremadeCult.CULT_COLUMN_EXPANSION + " DESC";

    // This is the select criteria
    static final String SELECTION = "((" +
            FactionReaderContract.PremadeCult.CULT_COLUMN_NAME + " NOTNULL) AND (" +
            FactionReaderContract.PremadeCult.CULT_COLUMN_NAME + " != '' ))";

    public interface onCultsPickedListener {
        public void onCultsChangedListener();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (onCultsPickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onCultsPickedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle(R.string.include_cult);

        setCults = new ArrayList<>();

        Bundle extras = getActivity().getIntent().getExtras();

        // check from the saved Instance
        cultUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState
                .getParcelable(FactionContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            cultUri = extras
                    .getParcelable(FactionContentProvider.CONTENT_ITEM_TYPE);
        }

        final View cultPickerView = inflater.inflate(R.layout.cult_picker_view, null);
        builder.setView(cultPickerView);
        final ListView listView = (ListView) cultPickerView.findViewById(R.id.cult_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckableLinearLayout cult_row = (CheckableLinearLayout) view.findViewById(R.id.list_row);
                CheckedTextView cult_name = (CheckedTextView) cult_row.findViewById(R.id.name);
                TextView cult_difficulty = (TextView) cult_row.findViewById(R.id.difficulty);
                if(cult_name.isChecked())
                {
                    setCults.add(new Cult(cult_name.getText().toString(), cult_difficulty.getText().toString(), null));
                    Toast.makeText(getDialog().getContext(),"Cult name: " + cult_name.getText().toString(),Toast.LENGTH_SHORT).show();
                }
                else
                {
                    for(int c = setCults.size()-1 ; c >= 0; c--){
                        if(setCults.get(c).getName() == cult_name.getText().toString()
                            && setCults.get(c).getDifficulty() == cult_difficulty.getText().toString() &&
                                setCults.get(c).getIcon() == null)
                        {
                            setCults.remove(c);
                        }
                    }
                }
            }
        });

       //Add action buttons
        builder.setPositiveButton(R.string.set_cults, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int count = 0;
                        for (int i = 0; i < listView.getCount(); i++)
                        {
                            dbHelper = new FactionReaderDbHelper(getActivity());
                            int selectedID = ((int)((SimpleCursorAdapter)listView.getAdapter()).getItemId(i));
                            if(listView.isItemChecked(i))
                            {
                                dbHelper.activateCult(selectedID, true);
                                count++;
                                Cursor c = dbHelper.getCult(selectedID);
                                int active = c.getInt(c.getColumnIndex(FactionReaderContract.PremadeCult.CULT_COLUMN_ACTIVE));
                                String TAG = "PremadeCultPickerDialog";
                                Log.d(TAG, "onClick: Set item " + selectedID + " to " + active);
                            }
                            else
                            {
                                dbHelper.activateCult(selectedID, false);
                            }
                        }
                        mCallback.onCultsChangedListener();
                        Toast.makeText(getDialog().getContext(),"You selected : " + count + " items.",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.remove_cults, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SparseBooleanArray checked = listView.getCheckedItemPositions();

                        int count = 0;
                        for (int i = 0; i < checked.size(); i++) {
                            dbHelper = new FactionReaderDbHelper(getActivity());
                            long selectedID = ((SimpleCursorAdapter)listView.getAdapter()).getItemId(checked.keyAt(i));
                            dbHelper.deleteCult((int)selectedID);
                            count++;
                        }
                        mCallback.onCultsChangedListener();
                        Toast.makeText(getDialog().getContext(),"You deleted : " + count + " items.",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        fillData();

        listView.setAdapter(sca);

        for(int i = 0; i < sca.getCursor().getCount(); i++)
        {
            sca.getCursor().moveToPosition(i);
            if(sca.getCursor().getInt(sca.getCursor().getColumnIndex(FactionReaderContract.PremadeCult.CULT_COLUMN_ACTIVE)) == 1)
            {
                listView.setItemChecked(i, true);
            }
        }

        //mCallbacks = (LoaderManager.LoaderCallbacks<Cursor>) this.getActivity();
        //Prepare the loader
        getLoaderManager().initLoader(LOADER_ID, null, this);

        return  builder.create();
    }

    private void fillData() {
        dbHelper = new FactionReaderDbHelper(getActivity());
        Cursor cursor = dbHelper.getAllCults();

        String[] fromColumns = {FactionReaderContract.PremadeCult.CULT_COLUMN_NAME, FactionReaderContract.PremadeCult.CULT_COLUMN_DIFFICULTY};
        int[] toViews = {R.id.name, R.id.difficulty}; // The TextView in simple_list_item_1

        sca = new SimpleCursorAdapter(getActivity(),
                R.layout.cult_row,
                cursor,
                fromColumns, toViews, 0);

//        final SimpleCursorAdapter.ViewBinder mViewBinder =
//                new SimpleCursorAdapter.ViewBinder() {
//                    @Override
//                    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//
//                        final int checkedIndex = cursor.getColumnIndexOrThrow(FactionReaderContract.PremadeCult.CULT_COLUMN_ACTIVE);
//                        final int isActive = cursor.getInt(checkedIndex);
//                        if(view.getId() == R.id.name && isActive == 1) {
//                            CheckedTextView cult_row = (CheckedTextView) view.findViewById(R.id.name);
//                            boolean isChecked = cult_row.isChecked();
//                            Log.d("LIST","VIEW: "+view+" NAME: "+cursor.getString(columnIndex)+" "+checkedIndex);
//                            cult_row.setText(cursor.getString(cursor.getColumnIndex(FactionReaderContract.PremadeCult.CULT_COLUMN_NAME)));
//                            return true;
//                        }
//
//                        return false;
//                    }
//                };
//
//        sca.setViewBinder(mViewBinder);
    }

    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(getActivity(), FactionContentProvider.CONTENT_URI,
                PROJECTION, null, null, null);
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // A switch-case is useful when dealing with multiple Loaders/IDs
        switch (loader.getId()) {
            case LOADER_ID:
                // The asynchronous load is complete and the data
                // is now available for use. Only now can we associate
                // the queried Cursor with the SimpleCursorAdapter.
                // Swap the new cursor in.  (The framework will take care of closing the
                // old cursor once we return.)
                sca.swapCursor(data);
                break;
        }
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        sca.swapCursor(null);
    }

}
