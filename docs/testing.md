# Testing
When you will integrate this library with your own application, you will of course need to test it.
Often we see developers get ahead of themselves, making a few trivial API calls with minimal values
and drawing the wrong conclusions about Akismet's accuracy.

## Simulate a positive (spam) result
Make a [comment check](usage/check_comment.md) API call with the `Author.name` set to `"viagra-test-123"`
or `Author.email` set to `"akismet-guaranteed-spam@example.com"`. Populate all other required fields with typical values.

The Akismet API will always return a `CheckResult.Spam` response to a valid request with one of those values.
If you receive anything else, something is wrong in your client, data, or communications.

```java
var author = new Author(InetAddress.getByName("127.0.0.1"));
author.name = "viagra-test-123";
author.userAgent = "Mozilla/5.0";

var blog = new Blog(URI.create("https://www.yourblog.com"));
var comment = new Comment(author, "A user comment.");
var result = new Client("123YourAPIKey", blog).checkComment(comment);

System.out.println("It should be 'CheckResult.Spam': " + result);
```

## Simulate a negative (not spam) result
Make a [comment check](usage/check_comment.md) API call with the `Author.role` set to `"administrator"`
and all other required fields populated with typical values.

The Akismet API will always return a `CheckResult.Ham` response. Any other response indicates a data or communication problem.

```java
var author = new Author(InetAddress.getByName("127.0.0.1"));
author.role = "administrator";
author.userAgent = "Mozilla/5.0";

var blog = new Blog(URI.create("https://www.yourblog.com"));
var comment = new Comment(author, "A user comment.");
var result = new Client("123YourAPIKey", blog).checkComment(comment);

System.out.println("It should be 'CheckResult.Ham': " + result);
```

## Automated testing
Enable the `Client.isTest` option in your tests.

That will tell Akismet not to change its behaviour based on those API calls: they will have no training effect.
That means your tests will be somewhat repeatable, in the sense that one test won't influence subsequent calls.

```java
var blog = new Blog(URI.create("https://www.yourblog.com"));
var client = new Client("123YourAPIKey", blog);
client.isTest = true;

var author = new Author(InetAddress.getByName("127.0.0.1"), "Mozilla/5.0");
var comment = new Comment(author, "A user comment.");

// It should not influence subsequent calls.
client.checkComment(comment);
```
