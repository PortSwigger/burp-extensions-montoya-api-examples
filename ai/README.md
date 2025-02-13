AI Example Extension
============================

###### Demonstrates using AI functionality.

---
This extension demonstrates using Burp's built-in functionality to issue requests to an LLM and process the responses.

This extension uses an LLM to analyze in-scope requests and determine whether they are related to authentication. 

The sample extension demonstrates the following techniques:
- Marking an extension as AI-compatible.
  - The implementation of `BurpExtension` overrides `BurpExtension.enhancedCapabilities()`
- `MyPromptMessage` provides an easy way to provide the `Message.systemMessage()` with the `Message.userMessage()`. This allows you to provide context at the same time as the user message when sending prompts to the LLM.

  > When sending prompts to the LLM, `Message.systemMessage()` needs to be sent each time to provide the context.
  > 
  > Conversations between the LLM and the user can be continued by chaining `Message.userMessage()` and `Message.assistantMessage()` within `Prompt.execute()`.

- By registering a `ProxyRequestHandler`, the extension can analyze all outgoing Proxy requests.
- For each request, the extension first checks if AI functionality is enabled using `Ai.isEnabled()`.
- To minimize unnecessary data sent to the LLM, the extension checks if the traffic is in scope.
- Provided the previous conditions are met, the extension will submit a task to the configured thread pool to execute the prompt.

  > LLM queries are slow and should be performed on their own thread to reduce impact on browsing speed.

- The task will issue a request to the LLM using `Prompt.execute()`.
- The `PromptResponse` is analyzed. Due to our `Message.systemMessage()`, the output should contain "yes" or "no".
  - If the outcome of the analysis is "yes", the extension highlights the request in the Proxy History table.
  - If the outcome of the analysis is "no", no action is taken.
- If a `PromptException` is thrown, additional handling has been put in place.
  - If the user does not have sufficient credits, a message is logged to the output stream of the extension.
  - If the prompt execution fails for a different reason, a message and the exception is logged to the error stream of the extension.

Further references:
- [Creating AI extensions](https://portswigger.net/burp/documentation/desktop/extensions/creating/creating-ai-extensions)
- [Best practices for writing AI extensions](https://portswigger.net/burp/documentation/desktop/extensions/creating/creating-ai-extensions/best-practices)
- [Developing AI features in extensions](https://portswigger.net/burp/documentation/desktop/extensions/creating/creating-ai-extensions/developing-ai-features)
- [Using AI extensions](https://portswigger.net/burp/documentation/desktop/extensions/using-ai-extensions)
- [AI security, privacy and data handling](https://portswigger.net/burp/documentation/desktop/extensions/ai-security-privacy-data-handling)