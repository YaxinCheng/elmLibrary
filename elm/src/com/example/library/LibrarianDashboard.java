package com.example.library;

import java.util.Collection;

import com.example.library.backend.Book;
import com.example.library.backend.BookService;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class LibrarianDashboard extends FormLayout {
	
	Grid rentList = new Grid();
	Grid lateList = new Grid();
	Button backButton = new Button("Back to Library", this::Back);
	private BeanItemContainer<Book> rentedBooks;
	private BeanItemContainer<Book> lateBooks;
	
	public LibrarianDashboard() {
		configureComponents();
		buildLayout();
	}
	
	public void configureComponents() {
		dataSourceForRentList();
		dataSourceForLateList();
		this.setVisible(true);
	}
	
	public void buildLayout() {
		this.setSizeFull();
		HorizontalLayout tables = new HorizontalLayout(rentList, lateList);
		HorizontalLayout main = new HorizontalLayout(tables, backButton);
		this.addComponent(main);
	}
	
	private void dataSourceForRentList() {
		BookService service = BookService.initialize();
		service.rentedBooks();
		rentedBooks = new BeanItemContainer(Book.class);
		Collection<Object> ids = service.shelf.getItemIds();
		for (Object id : ids) {
			EntityItem<Book> tmp = service.shelf.getItem(id);
			if (tmp == null) {
				System.out.print("Empty");
			} else {
				rentedBooks.addBean(tmp.getEntity());
			}
		}
		rentList.setContainerDataSource(rentedBooks);
		rentList.removeAllColumns();
		rentList.addColumn("title");
		rentList.addColumn("authors");
		rentList.addColumn("checkOutDate");
		rentList.setSizeFull();
		service.removeAllFilters();
	}
	
	private void dataSourceForLateList() {
		BookService service = BookService.initialize();
		service.lateBooks();
		lateBooks = new BeanItemContainer(Book.class);
		Collection<Object> ids = service.shelf.getItemIds();
		for (Object id : ids) {
			EntityItem<Book> tmp = service.shelf.getItem(id);
			if (tmp == null) {
				System.out.print("Empty");
			} else {
				lateBooks.addBean(tmp.getEntity());
			}
		}
		lateList.setContainerDataSource(lateBooks);
		lateList.removeAllColumns();
		lateList.addColumn("title");
		lateList.addColumn("authors");
		lateList.addColumn("returnDate");
		lateList.setSizeFull();
		service.removeAllFilters();
	}
	
	private void Back(Button.ClickEvent e) {
		getUI().logInSwitch(false);
	}
	
	public LibraryUI getUI() {
		return (LibraryUI) super.getUI();
	}
}
