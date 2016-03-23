package com.example.library;

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
		account.setMaxLength(180);
		password.setInputPrompt("Password");
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
		UserService instance = UserService.createDemoService();
		String accountValue = account.getValue().toLowerCase();
		String passwordValue = password.getValue();
		boolean result = instance.checklogIn(accountValue, passwordValue);
		Type type = result ? Type.TRAY_NOTIFICATION : Type.ERROR_MESSAGE;
		String msg = result ? "Welcome." : "Username and account do not match.";
		Notification.show(msg, type);
		user = instance.getUser(accountValue, passwordValue);
		getUI().user = user;
		getUI().logInSwitch(!result);
	}

	public void Register(Button.ClickEvent event) {
		getUI().showRegister();
	}

	public LibraryUI getUI() {
		return (LibraryUI) super.getUI();
	}
}
