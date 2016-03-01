package com.example.library;

import com.example.library.backend.User;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class UserPanel extends FormLayout {
	Label nameLabel = new Label();
	TextField account = new TextField("Account");
	TextField password = new TextField("Password");
	Button LogIn = new Button("Log in");
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
		
	}
}
