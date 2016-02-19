package com.example.library;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.example.library.backend.Book;
import com.example.library.backend.BookService;

/**
 * <h1>BookForm</h1> 
 * This class represents the form that will allow a user to create, edit, update, and delete a given book.
 *
 * @author Team-Elm
 * @version 1.0
 * @since 2016-02-01
 */
/**
 * The BookForm class This class is the UI of the adding book part There are
 * several buttons and TextField in this form
 */
@SuppressWarnings("serial")
public class BookForm extends FormLayout {

	/* adding all of the UI buttons */
	Button removeButton = new Button("Remove", this::remove);
	Button saveButton = new Button("Save", this::save);
	Button cancelButton = new Button("Cancel", this::cancel);
	Button addAuthors = new Button("+", this::moreAuthor);
	Button checkIO = new Button("Check Out", this::checkIO);
	TextField isbnField = new TextField("ISBN");
	TextField titleField = new TextField("Title");
	final List<TextField> authorField = new ArrayList<TextField>(Arrays.asList(new TextField("Author"),
			new TextField(""), new TextField(""), new TextField(""), new TextField("")));
	TextField publisherField = new TextField("Publisher");
	TextField yearField = new TextField("Year");
	TextField editionField = new TextField("Edition");
	private static int authorNumber = 1;
	boolean modification = false;
	/*
	 * creating an entity named book, this will be managed by the JPAContainer
	 */
	EntityItem<Book> book;

	/* this creates container to hold all of the books */
	BeanFieldGroup<EntityItem<Book>> formFieldBindings;

	/**
	 * when the book is clicked, to view, the according method to build all of
	 * the components will be called, they will then be stored in a layout
	 */
	public BookForm() {
		configureComponents();
		buildLayout();
	}

	private void configureComponents() {
		saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
		removeButton.setStyleName(ValoTheme.BUTTON_DANGER);
		checkIO.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		addAuthors.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		setVisible(false);
	}

	private void buildLayout() {
		setSizeUndefined();
		setMargin(true);

		HorizontalLayout actions = new HorizontalLayout(saveButton, removeButton, checkIO, cancelButton);
		actions.setSpacing(true);

		addComponents(actions, isbnField, titleField, publisherField, yearField, editionField);
		this.addComponent(new HorizontalLayout(authorField.get(0), addAuthors));
	}

	/**
	 * this function will allow the book to have more than one author the
	 * authors max out at 5
	 */
	public void moreAuthor(Button.ClickEvent event) {
		if (authorNumber == 5) {
			Notification.show("Can't add more", Type.ERROR_MESSAGE);
			return;
		}
		TextField authorOne = authorField.get(0);
		authorOne.setCaption("Authors");
		authorNumber++;
		this.addComponent(authorField.get(authorNumber - 1));
	}

	/**
	 * this function will be called when the 'remove' button is pressed when
	 * viewing a book, the book will be removed from the 'shelf' and then the
	 * books will be refreshed
	 */
	public void remove(Button.ClickEvent event) {
		try {
			formFieldBindings.commit();
			getUI().service.delete(book);
			BookService.shelf.removeItem(book.getItemId());
			String msg = String.format("Removed '%s - %s'.", book.getEntity().getIsbn(), book.getEntity().getTitle());
			Notification.show(msg, Type.TRAY_NOTIFICATION);
			getUI().refreshBooks();
		} catch (FieldGroup.CommitException e) {
			// Validation exceptions could be shown here
		} finally {
			this.setVisible(false);
			getUI().bookList.select(null);
			authorNumber = 1;
		}
	}

	/* this function will allow a user to check out a book or return a book */
	public void checkIO(Button.ClickEvent event) {
		if (book != null) {
			book.getEntity().setCheckOut(!book.getEntity().isCheckOut());
			String buttonTitle = book.getEntity().isCheckOut() ? "Return" : "Check Out";
			checkIO.setCaption(buttonTitle);
		}
	}

	public void cancel(Button.ClickEvent event) {
		// Place to call business logic.
		this.setVisible(false);
		Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
		getUI().bookList.select(null);
		authorNumber = 1;
	}

	/**
	 * this function will first check if an existing book has been modified, if
	 * it is a new book, it will then proceed to make sure all of the fields
	 * have been filled out, the book will then be added to the JPAContainer
	 * 'shelf'
	 */
	public void save(Button.ClickEvent event) {
		String ISBN = isbnField.getValue();
		String Title = titleField.getValue();
		List<String> author = new ArrayList<String>();
		for (TextField authorText : authorField) {
			String writer = authorText.getValue();
			if (!writer.isEmpty()) {
				author.add(writer);
			}
		}
		List<String> Author = author;
		String Publisher = publisherField.getValue();
		String Year = yearField.getValue();
		String Edition = editionField.getValue();
		if (ISBN.isEmpty() || Title.isEmpty() || author.size() == 0 || Publisher.isEmpty() || Year.isEmpty()
				|| Edition.isEmpty()) {
			Notification.show("Please fill all the information", Type.WARNING_MESSAGE);
			return;
		}
		if (!Year.matches("(2|1)[0-9]{3}")) {
			Notification.show("Please input the correct format of year", Type.ERROR_MESSAGE);
			return;
		}
		if (!modification) {
			if (BookService.checkDuplicate(ISBN)) {
				Object id = BookService.shelf.addEntity(new Book(ISBN, Title, author, Publisher, Year, Edition));
				book = BookService.shelf.getItem(id);
				formFieldBindings = BeanFieldGroup.bindFieldsBuffered(book, this);
				try {
					// Commit the fields from UI to DAO
					formFieldBindings.commit();
				} catch (FieldGroup.CommitException e) {
					// Validation exceptions could be shown here
				}
				String msg = String.format("Saved '%s'.", book.getEntity().getTitle());
				Notification.show(msg, Type.TRAY_NOTIFICATION);
				BookService.shelf.refresh();
				getUI().refreshBooks();
				return;
			} else {
				Notification.show("The book with the same ISBN already exists.", Type.ERROR_MESSAGE);
				//need to clear fields after book is rejected, weird issue where ISBN changes or something
				return;
			}
		}
		boolean result = getUI().service.save(book, modification);
		if (result) {
			BookService.shelf.addEntity(new Book(ISBN, Title, Author, Publisher, Year, Edition));
			String msg = String.format("Saved '%s'.", book.getEntity().getTitle());
			Notification.show(msg, Type.TRAY_NOTIFICATION);
			BookService.shelf.refresh();
			getUI().refreshBooks();
		} else {
			Notification.show("The book with the same ISBN has already existed", Type.ERROR_MESSAGE);
		}
		authorNumber = 1;
	}

	/**
	 * this function will be called when a book is clicked from the table, it
	 * will then display that book's attributes
	 */
	void edit(EntityItem<Book> book2) {
		if (book2 == null) {
			return;
		}
		this.book = book2;
		this.removeAllComponents();

		removeButton.setVisible(true);
		checkIO.setVisible(true);
		int authorsCount = book2.getEntity().getAuthors().size();
		buildLayout();
		for (int i = 1; i < authorsCount; i++) {
			this.addComponent(authorField.get(i));
		}
		setFields(book2);
		// Bind the properties of the Book POJO to fields in this form
		formFieldBindings = BeanFieldGroup.bindFieldsBuffered(book2, this);
		setVisible(book2 != null);
	}

	void edit() {
		this.removeAllComponents();
		buildLayout();
		setVisible(true);
	}

	private void setFields(EntityItem<Book> book2) {
		titleField.setValue(book2.getEntity().getTitle());
		isbnField.setValue(book2.getEntity().getIsbn());
		publisherField.setValue(book2.getEntity().getPublisher());
		yearField.setValue(book2.getEntity().getYear());
		editionField.setValue(book2.getEntity().getEdition());
		for (int i = 0; i < book2.getEntity().getAuthors().size(); i++) {
			authorField.get(i).setValue(book2.getEntity().getAuthors().get(i));
		}
	}

	public void clearFields() {
		isbnField.setValue("");
		titleField.setValue("");
		publisherField.setValue("");
		yearField.setValue("");
		editionField.setValue("");
		removeButton.setVisible(false);
		checkIO.setVisible(false);
		for (TextField field : authorField) {
			field.setValue("");
		}
	}

	@Override
	public LibraryUI getUI() {
		return (LibraryUI) super.getUI();
	}
}