package com.botno.millenariacultselection.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.botno.millenariacultselection.data.FactionReaderContract;
import com.botno.millenariacultselection.R;

/**
 * Created by siefe on 11/1/2016.
 */

public class CultAdapter extends SimpleCursorAdapter {

    private Context mContext;
    private Context appContext;
    private int layout;
    private Cursor cr;
    private final LayoutInflater inflater;
    private int[] activeCults;

    public CultAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.layout = layout;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.cr = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, parent, false);
    }

    @NonNull
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        TextView cultName = (TextView) view.findViewById(R.id.name);
        TextView cultExpansion = (TextView) view.findViewById(R.id.expansion);
        TextView cultDifficulty = (TextView) view.findViewById(R.id.difficulty);

        int cultNameIndex = cursor.getColumnIndexOrThrow(FactionReaderContract.PremadeCult.CULT_COLUMN_NAME);
        int cultExpansionIndex = cursor.getColumnIndexOrThrow(FactionReaderContract.PremadeCult.CULT_COLUMN_EXPANSION);
        int cultDifficultyIndex = cursor.getColumnIndexOrThrow(FactionReaderContract.PremadeCult.CULT_COLUMN_DIFFICULTY);
        //int cultActiveIndex = cursor.getColumnIndexOrThrow(FactionReaderContract.PremadeCult.CULT_COLUMN_ACTIVE);

        cultName.setText(cursor.getString(cultNameIndex));
        cultExpansion.setText(cursor.getString(cultExpansionIndex));
        cultDifficulty.setText(cursor.getString(cultDifficultyIndex));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}