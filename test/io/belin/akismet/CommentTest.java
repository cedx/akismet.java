package io.belin.akismet;

import static org.junit.jupiter.api.Assertions.*;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the features of the {@see Comment} class.
 */
@DisplayName("Comment")
final class CommentTest {

	@Test
	@DisplayName("toMap()")
	void toMap() throws UnknownHostException {
		// It should return only the author info with a newly created instance.
		var map = new Comment(new Author(InetAddress.getByName("127.0.0.1"))).toMap();
		assertEquals(1, map.size());
		assertEquals("127.0.0.1", map.get("user_ip"));

		// It should return a non-empty map with a initialized instance.
		var author = new Author(InetAddress.getByName("127.0.0.1"));
		author.name = "Cédric Belin";
		author.userAgent = "Doom/6.6.6";

		var comment = new Comment(author, "A user comment.", "blog-post");
		comment.date = Instant.parse("2000-01-01T00:00:00.000Z");
		comment.referrer = URI.create("https://belin.io");

		map = comment.toMap();
		assertEquals(7, map.size());
		assertEquals("Cédric Belin", map.get("comment_author"));
		assertEquals("A user comment.", map.get("comment_content"));
		assertEquals("2000-01-01T00:00:00Z", map.get("comment_date_gmt"));
		assertEquals("blog-post", map.get("comment_type"));
		assertEquals("https://belin.io", map.get("referrer"));
		assertEquals("Doom/6.6.6", map.get("user_agent"));
		assertEquals("127.0.0.1", map.get("user_ip"));
	}
}
