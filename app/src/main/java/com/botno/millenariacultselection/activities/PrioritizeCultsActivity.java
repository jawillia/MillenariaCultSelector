package com.botno.millenariacultselection.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.botno.millenariacultselection.R;
import com.botno.millenariacultselection.adapters.CultAdapter;
import com.botno.millenariacultselection.data.FactionReaderContract;
import com.botno.millenariacultselection.dslv.DragSortListView;
import com.botno.millenariacultselection.models.Cult;
import com.botno.millenariacultselection.models.Player;
import com.botno.millenariacultselection.widgets.CheckableLinearLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PrioritizeCultsActivity extends AppCompatActivity {
    private int numberOfPlayers;
    private int currentPlayerNumber;
    private ArrayList<Cult> listOfCults;
    private ArrayList<Player> listOfPlayers;
    private ArrayAdapter<String> adapter;
    private DragSortListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prioritize_cults);
        listView = (DragSortListView) findViewById(R.id.dragSortListView);
        Bundle bundle = getIntent().getExtras();
        numberOfPlayers = bundle.getInt("numberOfPlayers");
        currentPlayerNumber = bundle.getInt("currentPlayerNumber", -1);
        listOfCults = bundle.getParcelableArrayList("listOfCults");
        ArrayList<String> cultNameList = new ArrayList<>();
        for (int i = 0; i < listOfCults.size(); i++)
        {
            cultNameList.add(listOfCults.get(i).getName());
        }
        listOfPlayers = bundle.getParcelableArrayList("listOfPlayers");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Prioritized at rank: " + (position + 1), Toast.LENGTH_SHORT).show();
                CheckBox cbSelectAll = (CheckBox) findViewById(R.id.selectAll_cb);
                if(isEverythingChecked()) {
                    cbSelectAll.setChecked(true);
                }
                if (isEverythingUnchecked()) {
                    cbSelectAll.setChecked(false);
                }
            }
        });

        adapter = new ArrayAdapter<String>(this, R.layout.prioritize_cult_row, R.id.name, cultNameList);

        listView.setAdapter(adapter);

        listView.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
                if (from != to) {
                    DragSortListView list = listView;
                    String item = adapter.getItem(from);
                    adapter.remove(item);
                    adapter.insert(item, to);
                    list.moveCheckState(from, to);
                }
            }
        });

        setHeaderText();
        addListenerOnRandomizeChk();
        addListenerOnSelectAllChk();
    }

    private void setHeaderText()
    {
        TextView currentPlayerText = (TextView) findViewById(R.id.currentPlayerTextView);
        if(currentPlayerNumber != -1)
        {
            currentPlayerText.setText("Player " + currentPlayerNumber);
        }

        TextView totalPlayerText = (TextView) findViewById(R.id.totalPlayersTextView);
        totalPlayerText.setText("Out of " + numberOfPlayers);
    }

    public void prioritizeNextCult(View view)
    {
        if(currentPlayerNumber < numberOfPlayers)
        {
            Intent intent = new Intent(this, PrioritizeCultsActivity.class);
            intent.putExtra("numberOfPlayers", numberOfPlayers);
            intent.putParcelableArrayListExtra("listOfCults", listOfCults);
            intent.putParcelableArrayListExtra("listOfPlayers", addPlayerToList());
            intent.putExtra("currentPlayerNumber", ++currentPlayerNumber);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, DistributeCults.class);
            intent.putExtra("numberOfPlayers", numberOfPlayers);
            intent.putParcelableArrayListExtra("listOfCults", listOfCults);
            intent.putParcelableArrayListExtra("listOfPlayers", addPlayerToList());
            intent.putExtra("currentPlayerNumber", ++currentPlayerNumber);
            startActivity(intent);
        }
    }

    public void addListenerOnRandomizeChk() {
        CheckBox cbRandomize = (CheckBox) findViewById(R.id.randomize_cb);
        cbRandomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked())
                {
                    Toast.makeText(getApplicationContext(), "Randomize from selected cults.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addListenerOnSelectAllChk() {
        CheckBox cbSelectAll = (CheckBox) findViewById(R.id.selectAll_cb);
        cbSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isChecked = ((CheckBox)v).isChecked();
                for(int i = 0; i < listView.getChildCount(); i++)
                {
                    listView.setItemChecked(i, isChecked);
                }
            }
        });
    }

    private ArrayList<Player> addPlayerToList()
    {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        ArrayList<String> cultList = new ArrayList<String>();

        for(int i = 0; i < listView.getAdapter().getCount(); i++)
        {
            if(checked.get(i))
            {
                String cult = (String) listView.getItemAtPosition(i);
                cultList.add(cult);
            }
        }

        CheckBox cbRandomize = (CheckBox) findViewById(R.id.randomize_cb);
        if(!currentPlayerExists(listOfPlayers))
        {
            Player returnPlayer = new Player(currentPlayerNumber, cultList, cbRandomize.isChecked());
            listOfPlayers.add(returnPlayer);
        }
        else
        {
            listOfPlayers.get(currentPlayerNumber - 1).setChosenCults(cultList);
            listOfPlayers.get(currentPlayerNumber - 1).setAreCultsRandomized(cbRandomize.isChecked());
        }
        return listOfPlayers;
    }

    private Boolean currentPlayerExists(ArrayList<Player> playerList)
    {
        Boolean result = false;

        for (Player player:
             playerList) {
            if(player.getPlayerID() == currentPlayerNumber)
            {
                result = true;
            }
        }
        return result;
    }

    private boolean isEverythingChecked()
    {
        if(listView.getCheckedItemCount() == listOfCults.size()) {
            return true;
        }
        return false;
    }

    private boolean isEverythingUnchecked()
    {
        if(listView.getCheckedItemCount() == 0) {
            return true;
        }
        return false;
    }
}
