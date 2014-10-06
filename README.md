
TwitterStream
=============
TwitterStream is a small android app to demonstrate integration with the Twitter Streaming API. Functionally, the app features the following:
1.Allow user to login with his Twitter Account using the oAuth web interface. Once logged in, the oAuth session represented by the access token and access secret is persisted in Shared Preferences so that subsequent logins are not required.
2.Allow logged in user to specify a keyword for streaming specific tweets e.g. specifying "banking" on the search screen will open up a new screen with real time tweets containing "banking" as a sub string.

Implementation notes:
-Each piece of UI has been implemented as a fragment which sits in its own activity. This design allows for creating resusable ui components that can then be reused in different situations independently or as distinct part of a larger UI.

-The Twitter API integration has been done using the popular Twitter4J SDK (http://twitter4j.org/en/index.html) which is also optimised enough to be used for Android. This is already a robust and heavily production tested SDK thereby making a compelling case for resuse as opposed to re-implementing the complicated REST interface directly in the app.

-The streaming api has been implemented as an Android service that listens for incoming status messages and broadcasts them to components within the app for consumption. This is to ensure connection establishment and destruction does not block the UI thread as the activity transitions to various states in its lifecycle.


TODOs:
- More robust error handling 
- Add some JUNIT tests around the Service and activity lifecycle
- The app has been tested only on a Nexus 5 running 4.4.More testing is required across the device and api range.

