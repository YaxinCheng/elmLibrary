package com.example.library;

import com.example.library.backend.User;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

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

	public UserManagement(User user) {
		// if (user == null) {
		// throw new NullPointerException("The user cannot be empty.");
		// }
		this.user = user;
		configureComponents();
		buildLayout();
	}

	public void configureComponents() {
		checkInfo.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		if (user != null) {
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
		}
		this.setVisible(false);
	}

	public void buildLayout() {
		setSizeUndefined();
		setMargin(true);
		VerticalLayout information = new VerticalLayout(accountLabel, nameLabel, emailLabel, phoneLabel);
		information.setSpacing(true);
		VerticalLayout functions = new VerticalLayout(checkInfo, cancelButton);
		addComponents(information, functions);
		// setVisible(true);
	}

	public void check(Button.ClickEvent event) {
		Button button = event.getButton();
		if (button.getCaption().equals("Check Borrows")) {
		}
	}
	
	public void Popupviews(Button.ClickEvent e) {
		checkBooks = new PopupView(null, new UserDetails(user));
		checkBooks.setPopupVisible(true);
		this.addComponent(checkBooks);
	}

	public void Cancel(Button.ClickEvent event) {
		this.setVisible(false);
	}
	
	public void settingPanel(User user) {
		this.user = user;
	}

	public LibraryUI getUI() {
		return (LibraryUI) super.getUI();
	}
}
