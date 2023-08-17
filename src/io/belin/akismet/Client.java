package io.belin.akismet;

import java.io.IOException;
import java.io.Serial;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Submits comments to the Akismet service.
 */
public final class Client {

	/**
	 * The response returned by the `submit-ham` and `submit-spam` endpoints when the outcome is a success.
	 */
	private static final String success = "Thanks for making the web a better place.";

	/**
	 * The package version.
	 */
	private static final String version = "1.0.0";

	/**
	 * The Akismet API key.
	 */
	public final String apiKey;

	/**
	 * The base URL of the remote API endpoint.
	 */
	public final URI baseUrl;

	/**
	 * The front page or home URL of the instance making requests.
	 */
	public Blog blog;

	/**
	 * Value indicating whether the client operates in test mode.
	 */
	public boolean isTest;

	/**
	 * The user agent string to use when making requests.
	 */
	public String userAgent;

	/**
	 * Creates a new client.
	 * @param apiKey The Akismet API key.
	 * @param blog The front page or home URL of the instance making requests.
	 */
	public Client(String apiKey, Blog blog) {
		this(apiKey, blog, false, null, null);
	}

	/**
	 * Creates a new client.
	 * @param apiKey The Akismet API key.
	 * @param blog The front page or home URL of the instance making requests.
	 * @param userAgent The user agent string to use when making requests.
	 */
	public Client(String apiKey, Blog blog, String userAgent) {
		this(apiKey, blog, false, userAgent, null);
	}

	/**
	 * Creates a new client.
	 * @param apiKey The Akismet API key.
	 * @param blog The front page or home URL of the instance making requests.
	 * @param isTest Value indicating whether the client operates in test mode.
	 * @param userAgent The user agent string to use when making requests.
	 * @param baseUrl The base URL of the remote API endpoint.
	 */
	public Client(String apiKey, Blog blog, boolean isTest, String userAgent, URI baseUrl) {
		this.apiKey = Objects.requireNonNull(apiKey);
		this.baseUrl = Objects.requireNonNullElse(baseUrl, URI.create("https://rest.akismet.com/"));
		this.blog = Objects.requireNonNull(blog);
		this.isTest = isTest;

		var javaVersion = Runtime.version();
		this.userAgent = Objects.requireNonNullElse(userAgent, "Java/%d.%d.%d | Akismet/%s".formatted(
			javaVersion.feature(),
			javaVersion.interim(),
			javaVersion.update(),
			version
		));
	}

	/**
	 * Checks the specified comment against the service database, and returns a value indicating whether it is spam.
	 * @param comment The comment to be submitted.
	 * @return A value indicating whether the specified comment is spam.
	 * @throws Exception The remote server returned an invalid response.
	 */
	public CheckResult checkComment(Comment comment) throws Exception {
		var response = fetch("1.1/comment-check", comment.toMap());
		if (response.body().equals("false")) return CheckResult.Ham;
		var header = response.headers().firstValue("X-akismet-pro-tip");
		return header.isPresent() && header.get().equals("discard") ? CheckResult.PervasiveSpam : CheckResult.Spam;
	}

	/**
	 * Submits the specified comment that was incorrectly marked as spam but should not have been.
	 * @param comment The comment to be submitted.
	 * @throws Exception The remote server returned an invalid response.
	 */
	public void submitHam(Comment comment) throws Exception {
		var response = fetch("1.1/submit-ham", comment.toMap());
		if (!response.body().equals(success)) throw new Exception("Invalid server response.");
	}

	/**
	 * Submits the specified comment that was not marked as spam but should have been.
	 * @param comment The comment to be submitted.
	 * @throws Exception The remote server returned an invalid response.
	 */
	public void submitSpam(Comment comment) throws Exception {
		var response = fetch("1.1/submit-spam", comment.toMap());
		if (!response.body().equals(success)) throw new Exception("Invalid server response.");
	}

	/**
	 * Checks the API key against the service database, and returns a value indicating whether it is valid.
	 * @return `true` if the specified API key is valid, otherwise `false`.
	 * @throws Exception The remote server returned an invalid response.
	 */
	public boolean verifyKey() throws Exception {
		var map = new HashMap<String, String>();
		map.put("key", apiKey);
		var response = fetch("1.1/verify-key", map);
		return response.body().equals("valid");
	}

	/**
	 * Queries the service by posting the specified fields to a given end point, and returns the response.
	 * @param endpoint The URL of the end point to query.
	 * @param fields The fields describing the query body.
	 * @return The server response.
	 * @throws Exception An error occurred while querying the end point.
	 */
	private HttpResponse<String> fetch(String endpoint, Map<String, String> fields) throws Exception {
		var postFields = this.blog.toMap();
		postFields.putAll(fields);
		postFields.put("api_key", apiKey);
		if (isTest) postFields.put("is_test", "1");

		var charset = Charset.defaultCharset();
		var body = postFields.entrySet()
			.stream()
			.map(entry -> URLEncoder.encode(entry.getKey(), charset) + "=" + URLEncoder.encode(entry.getValue(), charset))
			.collect(Collectors.joining("&"));

		var request = HttpRequest.newBuilder(baseUrl.resolve(endpoint))
			.header("Content-Type", "application/x-www-form-urlencoded")
			.POST(HttpRequest.BodyPublishers.ofString(body))
			.build();

		try {
			var response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
			if ((response.statusCode() / 100) != 2) throw new Exception("An error occurred while sending the request.");

			var headers = response.headers();
			if (headers.firstValue("X-akismet-alert-code").isPresent()) throw new Exception(headers.firstValue("X-akismet-alert-msg").get());

			var header = headers.firstValue("X-akismet-debug-help");
			if (header.isPresent()) throw new Exception(header.get());

			return response;
		}
		catch (InterruptedException|IOException e) {
			throw new Exception(e);
		}
	}

	/**
	 * An exception caused by an error in a {@link Client} request.
	 */
	public static class Exception extends java.lang.Exception {

		/**
		 * The serialization version number.
		 */
		@Serial private static final long serialVersionUID = 1L;

		/**
		 * Creates a new client exception.
		 * @param message The error message.
		 */
		public Exception(String message) {
			super(message);
		}

		/**
		 * Creates a new client exception.
		 * @param cause The original cause of the error.
		 */
		public Exception(Throwable cause) {
			super(cause);
		}
	}
}
