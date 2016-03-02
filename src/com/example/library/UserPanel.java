package com.example.library;

import com.example.library.backend.User;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;

public abstract class UserPanel extends FormLayout {
	public void showPanel() {
		this.setVisible(true);
	}
	
	abstract public void settingPanel(User user);
	
	public void cancel(Button.Event e) {
		this.setVisible(false);
	}
}
