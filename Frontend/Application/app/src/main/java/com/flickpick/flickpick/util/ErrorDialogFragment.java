package com.flickpick.flickpick.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.flickpick.flickpick.R;
import com.flickpick.flickpick.databinding.FragmentTextChangeDialogBinding;

/**
 * Creates a generic dialog box with a user editable text box.
 * <pre>
 * Use {@code setTitle(int)} to set the title.
 * Use {@code setAccept(int)} to set the accept button text.
 * Use {@code setHint(int)} to set the hint text.
 * </pre>
 *
 * @author Dustin Cornelison
 */
public class ErrorDialogFragment extends DialogFragment {
	private int title = R.string.generic_error;
	private String msg = "";
	
	/**
	 * Sets the title of this dialog to the given resource.
	 * @param resource The resource to set the title to
	 * @return {@code this}, for chaining
	 */
	public ErrorDialogFragment setTitle(@StringRes int resource) {
		title = resource;
		return this;
	}
	
	/**
	 * Sets the message of this dialog to the given string.
	 * @param msg The message
	 * @return {@code this}, for chaining
	 */
	public ErrorDialogFragment setMessage(String msg) {
		this.msg = msg;
		return this;
	}
	
	/**
	 * Creates the dialog.
	 * @param savedInstanceState not used in this instance
	 * @return the created dialog
	 */
	@NonNull
	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title)
			.setMessage(msg)
			.setPositiveButton(android.R.string.ok, null);
		
		// Create the AlertDialog object and return it
		return builder.create();
	}
}