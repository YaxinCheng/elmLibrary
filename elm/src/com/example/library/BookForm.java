package com.example.library;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.example.library.backend.Book;

/* Create custom UI Components.
 *
 * Create your own Vaadin components by inheritance and composition.
 * This is a form component inherited from VerticalLayout. Use
 * Use BeanFieldGroup to bind data fields from DTO to UI fields.
 * Similarly named field by naming convention or customized
 * with @PropertyId annotation.
 */
public class BookForm extends FormLayout {

    Button removeButton = new Button("Remove", this::remove);
    Button saveButton = new Button("Save", this::save);
    Button cancelButton = new Button("Cancel", this::cancel);
    TextField isbnField = new TextField("ISBN");
    TextField titleField = new TextField("Title");
    // FIX ME: should be able to add more authors
    TextField authorField = new TextField("Author");
    TextField publisherField = new TextField("Publisher");
    TextField yearField = new TextField("Year");
    TextField editionField = new TextField("Edition");

    Book book;
    
    // Easily bind forms to beans and manage validation and buffering
    BeanFieldGroup<Book> formFieldBindings;

    public BookForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
    	saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
    	removeButton.setStyleName(ValoTheme.BUTTON_DANGER);
    	cancelButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(removeButton, saveButton, cancelButton);
        actions.setSpacing(true);

		addComponents(actions, isbnField, titleField, authorField, publisherField, 
											yearField, editionField);
    }

    public void remove(Button.ClickEvent event) {
        try {
            formFieldBindings.commit();

            getUI().service.delete(book);

            String msg = String.format("Removed '%s - %s'.",
            		book.getIsbn(),
                    book.getTitle());
            Notification.show(msg,Type.TRAY_NOTIFICATION);
            getUI().refreshBooks();
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        }
    }

    public void cancel(Button.ClickEvent event) {
        // Place to call business logic.
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        getUI().bookList.select(null);
    }
    
    public void save(Button.ClickEvent event) {
        try {
            // Commit the fields from UI to DAO
            formFieldBindings.commit();

            // Save DAO to backend with direct synchronous service API
            getUI().service.save(book);

            String msg = String.format("Saved '%s %s'.",
                    book.getIsbn(),
                    book.getTitle());
            Notification.show(msg,Type.TRAY_NOTIFICATION);
            getUI().refreshBooks();
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        }
    }
    
    void edit(Book book) {
        this.book = book;
        if(book != null) {
            // Bind the properties of the Book POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(book, this);
        }
        setVisible(book != null);
    }

    @Override
    public LibraryUI getUI() {
        return (LibraryUI) super.getUI();
    }

}