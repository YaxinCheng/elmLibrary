package com.example.library;

import com.example.library.backend.FormatCheckFailedException;
import com.example.library.backend.User;
import com.example.library.backend.UserService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * <h1>User Login</h1> 
 * This is the main UI class that the user will login or register from.
 *
 * @author Team-Elm
 * @version 4.92.2
 * @since 2016-02-01
 */
/** the user login class... */
@SuppressWarnings("serial")
public class UserLogin extends UserPanel {
	TextField account = new TextField();
	PasswordField password = new PasswordField();
	TextField nameField = new TextField();
	TextField emailField = new TextField();
	TextField phoneField = new TextField();
	Button Save = new Button("Save", this::Save);
	Button cancelButton = new Button("Cancel", this::Cancel);
	User user;

	public UserLogin() {
		configureComponent();
		buildLayout();
	}

	private void configureComponent() {
		account.setInputPrompt("Username");
		password.setInputPrompt("Password");
		nameField.setInputPrompt("Name");
		emailField.setInputPrompt("email@elm.ca");
		phoneField.setInputPrompt("Phone Number");
		cancelButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
		Save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		setVisible(false);
	}

	private void buildLayout() {
		setSizeUndefined();
		setMargin(true);
		VerticalLayout information = new VerticalLayout(cancelButton, account, password, nameField, emailField,
				phoneField);
		information.setSpacing(true);
		HorizontalLayout buttons = new HorizontalLayout(Save);
		buttons.setSpacing(true);
		addComponent(information);
		addComponent(buttons);
	}

	public void settingPanel(User user) {
		if (user == null) {

		}
	}

	public void Cancel(Button.ClickEvent event) {
		this.setVisible(false);
	}

	public void Save(Button.ClickEvent event) {
		UserService instance = UserService.createDemoService();
		String accountValue = account.getValue().toLowerCase();
		String passwordValue = password.getValue();
		String name = nameField.getValue();
		String email = emailField.getValue();
		String phone = phoneField.getValue();
		try {
			if (informationCheck(name, email, phone)) {
				String result = instance.register(accountValue, passwordValue);
				Type notificationType = result.equals("Register Success") ? Type.TRAY_NOTIFICATION : Type.ERROR_MESSAGE;
				Notification.show(result, notificationType);
				if (result.equals("Register Success")) {
					user = instance.getUser(accountValue, passwordValue);
					user.setName(name);
					user.setEmail(email);
					user.setPhone(phone);
					instance.replace(user);
					getUI().user = user;
					Cancel(null);
				}
			}
		} catch (FormatCheckFailedException e) {
			Notification.show(e.getLocalizedMessage(), Type.ERROR_MESSAGE);
		}

	}

	public boolean informationCheck(String name, String email, String phone) throws FormatCheckFailedException {
		if (name.matches(".*[0-9]+.*")) {
			throw new FormatCheckFailedException("Name can't contains numbers");
		}
		if (!email.matches(".+\\.?.*@.+\\..+")) {
			throw new FormatCheckFailedException("Please enter the correct email format");
		}
		if (!phone.matches("[0-9]{10}")) {
			throw new FormatCheckFailedException("Please enter the correct phone number");
		}
		return true;
	}

	public void clearFields() {
		account.setValue("");
		password.setValue("");
		nameField.setValue("");
		emailField.setValue("");
		phoneField.setValue("");
	}

	public LibraryUI getUI() {
		return (LibraryUI) super.getUI();
	}
}
