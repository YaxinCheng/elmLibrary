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

@SuppressWarnings("serial")
public class UserDetails extends UserPanel {
	private User user;
	private BeanItemContainer<Book> borrowedBooks;
	
	public UserDetails(User user) {
		this.user = user;
		configureComponents();
		buildLayout();
	}

	public void configureComponents() {
		if (user == null) {
			return;
		}
		borrowedBooks = new BeanItemContainer<>(Book.class);
		updateSource();
	}

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
	
	private void updateSource() {
		for (Book book : user.getBorrowed()) {
			borrowedBooks.addBean(book);
		}
	}
}
