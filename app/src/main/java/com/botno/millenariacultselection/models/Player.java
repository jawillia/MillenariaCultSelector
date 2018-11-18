package com.botno.millenariacultselection.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Player implements Parcelable, Comparable<Player> {
    protected int playerID;
    protected ArrayList<String> chosenCults;
    protected boolean areCultsRandomized;
    protected int cultLocation;
    protected String appointedCult;

    public Player(int id, ArrayList<String> cults, boolean randomizeCults, int cultLocation)
    {
        this.playerID = id;
        this.chosenCults = cults;
        this.areCultsRandomized = randomizeCults;
        this.cultLocation = cultLocation;
        appointedCult = new String();
    }

    public Player(int id, ArrayList<String> cults, boolean randomizeCults)
    {
        this.playerID = id;
        this.chosenCults = cults;
        this.areCultsRandomized = randomizeCults;
        appointedCult = new String();
    }

    public Player(int id, ArrayList<String> cults)
    {
        this.playerID = id;
        this.chosenCults = cults;
        this.areCultsRandomized = false;
        cultLocation = 0;
        appointedCult = new String();
    }

    public Player(Parcel in)
    {
        super();
            readFromParcel(in);
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {

            return new Player[size];
        }
    };

    public void readFromParcel(Parcel in) {
        playerID = in.readInt();
        chosenCults = in.readArrayList(String.class.getClassLoader());
        areCultsRandomized = in.readByte() != 0;
        cultLocation = in.readInt();
        appointedCult = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(playerID);
        dest.writeList(chosenCults);
        dest.writeByte((byte)(areCultsRandomized ? 1: 0));
        dest.writeInt(cultLocation);
        dest.writeString(appointedCult);
    }

    public int getPlayerID()
    {
        return playerID;
    }

    public void setChosenCults(ArrayList<String> cultList)
    {
        chosenCults = cultList;
    }

    public ArrayList<String> getChosenCults()
    {
        return chosenCults;
    }

    public boolean getAreCultsRandomized()
    {
        return areCultsRandomized;
    }

    public void setAreCultsRandomized(boolean randomize)
    {
        areCultsRandomized = randomize;
    }

    public String getAppointedCult()
    {
        return appointedCult;
    }

    public void setAppointedCult(String cult)
    {
        appointedCult = cult;
    }

    public void setCultLocation(int cultLocation) {
        this.cultLocation = cultLocation;
    }

    public int getCultLocation() {
        return cultLocation;
    }

    @Override
    public int compareTo(@NonNull Player other) {
        return Integer.compare(chosenCults.size(), other.getChosenCults().size());
    }

}
