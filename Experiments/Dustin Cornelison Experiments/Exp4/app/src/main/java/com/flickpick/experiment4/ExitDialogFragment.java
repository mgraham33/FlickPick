package com.flickpick.experiment4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ExitDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.exit_confirm_title)
			.setPositiveButton(R.string.generic_yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					System.exit(0);
				}
			})
			.setNegativeButton(R.string.generic_no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialog
				}
			});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
