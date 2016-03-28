import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;

import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.NotificationElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;

/**
 * This class contains JUnit tests, which are run using Vaadin TestBench 4.
 *
 * To run this, first get an evaluation license from
 * https://vaadin.com/addon/vaadin-testbench and follow the instructions at
 * https://vaadin.com/directory/help/installing-cval-license to install it.
 *
 * Once the license is installed, you can run this class as a JUnit test.
 */
public class LibraryTest extends TestBenchTestCase {
	@Rule
	public ScreenshotOnFailureRule screenshotOnFailureRule = new ScreenshotOnFailureRule(this, true);

	@Before
	public void setUp() throws Exception {
		setDriver(new ChromeDriver()); // Chrome
	}

	/**
	 * Opens the URL where the application is deployed.
	 */
	private void openTestUrl() {
		getDriver().get("http://localhost:8080/library/");
	}

	/**
	 * Test the Log In button
	 * 
	 * @throws Exception
	 */
	@Test
	public void test1() throws Exception {
		openTestUrl();
		assertTrue($(TextFieldElement.class).exists());
		assertTrue($(PasswordFieldElement.class).exists());
		assertTrue($(ButtonElement.class).exists());
		assertEquals(2, $(ButtonElement.class).all().size());
		// Click the Login button
		ButtonElement LoginButton = $(ButtonElement.class).caption("Log In").first();
		LoginButton.click();
		// There should now be one notification with the specified text
		assertEquals("Username and account do not match.", $(NotificationElement.class).first().getText().toString());
	}

	/*
	 * Test the Cancel Button
	 */
	@Test
	public void test2() throws Exception {
		openTestUrl();
		ButtonElement RegisterButton = $(ButtonElement.class).caption("Register").first();
		RegisterButton.click();
		ButtonElement CancelButton = $(ButtonElement.class).caption("Cancel").first();
		CancelButton.click();
		assertEquals(1, $(TextFieldElement.class).all().size());
		assertEquals(1, $(PasswordFieldElement.class).all().size());
		assertEquals(2, $(ButtonElement.class).all().size());
	}

	/*
	 * Test Save Button after click Register button
	 */
	@Test
	public void test3() throws Exception {
		openTestUrl();
		ButtonElement RegisterButton = $(ButtonElement.class).caption("Register").first();
		RegisterButton.click();
		ButtonElement SaveButton = $(ButtonElement.class).caption("Save").first();
		SaveButton.click();
		assertEquals("Please enter the correct name format", $(NotificationElement.class).first().getText().toString());
	}

	/*
	 * Test Register Successfully
	 */
	@Test
	public void test4() throws Exception {
		openTestUrl();
		ButtonElement RegisterButton = $(ButtonElement.class).caption("Register").first();
		RegisterButton.click();
		TextFieldElement account = $(TextFieldElement.class).id("1");
		account.setValue("RocKing");
		PasswordFieldElement password = $(PasswordFieldElement.class).id("2");
		password.setValue("Wz!23456");
		TextFieldElement name = $(TextFieldElement.class).id("3");
		name.setValue("Roc");
		TextFieldElement email = $(TextFieldElement.class).id("4");
		email.setValue("123@234.com");
		TextFieldElement phone = $(TextFieldElement.class).id("5");
		phone.setValue("9021234567");
		ButtonElement SaveButton = $(ButtonElement.class).caption("Save").first();
		SaveButton.click();
		assertEquals("Successfully registered.", $(NotificationElement.class).first().getText());
	}

	/*
	 * Test Register Failed with the same information before
	 */
	@Test
	public void test5() throws Exception {
		openTestUrl();
		ButtonElement RegisterButton = $(ButtonElement.class).caption("Register").first();
		RegisterButton.click();
		TextFieldElement account = $(TextFieldElement.class).id("1");
		account.setValue("RocKing");
		PasswordFieldElement password = $(PasswordFieldElement.class).id("2");
		password.setValue("Wz!23456");
		TextFieldElement name = $(TextFieldElement.class).id("3");
		name.setValue("Roc");
		TextFieldElement email = $(TextFieldElement.class).id("4");
		email.setValue("123@234.com");
		TextFieldElement phone = $(TextFieldElement.class).id("5");
		phone.setValue("9021234567");
		ButtonElement SaveButton = $(ButtonElement.class).caption("Save").first();
		SaveButton.click();
		assertEquals("The account is already in the database.",
				$(NotificationElement.class).first().getText().toString());
	}

	/*
	 * Register failed with wrong phone number
	 */
	@Test
	public void test6() throws Exception {
		openTestUrl();
		ButtonElement RegisterButton = $(ButtonElement.class).caption("Register").first();
		RegisterButton.click();
		TextFieldElement account = $(TextFieldElement.class).id("1");
		account.setValue("Bray");
		PasswordFieldElement password = $(PasswordFieldElement.class).id("2");
		password.setValue("Bray!23456");
		TextFieldElement name = $(TextFieldElement.class).id("3");
		name.setValue("Bray");
		TextFieldElement email = $(TextFieldElement.class).id("4");
		email.setValue("123@234.com");
		TextFieldElement phone = $(TextFieldElement.class).id("5");
		phone.setValue("123456");
		ButtonElement SaveButton = $(ButtonElement.class).caption("Save").first();
		SaveButton.click();
		assertEquals("Phone number needs to be in the format: xxx-xxx-xxxx",
				$(NotificationElement.class).first().getText().toString());
	}

	/*
	 * Register failed with wrong email format
	 */
	@Test
	public void test7() throws Exception {
		openTestUrl();
		ButtonElement RegisterButton = $(ButtonElement.class).caption("Register").first();
		RegisterButton.click();
		TextFieldElement account = $(TextFieldElement.class).id("1");
		account.setValue("Bray");
		PasswordFieldElement password = $(PasswordFieldElement.class).id("2");
		password.setValue("Bray!23456");
		TextFieldElement name = $(TextFieldElement.class).id("3");
		name.setValue("Bray");
		TextFieldElement email = $(TextFieldElement.class).id("4");
		email.setValue("123234.com");
		TextFieldElement phone = $(TextFieldElement.class).id("5");
		phone.setValue("9021234567");
		ButtonElement SaveButton = $(ButtonElement.class).caption("Save").first();
		SaveButton.click();
		assertEquals("Email needs to be in the format: johnny_rotten@elm.ca",
				$(NotificationElement.class).first().getText().toString());
	}

	/*
	 * Test Log In Successfully
	 */
	@Test
	public void test8() throws Exception {
		openTestUrl();
		TextFieldElement user = $(TextFieldElement.class).id("6");
		user.setValue("RocKing");
		PasswordFieldElement Pass = $(PasswordFieldElement.class).id("7");
		Pass.setValue("Wz!23456");
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}