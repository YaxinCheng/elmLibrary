package com.example.library;

import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;

import com.example.library.backend.Book;
import com.example.library.backend.BookService;
import com.example.library.BookForm;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


@SuppressWarnings("serial")
@Theme("library")
public class LibraryUI extends UI {

	/* Vaadin's user interface components are just Java objects that encapsulate
	 * and handle cross-browser support and client-server communication.
     */
    Grid bookList = new Grid();
    TextField filterField = new TextField();
    Button addBookButton = new Button("Add Book");
    
    BookService service = BookService.createDemoService();
    
 // BookForm is an example of a custom component class
    BookForm bookForm = new BookForm();


    /* The "Main method".
     *
     * This is the entry point method executed to initialize and configure
     * the visible user interface. Executed on every browser reload because
     * a new instance is created for each web page loaded.
     */
    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
    }
    
	private void configureComponents() {
		/*
		 * Synchronous event handling.
		 *
		 * Receive user interaction events on the server-side. This allows you
		 * to synchronously handle those events. Vaadin automatically sends only
		 * the needed changes to the web page without loading a new page.
		 */
		
		addBookButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (!bookForm.isVisible()) {
					bookForm.edit(new Book("", "", new ArrayList<String>(), "", "", ""));
				}
			}
		});

        filterField.setInputPrompt("Filter books...");
        filterField.addTextChangeListener(e -> refreshBooks(e.getText()));

		bookList.setContainerDataSource(new BeanItemContainer<>(Book.class));
		bookList.setColumnOrder("title", "authors", "year");		
		bookList.removeColumn("isbn");
		bookList.removeColumn("publisher");
		bookList.removeColumn("edition");
		
		bookList.setSelectionMode(Grid.SelectionMode.SINGLE);
		bookList.addSelectionListener(e -> bookForm.edit((Book) bookList.getSelectedRow()));
		refreshBooks();
	}

   /* Robust layouts.
    *
    * Layouts are components that contain other components.
    * HorizontalLayout contains TextField and Button. It is wrapped
    * with a Grid into VerticalLayout for the left side of the screen.
    * Allow user to resize the components with a SplitPanel.
    *
    * In addition to programmatically building layout in Java,
    * you may also choose to setup layout declaratively
    * with Vaadin Designer, CSS and HTML.
    */
   private void buildLayout() {
	   HorizontalLayout actions = new HorizontalLayout(filterField, addBookButton);
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

   /* Choose the design patterns you like.
    *
    * It is good practice to have separate data access methods that
    * handle the back-end access and/or the user interface updates.
    * You can further split your code into classes to easier maintenance.
    * With Vaadin you can follow MVC, MVP or any other design pattern
    * you choose.
    */   
   void refreshBooks() {
	   refreshBooks(filterField.getValue());
   }

   private void refreshBooks(String stringFilter) {
	   bookList.setContainerDataSource(new BeanItemContainer<>(
               Book.class, service.findAll(stringFilter)));
       bookForm.setVisible(false);
   }
   

   /*  Deployed as a Servlet or Portlet.
    *
    *  You can specify additional servlet parameters like the URI and UI
    *  class name and turn on production mode when you have finished developing the application.
    */
   @WebServlet(urlPatterns = "/*")
   @VaadinServletConfiguration(ui = LibraryUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}