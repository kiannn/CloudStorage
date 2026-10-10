## About the Project
CloudStorage is a secure and user-friendly application for managing files, notes, and website credentials. It enables file uploads, downloads, and management while offering note and credential handling. Authentication and real-time validation is apllied to provide security. 

## Key Features:

- **Secure Authentication:** User authentication with **Spring Security**.
- **File Management:** Upload, download, and manage files securely, with real-time validation for duplicate file names.
- **Note Management:** Add, edit, and delete personal notes.
- **Credential Management:** Store and manage website credentials with passwords encrypted using **AES-GCM**. A master encryption key is stored outside the database as an 
    environment variable, while a unique IV is generated for each encryption. The encrypted password and IV are stored in the database and                     
    decrypted only when required for display or editing.
- **Password Security:** User account passwords are hashed using **PBKDF2WithHmacSHA256** with a unique randomly generated salt for each account. The resulting password 
    hash and salt are Base64-encoded for storage. Base64 is used only as a text representation and does not provide security by itself.
- **Real-Time Feedback:** Success and error messages for all user actions.
- **Password Validation:** Enforces password length limit and pattern (minimum 8 characters, no spaces).

## Built With

- Java 22
- Spring Boot (Spring MVC, Spring Security)  
- MyBatis for SQL mapping and database access
- Thymeleaf
- MySQL
- JavaScript  
- HTML/CSS  
- Docker
- Railway

## Setup / Configuration

### Prerequisites
Before running the application locally, make sure the following are installed:

- JDK 22
- Maven
- MySQL

### Environment Variables
The application uses environment variables for database connectivity, application URL configuration, and credential encryption.

Set the following environment variables before starting the application:

| Variable                    | Description
|-----------------------------|---------------------------------------------------------------
| `databse.url`               | JDBC connection URL for the MySQL database 
| `databse.user`              | MySQL username  
| `databse.pass`              | MySQL password 
| `CREDENTIAL_ENCRYPTION_KEY` | Base64-encoded AES master key used for AES-GCM credential encryption 
| `BASE_URL`                  | Base URL of the application, should be set to `http://localhost:8080/` when the application is run locally

Example local configuration:

```text
databse.url=jdbc:mysql://localhost:3306/cloudstorage
databse.user=root
databse.pass=<your-database-password>
CREDENTIAL_ENCRYPTION_KEY=<your-own-Base64-encoded-AES-key>
BASE_URL=http://localhost:8080/
```
### Database Setup
The MySQL database must exist before the application starts, create an empty MySQL database before starting the application. You could use any database name, but the name used in `databse.url` must match the database you created. The application uses schema.sql to create the required tables automatically when it starts.

For example:

```sql
CREATE DATABASE cloudstorage;
```
### AES Master Key
The application requires a Base64-encoded AES master key for encrypting stored website credentials.
Generate your own secure AES key for local development and assign it to:

```text
CREDENTIAL_ENCRYPTION_KEY
```

## Usage

### User Perspective:
1. **Sign-Up and Login:**
   - New users can sign up with a secure password. Passwords must be at least 8 characters long, and spaces are not allowed.
   - Login provides access to personalized storage and management features.
   - User passwords are hashed using PBKDF2-HMAC-SHA256 before being stored in the database.
2. **File Management:**
   - Upload files to your account, with duplicate file names rejected to avoid overwriting.
   - Download or delete files directly from the dashboard.
   - Files uploaded by users are securely associated with their accounts and accessible only to the owning user.
   - File size validation with a maximum upload limit of 10 MB.
3. **Note Management:**
   - Add personal notes, edit them as needed, or delete them when no longer required.
4. **Website Credential Management:**
   - Securely store website usernames and passwords.
   - Website passwords are encrypted using **AES-GCM** before being stored in the database.
   - A master AES key is stored outside the database as an environment variable, while a unique IV is generated for each encryption.
   - The encrypted password and IV are stored in the database.
   - When users view or update a saved credential, the password is decrypted and displayed in its original form.  
5. **Real-Time Feedback:**
   - Users receive instant success or error messages for all actions, such as uploading files, signing up, or adding/editing/deleting notes or website credentials.

### Technical Perspective:
1. **Backend Functionality:**
   - Developed with **Spring Boot**, using **Spring MVC** for handling requests and **MyBatis** for database interactions.
   - Implements **Spring Security** for user authentication.
   - Utilizes a custom **AuthenticationProvider** for handling authentication logic.
2. **Password Security & Credential Encryption:**
   - **User account passwords:** Account passwords are hashed using **PBKDF2WithHmacSHA256** with a unique randomly generated salt for each user. The salt and resulting 
       derived hash are Base64-encoded before being persisted to the database. PBKDF2 provides the password protection; Base64 is used only to represent binary data as  
       text.
   - **Stored website credential passwords:** Website passwords are encrypted using **AES-GCM** because they must be recoverable when users view or edit saved   
     credentials. The application uses a master AES key stored outside the database as an environment variable. A fresh random IV is generated for every encryption, and 
     the encrypted password together with its IV is stored in the database.
   - **Encryption design:** AES-GCM provides both confidentiality and integrity protection. The master key remains separate from the database, while the IV does not need  
       to be secret and is stored alongside the ciphertext.
3. **Frontend Interactivity:**
   - Built with **Thymeleaf** for integration with backend data.
   - Enhanced with **JavaScript** to provide real-time feedback and interactivity.
4. **Data Access & Performance Design:**
   - Designed MyBatis data-fetching strategies to minimize payload size (avoid loading large binary **fileData**) and optimize performance.
   - Applied DTOs to separate concerns:
     - FileMetadata for listing file information without loading binary data.
     - FileDownload for retrieving file content only when users request a download.
     - UserCredentials for authentication-specific data access.
   - Avoided eager loading of BLOB data by using purpose-specific SQL queries for list views and download actions.
   - Enforced user-level access control by including userid constraints in all file retrieval queries.
5. **Validation:**
    - Passwords must meet security patterns (minimum 8 characters, no spaces) during signup.
    - Real-time feedback for errors such as invalid inputs or duplicate uploads.

## Testing

The application includes automated end-to-end tests using **JUnit** and **Selenium WebDriver**.

The Selenium test suite covers key user flows, including:

- User sign-up, login, and logout
- File upload
- Note creation, editing, and deletion
- Website credential creation, editing, and deletion
- Login page access
- Invalid URL handling
- Redirection behavior
- Access control for unauthenticated users

The current Selenium test suite passes successfully after the recent credential-encryption changes.

##  Deployment

The application is deployed on **Railway** using Docker.

A multi-stage Docker build is used:

- The first stage uses Maven to build the Spring Boot application and generate the executable JAR.
- The second stage copies the generated JAR into a Java runtime image and runs the application on port `8080`.
- Production configuration is supplied through Railway environment variables, such as database credentials, the application base URL, and the AES master encryption key.
- Sensitive configuration values are kept outside the source code and are not committed to the repository.
- The deployed application connects to a Railway-hosted MySQL database.

 ## Contact
 - Email: kpourd@gmail.com
 - GitHub Profile: https://github.com/kiannn/CloudStorage
 - Live Demo: https://cloudstorage-production-version.up.railway.app/login
