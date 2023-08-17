import io.belin.akismet.Author;
import io.belin.akismet.Blog;
import io.belin.akismet.Client;
import io.belin.akismet.Comment;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.URI;

/**
 * Submits ham to the Akismet service.
 */
@SuppressWarnings({"PMD.NoPackage", "PMD.UseUtilityClass"})
class SubmitHam {

	/**
	 * Application entry point.
	 * @param args The command line arguments.
	 */
	@SuppressWarnings("PMD.SystemPrintln")
	public static void main(String... args) throws UnknownHostException {
		try {
			var author = new Author(InetAddress.getByName("192.168.123.456"));
			author.userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/115.0";

			var comment = new Comment(author);
			comment.content = "I'm testing out the Service API.";

			var client = new Client("123YourAPIKey", new Blog(URI.create("https://www.yourblog.com")));
			client.submitHam(comment);
			System.out.println("The comment was successfully submitted as ham.");
		}
		catch (Client.Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
		}
	}
}
