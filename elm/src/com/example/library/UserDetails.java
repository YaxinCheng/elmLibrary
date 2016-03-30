package com.example.library;

import com.example.library.backend.Book;
import com.example.library.backend.BookService;
import com.example.library.backend.User;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * UI shows books the user borrowed and corresponding late fees
 * @author Team-Elm
 * @since 2016-03-01
 */
@SuppressWarnings("serial")
public class UserDetails extends UserPanel {
	private User user;
	private BeanItemContainer<Book> borrowedBooks;
	
	/**
	 * Specify the user before building the view
	 * @param user the specific user needs to check
	 */
	public UserDetails(User user) {
		this.user = user;
		configureComponents();
		buildLayout();
	}
	
	/**
	 * Set properties for components
	 */
	public void configureComponents() {
		if (user == null) {
			return;
		}
		borrowedBooks = new BeanItemContainer<>(Book.class);
		updateSource();
	}

	/**
	 * Set components layout
	 */
	public void buildLayout() {
		if (user == null) {
			return;
		}
		Table borrowList = new Table();
		Label label = new Label("Click your book to return");
		borrowList.setContainerDataSource(borrowedBooks);
		borrowList.setVisibleColumns("title", "checkOutDate", "returnDate", "fees");
		borrowList.setSizeFull();
		borrowList.setSelectable(true);
		borrowList.setImmediate(true);
		borrowList.addValueChangeListener(e -> {
			BookService service = BookService.initialize();
			Book selectedBook = (Book)borrowList.getValue();
			try {
				service.bookReturn(selectedBook, user);
			} catch(NullPointerException ex) {
				
			}
			borrowedBooks.removeItem(selectedBook);
		});
		VerticalLayout main = new VerticalLayout(label, borrowList);
		this.addComponent(main);
	}

	/**
	 * Update the view when user is updated
	 */
	@Override
	public void settingPanel(User user) {
		this.user = user;
		configureComponents();
		buildLayout();
	}

	/**
	 * Hide the view
	 * @param e
	 */
	public void Cancel(Button.ClickEvent e) {
		this.removeAllComponents();
		this.setVisible(false);
	}
	
	/**
	 * Get all borrowed books and put in the same container as a data source for the table
	 */
	private void updateSource() {
		for (Book book : user.getBorrowed()) {
			borrowedBooks.addBean(book);
		}
	}
}
