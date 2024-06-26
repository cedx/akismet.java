package io.belin.akismet;

import java.net.InetAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the author of a comment.
 */
public class Author {

	/**
	 * The author's mail address.
	 */
	public String email;

	/**
	 * The author's IP address.
	 */
	public InetAddress ipAddress;

	/**
	 * The author's name. If you set it to `"viagra-test-123"`, Akismet will always return `true`.
	 */
	public String name;

	/**
	 * The author's role. If you set it to `"administrator"`, Akismet will always return `false`.
	 */
	public String role;

	/**
	 * The URL of the author's website.
	 */
	public URI url;

	/**
	 * The author's user agent, that is the string identifying the Web browser used to submit comments.
	 */
	public String userAgent;

	/**
	 * Creates a new author.
	 * @param ipAddress The author's IP address.
	 */
	public Author(InetAddress ipAddress) {
		this(ipAddress, null, null, null, null, null);
	}

	/**
	 * Creates a new author.
	 * @param ipAddress The author's IP address.
	 * @param userAgent The author's user agent, that is the string identifying the Web browser used to submit comments.
	 */
	public Author(InetAddress ipAddress, String userAgent) {
		this(ipAddress, null, null, null, null, userAgent);
	}

	/**
	 * Creates a new author.
	 * @param ipAddress The author's IP address.
	 * @param role The author's role. If you set it to `"administrator"`, Akismet will always return `false`.
	 * @param userAgent The author's user agent, that is the string identifying the Web browser used to submit comments.
	 */
	public Author(InetAddress ipAddress, String role, String userAgent) {
		this(ipAddress, null, null, null, role, userAgent);
	}

	/**
	 * Creates a new author.
	 * @param ipAddress The author's IP address.
	 * @param name The author's name. If you set it to `"viagra-test-123"`, Akismet will always return `true`.
	 * @param email The author's mail address.
	 * @param url The URL of the author's website.
	 */
	public Author(InetAddress ipAddress, String name, String email, URI url) {
		this(ipAddress, name, email, url, null, null);
	}

	/**
	 * Creates a new author.
	 * @param ipAddress The author's IP address.
	 * @param name The author's name. If you set it to `"viagra-test-123"`, Akismet will always return `true`.
	 * @param email The author's mail address.
	 * @param url The URL of the author's website.
	 * @param role The author's role. If you set it to `"administrator"`, Akismet will always return `false`.
	 * @param userAgent The author's user agent, that is the string identifying the Web browser used to submit comments.
	 */
	public Author(InetAddress ipAddress, String name, String email, URI url, String role, String userAgent) {
		this.email = Objects.requireNonNullElse(email, "");
		this.ipAddress = Objects.requireNonNull(ipAddress);
		this.name = Objects.requireNonNullElse(name, "");
		this.role = Objects.requireNonNullElse(role, "");
		this.url = url;
		this.userAgent = Objects.requireNonNullElse(userAgent, "");
	}

	/**
	 * Returns a JSON representation of this object.
	 * @return The JSON representation of this object.
	 */
	public Map<String, String> toJson() {
		var map = new HashMap<String, String>();
		map.put("user_ip", ipAddress.getHostAddress());
		if (!email.isEmpty()) map.put("comment_author_email", email);
		if (!name.isEmpty()) map.put("comment_author", name);
		if (!role.isEmpty()) map.put("user_role", role);
		if (url != null) map.put("comment_author_url", url.toString());
		if (!userAgent.isEmpty()) map.put("user_agent", userAgent);
		return map;
	}
}
