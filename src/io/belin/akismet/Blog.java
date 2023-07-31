package io.belin.akismet;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents the front page or home URL transmitted when making requests.
 */
public class Blog {

	/**
	 * The character encoding for the values included in comments.
	 */
	public Charset charset;

	/**
	 * The languages in use on the blog or site, in ISO 639-1 format.
	 */
	public List<String> languages;

	/**
	 * The blog or site URL.
	 */
	public URI url;

	/**
	 * Creates a new blog.
	 * @param url The blog or site URL.
	 */
	public Blog(URI url) {
		this(url, null, null);
	}

	/**
	 * Creates a new blog.
	 * @param url The blog or site URL.
	 * @param charset The character encoding for the values included in comments.
	 */
	public Blog(URI url, Charset charset) {
		this(url, charset, null);
	}

	/**
	 * Creates a new blog.
	 * @param url The blog or site URL.
	 * @param languages The languages in use on the blog or site, in ISO 639-1 format.
	 */
	public Blog(URI url, List<String> languages) {
		this(url, null, languages);
	}

	/**
	 * Creates a new blog.
	 * @param url The blog or site URL.
	 * @param charset The character encoding for the values included in comments.
	 * @param languages The languages in use on the blog or site, in ISO 639-1 format.
	 */
	public Blog(URI url, Charset charset, List<String> languages) {
		this.charset = charset;
		this.languages = new ArrayList<>(Objects.requireNonNullElse(languages, Collections.emptyList()));
		this.url = Objects.requireNonNull(url);
	}

	/**
	 * Converts this object to a map.
	 * @return The map corresponding to this object.
	 */
	public Map<String, String> toMap() {
		var map = new HashMap<String, String>();
		map.put("blog", url.toString());
		if (charset != null) map.put("blog_charset", charset.toString());
		if (!languages.isEmpty()) map.put("blog_lang", languages.stream().collect(Collectors.joining(",")));
		return map;
	}
}
