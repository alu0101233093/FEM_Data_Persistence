package es.upm.miw.bantumi.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import es.upm.miw.bantumi.R;

public class ScoreViewHolder extends RecyclerView.ViewHolder {
    private final TextView scoreDateView;
    private final TextView scorePlayerNameView;
    private final TextView scoreSeedsView;

    private ScoreViewHolder(View itemView) {
        super(itemView);
        scoreDateView = itemView.findViewById(R.id.textViewDate);
        scorePlayerNameView = itemView.findViewById(R.id.textViewPlayerName);
        scoreSeedsView = itemView.findViewById(R.id.textViewSeeds);
    }

    public void bind(String date, String name, String seeds) {
        scoreDateView.setText(date);
        scorePlayerNameView.setText(name);
        scoreSeedsView.setText(seeds);
    }

    static ScoreViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }
}
