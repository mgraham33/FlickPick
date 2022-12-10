package com.flickpick.flickpick.ui.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.data.model.LoggedInUser;
import com.flickpick.flickpick.databinding.FragmentUserListBinding;
import com.flickpick.flickpick.databinding.WidgetUserBinding;
import com.flickpick.flickpick.ui.search.SearchFragment;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.Constants;
import com.flickpick.flickpick.util.NavigationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class UserListFragment extends Fragment {
	FragmentUserListBinding binding;
	ArrayList<UserElement> userList;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		binding = FragmentUserListBinding.inflate(inflater, container, false);
		userList = new ArrayList<>();
		
		binding.userSearchButton.setOnClickListener(v -> {
			for (UserElement e : userList) {
				e.binding.getRoot().setVisibility(View.VISIBLE);
				if (!e.username.toLowerCase().contains(binding.editTextTextPersonName.getText().toString().toLowerCase())) {
					e.binding.getRoot().setVisibility(View.GONE);
				}
			}
		});
		
		JsonArrayRequest allUsers = new JsonArrayRequest(Request.Method.GET, Constants.USERS_URL, null, response -> {
			try {
				for (int i = 0; i < response.length(); i++) addUser(response.getJSONObject(i));
				Collections.sort(userList, (o1, o2) -> o1.username.compareTo(o2.username));
			} catch (JSONException e) {
				Toast.makeText(getContext(), R.string.parse_user_error, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			
		}, error -> {
			error.printStackTrace();
			Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
		});
		
		AppController.getInstance().addToRequestQueue(allUsers);
		return binding.getRoot();
	}
	
	/**
	 * Resets the binding.
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
	
	@SuppressLint("SetTextI18n")
	private void addUser(JSONObject o) throws JSONException {
		int id = o.getInt("id");
		String name = o.getString("username");
		String email = o.getString("email");
		String bio = "";
		LoggedInUser.UserType type = LoggedInUser.UserType.fromString(o.getString("userType"));
		WidgetUserBinding u = WidgetUserBinding.inflate(getLayoutInflater());
		u.usernameTitle.setText(name + "#" + id);
		u.userEmail.setText(email);
		u.userBio.setText(bio);
		
		u.userRoot.setOnClickListener(v -> {
			Bundle b2 = new Bundle();
			b2.putInt(EditUserFragment.USER, id);
			((NavigationActivity)requireActivity()).navTo(R.id.editUserFragment, b2);
		});
		
		u.userRoot.setOnLongClickListener(v -> {
			new AlertDialog.Builder(getContext()).setTitle(name + "#" + id).setMessage(email + "\n" + bio).show();
			return false;
		});
		
		//Profile picture
		SearchFragment.imageRequest(u.userImage,
			() -> u.userResultProgress.setVisibility(View.GONE), getContext(), Constants.USERS_URL + "/" + id + "/pfp", "pfp");
		
		switch (type) {
			case User:
				u.userType.setImageDrawable(AppController.getDrawableRes(R.drawable.user_icon));
				break;
			case Admin:
				u.userType.setImageDrawable(AppController.getDrawableRes(R.drawable.admin_icon));
			default: break;
		}
		
		userList.add(new UserElement(name, u));
		binding.userListLayout.addView(u.getRoot());
	}
	
	/**
	 * Used to keep track of users and help sort
	 */
	private static class UserElement {
		String username;
		WidgetUserBinding binding;
		private UserElement(String name, WidgetUserBinding b) {
			username = name;
			binding = b;
		}
	}
}