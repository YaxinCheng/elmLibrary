import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;

import com.example.library.UserPanel;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
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
	public void testLogIn() throws Exception {
		openTestUrl();
		assertTrue($(TextFieldElement.class).exists());
		assertTrue($(PasswordFieldElement.class).exists());
		assertTrue($(ButtonElement.class).exists());
		assertEquals(2, $(ButtonElement.class).all().size());
		// Click the Login button
		ButtonElement LoginButton = $(ButtonElement.class).caption("Log In").first();
		LoginButton.click();
		// There should now be one notification with the specified text
		assertEquals("Username and account do not match.", $(NotificationElement.class).first().getText());
	}

	@Test
	public void testRegister() throws Exception {
		openTestUrl();
		ButtonElement RegisterButton = $(ButtonElement.class).caption("Register").first();
		RegisterButton.click();
	}
}