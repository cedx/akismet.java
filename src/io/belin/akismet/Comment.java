package io.belin.akismet;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a comment submitted by an author.
 */
public class Comment {

	/**
	 * The comment's author.
	 */
	public Author author;

	/**
	 * The comment's content.
	 */
	public String content;

	/**
	 * The context in which this comment was posted.
	 */
	public List<String> context;

	/**
	 * The UTC timestamp of the creation of the comment.
	 */
	public Instant date;

	/**
	 * The permanent location of the entry the comment is submitted to.
	 */
	public URI permalink;

	/**
	 * The UTC timestamp of the publication time for the post, page or thread on which the comment was posted.
	 */
	public Instant postModified;

	/**
	 * A string describing why the content is being rechecked.
	 */
	public String recheckReason;

	/**
	 * The URL of the webpage that linked to the entry being requested.
	 */
	public URI referrer;

	/**
	 * The comment's type.
	 */
	public String type;

	/**
	 * Creates a new comment.
	 * @param author The comment's author.
	 */
	public Comment(Author author) {
		this(author, null, null, null, null, null, null, null, null);
	}

	/**
	 * Creates a new comment.
	 * @param author The comment's author.
	 * @param content The comment's content.
	 */
	public Comment(Author author, String content) {
		this(author, content, null, null, null, null, null, null, null);
	}

	/**
	 * Creates a new comment.
	 * @param author The comment's author.
	 * @param content The comment's content.
	 * @param type The comment's type.
	 */
	public Comment(Author author, String content, String type) {
		this(author, content, type, null, null, null, null, null, null);
	}

	/**
	 * Creates a new comment.
	 * @param author The comment's author.
	 * @param date The UTC timestamp of the creation of the comment.
	 * @param postModified The UTC timestamp of the publication time for the post, page or thread on which the comment was posted.
	 */
	public Comment(Author author, Instant date, Instant postModified) {
		this(author, null, null, date, postModified, null, null, null, null);
	}

	/**
	 * Creates a new comment.
	 * @param author The comment's author.
	 * @param permalink The permanent location of the entry the comment is submitted to.
	 * @param referrer The URL of the webpage that linked to the entry being requested.
	 */
	public Comment(Author author, URI permalink, URI referrer) {
		this(author, null, null, null, null, permalink, referrer, null, null);
	}

	/**
	 * Creates a new comment.
	 * @param author The comment's author.
	 * @param content The comment's content.
	 * @param type The comment's type.
	 * @param date The UTC timestamp of the creation of the comment.
	 * @param postModified The UTC timestamp of the publication time for the post, page or thread on which the comment was posted.
	 * @param permalink The permanent location of the entry the comment is submitted to.
	 * @param referrer The URL of the webpage that linked to the entry being requested.
	 * @param recheckReason A string describing why the content is being rechecked.
	 * @param context The context in which this comment was posted.
	 */
	public Comment(
		Author author, String content, String type, Instant date, Instant postModified,
		URI permalink, URI referrer, String recheckReason, List<String> context
	) {
		this.author = Objects.requireNonNull(author);
		this.content = Objects.requireNonNullElse(content, "");
		this.context = new ArrayList<>(Objects.requireNonNullElse(context, Collections.emptyList()));
		this.date = date;
		this.permalink = permalink;
		this.postModified = postModified;
		this.recheckReason = Objects.requireNonNullElse(recheckReason, "");
		this.referrer = referrer;
		this.type = Objects.requireNonNullElse(type, "");
	}

	/**
	 * Returns a JSON representation of this object.
	 * @return The JSON representation of this object.
	 */
	@SuppressWarnings("PMD.NPathComplexity")
	public Map<String, String> toJson() {
		var map = author.toJson();
		if (!content.isEmpty()) map.put("comment_content", content);
		if (!context.isEmpty()) map.put("comment_context", context.toString());
		if (date != null) map.put("comment_date_gmt", date.toString());
		if (permalink != null) map.put("permalink", permalink.toString());
		if (postModified != null) map.put("comment_post_modified_gmt", postModified.toString());
		if (!recheckReason.isEmpty()) map.put("recheck_reason", recheckReason);
		if (referrer != null) map.put("referrer", referrer.toString());
		if (!type.isEmpty()) map.put("comment_type", type);
		return map;
	}
}
