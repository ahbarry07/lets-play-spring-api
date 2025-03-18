# CRUD API with Spring Boot and MongoDB

## lets-play

**Let's Play** is a RESTful API developed with Spring Boot, designed to manage users and products in a secure application. It uses MongoDB as a database (deployed via Docker Compose), JWT for authentication, and implements features like CORS, rate limiting and HTTPS to protect data in transit for enhanced security.

## Main Features
- **Authentication**: User registration and login with JWT.
- **User Management**: Creation, role updates (by admins), and retrieval of the logged-in user's information.
- **Product Management**: Creation, update, deletion, and retrieval of products, with access restrictions (only the owner can modify, owner and admin can delete).
- **Security**: Role-based authentication (`ROLE_USER`, `ROLE_ADMIN`), CORS, and rate limiting.
- **Use HTTPS**: to protect data in transit.

## Prerequisites
- **Java**: JDK 17 or higher
- **Maven**: To manage dependencies and build the project
- **Docker**: To run MongoDB via Docker Compose
- **Postman** (optional): To test API endpoints

## Installation and Execution

1. **Clone the project**
   ```sh
   git clone <repository_url>
   cd <project_name>
   ```
2. **Start MongoDB**
   - The database is configured using Docker Compose.
   - Run the following command to start MongoDB:
```sh
  docker compose up -d
```
3. **Create an Admin User (First Run Only)**
- Uncomment the following code in your application to create an admin user:
```java
// BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
//
// @Bean
// CommandLineRunner dataLoader(final UserRepository userRepository) {
//     return new CommandLineRunner() {
//         @Override
//         public void run(String... args) throws Exception {
//             User admin = User.builder()
//                             .name("Ahmad")
//                             .email("ahmad@gmail.com")
//                             .password(bCryptPasswordEncoder.encode("Ahmad123"))
//                             .role(Role.ADMIN)
//                             .build();
//
//             userRepository.save(admin);
//         }
//     };
// }
```
- Run the application once to create the admin user.
- Then, comment out the code to prevent duplicate admin creation in subsequent runs.

4. **Build and run the application**
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

## API Endpoints

### Authentication
- **Login**: `POST /api/auth/login`
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
    - Response: JWT Token

### User Management
- **Create a user**: `POST /api/users`
  ```json
  {
    "name": "John Doe",
    "email": "johndoe@example.com",
    "password": "password123"
  }
  ```
- **Get user list** (Admin only): `GET /api/users`
- **Get profile** : `GET /api/users/me`
- **Update a user**: `PUT /api/users/me`
- **Change user role** (Admin): `PUT /api/users/role/{id}`
- **Delete own account**: `DELETE /api/users/me`
- **Delete a user** (Admin): `DELETE /api/users/delete/{id}`

### Product Management
- **Get product list** (Accessible to all): `GET /api/products`
- **Get product by id**: `GET /api/products/{id}`
- **Get product list of user connected**: `GET /api/products/myproduct`
- **Create a product**: `POST /api/products/addproduct`
  ```json
  {
    "name": "Product A",
    "description": "Description of Product A",
    "price": 29.99
  }
  ```
- **Update a product** (User): `PUT /api/products/{id}`
- **Delete a product** (User and Admin): `DELETE /api/products/{id}`

## Security Measures
- JWT authentication to secure endpoints.
- Password hashing with BCrypt.
- Protection against MongoDB injection attacks.
- Role-based access control (admin, user).

## Author
Developed by **Ahmadou Barry**  
Github: [ahbarry07](https://github.com/ahbarry07)

