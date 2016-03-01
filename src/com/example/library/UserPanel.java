package com.example.library;

import com.example.library.backend.User;
import com.example.library.backend.UserService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class UserPanel extends FormLayout {
	Label nameLabel = new Label();
	TextField account = new TextField("Account");
	PasswordField password = new PasswordField("Password");
	Button LogIn = new Button("Log in", this::LogIn);
	Button Register = new Button("Register", this::Register);
	Button cancelButton = new Button("Cancel", this::Cancel);
	
	public UserPanel() {
		configureComponent();
		buildLayout();
	}
	
	private void configureComponent() {
		cancelButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
		Register.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		setVisible(false);
	}
	
	private void buildLayout() {
		setSizeUndefined();
		setMargin(true);
		VerticalLayout main = new VerticalLayout(cancelButton);
		main.addComponents(account, password);
		main.setSpacing(true);
		HorizontalLayout functions = new HorizontalLayout(LogIn, Register);
		functions.setSpacing(true);
		addComponent(main);
		addComponent(functions);
	}
	
	public void showPanel() {
		this.setVisible(true);
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
		boolean result = instance.logIn(accountValue, passwordValue);
		Type type = result ? Type.TRAY_NOTIFICATION : Type.ERROR_MESSAGE;
		String msg = result? "Welcome!" : "Password and account does not match!";
		Notification.show(msg, type);
	}
}

