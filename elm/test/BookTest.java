import com.example.library.backend.*;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class BookTest {

	List<String> grrm;
	List<String> weir;
	Book book1;
	Book book2;
	Book book3;
	Book book4;
	
	@Before
	public void init(){
		
		grrm = new ArrayList<String>();
		grrm.add("George R. R. Martin");
		
		weir = new ArrayList<String>();
		weir.add("Andy Weir");
		
		book1 = new Book();
		book2 = new Book("9780553801477","A Dance with Dragons",grrm,
				"Bantam Spectra","2011","1st");
		book3 = new Book("9780553801477","A Dance with Dragons",grrm,
				"Bantam Spectra","2011");
		book4 = new Book("9780804139021","The Martian",weir,"Crown","2014");
	}
	
	@Test
	public void compareToTest() {
		assertEquals(book2.compareTo(book3),0);
		assertEquals(book2.compareTo(book4),-19);
		
	}
	@Test(expected=NullPointerException.class)
	public void nullCompareToTest(){
		assertEquals(book1.compareTo(book2),0);
	}

	@Test
	public void equalsTest() {
		assertTrue(book2.equals(book3));
		assertFalse(book2.equals(book4));
		assertFalse(book1.equals(book4));
		assertFalse(book1.equals(book3));
	}
}
