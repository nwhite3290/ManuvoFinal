
/*
 * Main.java
 */

import javax.swing.*;

/**
 * Main class <br>
 * sets up the application window to start the program
 * - used to run the first method defined by the user
 */
public class Main {
	/**
	 * main(args) <br>
	 * main method
	 * - starts the program
	 * @param args (String[]) command line arguments
	 */
	static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Application application = new Application();
				IDandPasswords idandPasswords = new IDandPasswords();
				LoginPage loginPage = new LoginPage(idandPasswords.getLoginInfo());
			}
		});
	}
	// publicly available game over flag
	public static boolean gameOver = false;
}

