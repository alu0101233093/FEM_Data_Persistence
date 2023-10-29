package es.upm.miw.bantumi.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ScoreViewModel extends AndroidViewModel {
    private final ScoreRepository mRepository;

    private final LiveData<List<Score>> mAllScores;

    public ScoreViewModel(Application application) {
        super(application);
        mRepository = new ScoreRepository(application);
        mAllScores = mRepository.getAllScores();
    }

    public LiveData<List<Score>> getAllScores() {
        return mAllScores;
    }

    public void insert(Score score) {
        mRepository.insert(score);
    }
}
