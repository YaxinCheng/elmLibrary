package com.example.library;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.DecimalFormat;
import com.example.library.backend.Book;
import com.example.library.backend.User;
import com.example.library.backend.UserService;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;

@SuppressWarnings("serial")
public class LogInScreen extends UserPanel {

	TextField account = new TextField();
	PasswordField password = new PasswordField();
	Button Confirm = new Button("Log In", this::LogIn);
	Button Register = new Button("Register", this::Register);
	private User user;

	public LogInScreen() {
		configureComponents();
		buildLayout();
	}

	public void configureComponents() {
		setSizeUndefined();
		account.setInputPrompt("Username");
		account.setId("6");
		account.setMaxLength(180);
		password.setInputPrompt("Password");
		password.setId("7");
		password.setMaxLength(180);
		Confirm.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		Confirm.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		Register.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
	}

	public void buildLayout() {
		VerticalLayout textFields = new VerticalLayout(account, password);
		textFields.setSpacing(true);
		HorizontalLayout buttons = new HorizontalLayout(Confirm, Register);
		buttons.setSpacing(true);
		VerticalLayout LogIn = new VerticalLayout(textFields, buttons);
		LogIn.setComponentAlignment(buttons, com.vaadin.ui.Alignment.BOTTOM_CENTER);
		LogIn.setSpacing(true);
		LogIn.setMargin(true);
		LogIn.setSizeFull();
		LogIn.setWidth("100%");
		this.addComponent(LogIn);
	}

	@Override
	public void settingPanel(User user) {
		// TODO Auto-generated method stub
		this.user = user;
	}

	public void LogIn(Button.ClickEvent event) {
		UserService instance = UserService.initialize();
		String accountValue = account.getValue().toLowerCase();
		String passwordValue = password.getValue();
		boolean result = instance.checklogIn(accountValue, passwordValue);
		Type type = result ? Type.TRAY_NOTIFICATION : Type.ERROR_MESSAGE;
		String msg = result ? "Welcome." : "Username and account do not match.";
		Notification.show(msg, type);
		user = instance.getUser(accountValue, passwordValue);
		getUI().user = user;
		showLoginNotification(user);
		getUI().logInSwitch(!result);
	}

	public void Register(Button.ClickEvent event) {
		getUI().showRegister();
	}
	/**
	 * Shows a notification on login containing info pertaining to a specific user,
	 * including late fees, books due, and books on hold/wait.
	 * @param currentUser User that is logging in
	 */
	@SuppressWarnings("deprecation")
	public void showLoginNotification(User currentUser){
		//Notification generation
		Notification output = new Notification("Welcome","",Notification.TYPE_HUMANIZED_MESSAGE);
		output.setDelayMsec(-1);
		//String containing all notification info
		String notify = "";
		Date current = new Date();
		//Lists to display due and held books
		List<Book> due = new ArrayList<Book>();
		List<Book> held = new ArrayList<Book>();
		List<Book> close = new ArrayList<Book>();
		//Checking if the user has any books due
		if(currentUser.getBorrowed().size() > 0){
			//Check to see if any books on user's borrowed list are overdue
			for(int i = 0; i < currentUser.getBorrowed().size(); i++){
				if(currentUser.getBorrowed().get(i).getReturnDate().before(current)){
					due.add(currentUser.getBorrowed().get(i));
				}
			}
			//Will display list of overdue book if anything is overdue
			if(due.size() > 0){
				notify += "Overdue books: ";
				for(int i = 0; i < due.size(); i++){
					notify += "\n -" + due.get(i).getTitle();
				}
			}
			else{
				notify += "\nYou have no overdue books.";
			}
		}
		//Check if any books are close to being due
		if(currentUser.getBorrowed().size() > 0){
			//Check to see if any books on user's borrowed list are close to being due
			for(int i = 0; i < currentUser.getBorrowed().size(); i++){
				if(currentUser.getBorrowed().get(i).daysBefore() < 2){
					close.add(currentUser.getBorrowed().get(i));
				}
			}
			//Will display list of overdue book if anything is overdue
			if(close.size() > 0){
				notify += "\nThese books need to be returned soon: ";
				for(int i = 0; i < close.size(); i++){
					notify += "\n -" + close.get(i).getTitle();
				}
			}
			else{
				//Nothing
			}
		}
		
		//Add up user fees
		currentUser.setFees(currentUser.totalFees(due));
		//Checking the user's fees, adding info to notify
		if(currentUser.getFees() > 0){
			DecimalFormat df = new DecimalFormat(".00");
			notify += "\nYou have late fees that need to be paid: $" + df.format(currentUser.getFees());
		}
		else{
			notify += "\nYou have no outstanding fees. Enjoy your day!";
		}
		//Checking if anything is on hold/wait list, then displaying the book
		if(currentUser.getWaiting().size() > 0){
			//Check to see if any books on user's wait/hold list are available (not checked out)
			for(int i = 0; i < currentUser.getWaiting().size(); i++){
				if(!currentUser.getWaiting().get(i).isCheckOut()){
					held.add(currentUser.getWaiting().get(i));
				}
			}
			//Adds available books to notification
			if(held.size() > 0){
				notify += "\nBooks you have on hold are now available: ";
				for(int i = 0; i < held.size(); i++){
					notify += "\n -" + held.get(i).getTitle();
				}
			}
			else{
				//Nothing to show here, no need to disappoint the user
			}
		}
		//Show final notification
		if(currentUser.getFees() >0 ){
			output.show("Attention", notify, Notification.TYPE_ERROR_MESSAGE);
		}
		else{
			output.show("Attention", notify, Notification.TYPE_WARNING_MESSAGE);
		}
	}
	
	public LibraryUI getUI() {
		return (LibraryUI) super.getUI();
	}
}
