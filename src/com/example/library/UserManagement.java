package com.example.library;

import com.example.library.backend.User;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * <h1>UserManagement</h1> This is the main UI class that the program initializes from.
 *
 * @author Team-Elm
 * @version 4.92.2
 * @since 2016-02-01
 */
/** the userManagement class... */
@SuppressWarnings("serial")
public class UserManagement extends UserPanel {
	Label accountLabel = new Label("Account");
	Label nameLabel = new Label("Name");
	Label emailLabel = new Label("Email");
	Label phoneLabel = new Label("Phone");
	PopupView checkBooks;
	Button checkInfo = new Button("Check Books", this::Popupviews);
	User user;
	Button cancelButton = new Button("Cancel", this::Cancel);
	Button logOut = new Button("Log Out", this::LogOut);

	public UserManagement(User user) {
		this.user = user;
		configureComponents();
		buildLayout();
	}

	/* this is where the program will be structured from */
	public void configureComponents() {
		checkInfo.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		accountLabel.setCaption("Account");
		accountLabel.setValue(user.getAccount());
		accountLabel.setStyleName(ValoTheme.LABEL_COLORED);
		nameLabel.setCaption("Name");
		nameLabel.setValue(user.getName());
		nameLabel.setStyleName(ValoTheme.LABEL_COLORED);
		emailLabel.setCaption("Email");
		emailLabel.setValue(user.getEmail());
		emailLabel.setStyleName(ValoTheme.LABEL_COLORED);
		phoneLabel.setCaption("Phone");
		phoneLabel.setValue(user.getPhone());
		phoneLabel.setStyleName(ValoTheme.LABEL_COLORED);
		logOut.setStyleName(ValoTheme.BUTTON_DANGER);
		this.setVisible(false);
	}

	public void buildLayout() {
		setSizeUndefined();
		setMargin(true);
		VerticalLayout information = new VerticalLayout(accountLabel, nameLabel, emailLabel, phoneLabel);
		information.setSpacing(true);
		VerticalLayout functions = new VerticalLayout(checkInfo, cancelButton, logOut);
		functions.setSpacing(true);
		VerticalLayout alig = new VerticalLayout(information, functions);
		alig.setComponentAlignment(information, Alignment.MIDDLE_CENTER);
		alig.setComponentAlignment(functions, Alignment.BOTTOM_CENTER);
		addComponents(alig);
		// setVisible(true);
	}

	/* generating the button to check books */
	// View Borrows
	public void check(Button.ClickEvent event) {
		Button button = event.getButton();
		if (button.getCaption().equals("View Borrowed Books")) {
			//
		}
	}

	public void Popupviews(Button.ClickEvent e) {
		if (checkBooks != null) {
			this.removeComponent(checkBooks);
		}
		checkBooks = new PopupView(null, new UserDetails(user));
		checkBooks.setPopupVisible(true);
		this.addComponent(checkBooks);
	}

	public void Cancel(Button.ClickEvent event) {
		this.setVisible(false);
	}

	public void LogOut(Button.ClickEvent event) {
		this.user = null;
		getUI().user = null;
		Cancel(event);
		getUI().logInSwitch(true);
	}

	public void settingPanel(User user) {
		this.user = user;
	}

	public LibraryUI getUI() {
		return (LibraryUI) super.getUI();
	}
}
