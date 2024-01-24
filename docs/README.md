# Akismet for Java
Used by millions of websites, [Akismet](https://akismet.com) filters out hundreds of millions of spam comments from the Web every day.
Add Akismet to your [Java](https://www.oracle.com/java) applications so you don't have to worry about spam again.

## Quick start

### Get an API key
The Akismet service requires an API key. If you are not already registered, [sign up for an Akismet account](https://akismet.com/developers).

### Get the library
Download the latest JAR file of **Akismet for Java** from the GitHub releases:  
https://github.com/cedx/akismet.java/releases/latest

Add it to your class path. Now in your [Java](https://www.oracle.com/java) code, you can use:

```java
import io.belin.akismet.*;
```

## Usage
There are three different types of calls to [Akismet](https://akismet.com):

1. [Key verification](usage/verify_key.md) will verify whether or not a valid API key is being used. This is especially useful if you will have multiple users with their own Akismet subscriptions using your application.
2. [Comment check](usage/check_comment.md) is used to ask Akismet whether or not a given post, comment, profile, etc. is spam.
3. [Submit spam](usage/submit_spam.md) and [submit ham](usage/submit_ham.md) are follow-ups to let Akismet know when it got something wrong (missed spam and false positives). These are very important, and you shouldn't develop using the Akismet API without a facility to include reporting missed spam and false positives.

Before integrating this library into your application, you should [test your API calls](testing.md) to ensure a proper usage.

## See also
- [API reference](api/)
- [GitHub releases](https://github.com/cedx/akismet.java/releases)
