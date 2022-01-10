package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TenmoService;
import com.techelevator.view.ConsoleService;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private TenmoService tenmoService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.tenmoService = new TenmoService();
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
		BigDecimal balance = tenmoService.retrieveBalance();
		System.out.println("Your current account balance is: $" + balance);
		
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		Transfer[] allTransfers = tenmoService.displayAllTransfers(currentUser.getUser().getId());
		System.out.println("-------------------------------------------");
		System.out.println("Transfers");
		System.out.println("ID       From/To             Amount");
		System.out.println("-------------------------------------------");

		for (Transfer transfer : allTransfers) {

			System.out.print(transfer.getTransferId() + "\t");
			if (currentUser.getUser().getUsername().equals(transfer.getUsernameFrom())) { // if the sender of $$ is the current user
				System.out.printf(" %-15s", "To:   " + transfer.getUsernameTo());
			}
			else {
				System.out.printf(" %-15s", "From: " + transfer.getUsernameFrom());
			}
			System.out.printf("$ %8.2f\n", transfer.getAmount());

		}

		System.out.println("---------");

		int transferId = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");

		if (transferId != 0) {
			getTransferDetails(transferId);
		}
	}

	private void getTransferDetails(int transferId) {
    	Transfer transfer = tenmoService.getTransfer(transferId);
    	if (transfer != null) {
			System.out.println("--------------------------------------------");
			System.out.println("Transfer Details");
			System.out.println("--------------------------------------------");
			System.out.println("Id: " + transfer.getTransferId());
			System.out.println("From: " + transfer.getUsernameFrom());
			System.out.println("To: " + transfer.getUsernameTo());
			System.out.println("Type: " + transfer.getTransferType());
			System.out.println("Status: " + transfer.getTransferStatus());
			System.out.println("Amount: \\$" + transfer.getAmount());
		}
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO: display all users
		User[] users = tenmoService.retrieveUsers();
		System.out.println("-------------------------------------------");
		System.out.println("Users");
		System.out.println("ID\tName");
		System.out.println("-------------------------------------------");


		for (User user : users) {
			System.out.println(user.getId() + "\t" + user.getUsername());
		}
		System.out.println("---------\n");
		Integer accountToId = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel) ");

		if (accountToId != 0) {
			//create user objects for transfer
			User userFrom = tenmoService.retrieveUser(currentUser.getUser().getId());
			User userTo = tenmoService.retrieveUser(accountToId);

			if (userFrom != null && userTo != null) {
				//get transfer amount
				BigDecimal transferAmount = console.getUserInputBigDecimal("Enter amount");

				Transfer transfer = new Transfer();

				transfer.setTransferType("Send");
				transfer.setAmount(transferAmount);
				transfer.setAccountFromId(userFrom.getAccountId());
				transfer.setAccountToId(userTo.getAccountId());

				Transfer completedTransfer = tenmoService.sendMoney(transfer);
				// TODO: print transfer result
				if (completedTransfer.getTransferStatus().equals("Approved")) {
					System.out.println("Transfer approved!");
				} else {
					System.out.println("Transfer declined.");
				}
			}
		}
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
				tenmoService.setAuthToken(currentUser.getToken());
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
