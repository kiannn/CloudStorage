## About the Project
CloudStorage is a secure and user-friendly application for managing files, notes, and website credentials. It enables file uploads, downloads, and management while offering note and credential handling. Authentication and real-time validation is apllied to provide security. 

## Key Features:
- **Secure Authentication:** User authentication with **Spring Security**.
- **File Management:** Upload, download, and manage files securely, with real-time validation for duplicate file names.
- **Note Management:** Add, edit, and delete personal notes.
- **Credential Management:** Store and manage website credentials securely, with passwords encoded using the `Base64` encoding scheme for display.
- **Password Encoding:** User account passwords are encoded using the `Base64` encoding scheme for added security.
- **Real-Time Feedback:** Success and error messages for all user actions.
- **Password Validation:** Enforces password length limit and pattern (minimum 8 characters, no spaces).

## Built With

- **Spring Boot** (Spring MVC, Spring Security)  
- **MyBatis for ORM mapping**  
- **Thymeleaf**  
- **JavaScript**  
- **CSS/HTML**  

## Usage

### User Perspective:
1. **Sign-Up and Login:**
   - New users can sign up with a secure password. Passwords must be at least 8 characters long, and spaces are not allowed.
   - Login provides access to personalized storage and management features.
   - User passwords are encoded when being stored into the database to enhance security.
2. **File Management:**
   - Upload files to your account, with duplicate file names rejected to avoid overwriting.
   - Download or delete files directly from the dashboard.
3. **Note Management:**
   - Add personal notes, edit them as needed, or delete them when no longer required.
4. **Website Credential Management:**
   - Securely store usernames and passwords for websites.
   - Passwords are encoded when displayed to the user for security purposes.
   - When updating website credentials, the original (decoded) password is displayed to the user.
5. **Real-Time Feedback:**
   - Users receive instant success or error messages for all actions, such as uploading files, signing up, or adding/editing/deleting notes or website credentials.

### Technical Perspective:
1. **Backend Functionality:**
   - Developed with **Spring Boot**, using **Spring MVC** for handling requests and **MyBatis** for database interactions.
   - Implements **Spring Security** for user authentication.
   - Utilizes a custom **AuthenticationProvider** for handling authentication logic.
2. **Password Encoding:**
   - User account passwords are persisted into the database after encoding with the **Base64** encoding scheme.
   - Website credentials (specifically passwords) are encoded with **Base64** when displayed to users. When users edit website credentials, the original password is presented in its decoded form.
3. **Frontend Interactivity:**
   - Built with **Thymeleaf** for integration with backend data.
   - Enhanced with **JavaScript** to provide real-time feedback and interactivity.
4. **File Storage and Management:**
   - Files are stored and linked to the user account.
   - Duplicate file names are rejected during the upload process for data consistency.
   - File size greater than 10 MB are rejected for upload.
5. **Validation:**
   - Passwords must meet security patterns (minimum 8 characters, no spaces) during signup.
   - Real-time feedback for errors such as invalid inputs or duplicate uploads.
 ## Contact
 - Email: kpourd@gmail.com
 - GitHub Profile: https://github.com/kiannn/CloudStorage
 - Live Demo: https://cloudstorage-production-version.up.railway.app/login
