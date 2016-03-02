import static org.junit.Assert.*;

import org.junit.Test;

import com.example.library.backend.User;
import com.example.library.backend.UserService;

public class UserServiceTest {

	@Test
	public void testCreate() {
		UserService instance = UserService.createDemoService();
		assertNotNull(instance);
	}
	
	@Test
	public void testCount() {
		UserService instance = UserService.createDemoService();
		int count = instance.count();
		assertEquals(count, instance.Users.size());
	}

	@Test
	public void testRegister() {
		UserService instance = UserService.createDemoService();
		String result = instance.register("ycheng", "c123yx987&");
		assertEquals(result, "Register Success");
		result = instance.register("", "");
		assertEquals(result, "Account or Password Cannot Be Empty!");
	}
	
	@Test
	public void testCheckLogIn() {
		UserService instance = UserService.createDemoService();
		String result = instance.register("ycheng123", "c123yx987&");
		assertEquals(result, "Register Success");
		assertTrue(instance.checklogIn("ycheng123", "c123yx987&"));
		assertFalse(instance.checklogIn("", ""));
	}
	
	@Test
	public void testGetUser() {
		UserService instance = UserService.createDemoService();
		String result = instance.register("ychengxxx", "c123yx987&");
		assertEquals(result, "Register Success");
		User user = instance.getUser("ychengxxx", "c123yx987&");
		assertNotNull(user);
	}
}
