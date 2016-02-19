package com.example.library.backend;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
		
	}
}
