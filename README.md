# spring-security
spring-security

### 1. Form and Session Based Authentication: 
#### A. Default Spring Security configuration:
   - Spring Security provides built-in support for session-based authentication. It uses HTTP sessions to store user authentication information, allowing users to remain logged in across multiple requests.
   - Getting Auto-generated password in spring boot run console. and username is ```user```. 
   - Default security login page gives cookie ```JSESSIONID=171FD1D1236AFC8A5A48AD9D3D58E38E``` in response header. This cookie is used to identify the user's session on the server.
   - Next, Greeting API is called with the cookie in the request header ```cookie: JSESSIONID=D78EF1ACDE05EA0D44A8739DB80B440B```. The server retrieves the session information associated with the provided cookie and identifies the user as authenticated. The server then responds with a personalized greeting message, such as "Hello World".

#### B. Spring Security with properties file configuration:
   - Add the following properties in the application.properties file to configure session management:
     ``` application.properties
     server.servlet.session.timeout=10m
     spring.security.user.name=admin
     spring.security.user.password=admin123
     ```
#### C. Spring Security LOGOUT:
     - http://localhost:8080/logout

### 2. Basic Authentication: 
    - Check all steps in SecurityConfig.class.
        Step-1: Create a SecurityConfig class.
        Step-2: Override configure(HttpSecurity http) method.
        Step-3: Giving permission /hello can be used by without authentication.
    - POSTMAN testing:
        - URL: http://localhost:8080/v1/api/hi
        - Method: GET
        - Authorization: Basic Auth
            - Username: admin
            - Password: admin123
    - Basic64 decode command:
        ```bash
        echo -n 'admin:admin123' | base64
        ```
    - Basic64 encode command:
        ```bash
         echo "YWRtaW46YWRtaW4xMjM=" | base64 --decode
        ```
       