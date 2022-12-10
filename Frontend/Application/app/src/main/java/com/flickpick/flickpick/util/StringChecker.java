package com.flickpick.flickpick.util;

import android.util.Patterns;

import com.flickpick.flickpick.R;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Used to check if login/sign up details are valid
 *
 * @author Dustin Cornelison
 */
public class StringChecker {
	/**
	 * Check if the input username is valid.
	 *     Criteria:
	 *     <ul>
	 *         <li>Must be greater than 3 characters</li>
	 *         <li>Must only contain letters, numbers, and underscores</li>
	 *     </ul>
	 * @param username the user input username
	 * @return the results
	 * @see CheckResults
	 */
	public static CheckResults usernameCheck(String username) {
		if (username == null || username.length() < 3) {
			return new CheckResults(R.string.invalid_username_short);
		}
		if (username.contains("@")) {
			return new CheckResults(R.string.invalid_username_email);
		} else {
			if (username.equalsIgnoreCase("guest")) return new CheckResults(R.string.invalid_username_guest);
			String ch = username.toLowerCase(Locale.ROOT).replaceAll("[a-z]|[0-9]|_", "");
			if (ch.length() == 0) return new CheckResults(-1);
			return new CheckResults(R.plurals.invalid_username_character, ch.toCharArray());
		}
	}
	
	/**
	 * Check if the input email is valid. Criteria:
	 * <ul>
	 *     <li>Must match the {@code Patters.EMAIL_ADDRESS}</li>
	 *     <li>At least one character before the @</li>
	 *     <li>At least one character directly after the @, followed by a dot</li>
	 *     <li>At least one character following the dot</li>
	 * </ul>
	 * @param email the user input email
	 * @return the results
	 * @see CheckResults
	 * @see Patterns#EMAIL_ADDRESS
	 */
	public static CheckResults emailCheck(String email) {
		if (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches()) return new CheckResults(-1);
		return new CheckResults(R.string.invalid_email);
	}
	
	/**
	 * Check if the input password is valid. Criteria:
	 * <ul>
	 *     <li>At least 6 characters long</li>
	 *     <li>Cannot start or end with a space</li>
	 *     <li>
	 *         Must contain at least one of each of the following:
	 *         <ul>
	 *             <li>Lower case letter</li>
	 *             <li>Upper case letter</li>
	 *             <li>Number</li>
	 *             <li>Symbol</li>
	 *         </ul>
	 *     </li>
	 *     <li>Less than 32 characters</li>
	 * </ul>
	 * @param password the user input password
	 * @return the results
	 * @see CheckResults
	 */
	public static CheckResults passwordCheck(String password) {
		if (password == null || password.length() < 6) return new CheckResults(R.string.invalid_password_short);
		if (password.charAt(0) == ' ' || password.charAt(password.length() - 1) == ' ')
			return new CheckResults(R.string.invalid_password_space);
		if (password.replaceAll("[a-z]", "").length() == password.length() || //if no lowercase
			password.replaceAll("[A-Z]", "").length() == password.length() || //or no uppercase
			password.replaceAll("[0-9]", "").length() == password.length() || //or no number
			password.replaceAll("[a-z]|[A-Z]|[0-9]", "").length() == 0) { 	  //or no symbol
			return new CheckResults(R.string.invalid_password_weak);
		}
		if (password.length() > 32) return new CheckResults(R.string.invalid_password_long);
		return new CheckResults(-1);
	}
	
	/**
	 * Used to return whether something passes a check, and the reason if not.
	 */
	public static class CheckResults {
		private final int reasonID;
		private final char[] args;
		private String Args;
		
		/**
		 * Creates a result with the given reason.
		 * @param reason the string resource that gives the reason the check failed,
		 * or {@code -1} if the check passes.
		 * @param args any arguments needed inside the String Resource
		 */
		public CheckResults(int reason, char... args) {
			this.reasonID = reason;
			this.args = args;
		}
		
		/**
		 * @return the reason, or -1 if check passes
		 */
		public int getReason() {
			return reasonID;
		}
		
		/**
		 * @return the arguments supplied
		 */
		public char[] getArgs() {
			return args;
		}
		
		/**
		 * Formats the arguments as a string.
		 * @return the formatted arguments
		 */
		public String getArgsAsString() {
			if (Args != null) return Args;
			if (args.length == 0) return "";
			Set<Character> charSet = new LinkedHashSet<>();
			for (char c : args) charSet.add(c);
			
			StringBuilder b = new StringBuilder();
			for (Character c : charSet) {
				b.append(c);
				b.append("\", \"");
			}
			if (b.length() > 4) b.delete(b.length() - 4, b.length());
			
			Args = b.toString();
			return Args;
		}
		
		/**
		 * @return whether the check passed; in other words, whether the reason is {@code -1}
		 */
		public boolean passes() {
			return reasonID == -1;
		}
	}
}
