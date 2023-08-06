import io.belin.akismet.Author;
import io.belin.akismet.Blog;
import io.belin.akismet.Client;
import io.belin.akismet.Comment;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.URI;

/**
 * Submits spam to the Akismet service.
 */
@SuppressWarnings({"PMD.NoPackage", "PMD.UseUtilityClass"})
class SendMessage {

	/**
	 * Application entry point.
	 * @param args The command line arguments.
	 */
	@SuppressWarnings("PMD.SystemPrintln")
	public static void main(String... args) throws UnknownHostException {
		try {
			var author = new Author(InetAddress.getByName("192.168.123.456"));
			author.userAgent = "Spam Bot/6.6.6";

			var comment = new Comment(author);
			comment.content = "Spam!";

			var client = new Client("123YourAPIKey", new Blog(URI.create("https://www.yourblog.com")));
			client.submitSpam(comment);
			System.out.println("The comment was successfully submitted as spam.");
		}
		catch (Client.Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
		}
	}
}
