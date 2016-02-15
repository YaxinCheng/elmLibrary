package com.example.library;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;

import com.example.library.backend.Book;
import com.example.library.backend.BookService;
import com.example.library.BookForm;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.client.widget.grid.selection.SelectionEvent;
import com.vaadin.data.util.BeanItemContainer;
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
    
 // BookForm is an example of a custom component class
    BookForm bookForm = new BookForm();

    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
    }
    
	private void configureComponents() {

		addBookButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (!bookForm.isVisible()) {
					bookForm.authorField.get(0).setCaption("Author");
					bookForm.clearFields();
					bookForm.edit(new Book("", "", new ArrayList<String>(), "", "", ""));
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
			}
		});

        filterField.setInputPrompt("Filter books...");
        filterField.addTextChangeListener(e -> refreshBooks(e.getText()));

		//bookList.setContainerDataSource(new BeanItemContainer<>(Book.class));
        bookList.setContainerDataSource(BookService.shelf);
		bookList.setColumnOrder("title", "authors", "year");		
		bookList.removeColumn("isbn");
		bookList.removeColumn("publisher");
		bookList.removeColumn("edition");
		bookList.removeColumn("checkOut");
		
		bookList.setSelectionMode(Grid.SelectionMode.SINGLE);
		bookList.addSelectionListener(selectionEvent -> {
			bookForm.clearFields();
			
			//problem with editing the book, just need to connect it to the shelf
			//bookForm.edit((Book) bookList.getSelectedRow());
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

       // Split and allow resizing
       setContent(mainLayout);
   }
 
   void refreshBooks() {
	   refreshBooks(filterField.getValue());
   }

   private void refreshBooks(String stringFilter) {
	   try {
		//bookList.setContainerDataSource(new BeanItemContainer<>(Book.class, service.findAll(stringFilter)));
		   bookList.setContainerDataSource(BookService.shelf);
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       bookForm.setVisible(false);
   }
   
   @WebServlet(urlPatterns = "/*")
   @VaadinServletConfiguration(ui = LibraryUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}