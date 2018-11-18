package com.botno.millenariacultselection.activities;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.botno.millenariacultselection.R;
import com.botno.millenariacultselection.adapters.MyAdapter;
import com.botno.millenariacultselection.models.Cult;
import com.botno.millenariacultselection.models.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DistributeCults extends AppCompatActivity {
    private int numberOfPlayers;
    private ArrayList<Player> listOfPlayers;
    private ArrayList<Cult> listOfCults;
    private Set<String> allAvailableCults;
    private Set<String> selectedCults;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribute_cults);

        Bundle bundle = getIntent().getExtras();
        numberOfPlayers = bundle.getInt("numberOfPlayers");
        listOfPlayers = bundle.getParcelableArrayList("listOfPlayers");

        mRecyclerView = (RecyclerView)findViewById(R.id.chosen_cults_recycleView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        listOfCults = bundle.getParcelableArrayList("listOfCults");
        ArrayList<String> cultNameList = new ArrayList<>();
        for (int i = 0; i < listOfCults.size(); i++)
        {
            cultNameList.add(listOfCults.get(i).getName());
        }
        allAvailableCults = new HashSet<String>();
        allAvailableCults.addAll(cultNameList);
        //Log.d("Distribute Cults", "Available cults: " + allAvailableCults);
        selectedCults = new HashSet<String>();
        chooseCults(listOfPlayers);
        Collections.shuffle(listOfPlayers);
        chooseClockPositions();

        mAdapter = new MyAdapter(listOfPlayers);
        mRecyclerView.setAdapter(mAdapter);

        final Button button = (Button)findViewById(R.id.displayPlayers);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setShowPlayers(true);
                mRecyclerView.setAdapter(mAdapter);
                button.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void chooseCults(ArrayList<Player> playerArrayList)
    {
        playerArrayList = orderCults(playerArrayList);

        for (Player player:
             playerArrayList) {
            List<String> chosenCultsCopy = new ArrayList<String>(player.getChosenCults());
            if(player.getAreCultsRandomized()) {
                Collections.shuffle(chosenCultsCopy);
            }
            for (String cult:
                 player.getChosenCults()) {
                if(selectedCults.contains(cult)) {
                    chosenCultsCopy.remove(cult);
                }
                else {
                    player.setAppointedCult(cult);
                    Log.d("Distribute cults", "Selected cult " + cult + " for player " + player.getPlayerID());
                    selectedCults.add(cult);
                    //Log.d("Distribute Cults", "Selected cults so far: " + selectedCults);
                    break;
                }
            }
            if(chosenCultsCopy.size() == 0) {
                Set<String> playerAvailableCults = new HashSet<String>(allAvailableCults);
                playerAvailableCults.removeAll(selectedCults);
                Iterator it = playerAvailableCults.iterator();
                if(it.hasNext()) {
                    String cult = it.next().toString();
                    player.setAppointedCult(cult);
                    Log.d("Distribute cults", "Selected cult " + cult + " for player " + player.getPlayerID());
                    selectedCults.add(cult);
                    //Log.d("Distribute Cults", "Selected cults so far: " + selectedCults);
                }
            }
        }
    }

    private ArrayList<Player> orderCults(ArrayList<Player> playerArrayList)
    {
        Collections.sort(playerArrayList);
        for (Player player:
             playerArrayList) {
            //Log.d("Distribute Cults", "Player: " + player.getPlayerID() + ". Cults: " + player.getChosenCults());
        }
        return playerArrayList;
    }

    private void chooseClockPositions() {
        int clockPosition = 12;
        int clockInterval = 12/numberOfPlayers;
        for(int i = 0; i < numberOfPlayers; i++) {
            listOfPlayers.get(i).setCultLocation(clockPosition);
            if(clockPosition == 12) clockPosition = 0;
            clockPosition += clockInterval;
        }
    }
}
