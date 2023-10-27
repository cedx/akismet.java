package io.belin.akismet;

import static org.junit.jupiter.api.Assertions.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the features of the {@see Author} class.
 */
@DisplayName("Author")
final class AuthorTest {

	@Test
	@DisplayName("toJson()")
	@SuppressWarnings({"PMD.AvoidUsingHardCodedIP", "PMD.LinguisticNaming"})
	void toJson() throws UnknownHostException {
		// It should return only the IP address with a newly created instance.
		var map = new Author(InetAddress.getByName("127.0.0.1")).toJson();
		assertEquals(1, map.size());
		assertEquals("127.0.0.1", map.get("user_ip"));

		// It should return a non-empty map with a initialized instance.
		var author = new Author(InetAddress.getByName("192.168.0.1"), "Cédric Belin", "cedric@belin.io", URI.create("https://belin.io"));
		author.userAgent = "Mozilla/5.0";

		map = author.toJson();
		assertEquals(5, map.size());
		assertEquals("Cédric Belin", map.get("comment_author"));
		assertEquals("cedric@belin.io", map.get("comment_author_email"));
		assertEquals("https://belin.io", map.get("comment_author_url"));
		assertEquals("Mozilla/5.0", map.get("user_agent"));
		assertEquals("192.168.0.1", map.get("user_ip"));
	}
}
