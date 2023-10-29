package es.upm.miw.bantumi.views;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.upm.miw.bantumi.model.Score;

public class ScoreListAdapter extends ListAdapter<Score, ScoreViewHolder> {
    public ScoreListAdapter(@NonNull DiffUtil.ItemCallback<Score> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ScoreViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ScoreViewHolder holder, int position) {
        Score current = getItem(position);
        holder.bind(current.getDate(),current.getPlayerName(),String.valueOf(current.getSeeds()));
    }

    public static class ScoreDiff extends DiffUtil.ItemCallback<Score> {

        @Override
        public boolean areItemsTheSame(@NonNull Score oldItem, @NonNull Score newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Score oldItem, @NonNull Score newItem) {
            return (
                    oldItem.getDate().equals(newItem.getDate()) &&
                    oldItem.getPlayerName().equals(newItem.getPlayerName()) &&
                    oldItem.getSeeds() == oldItem.getSeeds()
            );
        }
    }
}
