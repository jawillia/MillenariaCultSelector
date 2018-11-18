package com.botno.millenariacultselection.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.botno.millenariacultselection.R;
import com.botno.millenariacultselection.models.Player;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Player> mDataset;
    private boolean showPlayers;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout mLayout;
        public MyViewHolder(LinearLayout v) {
            super(v);
            mLayout = v;
        }
    }

    public MyAdapter(ArrayList<Player> myDataset) {
        mDataset = myDataset;
        showPlayers = false;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.distribute_cult_row, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView cultName = (TextView) holder.mLayout.findViewById(R.id.txtDistributedCult);
        Log.d("Player Adapter: ", String.valueOf(showPlayers));
        if(!showPlayers) {
            cultName.setText(mDataset.get(position).getAppointedCult());
            Log.d("Player Adapter: ", mDataset.get(position).getAppointedCult());
        }
        else {
            cultName.setVisibility(View.INVISIBLE);
        }

        TextView clockPosition = (TextView) holder.mLayout.findViewById(R.id.position);
        clockPosition.setText(mDataset.get(position).getCultLocation() + "");

        TextView player = (TextView) holder.mLayout.findViewById(R.id.player);
        if(showPlayers) {
            player.setText(mDataset.get(position).getPlayerID() + "");
        }
        else {
            player.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setShowPlayers(boolean showPlayers) {
        this.showPlayers = showPlayers;
    }
}
