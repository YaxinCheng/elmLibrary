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
import com.vaadin.data.util.filter.Compare;
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
		configureComponents();
		buildLayout();
	}

	/**
	 * this function is called once the dash-board UI is created, it will
	 * display the add book function
	 */

	private void configureComponents() {
		addBookButton.addClickListener(new Button.ClickListener() {
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
		searchButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		searchButton.setClickShortcut(ShortcutAction.KeyCode.SPACEBAR);
		searchButton.addClickListener(e -> {
			String info = filterField.getValue();
			if (!info.isEmpty()) {
				refreshBooks(info);
			} else {
				filterField.focus();
				BookService.removeAllFilters();
				BookService.shelf.refresh();
				bookList.setContainerDataSource(BookService.shelf);
			}
		});

		/*
		 * i need to fix the filtering of books when searching and refreshing,
		 * just not quite sure how to do that with the new implementation of the
		 * 'shelf' at the moment
		 */
		filterField.setInputPrompt("Filter books...");
		// filterField.addTextChangeListener(e -> refreshBooks(e.getText()));
		// bookList.setContainerDataSource(new BeanItemContainer<>(Book.class));
		bookList.setContainerDataSource(BookService.shelf);
		bookList.setColumnOrder("title", "authors", "year");
		bookList.removeColumn("isbn");
		bookList.removeColumn("publisher");
		bookList.removeColumn("edition");
		bookList.removeColumn("checkOut");
		bookList.removeColumn("user");
		bookList.removeColumn("id");
		bookList.setSelectionMode(Grid.SelectionMode.SINGLE);

		/*
		 * this will allow a book to be edited or deleted when a row is clicked
		 * on
		 */
		bookList.addSelectionListener(selectionEvent -> {
			bookForm.clearFields();
			bookForm.modification = true;
			bookForm.edit(BookService.shelf.getItem(bookList.getSelectedRow()));
		});
		refreshBooks();
	}

	private void buildLayout() {
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

	/*
	 * here is where i have been having some trouble, i have left some of the
	 * original code as reference, just not quite sure how to use the findAll
	 * method on the 'shelf'
	 */
	void refreshBooks() {
		refreshBooks(filterField.getValue());
	}

	private void refreshBooks(String stringFilter) {
		try {
			// old code
			// bookList.setContainerDataSource(new
			// BeanItemContainer<>(Book.class, service.findAll(stringFilter)));

			// new code
			if(stringFilter.isEmpty()) {
				BookService.shelf.refresh();
				bookList.setContainerDataSource(BookService.shelf);
				return;
			}
			BookService.removeAllFilters();
			Filter filter = new Compare.Equal("title", stringFilter);
			BookService.shelf.addContainerFilter(filter);
			BookService.shelf.refresh();
			bookList.setContainerDataSource(BookService.shelf);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			bookForm.setVisible(false);
		}
	}

	@WebServlet(urlPatterns = "/*")
	@VaadinServletConfiguration(ui = LibraryUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}