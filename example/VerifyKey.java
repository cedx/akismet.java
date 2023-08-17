import io.belin.akismet.Blog;
import io.belin.akismet.Client;
import java.net.URI;

/**
 * Verifies an Akismet API key.
 */
@SuppressWarnings({"PMD.NoPackage", "PMD.UseUtilityClass"})
class VerifyKey {

	/**
	 * Application entry point.
	 * @param args The command line arguments.
	 */
	@SuppressWarnings("PMD.SystemPrintln")
	public static void main(String... args) {
		try {
			var client = new Client("123YourAPIKey", new Blog(URI.create("https://www.yourblog.com")));
			var isValid = client.verifyKey();
			System.out.println(isValid ? "The API key is valid." : "The API key is invalid.");
		}
		catch (Client.Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
		}
	}
}
