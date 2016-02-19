package com.example.library;

import javax.servlet.annotation.WebServlet;

import com.example.library.backend.Book;
import com.example.library.backend.BookService;
import com.example.library.BookForm;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
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

	/*
	 * this is where the program initializes, all of the components are set as
	 * well as their functions, also the view is built
	 */
	@Override
	protected void init(VaadinRequest request) {
		configureComponents();
		buildLayout();
	}

	/**
	 * this function is called once the dash-board UI is created, it will
	 * display the add book function
	 */

	private void configureComponents() {
		addBookButton.addClickListener(new Button.ClickListener() {
			/*
			 * When the bookForm is visible, disable the function of addButton
			 * By default an author is singular, the form is cleared, and the
			 * edit method is called with a false parameter, meaning that it is
			 * not a modification
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (!bookForm.isVisible()) {
					bookForm.authorField.get(0).setCaption("Author");
					bookForm.modification = false;
					bookForm.clearFields();
					bookForm.edit();
				}
			}
		});
		/* this area will set up the search function and apply the function */
		searchButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		searchButton.setClickShortcut(ShortcutAction.KeyCode.SPACEBAR);
		searchButton.addClickListener(e -> {
			String info = filterField.getValue();// Get text in the search field
			if (!info.isEmpty()) {// If it's not empty, search related
									// information
				refreshBooks(info);
			} else {// If it's empty
				filterField.focus();// Set the search filed to be the first
									// responder
				BookService.removeAllFilters();// Empty all filters, show all
												// the information in the db
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
		bookList.addSelectionListener(selectionEvent -> {// When a row is
															// clicked
			bookForm.clearFields();// Clear all author fields to prevent adding
									// junk information
			bookForm.modification = true;// It is a modification for a book
			bookForm.edit(BookService.shelf.getItem(bookList.getSelectedRow()));
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
			if (stringFilter.isEmpty()) {// If nothing in the search field, just
											// refresh
				BookService.shelf.refresh();
				bookList.setContainerDataSource(BookService.shelf);
				return;
			} else if (stringFilter.toLowerCase().matches("between[0-9]{4}to[0-9]{4}")) {
				// If text in the search bar follows such format, I take it as
				// searching for books in a specific time duration
				String[] components = stringFilter.toLowerCase().split("to");
				String toDate = components[1];
				String fromDate = components[0].split("between")[1];
				Filter from = new Compare.GreaterOrEqual("year", fromDate);
				Filter to = new Compare.LessOrEqual("year", toDate);
				Filter composite = new And(from, to);// Search for book in this
														// range
				BookService.shelf.addContainerFilter(composite);
				BookService.shelf.refresh();
				bookList.setContainerDataSource(BookService.shelf);
				return;// End here
			}
			Filter title = new Like("title", "%" + stringFilter + "%", false);
			Filter publisher = new Like("publisher", "%" + stringFilter + "%", false);
			Filter author = new Like("authorInformation", "%" + stringFilter + "%", false);
			Filter composite = new Or(title, publisher, author);
			BookService.shelf.addContainerFilter(composite);// add filter
			BookService.shelf.refresh();
			bookList.setContainerDataSource(BookService.shelf);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			bookForm.setVisible(false);// Close the bookform
		}
	}

	@WebServlet(urlPatterns = "/*")
	@VaadinServletConfiguration(ui = LibraryUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}