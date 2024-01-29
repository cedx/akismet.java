# Comment check
This is the call you will make the most. It takes a number of arguments and characteristics about the submitted content
and then returns a thumbs up or thumbs down. **Performance can drop dramatically if you choose to exclude data points.**
The more data you send Akismet about each comment, the greater the accuracy. We recommend erring on the side of including too much data.

```java
CheckResult Client.checkComment(Comment comment)
```

It is important to [test Akismet](testing.md) with a significant amount of real, live data in order to draw any conclusions on accuracy.
Akismet works by comparing content to genuine spam activity happening **right now** (and this is based on more than just the content itself),
so artificially generating spam comments is not a viable approach.

See the [Akismet API documentation](https://akismet.com/developers/comment-check) for more information.

## Parameters

### Comment **comment**
The `Comment` providing the user's message to be checked.

## Return value
A `CheckResult` value indicating whether the given `Comment` is ham, spam or pervasive spam.

> A comment classified as pervasive spam can be safely discarded.

The method throws a `Client.Exception` when an error occurs.
The exception `getMessage()` usually includes some debug information, provided by the `X-akismet-debug-help` HTTP header, about what exactly was invalid about the call.

It can also throw a custom error code and message (respectively provided by the `X-akismet-alert-code` and `X-akismet-alert-msg` headers).
See [Response Error Codes](https://akismet.com/developers/errors) for more information.

## Example

```java
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

class Program {
  public static void main(String... args) throws UnknownHostException {
    try {
      var author = new Author(InetAddress.getByName("192.168.123.456"));
      author.email = "john.doe@domain.com";
      author.name = "John Doe";
      author.role = "guest";
      author.userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36";

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
```

See the [API reference](api/) for detailed information about the `Author` and `Comment` classes, and their properties.
