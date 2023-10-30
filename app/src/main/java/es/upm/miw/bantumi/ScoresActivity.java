package es.upm.miw.bantumi;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.upm.miw.bantumi.model.ScoreViewModel;
import es.upm.miw.bantumi.views.ScoreListAdapter;

public class ScoresActivity extends AppCompatActivity {
    ScoreViewModel scoreViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.best_scores);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final ScoreListAdapter adapter = new ScoreListAdapter(new ScoreListAdapter.ScoreDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);

        scoreViewModel.getAllScores().observe(this, adapter::submitList);

        final Button deleteButton = findViewById(R.id.deleteScores);
        deleteButton.setOnClickListener(view -> new DeleteAlertDialog().show(getSupportFragmentManager(), "ALERT_DIALOG"));

        final Button exitButton = findViewById(R.id.exitScores);
        exitButton.setOnClickListener(view -> finish());
    }
}
