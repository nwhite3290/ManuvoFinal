
/*
 * IDandPasswords.java
 */

import java.util.*;

/**
 * IDandPasswords class <br>
 * stores the usernames and passwords for the login feature
 * - Uses Hashmap to access the user ID and passwords
 */
public class IDandPasswords {

	HashMap<String, String> loginInfo = new HashMap<String, String>();

	/**
	 * IDandPasswords() <br>
	 * contains the user ID's and passwords
	 * - used with the Login feature
	 * - stored as Hashmap\<String,String\>
	 */
	IDandPasswords() {
		loginInfo.put("admin", "admin");
		loginInfo.put("user", "password");
		loginInfo.put("nathan", "Nathan10");
		loginInfo.put("", "");
	}

	/**
	 *  getLoginInfo() <br>
	 *  returns the login information to the caller
	 * @return Hashmap\<String,String\> to the caller
	 */
	protected HashMap<String,String> getLoginInfo() {
		return loginInfo;
	}
}

