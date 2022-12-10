package com.flickpick.flickpick.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flickpick.flickpick.R;
import com.flickpick.flickpick.databinding.FragmentTextChangeDialogBinding;

/**
 * Creates a generic dialog box with a user editable text box.
 * <pre>
 * Use {@code setTitle(int)} to set the title.
 * Use {@code setAccept(int)} to set the accept button text.
 * Use {@code setHint(int)} to set the hint text.
 * </pre>
 */
public class TextChangeDialogFragment extends DialogFragment {
	private int title = R.string.text_dialog_default;
	private int accept = R.string.text_dialog_default;
	private int hint = R.string.empty;
	private String text = "";
	private TextWatcher textListener = null;
	private DialogInterface.OnClickListener okListener = null;
	private DialogInterface.OnClickListener cancelListener = null;
	private EditText textbox;
	private EditText textbox2;
	private boolean disableOk = false;
	private boolean isPassword = false;
	private int inputType = InputType.TYPE_CLASS_TEXT;
	
	/**
	 * Sets the title of this dialog to the given resource. Will also set the accept
	 * button text if it is the default {@code text_dialog_default}
	 * @param resource The resource to set the title to
	 * @return {@code this}, for chaining
	 */
	public TextChangeDialogFragment setTitle(@StringRes int resource) {
		title = resource;
		if (accept == R.string.text_dialog_default) accept = resource;
		return this;
	}
	/**
	 * Sets the accept button text of this dialog to the given resource
	 * @param resource The resource to set the accept button text to
	 * @return {@code this}, for chaining
	 */
	public TextChangeDialogFragment setAccept(@StringRes int resource) {
		accept = resource;
		return this;
	}
	/**
	 * Sets the hint text of this dialog to the given resource
	 * @param resource The resource to set the hint text to
	 * @return {@code this}, for chaining
	 */
	public TextChangeDialogFragment setHint(@StringRes int resource) {
		hint = resource;
		return this;
	}
	/**
	 * Sets the text of this dialog's text box to the given {@code String}
	 * @param text The {@code String} to set the text box's text to
	 * @return {@code this}, for chaining
	 */
	public TextChangeDialogFragment setPrefillText(String text) {
		this.text = text;
		return this;
	}
	/**
	 * Sets the {@code TextWatcher} of this dialog's text box
	 * @param watcher What to do when the text is changed
	 * @return {@code this}, for chaining
	 */
	public TextChangeDialogFragment setTextChangeListener(TextWatcher watcher) {
		textListener = watcher;
		return this;
	}
	/**
	 * Sets the {@code OnClickListener} of this dialog's ok button
	 * @param listener What to do when the ok button is pushed
	 * @return {@code this}, for chaining
	 */
	public TextChangeDialogFragment setOkListener(DialogInterface.OnClickListener listener) {
		okListener = listener;
		return this;
	}
	/**
	 * Sets the {@code OnClickListener} of this dialog's cancel button
	 * @param listener What to do when the cancel button is pushed
	 * @return {@code this}, for chaining
	 */
	public TextChangeDialogFragment setCancelListener(DialogInterface.OnClickListener listener) {
		cancelListener = listener;
		return this;
	}
	/**
	 * Disables this dialog's ok button when created
	 * @return {@code this}, for chaining
	 */
	public TextChangeDialogFragment initiallyDisableOk() {
		disableOk = true;
		return this;
	}
	
	/**
	 * Will provide another text box for confirming a password, and will
	 * star both text boxes
	 * @return {@code this}, for chaining
	 */
	//Note: this method can be split into multiple methods if needed
	public TextChangeDialogFragment setAsPasswordField() {
		isPassword = true;
		return this;
	}
	
	/**
	 * @return the text-box's text
	 */
	public String getTextBoxText() {
		if (textbox == null) return null;
		return textbox.getText().toString();
	}
	
	/**
	 * Checks if both text boxes are the same (for password check).
	 * @return whether the text boxes contain the same text
	 */
	public boolean areTextBoxesSame() {
		if (textbox2 == null) {
			Log.w("TextChangeDialogFrag", "areTextBoxesSame: ",
			      new Throwable("The second text box is not in use! " +
				                    "Check if you meant to call this method, " +
				                    "or call setAsPasswordField() when creating this dialog!"));
			return false;
		} else return textbox.getText().toString().equals(textbox2.getText().toString());
	}
	
	/**
	 * @param enabled whether to enable the ok button
	 */
	public void setOkEnabled(boolean enabled) {
		AlertDialog r = ((AlertDialog)getDialog());
		if (r != null) {
			Button ok = r.getButton(AlertDialog.BUTTON_POSITIVE);
			if (ok != null) ok.setEnabled(enabled);
		}
	}
	
	/**
	 * Sets the input type for the text box.
	 * @param type the type of text box to set to. Should be a type specified in {@code InputType}
	 * @return {@code this}, for chaining
	 * @see InputType
	 */
	public TextChangeDialogFragment setInputType(int type) {
		inputType = type;
		return this;
	}
	
	/**
	 * Sets the error for the text box to the given string resource
	 * @param res the resource to set the error to
	 */
	public void setTextboxError(Integer res) {
		if (res == null) textbox.setError(null);
		else textbox.setError(getString(res));
	}
	/**
	 * Sets the error for the text box to the given string
	 * @param res the string to set the error to
	 */
	public void setTextboxError(String res) {
		textbox.setError(res);
	}
	
	/**
	 * Sets the error for the second text box to the given string resource
	 * @param res the resource to set the error to
	 */
	public void setTextbox2Error(Integer res) {
		if (textbox2 == null)
			Log.w("TextChangeDialogFrag", "setTextbox2Error: ",
			      new Throwable("The second text box is not in use! " +
				                    "Check if you meant to call this method, " +
				                    "or call setAsPasswordField() when creating this dialog!"));
		else if (res == null) textbox2.setError(null);
		else textbox2.setError(getString(res));
	}
	
	/**
	 * Helper to determine if the given {@code Editable} is from the first text box.
	 * @param e the {@code Editable} to check
	 * @return whether the {@code Editable} is from the first text box
	 */
	public boolean isTextbox1(Editable e) {
		return textbox.getEditableText() == e;
	}
	
	/**
	 * Creates the dialog.
	 * @param savedInstanceState not used in this instance
	 * @return the created dialog
	 */
	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		FragmentTextChangeDialogBinding binding = FragmentTextChangeDialogBinding.inflate(getLayoutInflater());
		textbox = binding.dialogTextbox;
		textbox.setHint(hint);
		textbox.setText(text);
		textbox.setInputType(inputType);
		if (isPassword) {
			textbox2 = binding.dialogTextbox2;
			textbox.setInputType(textbox2.getInputType());
			textbox2.setLayoutParams(textbox.getLayoutParams());
			textbox2.setEnabled(true);
			if (textListener != null) textbox2.addTextChangedListener(textListener);
		}
		if (textListener != null) textbox.addTextChangedListener(textListener);
		
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(binding.getRoot())
			.setMessage(title)
			.setPositiveButton(accept, okListener)
			.setNegativeButton(android.R.string.cancel, cancelListener);
		
		// Create the AlertDialog object and return it
		AlertDialog r = builder.create();
		r.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				setOkEnabled(!disableOk);
			}
		});
		return r;
	}
}