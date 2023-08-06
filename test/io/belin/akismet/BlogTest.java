package io.belin.akismet;

import static org.junit.jupiter.api.Assertions.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the features of the {@see Blog} class.
 */
@DisplayName("Blog")
final class BlogTest {

	@Test
	@DisplayName("toMap()")
	@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.LinguisticNaming"})
	void toMap() {
		// It should return only the blog URL with a newly created instance.
		var map = new Blog(URI.create("https://github.com/cedx/akismet.java")).toMap();
		assertEquals(1, map.size());
		assertEquals("https://github.com/cedx/akismet.java", map.get("blog"));

		// It should return a non-empty map with a initialized instance.
		map = new Blog(URI.create("https://github.com/cedx/akismet.java"), StandardCharsets.UTF_8, List.of("en", "fr")).toMap();
		assertEquals(3, map.size());
		assertEquals("https://github.com/cedx/akismet.java", map.get("blog"));
		assertEquals("UTF-8", map.get("blog_charset"));
		assertEquals("en,fr", map.get("blog_lang"));
	}
}
