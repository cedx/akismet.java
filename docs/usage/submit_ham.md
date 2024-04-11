# Submit ham
This call is intended for the submission of false positives - items that were incorrectly classified as spam by Akismet.
It takes identical arguments as [comment check](check_comment.md) and [submit spam](submit_spam.md).

```java
void Client.submitHam(Comment comment)
```

Remember that, as explained in the [submit spam](submit_spam.md) documentation, you should ensure
that any values you're passing here match up with the original and corresponding [comment check](check_comment.md) call.

See the [Akismet API documentation](https://akismet.com/developers/detailed-docs/submit-ham-false-positives) for more information.

## Parameters

### Comment **comment**
The user's `Comment` to be submitted, incorrectly classified as spam.

!!! note
    Ideally, it should be the same object as the one passed to the original [comment check](check_comment.md) API call.

## Return value
None.

The method throws a `Client.Exception` when an error occurs.
The exception `getMessage()` usually includes some debug information, provided by the `X-akismet-debug-help` HTTP header, about what exactly was invalid about the call.

It can also throw a custom error code and message (respectively provided by the `X-akismet-alert-code` and `X-akismet-alert-msg` headers).
See [Response Error Codes](https://akismet.com/developers/detailed-docs/errors) for more information.

## Example

```java
import io.belin.akismet.Author;
import io.belin.akismet.Blog;
import io.belin.akismet.Client;
import io.belin.akismet.Comment;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.URI;

class Program {
  public static void main(String... args) throws UnknownHostException {
    try {
      var author = new Author(
        InetAddress.getByName("192.168.123.456"),
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36"
      );

      var comment = new Comment(author, "I'm testing out the Service API.");
      var client = new Client("123YourAPIKey", new Blog(URI.create("https://www.yourblog.com")));
      client.submitHam(comment);
      
      System.out.println("The comment was successfully submitted as ham.");
    }
    catch (Client.Exception e) {
      System.err.println("An error occurred: " + e.getMessage());
    }
  }
}
```
