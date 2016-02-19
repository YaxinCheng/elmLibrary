package com.example.library.backend;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;

public class BookServiceTest {
	
	static BookService instance;
	
	@Before
	public void init(){
//		BookService.populateBookService(instance,"C:/Users/nickm/workspace/library/src/com/example/library/book-service-config.txt");
		instance = BookService.createDemoService();
	}
	
	@Test
	public void testCount() {
		assertTrue(instance.count() >= 0);
	}
	
	@Test
	public void testDelete() {
		Object id = BookService.shelf.addEntity(new Book());
		assertTrue(instance.delete(BookService.shelf.getItem(id)));
	}
	
	@Test
	public void testDuplicate() {
		List<String> authors = new ArrayList(Arrays.asList("T"));
		Object id = BookService.shelf.addEntity(new Book("123", "1",authors, "1","2016"));
		assertTrue(BookService.checkDuplicate("123"));
		instance.delete(BookService.shelf.getItem(id));
	}
	
	@Test
	public void testRemoveFilters() {
		List<String> authors = new ArrayList(Arrays.asList("T"));
		Object id = BookService.shelf.addEntity(new Book("123", "1",authors, "1","2016"));
		assertTrue(instance.count() > 0);
		Filter filter = new Compare.Equal("isbn", "");
		instance.shelf.addContainerFilter(filter);
		assertTrue(instance.count() == 0);
		instance.removeAllFilters();
		assertTrue(instance.count() > 0);
		instance.delete(BookService.shelf.getItem(id));
	}
}
