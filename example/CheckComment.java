import io.belin.akismet.Author;
import io.belin.akismet.Blog;
import io.belin.akismet.CheckResult;
import io.belin.akismet.Client;
import io.belin.akismet.Comment;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

/**
 * Checks a comment against the Akismet service.
 */
@SuppressWarnings({"PMD.NoPackage", "PMD.UseUtilityClass"})
class CheckComment {

	/**
	 * Application entry point.
	 * @param args The command line arguments.
	 */
	@SuppressWarnings("PMD.SystemPrintln")
	public static void main(String... args) throws UnknownHostException {
		try {
			var author = new Author(InetAddress.getByName("192.168.123.456"));
			author.email = "john.doe@domain.com";
			author.name = "John Doe";
			author.role = "guest";
			author.userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36";

			var comment = new Comment(author, "A user comment.", "contact-form");
			comment.date = Instant.now();
			comment.referrer = URI.create("https://github.com/cedx/akismet.java");

			var blog = new Blog(URI.create("https://www.yourblog.com"), StandardCharsets.UTF_8, List.of("fr"));
			var result = new Client("123YourAPIKey", blog).checkComment(comment);
			System.out.println(result.equals(CheckResult.Ham) ? "The comment is ham." : "The comment is spam.");
		}
		catch (Client.Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
		}
	}
}
