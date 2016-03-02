package com.example.library;

import com.example.library.backend.User;
import com.example.library.backend.UserService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class UserLogin extends UserPanel {
	Label nameLabel = new Label();
	TextField account = new TextField("Account");
	PasswordField password = new PasswordField("Password");
	TextField nameField = new TextField("Name");
	TextField emailField = new TextField("Email");
	TextField phoneField = new TextField("Phone");
	Button LogIn = new Button("Log in", this::LogIn);
	Button Register = new Button("Register", this::Register);
	Button Save = new Button("Save", this::Save);
	Button cancelButton = new Button("Cancel", this::Cancel);
	User user;
	
	public UserLogin() {
		configureComponent();
		buildLayout();
	}
	
	private void configureComponent() {
		cancelButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
		Register.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		Save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		setVisible(false);
	}
	
	private void buildLayout() {
		setSizeUndefined();
		setMargin(true);
		removeAllComponents();
		if (user == null) {
			addLogInView();
		} else if (!user.isInformationFilled()) {
			addUserView();
		} else {
			getUI().refresh();
			Cancel(null);
		}
	}

	public void settingPanel(User user) {
		if (user == null) {
			
		}
	}
	
	public void Cancel(Button.ClickEvent event) {
		this.setVisible(false);
	}
	
	public void Register(Button.ClickEvent event) {
		UserService instance = UserService.createDemoService();
		String accountValue = account.getValue();
		String passwordValue = password.getValue();
		String result = instance.register(accountValue, passwordValue);
		Type notificationType = result.equals("Register Success") ? Type.TRAY_NOTIFICATION : Type.ERROR_MESSAGE;
		Notification.show(result, notificationType);
	}
	
	public void LogIn(Button.ClickEvent event) {
		UserService instance = UserService.createDemoService();
		String accountValue = account.getValue();
		String passwordValue = password.getValue();
		boolean result = instance.checklogIn(accountValue, passwordValue);
		Type type = result ? Type.TRAY_NOTIFICATION : Type.ERROR_MESSAGE;
		String msg = result? "Welcome!" : "Password and account does not match!";
		Notification.show(msg, type);
		user = instance.getUser(accountValue, passwordValue);
		getUI().user = user;
		buildLayout();
	}
	
	public void Save(Button.ClickEvent event) {
		String name = nameField.getValue();
		String email = emailField.getValue();
		String phone = phoneField.getValue();
		user.setName(name);
		user.setEmail(email);
		user.setPhone(phone);
		getUI().user = user;
		getUI().refresh();
		Cancel(null);
	}
	
	private void addUserView() {
		VerticalLayout information = new VerticalLayout(cancelButton, nameField, emailField, phoneField);
		HorizontalLayout buttons = new HorizontalLayout(Save);
		buttons.setSpacing(true);
		addComponent(information);
		addComponent(buttons);
	}
	
	private void addLogInView() {
		VerticalLayout main = new VerticalLayout(cancelButton);
		main.addComponents(account, password);
		main.setSpacing(true);
		HorizontalLayout functions = new HorizontalLayout(LogIn, Register);
		functions.setSpacing(true);
		addComponent(main);
		addComponent(functions);
	}
	
	public LibraryUI getUI() {
		return (LibraryUI)super.getUI();
	}
}

