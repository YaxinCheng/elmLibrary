import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.example.library.backend.Book;
import com.example.library.backend.BookService;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;

public class BookServiceTest {

	static BookService instance;
	static List<String> authors = Arrays.asList("Roc");
	static Filter filter = new Compare.Equal("isbn", "");

	@Before
	public void init() {
		instance = new BookService();
	}

	@Test
	public void testCount() {
		assertEquals(0, instance.count());
	}

	@Test
	public void testDelete() {
		Object id = instance.shelf.addEntity(new Book("","",authors,"","",""));
		instance.delete(instance.shelf.getItem(id));
		assertFalse(instance.delete(instance.shelf.getItem(id)));
	}

	@Test
	public void testRemoveFilters() {
		Object id = instance.shelf.addEntity(new Book("123", "1", authors, "person", "1", "2016"));
		assertTrue(instance.count() > 0);
		instance.shelf.addContainerFilter(filter);
		assertTrue(instance.count() == 0);
		instance.removeAllFilters();
		assertTrue(instance.count() > 0);
		instance.delete(instance.shelf.getItem(id));
		assertEquals(0,instance.count());
	}

	@Test
	public void testSaveAndDuplicate() {
		assertEquals(0,instance.count());
		assertFalse(instance.checkDuplicate("qwer"));
		instance.save("qwer", "1", "person", "1", "2016", authors);
		assertEquals(1,instance.count());
		assertTrue(instance.save("qwer", "1", "person", "1", "2016", authors));
	}
}
