import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.example.library.backend.User;
import com.example.library.backend.UserService;

public class UserServiceTest {

	@Test
	public void testCreate() {
		UserService instance = UserService.initialize();
		assertNotNull(instance);
	}
	
	@Test
	public void testCount() {
		UserService instance = UserService.initialize();
		int count = instance.count();
		assertEquals(count, instance.Users.size());
	}

	@Test
	public void testRegister() {
		UserService instance = UserService.initialize();
		String result = instance.register("ycheng", "c123yx987&");
		assertEquals(result, "Register Success");
		result = instance.register("", "");
		assertEquals(result, "Account or Password Cannot Be Empty!");
	}
	
	@Test
	public void testCheckLogIn() {
		UserService instance = UserService.initialize();
		String result = instance.register("ycheng123", "c123yx987&");
		assertEquals(result, "Register Success");
		assertTrue(instance.checklogIn("ycheng123", "c123yx987&"));
		assertFalse(instance.checklogIn("", ""));
	}
	
	@Test
	public void testGetUser() {
		UserService instance = UserService.initialize();
		String result = instance.register("ychengxxx", "c123yx987&");
		assertEquals(result, "Register Success");
		User user = instance.getUser("ychengxxx", "c123yx987&");
		assertNotNull(user);
	}
}
