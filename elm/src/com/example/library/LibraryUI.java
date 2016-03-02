package com.example.library;

import javax.servlet.annotation.WebServlet;

import com.example.library.backend.Book;
import com.example.library.backend.BookService;
import com.example.library.backend.User;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Theme("library")
public class LibraryUI extends UI {

	// UI Components
	Grid bookList = new Grid();
	TextField filterField = new TextField();
	Button searchButton = new Button("Search", this::searchBook);
	Button addBookButton = new Button("Add Book", this::addBook);
	Button userManagement = new Button("User", this::manageUser);
	BookService service = BookService.createDemoService();
	BookForm bookForm = new BookForm();
	UserPanel userPanel;
	User user;
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
		userPanel = user == null ? new UserLogin() : new UserManagement(user);
		BookService service = BookService.createDemoService();
		/* this area will set up the search function and apply the function */
		searchButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		userManagement.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		filterField.setInputPrompt("Search books...");
		bookList.setContainerDataSource(service.shelf);
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
			if (userPanel.isVisible()) {
				userPanel.setVisible(false);
				;
			}
			bookForm.clearFields();// Clear all author fields to prevent adding
									// junk information
			bookForm.modification = true;// It is a modification for a book
			bookForm.edit(service.shelf.getItem(bookList.getSelectedRow()));
		});
		refreshBooks();
	}

	private void buildLayout() {
		// Set places for components
		HorizontalLayout buttons = new HorizontalLayout(searchButton, userManagement, addBookButton);
		buttons.setSpacing(true);
		HorizontalLayout actions = new HorizontalLayout(filterField, buttons);
		actions.setWidth("100%");
		filterField.setWidth("100%");
		actions.setExpandRatio(filterField, 1);

		VerticalLayout left = new VerticalLayout(actions, bookList);
		left.setSizeFull();
		bookList.setSizeFull();
		left.setExpandRatio(bookList, 1);

		HorizontalLayout mainLayout = new HorizontalLayout(left, bookForm, userPanel);
		mainLayout.setSizeFull();
		mainLayout.setExpandRatio(left, 1);
		setContent(mainLayout); // Split and allow resizing
	}

	void refreshBooks() {
		refreshBooks(filterField.getValue());
	}

	private void refreshBooks(String stringFilter) {
		BookService service = BookService.createDemoService();
		service.removeAllFilters();// Remove other filters before searching
		try {
			if (stringFilter.isEmpty()) {// If nothing in the search field, just
											// refresh
				service.shelf.refresh();
				bookList.setContainerDataSource(service.shelf);
				return;
			} else if (stringFilter.toLowerCase().matches("[0-9]{4}\\-[0-9]{4}")) {
				// If text in the search bar follows such format, I take it as
				// searching for books in a specific time duration
				service.searchYear(stringFilter);
				bookList.setContainerDataSource(service.shelf);
				return;// End here
			}
			service.searchInfo(stringFilter);
			bookList.setContainerDataSource(service.shelf);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			bookForm.setVisible(false);// Close the bookform
		}
	}

	public void refresh() {
		userPanel = user == null ? new UserLogin() : new UserManagement(user);
		this.buildLayout();
	}

	public void addBook(Button.ClickEvent e) {
		if (userPanel.isVisible()) {
			userPanel.setVisible(false);
		}
		if (bookForm.isVisible()) {
			bookForm.setVisible(false);
			bookForm.clearFields();
		}
		if (!bookForm.isVisible()) {
			bookForm.authorField.get(0).setCaption("Author");
			bookForm.modification = false;
			bookForm.clearFields();
			bookForm.edit();
		}
	}

	public void searchBook(Button.ClickEvent e) {
		String info = filterField.getValue();// Get text in the search field
		if (!info.isEmpty()) {// If it's not empty, search related
								// information
			refreshBooks(info);
		} else {// If it's empty
			filterField.focus();// Set the search filed to be the first
								// responder
			service.removeAllFilters();// Empty all filters, show all
										// the information in the db
			service.shelf.refresh();// Refresh
			bookList.setContainerDataSource(service.shelf);
		}
	}

	public void manageUser(Button.ClickEvent e) {
		if (bookForm.isVisible()) {
			bookForm.cancel(e);
		}
		if (userPanel.isVisible()) {
			userPanel.setVisible(false);
		}
		userPanel.showPanel();
		userPanel.settingPanel(user);
	}

	@WebServlet(urlPatterns = "/*")
	@VaadinServletConfiguration(ui = LibraryUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}