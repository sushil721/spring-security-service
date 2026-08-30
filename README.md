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

### 2. Basic (Session-Based) Authentication: 
#### A. Check all steps in SecurityConfig.class with application.properties credential.
   - Step-1: Create a SecurityConfig class.
   - Step-2: Override configure(HttpSecurity http) method.
   - Step-3: Giving permission /hello can be used by without authentication.
#### B.  POSTMAN testing:
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
#### C. DB (MySQL) based authentication:
   - Step-1: Add JPA dependency and MySQL connector dependency in pom.xml file.
   - Step-2: Add the following properties in the application.properties file to configure database connection:
     ``` application.properties
        spring.datasource.url=jdbc:mysql://localhost:3306/spring_security_db
        spring.datasource.username=root
        spring.datasource.password=root
        spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
        spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
        spring.jpa.hibernate.ddl-auto=update
        spring.jpa.show-sql=true
     ```
   - Step-3: Create a database named `spring_security_db` in MySQL.
   - Step-4: Create a table in the database to store user credentials.
   - Step-5: Configure Spring Security to use JDBC authentication by providing the necessary database connection details in the application.properties file.
   - Step-6: Implement a custom UserDetailsService to retrieve user details from the database.
   - Step-7: Created UserEntity class to map the user table in the database.
   - Step-8: Created UserRepository interface to perform findByUsernameAndIsActive operations.
   - Step-9: Created UserService class to implement the business logic for user authentication.
   - Step-10: Implements UserDetailsService in UserService class to override loadUserDetails from the database.
   - Step-11: Load UserEntity data in UserDetails for Spring Security authentication.
   - Step-12: Create bean of UserDetailsService bean in SecurityConfig class to use the custom UserDetailsService implementation for authentication.
   - Step-13: Create AuthenticationManager bean in SecurityConfig class to use the custom UserDetailsService for authentication.
   - Step-14: Instert user data in the database with username, password, and is_active fields.
     `insert into spring_security_db.users(username, password, is_active) value ('sushil','sushil', 1);`
   - Step-15: Test the API with POSTMAN using Basic Auth with username and password from the database.
   - Step-16: Getting 401 Unauthorized error in POSTMAN testing. This error occurs when the provided credentials are invalid or the user is not active in the database. Ensure that the username and password are correct and that the user is marked as active in the database.
     `Given that there is no default password encoder configured, each password must have a password encoding prefix. Please either prefix this password with '{noop}' or set a default password encoder in DelegatingPasswordEncoder.`
   - Step-17: To resolve the 401 Unautherized error, We need to add a password encoder in the SecurityConfig class. This can be done by creating a bean of PasswordEncoder and using it to encode the passwords before storing them in the database. For example, we can use the BCryptPasswordEncoder as follows:
       ```java
       @Bean
       public PasswordEncoder passwordEncoder() {
           return new BCryptPasswordEncoder();
       }
       ```
   - Step-18: Delete user entry from the table.
   - Step-19: Create UserEntity with encoded password we need to encode the password before saving it to the database. For example, we can use the PasswordEncoder bean to encode the password as follows:
       ```java
       public void createUser() {
        String encodedPassword = passwordEncoder.encode("sushil");
        UserEntity user = new UserEntity();
        user.setUsername("sushil");
        user.setPassword(encodedPassword);
        user.setIsActive(true);
        userRepository.save(user);
       }
       ```
   - Step-20: Create a UserController class and implement a REST API endpoint to create a new user. This endpoint should accept the username and password as input, encode the password using the PasswordEncoder bean, and save the user details in the database.
   - Step-21: Exclude /encoded-user from security `.requestMatchers("/v1/api/user/**").permitAll()` .
   - Step-22: Add user by the API with POSTMAN.
   - Step-23: Test the API with POSTMAN using Basic Auth with username and password from the database.

### 3. JWT (Json Web Token) Authentication: 
- JWT stands for JSON Web Token. It is a compact, URL-safe string used to securely transmit information between two parties, most commonly for authentication and authorization.
#### A JWT has three parts separated by dots (.): xxxxx.yyyyy.zzzzz
   - A. Header: Specifies the token type (JWT) and the signing algorithm (e.g., HS256, RS256).
        ```json
        {
        "alg": "HS256",
        "typ": "JWT"
        }
        ```
   - B. Payload: Contains the data (called claims), such as:
          ```json
          {
          "sub": "1234567890",
          "name": "Alice",
          "role": "admin",
          "exp": 1756358400
          }
       ```
   - C. Signature: Ensures the token hasn't been tampered with. Created by signing the header and payload with a secret key or private key.
   - JWT steps to authentication and authorization.
   - Step-1: User logs in with username and password.
   - Step-2: Server verifies the credentials.
   - Step-3: Server generates a JWT and sends it to the client.
   - Step-4: Client stores the token (often in memory or a secure cookie).
   - Step-5: For future requests, the client sends:
    ```Authorization: Bearer eyJhbGciOiJIUzI1NiIs...```
   - Step-6: The server verifies the token's signature and expiration.
   - Step-7: If valid, the server processes the request.
   - Step-8: Integration of JWT in our project and authenticate user.
        - Step-a: Add all three JWT dependencies in pom.xml
        - Step-b: Add ```.csrf(csrf -> csrf.disable())``` in SecurityConfig class.
        - Step-c: Create UserEntity model class with username and password.
        - Step-d: Create JwtService class and generateToken method with key.
        - Step-e: Create ```/authenticate``` controller in UserController class.
        - Step-f: Add ```.requestMatchers("/v1/api/users/authenticate").permitAll()``` in SecurityConfig clas.
        - Step-g: Run Application and call ```/authenticate``` api with username and password body.
        - Step-h: Call ```/hi``` API, with basic Auth its working because of basic authentication ``` .httpBasic(Customizer.withDefaults());``` in SecurityConfig class, but not with token because we only authenticated the user (generateToken).
        - Step-g: For **Authorization** we need to validateToken. Going to next todo.

   - Step-8: Authorization: Authorize/Validate requests and check whether the token is expired or not.
        - Step-a: Add verifySignatureAndExtractClaims method for extracting values from token in JwtService class.
        - Step-b: Add extractUserName method for getting username from token in JwtService class.
        - Step-c: Add getExpiration method for getting token is expiration time in JwtService class.
        - Step-d: Add isTokenExpired method for check method is expired or live in JwtService class.
        - Step-e: Create JwtFilter class with extends of OncePerRequestFilter.
        - Step-f: Implements method doFilterInternal and set all details.
        - Step-g: Remove ```.httpBasic(Customizer.withDefaults());``` from SecurityConfig.basicAuthentication method.
        - Step-h: Add ```.addFilterAt(jwtFilter, UsernamePasswordAuthenticationFilter.class);``` at same line.
        - Step-i: Run Application and create token by POSTMAN ```/authenticate```. copy token of response body.
        - Step-j: Open ```/hi``` api and change Authorization from```Basic Auth``` to ```Bearer Auth```.
        - Step-k: Paste copied token and hit api. Token Authorization is working... :)
        - Problem/Dis-advantage: If we can delete user from table after creating token. so that token will working till end of expiration time.
        - Solution-1: We can put our token expiration time less.
        - Solution-2: Or we can put our all user details in REDIS cache and check after every authorization, and delete this user details from Redis at time of deleting user from DB.


   - Step-9: Authorization: Roles and permissions.
        - Step-a: Add role properties in UserEntity class.
        - Step-b: Replace ```.authorities(Collections.emptyList())``` to ```.authorities(new SimpleGrantedAuthority(userEntity.getRole()))``` for adding role in spring security UserDetails.
        - Step-c: Add Role in claims of JwtService class at generateToken using HashMap.
        - Step-d: In UserController ```/authenticate``` API, Get role from Spring Security authenticate object and add for use of generateToken method.
        - Step-e: Get role from claim in JwtFilter and add as ```simpleGrantedAuthorities``` in place of ```new ArrayList<>()```.
          ```java
          String role = claims.get("Role", String.class);
          List<SimpleGrantedAuthority> simpleGrantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
          ```
        - Step-f: Add RoomController with 3 apis addRoom, getRoomById, and getRooms.
        - Step-g: Perform role based authorization on it.
   - Step-h: Add requestMatcher with different-different role in SecurityConfig class, like below,
     ```java
        //authorize only admin can add room
        .requestMatchers(HttpMethod.POST, "/v1/api/rooms/addRoom")
              .hasRole("ADMIN")
        //All room access only have Admin and Staff
        .requestMatchers(HttpMethod.GET, "/v1/api/rooms")
              .hasAnyRole("ADMIN", "STAFF")
        //a specific room (booked) can be  accessed by Admin, Staff, and Guest.
        .requestMatchers(HttpMethod.GET, "/v1/api/rooms/**")
               .hasAnyRole("ADMIN", "STAFF", "GUEST")
     ```
     - Step-i: Get role parameter from ```/encoded-user``` and set it in UserEntity object for save in DB.
     - Step-j: Add 3 different users in db with role=ROLE_ADMIN, role=ROLE_STAFF, and role=ROLE_GUEST.
     - Step-k: Hit call of APIs and perform role based authorization.
     - Step-l: User with ADMIN role access all apis.
     - Step-m: User with STAFF role access /rooms and /room/id
     - Step-n: User with GUEST role only can access their own room /room/id api.
     

  