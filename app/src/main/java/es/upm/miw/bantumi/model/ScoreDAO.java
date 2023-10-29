package es.upm.miw.bantumi.model;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import es.upm.miw.bantumi.model.Score;

@Dao
public interface ScoreDAO {
    @Query("SELECT * FROM Score ORDER BY seeds DESC LIMIT 10")
    LiveData<List<Score>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Score score);

    @Query("DELETE FROM Score")
    void deleteAll();

}

