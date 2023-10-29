package es.upm.miw.bantumi.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Score {
    @PrimaryKey
    @NonNull
    public String date;

    public Score(@NonNull String date, String playerName, int seeds){
        this.date = date;
        this.playerName = playerName;
        this.seeds = seeds;
    }

    @ColumnInfo(name = "player_name")
    public String playerName;

    @ColumnInfo(name = "seeds")
    public int seeds;

    @NonNull
    public String getDate(){
        return this.date;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getSeeds() {
        return this.seeds;
    }
}

