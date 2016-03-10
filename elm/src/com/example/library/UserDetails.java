package com.example.library;

import com.example.library.backend.Book;
import com.example.library.backend.User;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class UserDetails extends UserPanel {
	private User user;
	
	public UserDetails(User user) {
		this.user = user;
		configureComponents();
		buildLayout();
	}
	
	public void configureComponents() {
		if (user == null) {
			return;
		}
	}
	
	public void buildLayout() {
		if (user == null) {
			return;
		}
		VerticalLayout bookLayouts = new VerticalLayout();
		for (Book book : user.getBorrowed()) {
			Label label = new Label(book.getTitle() + " by " + book.getAuthors());
			bookLayouts.addComponent(label);
		}
		this.addComponent(bookLayouts);
	}
	@Override
	public void settingPanel(User user) {
		this.user = user;
		configureComponents();
		buildLayout();
	}
	
	public void Cancel(Button.ClickEvent e) {
		this.removeAllComponents();
		this.setVisible(false);
	}
}
