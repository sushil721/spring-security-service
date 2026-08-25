# spring-security
spring-security

### 1. Session Based Authentication: 
#### A. Default Spring Security configuration:
   - Spring Security provides built-in support for session-based authentication. It uses HTTP sessions to store user authentication information, allowing users to remain logged in across multiple requests.
   - Getting Auto-generated password in spring boot run console. and username is ```user```. 
   - Default security login page gives cookie ```JSESSIONID=171FD1D1236AFC8A5A48AD9D3D58E38E``` in response header. This cookie is used to identify the user's session on the server.
   - Next, Greeting API is called with the cookie in the request header ```cookie: JSESSIONID=D78EF1ACDE05EA0D44A8739DB80B440B```. The server retrieves the session information associated with the provided cookie and identifies the user as authenticated. The server then responds with a personalized greeting message, such as "Hello World".

