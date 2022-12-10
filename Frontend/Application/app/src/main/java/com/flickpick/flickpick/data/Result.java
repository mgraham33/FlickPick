package com.flickpick.flickpick.data;

import androidx.annotation.StringRes;

/**
 * A generic class that holds a result success with data or an error exception.
 *
 * @author Dustin Cornelison
 */
public class Result {
	// hide the private constructor to limit subclass types (Success, Error)
	private Result() {}
	
	/**
	 * @return a formatted string for this object
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String toString() {
		if (this instanceof Result.Success) {
			Result.Success success = (Result.Success)this;
			return "Success[data=" + success.getData().toString() + "]";
		} else if (this instanceof Result.Error) {
			Result.Error error = (Result.Error)this;
			return "Error[exception=" + error.getError().toString() + "]";
		}
		return "";
	}
	
	/**
	 * Subclass that denotes success; contains an object of type {@code <T>}.
	 * @param <T> the type of object this success should contain/be able to return
	 */
	public final static class Success<T> extends Result {
		private T data;
		
		/**
		 * Creates a success object with the given data.
		 * @param data the data this object should contain
		 */
		public Success(T data) {
			this.data = data;
		}
		
		/**
		 * @return the data contained within
		 */
		public T getData() {
			return this.data;
		}
	}
	
	/**
	 * Subclass that denotes failure.
	 */
	public final static class Error extends Result {
		private Exception error;
		private Integer message;
		private int type;
		
		/**
		 * Creates an error object with specified error information.
		 * @param error the generated exception
		 * @param message the id of the message that should be supplied to the user
		 * @param type the type of error, as used in LoginDataSource;
		 * (simply provides more information to the end user)
		 * @see LoginDataSource
		 */
		public Error(Exception error, @StringRes Integer message, int type) {
			this.error = error;
			this.message = message;
			this.type = type;
		}
		
		/**
		 * @return the exception, not the status quo
		 */
		public Exception getError() {
			return this.error;
		}
		
		/**
		 * @return the id of the string resource to be displayed to the user
		 */
		public Integer getMessage() {
			return message;
		}
		
		/**
		 * @return the type of error encountered: extra information for the user
		 */
		public int getType() {
			return type;
		}
	}
}