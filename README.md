# BurpRequestCleaner
This extension redacts potentially sensitive header and parameter values from requests using Shannon Entropy analysis.

While on Twitter I saw Ben's [tweet](https://twitter.com/pry0cc/status/1343629699487039495) wanting Burp to have a "demo" mode that replaced the domain and potentially secret values so that it could be easily shareable. This is my attempt at solving that. It targets the Host header directly by replacing all but the root FQDN, resulting in transformations like so:
- "foo.bar.baz.root.com" ---> "redacted.root.com"
- "foo.root.com" ---> "redacted.root.com"

For all other headers the extension check them against a list of known sensitive headers and if they match it redact them. Currently it checks for just 2:
- Authorization
- Proxy-Authorization

This is by far the weakest part of the extension and something I'll give more thought to. Shannon Entropy fails us with headers because lots of headers have very high entropy but are obviously safe.

For example, the `User-Agent` header shouldn't really ever be redacted but based on Shannon Entropy its a strong candidate for redaction.

The best I can currently think of is letting the user decide which headers they want to ignore along with some likely safe suggestions.

For parameters we use Shannon Entropy to determine if values in Cookies, JSON/XML fields, URL query string, body, or multi-part fields are likely sensitive content and should be redacted.

Some features to add down the road:
- Cleaning Responses
- User cofiguration for headers to clean
- User configuration for redaction string used to replace values
- Other features I haven't thought of yet
- Community Suggestions
