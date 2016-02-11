package com.example.library;

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

/* Create custom UI Components.
 *
 * Create your own Vaadin components by inheritance and composition.
 * This is a form component inherited from VerticalLayout. Use
 * Use BeanFieldGroup to bind data fields from DTO to UI fields.
 * Similarly named field by naming convention or customized
 * with @PropertyId annotation.
 * ok
 */
@SuppressWarnings("serial")
public class BookForm extends FormLayout {

	Button removeButton = new Button("Remove", this::remove);
	Button saveButton = new Button("Save", this::save);
	Button cancelButton = new Button("Cancel", this::cancel);
	Button addAuthors = new Button("+", this::moreAuthor);
	Button checkIO = new Button("Check Out", this::checkIO);
	TextField isbnField = new TextField("ISBN");
	TextField titleField = new TextField("Title");
	List<TextField> authorField = new ArrayList<TextField>(Arrays.asList(new TextField("Author"),
			new TextField(""), new TextField(""), new TextField(""), new TextField("")));
	TextField publisherField = new TextField("Publisher");
	TextField yearField = new TextField("Year");
	TextField editionField = new TextField("Edition");
	private static int authorNumber = 1;
	Book book;

	// Easily bind forms to beans and manage validation and buffering
	BeanFieldGroup<Book> formFieldBindings;

	public BookForm() {
		configureComponents();
		buildLayout();
	}

	private void configureComponents() {
		saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		removeButton.setStyleName(ValoTheme.BUTTON_DANGER);
		checkIO.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		addAuthors.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		setVisible(false);
	}

	private void buildLayout() {
		setSizeUndefined();
		setMargin(true);

		HorizontalLayout actions = new HorizontalLayout(removeButton, saveButton, checkIO, cancelButton);
		actions.setSpacing(true);

		addComponents(actions, isbnField, titleField, publisherField, yearField, editionField);
		this.addComponent(new HorizontalLayout(authorField.get(0), addAuthors));
	}

	public void moreAuthor(Button.ClickEvent event) {
		if (authorNumber == 6) {
			Notification.show("Can't add more", Type.ERROR_MESSAGE);
			return;
		}
		TextField authorOne = authorField.get(0);
		authorOne.setCaption("Authors");
		authorNumber++;
		this.addComponent(authorField.get(authorNumber - 1));
	}

	public void remove(Button.ClickEvent event) {
		try {
			formFieldBindings.commit();

			getUI().service.delete(book);

			String msg = String.format("Removed '%s - %s'.", book.getIsbn(), book.getTitle());
			Notification.show(msg, Type.TRAY_NOTIFICATION);
			getUI().refreshBooks();
		} catch (FieldGroup.CommitException e) {
			// Validation exceptions could be shown here
		} finally {
			cancel(event);
		}
	}
	
	public void checkIO(Button.ClickEvent event) {
		if (book != null) {
			book.setCheckOut(!book.isCheckOut());
			String buttonTitle = book.isCheckOut()?"Return":"Check Out";
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

	public void save(Button.ClickEvent event) {
		try {
			// Commit the fields from UI to DAO
			formFieldBindings.commit();
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
			book = new Book(ISBN, Title, Author, Publisher, Year, Edition);
			// Save DAO to backend with direct synchronous service API
			getUI().service.save(book);

			String msg = String.format("Saved '%s'.", book.getTitle());
			Notification.show(msg, Type.TRAY_NOTIFICATION);
			getUI().refreshBooks();
			authorNumber = 1;
		} catch (FieldGroup.CommitException e) {
			// Validation exceptions could be shown here
		}
	}

	void edit(Book book) {
		if (book == null) {
			return;
		}
		this.book = book;
		this.removeAllComponents();
		if (book.compareTo(new Book("", "", new ArrayList<String>(), "", "", "")) == 0) {
			isbnField.setValue("");
			titleField.setValue("");
			authorField.get(0).setValue("");
			publisherField.setValue("");
			yearField.setValue("");
			editionField.setValue("");
			removeButton.setVisible(false);
			checkIO.setVisible(false);
			buildLayout();
		} else {
			removeButton.setVisible(true);
			checkIO.setVisible(true);
			int authorsCount = book.getAuthors().size();
			buildLayout();
			for (int i = 1; i < authorsCount; i++) {
				this.addComponent(authorField.get(i));
			}
		}

		setFields(book);
		// Bind the properties of the Book POJO to fields in this form
		formFieldBindings = BeanFieldGroup.bindFieldsBuffered(book, this);

		setVisible(book != null);
	}

	private void setFields(Book book) {
		titleField.setValue(book.getTitle());
		isbnField.setValue(book.getIsbn());
		publisherField.setValue(book.getPublisher());
		yearField.setValue(book.getYear());
		editionField.setValue(book.getEdition());
		for (int i = 0; i < book.getAuthors().size(); i++) {
			authorField.get(i).setValue(book.getAuthors().get(i));
		}
	}

	@Override
	public LibraryUI getUI() {
		return (LibraryUI) super.getUI();
	}

}