package com.jwdnd.cloudstorage;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CredentialsPage {

    private final WebDriverWait webDriverWait;
    
    @FindBy(id="add-cred-button")
    private WebElement addCredButton;
    
    @FindBy(name= "url")
    private WebElement urlField;
    
    @FindBy(name= "username")
    private WebElement usernameField;  
    
    @FindBy(name= "password")
    private WebElement passwordField;
    
    @FindBy(id="credSubmit")
    private WebElement credSubmitButton;
    
    @FindBy(css = "tr.CredItems th")
    private List<WebElement> allCredURLs;
    
    @FindBy(id="credUsername")
    private List<WebElement> allCredusernames;
    
    @FindBy(id="encoded-password")
    private List<WebElement> allCredEncodedPassword;
    
    @FindBy(css = "td.CredEditButtom a")
    private List<WebElement> allCredDeleteButtons;
    
    @FindBy(css = "td.CredEditButtom button")
    private List<WebElement> allCredEditButtons;
    
    @FindBy(className = "CredItems")
    private List<WebElement> credTableRows;
  
    @FindBy(id="closeCredEditeModal")
    private WebElement closeEditeModalButton;
    
    public CredentialsPage(WebDriver driver, WebDriverWait webDriverWait) {
        PageFactory.initElements(driver, this);
        this.webDriverWait = webDriverWait;

    }

    
    public void AddCredentials(List<String[]> url_username_password) {
        
        for (String[] urlUsernamePassword : url_username_password) {
                        
            //Click on add-cred-button to create a cred
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-cred-button")));
            addCredButton.click();

            //Fill in the credential url 
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("url")));
            urlField.sendKeys(urlUsernamePassword[0]);

            //Fill in the credential username 
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
            usernameField.sendKeys(urlUsernamePassword[1]);
            
            //Fill in the credential password 
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
            passwordField.sendKeys(urlUsernamePassword[2]);

            //Attemp to save the credential
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credSubmit")));
            credSubmitButton.click();

        }
    }

    public List<String> URLs_Shown_On_Edit_ModalBox() {
    
                    
        List<String> URLs= new ArrayList<>();
        
        int i = 1;
        for(WebElement editButton : allCredEditButtons) {
        // Loop through each Edit button to display the modal box of corresponding credential
        
            //click on Edit button to show modal box
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credEdit"+i)));
            editButton.click();
            
            i++;
          
            //Get the current title value
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("url")));
            URLs.add(urlField.getAttribute("value")); 
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("closeCredEditeModal")));
            
            //close the modal
            closeEditeModalButton.click();
        }
        return URLs;
    
    }
    
    public List<String> usernams_Shown_On_Edit_ModalBox(){
    
        List<String> usernams= new ArrayList<>();
        
        int i = 1;
        for(WebElement editButton : allCredEditButtons) {
        // Loop through each Edit button to display the modal box of corresponding credential
        
            //click on Edit button to show modal box
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credEdit"+i)));
            editButton.click();
            
            i++;
          
            //Get the current description value
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
            usernams.add(usernameField.getAttribute("value"));
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("closeCredEditeModal")));
            
            //Close the modal
            closeEditeModalButton.click();
            
        
        }
        return usernams;   
    }
    
    public List<String> password_Shown_On_Edit_ModalBox(){
    
        List<String> password= new ArrayList<>();
        
        int i = 1;
        for(WebElement editButton : allCredEditButtons) {
        // Loop through each Edit button to display the modal box of corresponding credential
        
            //click on Edit button to show modal box
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credEdit"+i)));
            editButton.click();
            
            i++;
          
            //Get the current description value
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
            password.add(passwordField.getAttribute("value"));
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("closeCredEditeModal")));
            
            //Close the modal
            closeEditeModalButton.click();
        }
        return password;
    
    }
        
    
    public List<String> getSavedCredentialsURLs() {
        
        webDriverWait.until(webDriver -> webDriver.findElement(By.cssSelector("tr.CredItems")));
        List<String> titles = allCredURLs.stream().map(cred->cred.getText()).collect(Collectors.toList());
        
        return titles;

    }

    public List<String> getSavedCredentialsUsernames() {
        
        webDriverWait.until(webDriver -> webDriver.findElement(By.cssSelector("tr.CredItems")));
        List<String> usernames = allCredusernames.stream().map(cred->cred.getText()).collect(Collectors.toList());

        return  usernames;
    }
    
    public List<String> getSavedCredentialsEncodedPasswords() {
        
        webDriverWait.until(webDriver -> webDriver.findElement(By.cssSelector("tr.CredItems")));
        List<String> usernames = allCredEncodedPassword.stream().map(cred->cred.getText()).collect(Collectors.toList());

        return  usernames;
    }
    
    public int numberOfCredentialsAdded() {

        return  credTableRows.size();
    }
    
    public void editCredentials(int rowNumber, String newURL, String newUsername, String newDecryptedPassword ){
            
        //Press on Edit button located at the given row number ( id = "credEdit"+rowNumber)
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credEdit"+rowNumber))); 
        WebElement credEditButton = allCredEditButtons.stream()
                                                      .filter(editButton -> editButton.getAttribute("id").equals("credEdit"+rowNumber))
                                                      .findFirst().get();
        credEditButton.click();

        //Edit credential url 
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("url")));
        urlField.clear();
        urlField.sendKeys(newURL);

        //Edit credential username 
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.clear();
        usernameField.sendKeys(newUsername); 
        
        //Edit credential password 
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
        passwordField.clear();
        passwordField.sendKeys(newDecryptedPassword); 
        
        //Attemp to save the edited credential
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credSubmit")));
        credSubmitButton.click();
    
    }
    
    public void deleteCredentials(int rowNumber){
    
        //Press on Delete button located at the given row number ( id = "credEdit"+rowNumber) 
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credDelete"+rowNumber)));
        WebElement credEditButton = allCredDeleteButtons.stream()
                                                        .filter(deleteButton -> deleteButton.getAttribute("id").equals("credDelete"+rowNumber))
                                                        .findFirst().get();

        credEditButton.click();
    }
    
}
