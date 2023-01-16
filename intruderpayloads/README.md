Custom Intruder Payloads Example Extension
============================

###### Provides custom Intruder payloads and payload processing.

 ---

This example shows how you can use an extension to:
- Generate custom Intruder payloads
- Apply custom processing to Intruder payloads (including built-in ones)

When an extension registers itself as an Intruder payload provider, this will be available within the Intruder UI for the user to select as the payload source for an attack. When an extension registers itself as a payload processor, the user can create a payload processing rule and select the extension's processor as the rule's action.

The extension uses the following techniques:
- Registers a new `PayloadGeneratorProvider`, which returns a new `PayloadGenerator`
- Registers a new `PayloadProcessor`
- The `PayloadGenerator` does the following:
  - Contains a list of payloads to be used
  - Iterates through the payload list, until there are no longer any payloads available
- The `PayloadProcessor` does the following:
  - Decodes the base value of the payload
  - Parses the location of the `input` string in the decoded data
  - Rebuilds the serialized data with the new payload