package com.jwdnd.cloudstorage;

import com.jwdnd.cloudstorage.Mappers.CredentialsMapper;
import com.jwdnd.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;
        
        @Autowired
        CredentialsMapper CredentialsMapper;
        
        @Autowired
        EncryptionService EncryptionService;
        
	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
                
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 *  
	 * Helper method for sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	/**
	 *  
	 * Helper method for  sanity checks.
	 **/
	private void doLogIn(String userName, String password){
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123456789");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login?sigUpSuccess", driver.getCurrentUrl());
	}

	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123456789");
		doLogIn("UT", "123456789");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
                
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
                Assertions.assertEquals("Custom Errors Page", driver.getTitle());  
	}

    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123456789");
        doLogIn("LFT", "123456789");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(3));
        String fileName = "15MB.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr.fileItems th")));

        //Check if success message displays after upload
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("uploaded successfully !"));

        //Verify if uploaded file’s name displays on the files table
        Assertions.assertEquals("15MB.zip", driver.findElement(By.cssSelector("tr.fileItems th")).getText());

        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }

     /**********************************************************************
      * The following test methods are :
      * 
      *   test_HomePage_Not_Accessible_Without_Login
      *   test_Signup_Login_Logout_Flow
      *   test_Create_Edit_Delete_Notes
      *   test_Create_Edit_Delete_Credentials
      * 
      * *********************************************************************
      */ 
        
    @Test
    public void test_HomePage_Not_Accessible_Without_Login() {
        
        driver.get("http://localhost:" + this.port + "/home");
        
        // Verify that we do not have access to Home page without logging-in
        Assertions.assertNotEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl()); 
        Assertions.assertNotEquals("Home",driver.getTitle());
        Assertions.assertFalse(driver.getPageSource().contains("id=\"nav-files\"")); 
        Assertions.assertFalse(driver.getPageSource().contains("id=\"nav-notes\""));
        Assertions.assertFalse(driver.getPageSource().contains("id=\"nav-credentials\""));
        
         // Verify that we have been redirected to Login page after attempting to access Home page without login 
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
        Assertions.assertEquals("Login", driver.getTitle());
        Assertions.assertTrue(driver.getPageSource().contains("id=\"login-button\"")); 
        
    }
    
   @Test
    public void test_Signup_Login_Logout_Flow() throws InterruptedException {
        
        doMockSignUp("Signup_Login_LogOut_Flow","Test","user1","123456789");
	doLogIn("user1", "123456789");
        
        // Access Home page after login
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl()); 
        Assertions.assertEquals("Home",driver.getTitle());
        Assertions.assertTrue(driver.getPageSource().contains("id=\"nav-files\"")); 
        Assertions.assertTrue(driver.getPageSource().contains("id=\"nav-notes\""));
        Assertions.assertTrue(driver.getPageSource().contains("id=\"nav-credentials\""));
        
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        webDriverWait.until(webDriver -> webDriver.findElement(By.id("logout-button")));      
        
        // Attempt to logout
        WebElement logoutButton = driver.findElement(By.id("logout-button"));
	logoutButton.click();
        
        // Verify that we have been redirected to Login page after logout 
        Assertions.assertEquals("http://localhost:" + this.port + "/login?logout", driver.getCurrentUrl());
        Assertions.assertTrue(driver.getPageSource().contains("You have been logged out"));
        Assertions.assertEquals("Login", driver.getTitle());
        
        // Attempt to RE-ACCESS Home page after logout
        driver.get("http://localhost:" + this.port + "/home");
        webDriverWait.until(webDriver -> webDriver.findElement(By.id("inputUsername")));
        
        // Verify that Home page is no longer accessible after logout
        Assertions.assertNotEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl()); 
        Assertions.assertNotEquals("Home",driver.getTitle());
        Assertions.assertFalse(driver.getPageSource().contains("id=\"nav-files\"")); 
        Assertions.assertFalse(driver.getPageSource().contains("id=\"nav-notes\""));
        Assertions.assertFalse(driver.getPageSource().contains("id=\"nav-credentials\""));
        
        // Verify that we are redirected again to Login page after attempting to re-access Home page without login 
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
        Assertions.assertEquals("Login", driver.getTitle());

    }

    @Test
    public void test_Create_Edit_Delete_Notes() throws InterruptedException {
        
        //Creat a user and log it in
        doMockSignUp("Create_Edit_Delete_Notes","Test","user2","123456789");
	doLogIn("user2", "123456789");
        
        //Verify that Home page is accessed
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl()); 
        Assertions.assertEquals("Home",driver.getTitle());
        
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(3));

        //Click on note tab
        webDriverWait.until(webDriver -> webDriver.findElement(By.id("nav-notes-tab")));    
        WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
        noteTab.click();
                       
        /***************************************************
         * Create two notes to be displayed on the note list
         *
         ***************************************************/
       
        // Define title and description for two notes
        List<String[]> titles_descriptions = new ArrayList<>( Arrays.asList(new String[]{"First note title for test purpose",
                                                                                           "First note description for test purpose"}, 
                                                                              new String[]{"Second note title for test purpose",
                                                                                           "Second note description for test purpose"}));
        
        // List all actual titles and descriptions
        List<String> allActualDescriptions = titles_descriptions.stream().map(note ->note[1]).collect(Collectors.toList());
        List<String> allActualTitles = titles_descriptions.stream().map(note ->note[0]).collect(Collectors.toList());

        // Create NotePage object
        NotePage NotePage = new NotePage(driver, webDriverWait);
                 
        //Add and save notes
        NotePage.add_and_Save_Notes(titles_descriptions); 
        
        //Check if success message displays after note create
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("successfully created !"));
        
        // Check if titles shown on edit modal box are the same as actual ones
        List<String> titles_Shown_On_Edit_ModalBox = NotePage.titles_Shown_On_Edit_ModalBox();
        Assertions.assertEquals(allActualTitles, titles_Shown_On_Edit_ModalBox);
        
        // Check if descriptions shown on edit modal box are the same as actual ones
        List<String> description_Shown_On_Edit_ModalBox = NotePage.description_Shown_On_Edit_ModalBox();
        Assertions.assertEquals(allActualDescriptions, description_Shown_On_Edit_ModalBox);
        
        //Retrieve all titles from note table  
        List<String> savedTitles = NotePage.getSavedNotesTitles();
  
        //Retrieve all descriptions from note table 
        List<String> savedDescriptions = NotePage.getSavedNoteDescriptions();
        
        //Verify displayed notes on the table are the same as inserted
        Assertions.assertEquals(titles_descriptions.size(), NotePage.numberOfNotesAdded());
        Assertions.assertEquals(allActualTitles, savedTitles);
        Assertions.assertEquals(allActualDescriptions, savedDescriptions);
        
        /*****************************
         * Edit a note at a chosen row
         *
         *****************************/
        
        // Choose an arbitrary row number to edit its corresponding note
        int rowNumber = 2; // the second note is chosen

        // Define a new title and description for the chosen note to be edited
        titles_descriptions.set(rowNumber-1, new String[]{"Edited note title","Edited note description"});

        // List all actual titles and descriptions after edit
        allActualDescriptions = titles_descriptions.stream().map(note ->note[1]).collect(Collectors.toList());
        allActualTitles = titles_descriptions.stream().map(note ->note[0]).collect(Collectors.toList());
        
        //Edit and save note at the given row numer which corresponds to the note id
        NotePage.edit_and_Save_Note(rowNumber, titles_descriptions.get(rowNumber-1)[0], titles_descriptions.get(rowNumber-1)[1]); 

        //Check if success message displays after note edit
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("successfully edited !"));
        
        //Retrieve all titles from note table after edit
        savedTitles = NotePage.getSavedNotesTitles();
         
        //Retrieve all descriptions from note table after edit
        savedDescriptions = NotePage.getSavedNoteDescriptions();
                
        // Verify displayed notes on the table after edit are updated as expected 
        Assertions.assertEquals(titles_descriptions.size(), NotePage.numberOfNotesAdded());
        Assertions.assertEquals(allActualTitles, savedTitles);
        Assertions.assertEquals(allActualDescriptions, savedDescriptions);

        /********************************
         * Delete a note at a chosen row
         *
        ********************************/
        
        // Choose an arbitrary row number to delete its corresponding note
        rowNumber = 1; // the first note is chosen
        
        titles_descriptions.remove(rowNumber-1);
        
        // List all actual titles and descriptions after delete
        allActualDescriptions = titles_descriptions.stream().map(note ->note[1]).collect(Collectors.toList());
        allActualTitles = titles_descriptions.stream().map(note ->note[0]).collect(Collectors.toList());
        
        // Delete note at the given row numer which corresponds to the note id
        NotePage.deleteNote(rowNumber); 
        
        //Check if success message displays after note delete
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("successfully deleted !"));
        
        //Retrieve all titles from note table after delete
        savedTitles = NotePage.getSavedNotesTitles();
  
        //Retrieve all descriptions from note table after delete 
        savedDescriptions = NotePage.getSavedNoteDescriptions();
        
        // Verify displayed notes on the table after delete are updated as expected 
        Assertions.assertEquals(titles_descriptions.size(), NotePage.numberOfNotesAdded()); // Verify number of table's rows are 1
        Assertions.assertEquals(allActualTitles, savedTitles);
        Assertions.assertEquals(allActualDescriptions, savedDescriptions);
        
    }
      
      @Test    
      public void test_Create_Edit_Delete_Credentials() {
          
        //Creat a user and log it in
        doMockSignUp("test_Create_Edit_Delete_Credentials","Test","user3","123456789");
	doLogIn("user3", "123456789");
        
        //Verify that Home page is accessed
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl()); 
        Assertions.assertEquals("Home",driver.getTitle());
        
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(3));

        //Click on credentials tab
        webDriverWait.until(webDriver -> webDriver.findElement(By.id("nav-credentials-tab")));    
        WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTab.click();
        
        /******************************************************************
         * Create three credentials to be displayed on the credentials list
         *
         ******************************************************************/
        
        // Define url username password for two credentials
        List<String[]> urls_username_password = new ArrayList<>( Arrays.asList(new String[]{"https://www.google.com/",
                                                                                               "google-user",
                                                                                                "123456789abc"}, 
                                                                                 new String[]{"https://www.youtube.com/",
                                                                                              "youtube-user",
                                                                                              "abc123456789"},
                                                                                 new String[]{"https://github.com/",
                                                                                              "github-user",
                                                                                              "123456789"}));
        // List all actual url username unencrypted-password
        List<String> allActualURLs = urls_username_password.stream().map( elm -> elm[0]).collect(Collectors.toList());
        List<String> allActualUsernams = urls_username_password.stream().map( elm -> elm[1]).collect(Collectors.toList());
        List<String> allActualUnencryptedPasswords = urls_username_password.stream().map( elm -> elm[2]).collect(Collectors.toList());
        
        // Create CredentialsPage object
        CredentialsPage CredentialsPage = new CredentialsPage(driver, webDriverWait);
        
        //Add and save credentials
        CredentialsPage.AddCredentials(urls_username_password);
        
        // Check if success message displays after credential create
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("credential is successfully created !"));
        
        // Check if URLs shown on edit modal box are the same as actual ones
        List<String> URLs_Shown_On_Edit_ModalBox = CredentialsPage.URLs_Shown_On_Edit_ModalBox();
        Assertions.assertEquals(allActualURLs, URLs_Shown_On_Edit_ModalBox);
        
        // Check if usernams shown on edit modal box are the same as actual ones
        List<String> usernams_Shown_On_Edit_ModalBox = CredentialsPage.usernams_Shown_On_Edit_ModalBox();
        Assertions.assertEquals(allActualUsernams, usernams_Shown_On_Edit_ModalBox);
        
        // Check if unencrypted passwords shown on edit modal box are the same as actual unencrypted ones
        List<String> UnencryptedPassword_Shown_On_Edit_ModalBox = CredentialsPage.password_Shown_On_Edit_ModalBox();
        Assertions.assertEquals(allActualUnencryptedPasswords, UnencryptedPassword_Shown_On_Edit_ModalBox);
        
        // Retrieve all urls from credentials table
        List<String> savedURLs = CredentialsPage.getSavedCredentialsURLs();
        
        // Retrieve all usernams from credentials table
         List<String> SavedUsernames = CredentialsPage.getSavedCredentialsUsernames();
         
        // Retrieve all encrypted passwords from credentials table
        List<String> SavedEncodedPasswords = CredentialsPage.getSavedCredentialsEncodedPasswords();
     
    /************ Encode each actual password with the same encoded key stored in database **********/
    
         List<String> allActualEncryptedPasswords = new ArrayList<>();
        
        //Ids of created credentials
         Integer[] Ids = {1, 2, 3};
          
        // Retrieve each encoded key stored in databsse and encode the actual passwords
          for (int id : Ids) {
              String keyEnc = CredentialsMapper.findCredentialByID(id).getKeyEnc();
              String encryptValue = EncryptionService.encryptValue(allActualUnencryptedPasswords.get(id - 1), keyEnc);
              allActualEncryptedPasswords.add(encryptValue);
          }
          
    /***************************************************************************/
    
        // Verify displayed credentials on the table are the same as inserted
        Assertions.assertEquals(urls_username_password.size(), CredentialsPage.numberOfCredentialsAdded()); //Verify the number of created credentials
        Assertions.assertEquals(allActualURLs, savedURLs);
        Assertions.assertEquals(allActualUsernams, SavedUsernames);
        
        // Verify displayed password on the table is encrypted and has the same encrypted value stored in databse
        Assertions.assertEquals(allActualEncryptedPasswords, SavedEncodedPasswords);
        
        
        /***********************************
         * Edit a credential at a chosen row
         *
         ***********************************/
               
        // Choose an arbitrary row number to edit its corresponding credential
         int rowNumber = 2; // the second credential is chosen
        
        // Define a new url username unencrypted-password for the chosen credential to be edited
        urls_username_password.set(rowNumber-1, new String[]{"https://www.msn.com/en-ca",
                                                              "Edited username",
                                                              "Edited password"});
        
        // List all actual url usernams unencrypted password after edit
         allActualURLs = urls_username_password.stream().map( elm -> elm[0]).collect(Collectors.toList());
         allActualUsernams = urls_username_password.stream().map( elm -> elm[1]).collect(Collectors.toList());
         allActualUnencryptedPasswords = urls_username_password.stream().map( elm -> elm[2]).collect(Collectors.toList());
         
        // Edit and save credential at the given row numer which corresponds to the credential id
        CredentialsPage.editCredentials(rowNumber, urls_username_password.get(rowNumber-1)[0], 
                                                   urls_username_password.get(rowNumber-1)[1], 
                                                   urls_username_password.get(rowNumber-1)[2]);
        
        // Check if success message displays after credential edit
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("credential is successfully edited !"));
        
        // Retrieve all urls from credentials table after edit
         savedURLs = CredentialsPage.getSavedCredentialsURLs();
        
        // Retrieve all usernams from credentials table after edit
         SavedUsernames = CredentialsPage.getSavedCredentialsUsernames();
        
        // Retrieve all encrypted passwords from credentials table after edit
         SavedEncodedPasswords = CredentialsPage.getSavedCredentialsEncodedPasswords();
         
        /************ Encode each actual password with the same encoded key stored in database **********/
        
        allActualEncryptedPasswords = new ArrayList<>();
        
        // Ids of created credentials remains unchanged after edit
        // Retrieve each encoded key stored in databsse and encode the actual passwords
          for (int id : Ids) {
              String keyEnc = CredentialsMapper.findCredentialByID(id).getKeyEnc();
              String encryptValue = EncryptionService.encryptValue(allActualUnencryptedPasswords.get(id - 1), keyEnc);
              allActualEncryptedPasswords.add(encryptValue);
          }
         
        /***********************************************************************/
        
        // Verify displayed credentials on the table after edit are updated as expected
        Assertions.assertEquals(urls_username_password.size(), CredentialsPage.numberOfCredentialsAdded());//Verify the number of created credentials
        Assertions.assertEquals(allActualURLs, savedURLs);
        Assertions.assertEquals(allActualUsernams, SavedUsernames);
        
        // Verify displayed password on the table after edit is encrypted and has the same encrypted value stored in databse
        Assertions.assertEquals(allActualEncryptedPasswords, SavedEncodedPasswords);

        /*************************************
         * Delete a credential at a chosen row
         *
         *************************************/
        
        // Choose an arbitrary row number to delete its corresponding credential
         rowNumber = 3; // the third credential is chosen
         
         urls_username_password.remove(rowNumber-1);
          
        // List all actual url username unencrypted-password after delete
        allActualURLs = urls_username_password.stream().map( elm -> elm[0]).collect(Collectors.toList());
        allActualUsernams = urls_username_password.stream().map( elm -> elm[1]).collect(Collectors.toList());
        allActualUnencryptedPasswords = urls_username_password.stream().map( elm -> elm[2]).collect(Collectors.toList());
        
        // Delete credential at the given row numer which corresponds to the credential id
        CredentialsPage.deleteCredentials(rowNumber);
        
        // Check if success message displays after credential delete
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("credential is successfully deleted !"));
        
        // Retrieve all urls from credentials table after delete
         savedURLs = CredentialsPage.getSavedCredentialsURLs();
        
        // Retrieve all usernams from credentials table after delete
         SavedUsernames = CredentialsPage.getSavedCredentialsUsernames();
        
        // Retrieve all encrypted passwords from credentials table after delete
         SavedEncodedPasswords = CredentialsPage.getSavedCredentialsEncodedPasswords();
         
        /************ Encode each actual password with the same encoded key stored in database **********/
            
         allActualEncryptedPasswords = new ArrayList<>();
        
        //Ids of created credentials after delete
          Ids = new Integer[]{1, 2};
          
        // Retrieve each encoded key stored in databsse and encode the actual passwords
          for (int id : Ids) {
              String keyEnc = CredentialsMapper.findCredentialByID(id).getKeyEnc();
              String encryptValue = EncryptionService.encryptValue(allActualUnencryptedPasswords.get(id - 1), keyEnc);
              allActualEncryptedPasswords.add(encryptValue);
          }
          
        /***********************************************************************/  
          
        // Verify displayed credentials on the table after delete are updated as expected
        Assertions.assertEquals(urls_username_password.size(), CredentialsPage.numberOfCredentialsAdded()); //Verify the number of created credentials
        Assertions.assertEquals(allActualURLs, savedURLs);
        Assertions.assertEquals(allActualUsernams, SavedUsernames);
        
        // Verify displayed password on the table after delete is encrypted and has the same encrypted value stored in databse
        Assertions.assertEquals(allActualEncryptedPasswords, SavedEncodedPasswords);
 
    }
}
