
## Part 1 - Setting Project
- Docker Compose file
```yaml
version: '3.8'  
  
services:  
  db:  
    image: postgres:17-alpine  
    ports:  
      - "5432:5432"  
    restart: always  
    environment:  
      POSTGRES_PASSWORD: admin  
      POSTGRES_USER: postgres  
      POSTGRES_DB: starter  
    volumes:  
      - pgdata:/var/lib/postgresql/data  
  
volumes:  
  pgdata:
```
- application.yaml
```yaml
spring:  
  application:  
    name: spring-starter  
  datasource:  
    driver-class-name: org.postgresql.Driver  
    url: jdbc:postgresql://localhost:5432/starter  
    username: postgres  
    password: admin  
    jpa:  
      hibernate:  
        ddl-auto: create-drop
```
## Part 2 - Explaining DB Connection - JPA - Code first approach
#### Lombok dependency
Java library that helps you keep your code clean, concise, and less repetitive by **automatically generating boilerplate code** for you.
- Dependencies
```gradle
implementation 'org.projectlombok:lombok'  
compileOnly("org.projectlombok:lombok")  
annotationProcessor("org.projectlombok:lombok")
```
## Part 3 - Explaining Liquibase - Setting and Migrating.
#### Liquid Base set up
- Dependency: `implementation 'org.liquibase:liquibase-core'`
- application.yaml
```yaml
liquibase:
  change-log: classpath:db/changelog/changelog-master.xml 
  enabled: true
```
- Master changelog file: `src/main/resources/db/changelog/db.changelog-master.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.0.xsd">

    <!-- <include relativeToChangelogFile="true" file=""/> -->

</databaseChangeLog>
```
- Changeset file `src/main/resources/db/changelog/*****.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.0.xsd">

    <changeSet id="#" author="first_name.last_name">
        <!-- Your Liquid Base migration -->
    </changeSet>

</databaseChangeLog>
```
____
- Creating extension
```xml
<sql> CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; </sql>
```
- Creating schema
```xml
<sql>
	CREATE SCHEMA ____;
</sql>
```
- Creating a table
```xm
<createTable tableName="product">
	<column name="id" type="uuid" defaultValueComputed="uuid_generate_v4()">
		<constraints primaryKey="true" nullable="false"/>
	</column>
	
	<column name="name" type="VARCHAR(25)">
		<constraints nullable="false" unique="true"/>
	</column>
	
	<column name="description" type="VARCHAR(255)"/>
	
	<column name="price" type="NUMERIC(6, 1)">
		<constraints nullable="false"/>
	</column>
	
	<column name="stock_quantity" type="INT">
		<constraints nullable="false"/>
	</column>
</createTable>
```

## Part 4 - Explaining REST API - Entity, Repository, Service, Controller
## Part 5 - Implemented CRUD Operations on `ProductController`
### Part 6 - Validation, Swagger, `@ControllerAdvise`
- Gradle imports for validation and swagger docs
```gradle
implementation 'org.springframework.boot:spring-boot-starter-validation'  
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5'
```
- `SwaggerConfiguration`
```Java
@Configuration  
@OpenAPIDefinition(  
    info = @Info(title = "Spring Boot Starter", version = "v1")  
)  
public class SwaggerConfiguration {  
}
```
- `GlobalExceptionHandler`
```Java
@RestControllerAdvice  
public class GlobalExceptionHandler {  
  
    @ExceptionHandler(ResponseStatusException.class)  
    public ProblemDetail handleException(ResponseStatusException ex) {  
        final var error = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getReason());  
        error.setProperty("timestamp", Instant.now());  
        return error;  
    }  
  
    @ExceptionHandler(MethodArgumentNotValidException.class)  
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {  
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(  
            ex.getStatusCode(),  
            "Validation failed for one or more fields"  
        );  
        final var errors = new HashMap<>();  
        ex.getBindingResult().getFieldErrors().forEach(error ->  
            errors.put(error.getField(), error.getDefaultMessage())  
        );  
        problemDetail.setProperty("errors", errors);  
        problemDetail.setProperty("timestamp", Instant.now());  
        return problemDetail;  
    }  
  
}
```

## Part 7 - Unit Test
- Adding dependencies for the test functionality
```gradle 
testImplementation 'org.assertj:assertj-core'  
testImplementation 'org.testcontainers:postgresql'  
testCompileOnly 'org.projectlombok:lombok'  
testAnnotationProcessor 'org.projectlombok:lombok'  
testImplementation 'io.rest-assured:rest-assured'  
testImplementation 'io.rest-assured:spring-mock-mvc'
```
- Unit Testing `**Service`
    - We are testing the service logic.
    - First create a `static final class` that implement the service, and then create your own logic
- Unit Testing `**Request`
    - We are testing the dtos.
    - Just validating the fields.
    - Create a abstract class to use `Validator`.
```Java 
public abstract class ValidationTestBase {  
  
    protected static Validator validator;  
  
    @BeforeAll  
    static void setUp() {  
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();  
        validator = factory.getValidator();  
    }  
  
}
```
## Part 8 - Integration Testing
- For testing controllers.
- First create an abstract class for configuration.
```Java 
@SpringBootTest  
@AutoConfigureMockMvc(addFilters = false)  
@ActiveProfiles("test")  
@Transactional  
@ExtendWith({SpringExtension.class, SoftAssertionsExtension.class})
```
- Add configuration for the `application-test.yaml` file for Database connection.
```yml
spring:  
  datasource:  
    url: jdbc:tc:postgresql:17-alpine:///testdb  
    driver-class-name: org.testcontainers.jdbc.ContainerDatabaseDriver  
  
  jpa:  
    hibernate:  
      ddl-auto: none  
  
  liquibase:  
    enabled: true
```
- Adding a class called `ProductControllerIntegrationTest`
```Java
public class ProductControllerIntegrationTest extends IntegrationTestBase {  
  
    @Autowired  
    private ProductService productService;  
    @Autowired  
    private ObjectMapper objectMapper;  
    @Autowired  
    private MockMvc mockMvc;  
  
    private static final String PRODUCT_NAME = "Shoe";  
    private static final String PRODUCT_DESCRIPTION = "Shoe description";  
    private static final Double PRODUCT_PRICE = 100.0;  
    private static final Integer PRODUCT_STOCK_QUANTITY = 10;  
  
    @Test  
    void shouldGetAllProducts(SoftAssertions softly) throws Exception {  
        // given  
        createProduct();  
  
        // when  
        final var result = mockMvc.perform(get("/protected/products"))  
            .andExpect(status().isOk())  
            .andReturn();  
  
        // then  
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<ProductResponse>>() {});  
        softly.assertThat(response).size().isEqualTo(1);  
        softly.assertThat(response.getFirst().getName()).isEqualTo(PRODUCT_NAME);  
        softly.assertThat(response.getFirst().getDescription()).isEqualTo(PRODUCT_DESCRIPTION);  
        softly.assertThat(response.getFirst().getPrice()).isEqualTo(PRODUCT_PRICE);  
        softly.assertThat(response.getFirst().getStockQuantity()).isEqualTo(PRODUCT_STOCK_QUANTITY);  
    }  
  
    @Test  
    void shouldGetProduct(SoftAssertions softly) throws Exception {  
        // given  
        final var productId = createProduct();  
  
        // when  
        final var result = mockMvc.perform(get("/protected/products/{productId}", productId))  
            .andExpect(status().isOk())  
            .andReturn();  
  
        // then  
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProductResponse.class);  
        softly.assertThat(response.getId()).isEqualTo(productId);  
        softly.assertThat(response.getName()).isEqualTo(PRODUCT_NAME);  
        softly.assertThat(response.getDescription()).isEqualTo(PRODUCT_DESCRIPTION);  
        softly.assertThat(response.getPrice()).isEqualTo(PRODUCT_PRICE);  
        softly.assertThat(response.getStockQuantity()).isEqualTo(PRODUCT_STOCK_QUANTITY);  
    }  
  
    @Test  
    void shouldReturnProductNotFound(SoftAssertions softly) throws Exception {  
        // given  
        final var productId = UUID.randomUUID();  
  
        // when  
        final var result = mockMvc.perform(get("/protected/products/" + productId))  
            .andExpect(status().isNotFound())  
            .andReturn();  
  
        // then  
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);  
        softly.assertThat(response.getDetail()).isEqualTo("Product not found");  
    }  
  
    @Test  
    void shouldCreateProduct(SoftAssertions softly) throws Exception {  
        // given  
        final var request = new ProductRequest("Keyboard", "Keyboard description", 100.0, 10);  
  
        // when  
        final var result = mockMvc.perform(  
                post("/protected/products")  
                    .contentType(MediaType.APPLICATION_JSON)  
                    .content(objectMapper.writeValueAsString(request))  
            )  
            .andExpect(status().isOk()).andReturn();  
  
        // then  
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProductResponse.class);  
        softly.assertThat(response.getName()).isEqualTo(request.getName());  
        softly.assertThat(response.getDescription()).isEqualTo(request.getDescription());  
        softly.assertThat(response.getPrice()).isEqualTo(request.getPrice());  
        softly.assertThat(response.getStockQuantity()).isEqualTo(request.getStockQuantity());  
    }  
  
    @Test  
    void shouldUpdateProduct(SoftAssertions softly) throws Exception {  
        // given  
        final var productId = createProduct();  
        final var request = new ProductRequest("Keyboard", "Silver style", 50.0, 5);  
  
        // when  
        mockMvc.perform(  
                put("/protected/products/" + productId)  
                    .contentType(MediaType.APPLICATION_JSON)  
                    .content(objectMapper.writeValueAsString(request))  
            )  
            .andExpect(status().isNoContent());  
  
        // then  
        final var updatedProduct = productService.getProduct(productId);  
        softly.assertThat(updatedProduct.getName()).isEqualTo(request.getName());  
        softly.assertThat(updatedProduct.getDescription()).isEqualTo(request.getDescription());  
        softly.assertThat(updatedProduct.getPrice()).isEqualTo(request.getPrice());  
        softly.assertThat(updatedProduct.getStockQuantity()).isEqualTo(request.getStockQuantity());  
    }  
  
    @Test  
    void shouldNotUpdateProductDueNotFound(SoftAssertions softly) throws Exception {  
        // given  
        final var productId = UUID.randomUUID();  
        final var request = new ProductRequest("Keyboard", "Silver style", 50.0, 5);  
  
        // when  
        final var result = mockMvc.perform(  
                put("/protected/products/" + productId)  
                    .contentType(MediaType.APPLICATION_JSON)  
                    .content(objectMapper.writeValueAsString(request))  
            )  
            .andExpect(status().isNotFound())  
            .andReturn();  
  
        //  
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);  
        softly.assertThat(response.getDetail()).isEqualTo("Product not found");  
    }  
  
    @Test  
    void shouldUpdateProductStockQuantity(SoftAssertions softly) throws Exception {  
        // given  
        final var productId = createProduct();  
        final var request = new ProductStockRequest(5);  
  
        // when  
        mockMvc.perform(  
                patch("/protected/products/" + productId + "/stock")  
                    .contentType(MediaType.APPLICATION_JSON)  
                    .content(objectMapper.writeValueAsString(request))  
            )  
            .andExpect(status().isNoContent());  
  
        // then  
        final var updatedProduct = productService.getProduct(productId);  
        softly.assertThat(updatedProduct.getStockQuantity()).isEqualTo(request.getQuantity()+PRODUCT_STOCK_QUANTITY);  
    }  
  
    @Test  
    void shouldDeleteProduct(SoftAssertions softly) throws Exception {  
        // given  
        final var productId = createProduct();  
  
        // when  
        mockMvc.perform(delete("/protected/products/{productId}", productId))  
            .andExpect(status().isNoContent());  
  
        // then  
        softly.assertThatThrownBy(() -> productService.getProduct(productId))  
            .isInstanceOf(ResponseStatusException.class);  
    }  
  
    private UUID createProduct() {  
        return productService.createProduct(new ProductRequest(  
            PRODUCT_NAME, PRODUCT_DESCRIPTION, PRODUCT_PRICE, PRODUCT_STOCK_QUANTITY  
        )).getId();  
    }  
  
}
```

## Part 9 - Employee Table, Entity, Repository

## Part 10 - Employee Service, Controller
## Part 11 - Adding two endpoints, implementing test cases
## Part 12 - Customer Table, Entity, Repository
## Part 13 - `CustomerController` endpoints
## Part 14 - Adding `Status` column to 3 table, adding `phone_number` to employee table
## Part 15 - 4 endpoints, activate & deactivate employee & customer
## Part 16 - Implementing Spring Security
#### Section 1
1. Gradle dependencies
```Gradle
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'io.jsonwebtoken:jjwt-api:0.12.6'

runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'

testImplementation 'org.springframework.security:spring-security-test'
```
2. Adding `SecurityConfiguration`
``` Java 
@Configuration  
@EnableWebSecurity  
@EnableMethodSecurity  
@RequiredArgsConstructor  
public class SecurityConfiguration {  
  
  
    @Bean  
    public PasswordEncoder passwordEncoder() {  
        return new BCryptPasswordEncoder();  
    }  
  
    @Bean  
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {  
        var provider = new DaoAuthenticationProvider(userDetailsService);  
        provider.setPasswordEncoder(passwordEncoder());  
  
        return provider;  
    }  
  
    @Bean  
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {  
        return config.getAuthenticationManager();  
    }  
  
    @Bean  
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {  
        // tell Spring to:  
        // 1- Use stateless session (JWT - Token based authentication)        
        // 2- Disable CSRF (Cross Side Request Forwarding)        
        // 3- Authorize HTTP requests        
        // 4- add JwtAuthenticationFilter        
        // 5- add response codes (401, 403).        
        return http  
		    .sessionManagement(c ->  
		        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  
		    )  
		    .csrf(AbstractHttpConfigurer::disable)  
		    .authorizeHttpRequests(c -> c  
		        .requestMatchers(  
		            "/swagger-ui/**",  
		            "/swagger-ui.html",  
		            "/v3/api-docs/**"  
		        ).permitAll()  
		        .requestMatchers("/public/**").permitAll()  
		        .anyRequest().authenticated()  
		    )  
		    .exceptionHandling(c -> {  
		            c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));  
		            c.accessDeniedHandler((req, response, accessDeniedException) ->  
		                response.setStatus(HttpStatus.FORBIDDEN.value())  
		            );  
		        }  
		    )  
		    .build();  
	    }  
  
}
```
3. Create a `User` interface to generalize user in terms of spring security.
    - We have two entities (`Customer`, `Employee`) When loading a user by email, we don't know which object is which.
    - So we create an interface to generalize the two entities.
        - Rename `BackofficeUserRole` to `UserRole`.
``` Java
public interface User {  
    UUID getId();  
    String getEmail();  
    String getPassword();  
    UserRole getRole();  
}
```
4. Create a principal object class, so that we can store it in the `Security Context`
    - `public class UserPrincipal implements UserDetails, User`
    - It should implement `UserDetails` so that we can put it in the context.
    - Also should implement `User` so that we can convert it back to our application user.
5. Implement `UserDetailsService`.
    - Used for loading the user from our DB by Email.
    - Consider adding index to speed up the query, in our case it is already indexed.
#### Section 2
1. Create `AuthController`
    - Add three endpoints, without implementation. IN section 3 we will implement.
    - Add `BadCredentialsException.class` in the `GlobalExceptionHandler` to return 401.
```Java
@ExceptionHandler(BadCredentialsException.class)  
public ProblemDetail handleInvalidLogin() {  
    return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Invalid email or password");  
}
```
2. Modify `SwaggerConfiguration`
   ```Java
@Configuration  
@OpenAPIDefinition(  
info = @Info(title = "Spring Boot Starter", version = "v1"),  
security =@SecurityRequirement(name = "bearerAuth")  
)  
@SecurityScheme(  
name = "bearerAuth",  
type = SecuritySchemeType.HTTP,  
scheme = "bearer",  
bearerFormat = "JWT"  
)
```
3. Add JWT related properties
```yaml
jwt:  
  secret: secret
  accessTokenExpiration: 900 # 15 minutes  
  refreshTokenExpiration: 86400 # 1 day
```
4. Create `JwtConfig` class to hold the properties configuration
    - Create a method to return the `Secret Key` object `Keys.hmacShaKeyFor(secret.getBytes());`
``` Java
@Configuration  
@ConfigurationProperties(prefix = "spring.jwt")  
@RequiredArgsConstructor  
@Setter  
public class JwtConfig {  
  
    private String secret; // must be more than 64  
    @Getter  
    private int accessTokenExpiration; // 5 minutes  
    @Getter  
    private int refreshTokenExpiration;  
  
    public SecretKey getSecretKey() {  
        return Keys.hmacShaKeyFor(secret.getBytes());  
    }  
  
}
```
5. Create `Jwt` class, to hold the token, claims, and additional data.
    - This class will be used as a domain. We use some concepts of DDD (Domain Driven Design).
```Java
@Getter  
private final String token;  
private final Claims claims;  
@Getter  
private final Type type;  
private final SecretKey secretKey;

public Jwt(String token, SecretKey secretKey) {  
    this.token = token;  
    this.secretKey = secretKey;  
    this.claims = extractClaims();  
    this.type = Type.valueOf(claims.get("tokenType").toString());  
}

public static Jwt of(User user, int expiration, Type tokenType, SecretKey secretKey) {  
    final var token = Jwts.builder()  
        .subject(user.getId().toString())  
        .claim("jwtId", UUID.randomUUID().toString())  
        .claim("email", user.getEmail())  
        .claim("role", user.getRole())  
        .claim("tokenType", tokenType)  
        .issuedAt(new Date())  
        .expiration(new Date(System.currentTimeMillis() + 1000L * expiration))  
        .signWith(secretKey)  
        .compact();  
  
	return new Jwt(token, secretKey);  
}

private Claims extractClaims() {  
	return Jwts.parser()  
		.verifyWith(this.secretKey)  
		.build()  
		.parseSignedClaims(this.token)  
		.getPayload();  
}

public enum Type {  
    ACCESS, REFRESH 
}
```

#### Section 3
1. Create `JwtService` interface, with three methods to handle token creation, and parsing.
```Java
String generateAccessToken(User user);  
  
String generateRefreshToken(User user);  
  
Jwt parseToken(String token);
```
2. Complete login endpoint, to use `JwtService` to validate and return token.
    - Add request and response,
3. Finally create `JwtAuthenticationFilter`.
    - extends `OncePerRequestFilter` and override two of it is methods.
    - First `shouldNotFilter` because public endpoints don't need token
    - Second `doFilterInternal` for checking token and setting security context.
        - Check authorization header.
        - Check token
        - Finally create auth context.
```Java
@Override  
protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

}

@Override  
protected boolean shouldNotFilter(HttpServletRequest request) {  
    final var path = request.getRequestURI();  
    return path.startsWith("/public/") || path.contains("swagger");  
}
```


#### Section 4
1. Adding authorization role per `UserRole`
```Java
@Target({ElementType.METHOD, ElementType.TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
@PreAuthorize()
public @interface **RoleRequired
```
2. Implement refresh endpoint.
3. Modify `me` endpoint to return DTO response.
4. Modify `register` endpoint to support creating customer.
#### Section 5
- Before logging in check `UserStatus`
    - It must be active otherwise return `UNAUTHORIZED`.

## Part 17 - Pagination & Sorting
- To simplify paging response use this configuration
```java 
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class PagingConfig {
    // Other web configs if needed
}
```
- For endpoints that returns all of the data, we have to implement pagination and sorting.
    - By using `RequestParam()` to get the data from the user, and then return a section from the data.
```Java
@RequestParam(name = "page", defaultValue = "0") int page,  
@RequestParam(name = "size", defaultValue = "10") int size,  
@RequestParam(name = "sort", defaultValue = "id") String sortBy,  
@RequestParam(name = "direction", defaultValue = "asc") String direction
```
- Using a dedicated class `PagingRequest` to check the user inputs and return `Pageable` so that JPA can use it
```java
@Value  
public class PagingRequest {  
    int page;  
    int size;  
    String sort;  
    Sort.Direction direction;  
  
    public PagingRequest(int page, int size, String sort, String direction) {  
        this.page = Math.max(page, 0);  
        this.size = Math.max(size, 1);  
        this.sort = sort;  
        this.direction = !direction.equals("desc") ? Sort.Direction.ASC : Sort.Direction.DESC;  
    }  
  
    public Pageable toPageable(List<String> allowedSorts) {  
        final var sortProperty = allowedSorts.contains(sort) ? sort : allowedSorts.getFirst();  
        final Sort sort = Sort.by(direction, sortProperty);  
        return PageRequest.of(page, size, sort);  
    }  
  
}
```
- Create another method in the service dedicated for the pagination
    - `getPaginatedProducts(Pageable pageable, ....) - getPaginatedEmployees - getPaginatedCustomers`
- We can straight use `**repository.findAll(pageable)` it supports, but we have some filtering based on the status and role like:
```
@Override  
public Page<EmployeeResponse> getPaginatedEmployees(Pageable pageable, List<UserStatus> statuses, List<UserRole> roles) {  
    return employeeRepository.findAllByStatusInAndRoleIn(pageable, statuses, roles)  
        .map(EmployeeEntity::toDto);  
}
```
- So we have add a method in the repository:
    - `Page<EmployeeEntity> findAllByStatusInAndRoleIn(Pageable pageable, List<UserStatus> statuses, List<UserRole> roles);`

## Part 18 - `Order, OrderItem` Entity, Repository
1. Create two migration for creating two tables:
    - `order` table -> `id, customer_id, status, total_price, updated_at, created_at`
    - `order_item` table -> `id, order_id, product_id, quantity, price, updated_at, created_at`
2. Create corresponding entity for `order` table -> `OrderEntity
    - The only that is new is the relation mapping.
    - Since a Customer can have many orders it is a `OneToMany` relation
    - So in the customer we add this, so that we can fetch it is orders based on the customer.
```Java
// In the Customer Entity
@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)  
private List<OrderEntity> orders;

// In the Order Entity 
@ManyToOne(fetch = FetchType.LAZY)  
@JoinColumn(name = "customer_id")  
private CustomerEntity customer;
```
3. Create corresponding entity for `order_item` table -> `OrderItem`
    - Again we use `OneToMany` relation, between `OrderEntity` and `ProductEntity`
```Java 
// In the Order Entity 
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)  
private List<OrderItemEntity> orderItems;

// In the Product Entity 
@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)  
private List<OrderItemEntity> orderItems;

// In the OrderItem Entity 
@ManyToOne(fetch = FetchType.LAZY)  
@JoinColumn(name = "order_id")  
private OrderEntity order;  
  
@ManyToOne(fetch = FetchType.LAZY)  
@JoinColumn(name = "product_id")  
private ProductEntity product;
```
4. Implement the repository and the service interfaces for it.

## Part 19 - Order Controller, Services
The functionality that we have:
- Get orders, Get one order with items
- Initiate order
- Add/update/delete item from the order
- remove all items from order.
- remove order
- complete order
## Part 20 - Upgrading Java, Spring Boot, Gradle - Failed Attempt :(
Unfortunately I tried upgrading it, but only managed to run the main package! As for the test package it has import and module problem.
- Java -> 25.
- Spring Boot -> 4.0.5
- Gradle -> 9.4.1
- Update dependency modules.
```gradle
dependencies {  
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'  
    implementation 'org.springframework.boot:spring-boot-starter-web'  
    implementation 'org.springframework.boot:spring-boot-starter-validation'  
    implementation 'org.springframework.boot:spring-boot-starter-security'  
    implementation 'org.liquibase:liquibase-core:5.0.1'  
    runtimeOnly 'org.postgresql:postgresql'  
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2'  
    implementation 'io.jsonwebtoken:jjwt-api:0.12.6'  
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'  
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'  
    compileOnly 'org.projectlombok:lombok'  
    annotationProcessor 'org.projectlombok:lombok'  
  
    testImplementation 'org.springframework.boot:spring-boot-starter-test'  
    testImplementation 'org.springframework.security:spring-security-test'  
    testImplementation 'org.testcontainers:postgresql:1.20.4'  
    testImplementation 'io.rest-assured:rest-assured:6.0.0'  
    testImplementation 'io.rest-assured:spring-mock-mvc:6.0.0'  
    testImplementation 'org.assertj:assertj-core'  
    testCompileOnly 'org.projectlombok:lombok'  
    testAnnotationProcessor 'org.projectlombok:lombok'  
}  
  
tasks.withType(JavaCompile).configureEach {  
    options.compilerArgs << "-parameters"  
}
```