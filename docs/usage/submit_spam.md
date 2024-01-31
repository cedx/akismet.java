# Submit spam
This call is for submitting comments that weren't marked as spam but should have been.

```java
void Client.submitHam(Comment comment)
```

It is very important that the values you submit with this call match those of your [comment check](usage/check_comment.md) calls as closely as possible.
In order to learn from its mistakes, Akismet needs to match your missed spam and false positive reports
to the original [comment check](usage/check_comment.md) API calls made when the content was first posted. While it is normal for less information
to be available for [submit spam](usage/submit_spam.md) and [submit ham](usage/submit_ham.md) calls (most comment systems and forums will not store all metadata),
you should ensure that the values that you do send match those of the original content.

See the [Akismet API documentation](https://akismet.com/developers/detailed-docs/submit-spam-missed-spam) for more information.

## Parameters

### Comment **comment**
The user's `Comment` to be submitted, incorrectly classified as ham.

> Ideally, it should be the same object as the one passed to the original [comment check](usage/check_comment.md) API call.

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
      var author = new Author(InetAddress.getByName("192.168.123.456"), "Spam Bot/6.6.6");
      var comment = new Comment(author, "Spam!");

      var client = new Client("123YourAPIKey", new Blog(URI.create("https://www.yourblog.com")));
      client.submitSpam(comment);
      
      System.out.println("The comment was successfully submitted as spam.");
    }
    catch (Client.Exception e) {
      System.err.println("An error occurred: " + e.getMessage());
    }
  }
}
```
