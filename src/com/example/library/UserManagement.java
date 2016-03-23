package com.example.library;

import com.example.library.backend.FormatCheckFailedException;
import com.example.library.backend.User;
import com.example.library.backend.UserService;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
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
	TextField nameField = new TextField();
	TextField emailField = new TextField();
	TextField phoneField = new TextField();
	VerticalLayout infoLayout;
	VerticalLayout editingLayout;
	PopupView checkBooks;
	Button checkInfo = new Button("View checked-out books", this::Popupviews);
	User user;
	boolean editing = false;
	Button editButton = new Button("Edit", this::Edit);
	Button cancelButton = new Button("Cancel", this::Cancel);
	Button logOutButton = new Button("Log Out", this::LogOut);

	public UserManagement(User user) {
		this.user = user;
		configureComponents();
		buildLayout();
	}

	/* this is where the program will be structured from */
	public void configureComponents() {
		checkInfo.setStyleName(ValoTheme.BUTTON_QUIET);
		accountLabel.setCaption("Username");
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
		nameField.setInputPrompt("Name");
		emailField.setInputPrompt("email@elm.ca");
		phoneField.setInputPrompt("Phone Number");
		logOutButton.setStyleName(ValoTheme.BUTTON_DANGER);
		this.setVisible(false);
	}

	public void buildLayout() {
		setSizeUndefined();
		setMargin(true);
		editButton.setCaption("Edit");
		VerticalLayout information = new VerticalLayout(accountLabel);
		buildLayoutForInfoLayout();
		buildLayoutForEditingLayout();
		VerticalLayout functions = new VerticalLayout(checkInfo, editButton, cancelButton, logOutButton);
		functions.setSpacing(true);
		VerticalLayout alig;
		if (!editing) {
			alig = new VerticalLayout(information, infoLayout, functions);
		} else {
			alig = new VerticalLayout(information, editingLayout, functions);
			alig.setComponentAlignment(editingLayout, Alignment.MIDDLE_CENTER);
		}
		addComponents(alig);
	}

	private void buildLayoutForInfoLayout() {
		infoLayout = new VerticalLayout(nameLabel, emailLabel, phoneLabel);
		infoLayout.setSpacing(true);
	}

	private void buildLayoutForEditingLayout() {
		editingLayout = new VerticalLayout(nameField, emailField, phoneField);
		editingLayout.setSpacing(true);
	}

	/* generating the button to check books */
	public void Popupviews(Button.ClickEvent e) {
		if (checkBooks != null) {
			this.removeComponent(checkBooks);
		}
		checkBooks = new PopupView(null, new UserDetails(user));
		checkBooks.setPopupVisible(true);
		this.addComponent(checkBooks);
	}

	public void Edit(Button.ClickEvent event) {
		editing = !editing;
		this.removeAllComponents();
		if (editing == true) {
			setFields();
			buildLayout();
			event.getButton().setCaption("Done");
		} else {
			UserService instance = UserService.createDemoService();
			event.getButton().setCaption("Edit");
			String name = nameField.getValue();
			String email = emailField.getValue();
			String phone = phoneField.getValue();
			try {
				if (instance.informationCheck(name, email, phone)) {
					user.setName(name);
					user.setEmail(email);
					user.setPhone(phone);
					instance.replace(user);
					nameLabel.setValue(name);
					emailLabel.setValue(email);
					phoneLabel.setValue(phone);
				} 
			} catch (FormatCheckFailedException e) {
				Notification.show(e.getLocalizedMessage(), Type.ERROR_MESSAGE);
			}
			buildLayout();
		}
	}

	public void Cancel(Button.ClickEvent event) {
		this.removeAllComponents();
		editing = false;
		buildLayout();
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

	private void setFields() {
		nameField.setValue(nameLabel.getValue());
		emailField.setValue(emailLabel.getValue());
		phoneField.setValue(phoneLabel.getValue());
	}

	public LibraryUI getUI() {
		return (LibraryUI) super.getUI();
	}
}
