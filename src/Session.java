
/*
 * Session.java
 */

/**
 * Session class <br>
 * isolates the current session User
 */
public final class Session {
    private static String currentUserId;

	/**
	 * Session() <br>
	 * private constructor
	 * - prevents instantiation
	 */
    private Session() {} // prevent instantiation

	/**
	 * setCurrentUserId(userId) <br>
	 * sets the current user ID
	 * @param userId (String) current user ID
	 */
    public static void setCurrentUserId(String userId) {
        currentUserId = userId;
    }

	/**
	 * getCurrentUserId() <br>
	 * returns the current user ID to the caller
	 * @return (String) current user ID
	 */
    public static String getCurrentUserId() {
        return currentUserId;
    }
}
