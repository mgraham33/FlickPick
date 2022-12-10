package com.flickpick.flickpick.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.flickpick.flickpick.data.LoginDataSource;
import com.flickpick.flickpick.data.LoginRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor.
 *
 * @author Dustin Cornelison
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {
	
	/**
	 * Creates a new instance of the given 'Class'.
	 *
	 * @param modelClass a 'Class' whose instance is requested
	 * @return a newly created ViewModel
	 */
	@NonNull
	@Override
	@SuppressWarnings("unchecked")
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		if (modelClass.isAssignableFrom(LoginViewModel.class)) {
			return (T)new LoginViewModel(LoginRepository.getInstance(new LoginDataSource()));
		} else {
			throw new IllegalArgumentException("Unknown ViewModel class");
		}
	}
}