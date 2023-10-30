package es.upm.miw.bantumi.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ScoreRepository {
    private final ScoreDAO mScoreDao;
    private final LiveData<List<Score>> mAllScores;

    ScoreRepository(Application application) {
        ScoreRoomDatabase db = ScoreRoomDatabase.getDatabase(application);
        mScoreDao = db.scoreDAO();
        mAllScores = mScoreDao.getAll();
    }

    LiveData<List<Score>> getAllScores() {
        return mAllScores;
    }

    void insert(Score score) {
        ScoreRoomDatabase.databaseWriteExecutor.execute(() -> mScoreDao.insert(score));
    }

    void deleteAll() {
        ScoreRoomDatabase.databaseWriteExecutor.execute(() -> mScoreDao.deleteAll());
    }
}
