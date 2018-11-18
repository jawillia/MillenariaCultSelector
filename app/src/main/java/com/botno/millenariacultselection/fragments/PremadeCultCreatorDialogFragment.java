package com.botno.millenariacultselection.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.botno.millenariacultselection.R;
import com.botno.millenariacultselection.data.FactionContentProvider;
import com.botno.millenariacultselection.data.FactionReaderContract;
import com.botno.millenariacultselection.data.FactionReaderDbHelper;

import java.util.ArrayList;

/**
 * Created by User on 1/15/2015.
 */
public class PremadeCultCreatorDialogFragment extends DialogFragment {
    //Database fields
    private SQLiteDatabase database;
    private FactionReaderDbHelper dbHelper;
    private String name;
    private String expansion;
    private String difficulty;
    private String image;

    private Uri cultUri;
    private ArrayList<Uri> cultUriList;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final FactionReaderDbHelper dbHelper = new FactionReaderDbHelper(getActivity());

        Bundle extras = getActivity().getIntent().getExtras();

        // check from the saved Instance
        cultUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState
                .getParcelable(FactionContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            cultUri = extras
                    .getParcelable(FactionContentProvider.CONTENT_ITEM_TYPE);

            //fillData(todoUri);
        }

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View cultCreatorView = inflater.inflate(R.layout.cult_creator_view, null);
        builder.setView(cultCreatorView)
                // Add action buttons
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText etName = (EditText) cultCreatorView.findViewById(R.id.faction_name);
                        name = etName.getText().toString();
                        if (name.length() < 2) {
                            etName.requestFocus();
                            etName.setError("FIELD CANNOT BE EMPTY");
                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Error - Name cannot be empty.", Toast.LENGTH_LONG);
                            toast.show();
                        } else if (cultNameExists(name)) {
                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Cult already exists.", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            EditText etExpansion = (EditText) cultCreatorView.findViewById(R.id.expansion);
                            expansion = etExpansion.getText().toString();
                            EditText etDifficulty = (EditText) cultCreatorView.findViewById(R.id.difficulty);
                            difficulty = etDifficulty.getText().toString();
                            writeDatabase();
                        }
                        //cultUriList.add(cultUri);
                        //getActivity().getIntent().putExtra("cultUriList", cultUriList);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PremadeCultCreatorDialogFragment.this.getDialog().cancel();
                    }
                })
                .setNeutralButton(R.string.picture, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //browse for a picture or icon
                    }
                });

        return  builder.create();
    }

    private boolean cultNameExists(String cultName) {
        boolean result = false;
        FactionReaderDbHelper dbHelper = new FactionReaderDbHelper(this.getActivity());
        Cursor cursor = dbHelper.getAllCults();
        cursor.moveToPosition(0);
        for (int i = 0; i < cursor.getCount(); i++)
        {
            String cursorCult = cursor.getString(cursor.getColumnIndex(FactionReaderContract.PremadeCult.CULT_COLUMN_NAME));
            cursor.moveToNext();
            if(cursorCult.equals(cultName))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    public void openDatabase() {
        database = dbHelper.getWritableDatabase();
    }

    public void writeDatabase() {
        //openDatabase();
        Log.d("Create Database", "Creating database...");
        //Create a new map of values, where column names are the keys

        ContentValues values = new ContentValues();
        values.put(FactionReaderContract.PremadeCult.CULT_COLUMN_NAME, name);
        values.put(FactionReaderContract.PremadeCult.CULT_COLUMN_EXPANSION, expansion);
        values.put(FactionReaderContract.PremadeCult.CULT_COLUMN_DIFFICULTY, difficulty);
        Log.d("Create Database", "Inserting " + name + ", " + expansion + ", " + difficulty + "...");
        //Insert drawable to database

        if (cultUri == null) {
            // New cult
            cultUri = getActivity().getContentResolver().insert(FactionContentProvider.CONTENT_URI, values);
            Log.d("Create Database", "Creating cult uri: " + cultUri);
        } else {
            // Update cult
            getActivity().getContentResolver().update(cultUri, values, null, null);
        }
        Log.d("Create Database", "Database creation complete.");
    }

}
