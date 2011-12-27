package org.ic.tennistrader.authentication;

import org.ic.tennistrader.exceptions.LoginFailedException;
import org.ic.tennistrader.model.connection.BetfairConnectionHandler;
import org.ic.tennistrader.ui.login.LoginListener;
import org.ic.tennistrader.ui.login.LoginResponse;
import org.ic.tennistrader.ui.login.LoginShell;

public class BetfairAuthenticator {
	
	public static boolean checkLogin(String username, String password) {
		// Perform the login
		try {
			BetfairConnectionHandler.login(username, password);
		} catch (LoginFailedException e) {
			LoginShell.log.info(e.getMessage());
			return false;
		}

		LoginShell.log.info("Login succeeded with token - "
				+ BetfairConnectionHandler.getApiContext().getToken());

		return true;
	}

	public static void checkLogin(final String username,final String password, final LoginListener loginListener) {
		// Perform the login
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					BetfairConnectionHandler.login(username, password);
					loginListener.handleLogin(LoginResponse.SUCCESS);
					LoginShell.log.info("Login succeeded with token - "
							+ BetfairConnectionHandler.getApiContext().getToken());
				} catch (LoginFailedException e) {
					LoginShell.log.info(e.getMessage());
					loginListener.handleLogin(LoginResponse.FAIL);
				}
			}
		});
		t.start();
	}

}
