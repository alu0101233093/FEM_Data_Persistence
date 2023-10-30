package es.upm.miw.bantumi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;


public class DeleteAlertDialog extends DialogFragment {

    @NonNull
    @Override
	public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
		final ScoresActivity scores = (ScoresActivity) requireActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(scores);
        builder
                .setTitle(R.string.txtBorrarPuntuaciones)
                .setMessage(R.string.txtBorrarPuntuacionesPregunta)
                .setPositiveButton(
                        getString(android.R.string.ok),
                        (dialog, which) -> scores.scoreViewModel.deleteAll()
                )
                .setNegativeButton(
                        getString(android.R.string.cancel),
                        (dialog, which) -> {}
                );
		return builder.create();
	}
}
