package com.example.library;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;

import com.example.library.backend.Book;
import com.example.library.backend.BookService;
import com.example.library.BookForm;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.client.widget.grid.selection.SelectionEvent;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("library")
public class LibraryUI extends UI {

	// UI Components
	Grid bookList = new Grid();
	TextField filterField = new TextField();
	Button searchButton = new Button("Search");
	Button addBookButton = new Button("Add Book");
	BookService service = BookService.createDemoService();
	BookForm bookForm = new BookForm();
	EntityItem<Book> book;

	/* this is where the program initializes from */
	@Override
	protected void init(VaadinRequest request) {
		configureComponents();// Set all components to their places, and set their functions
		buildLayout();// Show the view
	}

	/**
	 * this function is called once the dash-board UI is created, it will
	 * display the add book function
	 */

	private void configureComponents() {
		addBookButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (!bookForm.isVisible()) {// When the bookForm is visible, disable the function of addButton
					searchButton.removeClickShortcut();
					bookForm.authorField.get(0).setCaption("Author");// By default, only one author, so singular
					bookForm.modification = false;// It's adding a new book, not modify a book
					bookForm.clearFields();// Clear all author fields
					bookForm.edit();// Show the bookForm
				}
			}
		});
		searchButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		searchButton.setClickShortcut(ShortcutAction.KeyCode.SPACEBAR);// Space is the shortcut for search
		searchButton.addClickListener(e -> {
			String info = filterField.getValue();// Get text in the search field
			if (!info.isEmpty()) {// If it's not empty, search related information
				refreshBooks(info);
			} else {// If it's empty
				filterField.focus();// Set the search filed to be the first responder
				BookService.removeAllFilters();// Empty all filters, show all the information in the db
				BookService.shelf.refresh();// Refresh
				bookList.setContainerDataSource(BookService.shelf);
			}
		});

		filterField.setInputPrompt("Search books...");
		bookList.setContainerDataSource(BookService.shelf);
		bookList.setColumnOrder("title", "authors", "year");
		// Those columns are useless here
		bookList.removeColumn("isbn");
		bookList.removeColumn("publisher");
		bookList.removeColumn("edition");
		bookList.removeColumn("checkOut");
		bookList.removeColumn("user");
		bookList.removeColumn("id");
		bookList.removeColumn("authorInformation");
		bookList.setSelectionMode(Grid.SelectionMode.SINGLE);

		/*
		 * this will allow a book to be edited or deleted when a row is clicked
		 * on
		 */
		bookList.addSelectionListener(selectionEvent -> {// When a row is clicked
			searchButton.removeClickShortcut();
			bookForm.clearFields();// Clear all author fields to prevent adding junk information
			bookForm.modification = true;// It is a modification for a book
			bookForm.edit(BookService.shelf.getItem(bookList.getSelectedRow()));// Pass the book to edit
		});
		refreshBooks();
	}

	private void buildLayout() {
		// Set places for components
		HorizontalLayout buttons = new HorizontalLayout(searchButton, addBookButton);
		buttons.setSpacing(true);
		HorizontalLayout actions = new HorizontalLayout(filterField, buttons);
		actions.setWidth("100%");
		filterField.setWidth("100%");
		actions.setExpandRatio(filterField, 1);

		VerticalLayout left = new VerticalLayout(actions, bookList);
		left.setSizeFull();
		bookList.setSizeFull();
		left.setExpandRatio(bookList, 1);

		HorizontalLayout mainLayout = new HorizontalLayout(left, bookForm);
		mainLayout.setSizeFull();
		mainLayout.setExpandRatio(left, 1);
		setContent(mainLayout); // Split and allow resizing
	}

	void refreshBooks() {
		refreshBooks(filterField.getValue());
	}

	private void refreshBooks(String stringFilter) {
		BookService.removeAllFilters();// Remove other filters before searching
		try {
			if(stringFilter.isEmpty()) {// If nothing in the search field, just refresh
				BookService.shelf.refresh();
				bookList.setContainerDataSource(BookService.shelf);
				return;
			} else if (stringFilter.toLowerCase().matches("between[0-9]{4}to[0-9]{4}")) {
				// If text in the search bar follows such format, I take it as searching for books in a specific time duration
				String[] components = stringFilter.toLowerCase().split("to");// Split the string to get information I want
				String toDate = components[1];
				String fromDate = components[0].split("between")[1];
				Filter from = new Compare.GreaterOrEqual("year", fromDate);
				Filter to = new Compare.LessOrEqual("year", toDate);
				Filter composite = new And(from, to);// Search for book in this range
				BookService.shelf.addContainerFilter(composite);
				BookService.shelf.refresh();
				bookList.setContainerDataSource(BookService.shelf);
				return;// End here
			}
			Filter title = new Like("title", "%" + stringFilter + "%", false);// Search title
			Filter publisher = new Like("publisher", "%" + stringFilter + "%", false);// Search publisher
			Filter author = new Like("authorInformation", "%" + stringFilter + "%", false);// Search author
			Filter composite = new Or(title, publisher, author);
			BookService.shelf.addContainerFilter(composite);// add filter
			BookService.shelf.refresh();
			bookList.setContainerDataSource(BookService.shelf);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			bookForm.setVisible(false);// Close the bookform:
			searchButton.setClickShortcut(ShortcutAction.KeyCode.SPACEBAR);
		}
	}

	@WebServlet(urlPatterns = "/*")
	@VaadinServletConfiguration(ui = LibraryUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}