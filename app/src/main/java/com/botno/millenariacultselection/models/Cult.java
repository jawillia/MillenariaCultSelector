package com.botno.millenariacultselection.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.botno.millenariacultselection.R;

import java.io.Serializable;

/**
 * Created by Jason Williams on 1/16/2015.
 */
public class Cult implements Parcelable {
    protected String name;
    protected String difficulty;
    protected String expansion;
    protected Integer icon;
    protected boolean isActive;

    public Cult(String name, String difficulty, String expansion, Integer icon)
    {
        this.name = name;
        this.difficulty = difficulty;
        this.expansion = expansion;
        this.icon = icon;
        isActive = false;
    }

    public Cult(String name, String difficulty, String expansion)
    {
        this.name = name;
        this.difficulty = difficulty;
        this.expansion = expansion;
        this.icon = R.drawable.doom;
        isActive = false;
    }

    public Cult(Parcel in)
    {
        super();
        readFromParcel(in);
    }

    public Cult()
    {
        this.name = "NewCultName";
        this.difficulty = "01";
        this.expansion = "";
        isActive = false;
    }

    public static final Parcelable.Creator<Cult> CREATOR = new Parcelable.Creator<Cult>() {
        public Cult createFromParcel(Parcel in) {
            return new Cult(in);
        }

        public Cult[] newArray(int size) {

            return new Cult[size];
        }

    };

    public String getName() {
        return name;
    }

    public void setName(String cultName) {
        this.name = cultName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String cultDifficulty) {
        this.difficulty = cultDifficulty;
    }

    public void setExpansion(String cultExpansion) { this.expansion = cultExpansion; }

    public String getExpansion() { return expansion; }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean getIsActive() { return isActive;}

    public void setIsActive(boolean isActive) { this.isActive = isActive; }

    public void readFromParcel(Parcel in) {
        name = in.readString();
        difficulty = in.readString();
        expansion = in.readString();
        icon = in.readInt();
        isActive = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(difficulty);
        dest.writeString(expansion);
        dest.writeInt(icon);
        dest.writeByte((byte) (isActive ? 1 : 0));
    }
}
