package io.belin.akismet;

import static org.junit.jupiter.api.Assertions.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.URI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the features of the {@see Client} class.
 */
@DisplayName("Client")
@SuppressWarnings("PMD.AvoidUsingHardCodedIP")
final class ClientTest {

	/**
	 * The client used to query the remote API.
	 */
	private static Client client;

	/**
	 * A comment with content marked as ham.
	 */
	private static Comment ham;

	/**
	 * A comment with content marked as spam.
	 */
	private static Comment spam;

	@BeforeAll
	@SuppressWarnings("PMD.AvoidUsingHardCodedIP")
	static void beforeAll() throws UnknownHostException {
		client = new Client(System.getenv("AKISMET_API_KEY"), new Blog(URI.create("https://github.com/cedx/akismet.java")));
		client.isTest = true;

		var hamAuthor = new Author(InetAddress.getByName("192.168.0.1"));
		hamAuthor.name = "Akismet";
		hamAuthor.role = "administrator";
		hamAuthor.url = URI.create("https://belin.io");
		hamAuthor.userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/115.0";

		ham = new Comment(hamAuthor, "I'm testing out the Service API.", "comment");
		ham.referrer = URI.create("https://mvnrepository.com/artifact/io.belin/akismet");

		var spamAuthor = new Author(InetAddress.getByName("127.0.0.1"));
		spamAuthor.email = "akismet-guaranteed-spam@example.com";
		spamAuthor.name = "viagra-test-123";
		spamAuthor.userAgent = "Spam Bot/6.6.6";

		spam = new Comment(spamAuthor, "Spam!", "blog-post");
	}

	@Test
	@DisplayName("checkComment()")
	void checkComment() throws Client.Exception {
		// It should return `CheckResult.Ham` for valid comment (e.g. ham).
		assertEquals(CheckResult.Ham, client.checkComment(ham));

		// It should return `CheckResult.Spam` for invalid comment (e.g. spam).
		var result = client.checkComment(spam);
		assertTrue(result.equals(CheckResult.Spam) || result.equals(CheckResult.PervasiveSpam));
	}

	@Test
	@DisplayName("submitHam()")
	void submitHam() {
		assertDoesNotThrow(() -> client.submitHam(ham));
	}

	@Test
	@DisplayName("submitSpam()")
	void submitSpam() {
		assertDoesNotThrow(() -> client.submitSpam(spam));
	}

	@Test
	@DisplayName("verifyKey()")
	void verifyKey() throws Client.Exception {
		// It should return `true` for a valid API key.
		assertTrue(client.verifyKey());

		// It should return `false` for an invalid API key.
		var newClient = new Client("0123456789-ABCDEF", client.blog);
		newClient.isTest = true;
		assertFalse(newClient.verifyKey());
	}
}
