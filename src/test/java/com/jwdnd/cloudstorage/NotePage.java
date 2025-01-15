/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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


public class NotePage {
    
@FindBy(id="add-note-button")
private WebElement addNoteButton; 

@FindBy(name="noteTitle")
private WebElement noteTitleField;  
        
@FindBy(name="noteDescription")
private WebElement noteDescriptionField;   
        
@FindBy(id="submitNote")
private WebElement noteSubmitButton;

@FindBy(css = "tr.noteItems th")
private List<WebElement> allNoteTitles;

@FindBy(css = "td.description-note")
private List<WebElement> allNoteDescriptions;

@FindBy(css="tr.noteItems")
private List<WebElement> noteTableRows;

@FindBy(css="tr.noteItems button")
private List<WebElement> allNoteEditButtons;

@FindBy(css="tr.noteItems a")
private List<WebElement> allNoteDeleteButtons;

@FindBy(id="closeEditeModal")
private WebElement closeEditeModalButton;

private WebDriverWait webDriverWait;

WebDriver driver;

    public NotePage(WebDriver driver, WebDriverWait webDriverWait) {
        PageFactory.initElements(driver, this);
        this.webDriverWait = webDriverWait;
        this.driver= driver;

    }

    
    public void add_and_Save_Notes(List<String[]> titles_Descriptions) {
        
        for (String[] titleDescription : titles_Descriptions) {
                        
            //Click on add-note-button to create a note
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note-button")));
            addNoteButton.click();

            //Fill in the note titile field
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteTitle")));
            noteTitleField.sendKeys(titleDescription[0]);

            //Fill in the note description field
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteDescription")));
            noteDescriptionField.sendKeys(titleDescription[1]);

            //Attemp to save the note
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitNote")));
            noteSubmitButton.click();

        }
    }

    public List<String> getSavedNotesTitles() {
        
        webDriverWait.until(webDriver -> webDriver.findElement(By.cssSelector("tr.noteItems th")));
        List<String> titles = allNoteTitles.stream().map(note->note.getText()).collect(Collectors.toList());
        
        return titles;

    }

    public List<String> getSavedNoteDescriptions() {
        
        webDriverWait.until(webDriver -> webDriver.findElement(By.cssSelector("tr.noteItems th")));
        List<String> descriptions = allNoteDescriptions.stream().map(note->note.getText()).collect(Collectors.toList());

        return  descriptions;
    }
    
    
    public int numberOfNotesAdded() {

        return  noteTableRows.size();
    }
    
    public List<String> titles_Shown_On_Edit_ModalBox(){
    
                    
        List<String> titles= new ArrayList<>();
        
        int i = 1;
        for(WebElement editButton : allNoteEditButtons) {
        // Loop through each Edit button to display the modal box of corresponding note
        
            //click on Edit button to show modal box
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteEdit"+i)));
            editButton.click();
            
            i++;
          
            //Get the current title value
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteTitle")));
            titles.add(noteTitleField.getAttribute("value")); 
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("closeEditeModal")));
            
            //close the modal
            closeEditeModalButton.click();
        }
        return titles;
    
    }
    
    public List<String> description_Shown_On_Edit_ModalBox(){
    
        List<String> descriptions= new ArrayList<>();
        
        int i = 1;
        for(WebElement editButton : allNoteEditButtons) {
        // Loop through each Edit button to display the modal box of corresponding note
        
            //click on Edit button to show modal box
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteEdit"+i)));
            editButton.click();
            
            i++;
          
            //Get the current description value
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteDescription")));
            descriptions.add(noteDescriptionField.getAttribute("value"));
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("closeEditeModal")));
            
            //Close the modal
            closeEditeModalButton.click();
            
        
        }
        return descriptions;
    
    }
    
    public void edit_and_Save_Note(int rowNumber, String newTitle, String newDescription){
        
        
        //Press on Edit button located at the given row number ( id = "noteEdit"+rowNumber)
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteEdit"+rowNumber))); 
        WebElement noteEditButton = allNoteEditButtons.stream()
                                                      .filter(editButton -> editButton.getAttribute("id").equals("noteEdit"+rowNumber))
                                                      .findFirst().get();
        noteEditButton.click();

        //Edit note titile 
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteTitle")));
        noteTitleField.clear();
        noteTitleField.sendKeys(newTitle);

        //Edit note description 
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteDescription")));
        noteDescriptionField.clear();
        noteDescriptionField.sendKeys(newDescription); 
        
        //Attempt to save the edited note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitNote")));
        noteSubmitButton.click();
    
    }
    
    public void deleteNote(int rowNumber){
    
        //Press on Delete button located at the given row number ( id = "noteEdit"+rowNumber) 
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteDelete"+rowNumber)));
        WebElement noteEditButton = allNoteDeleteButtons.stream()
                                                        .filter(deleteButton -> deleteButton.getAttribute("id").equals("noteDelete"+rowNumber))
                                                        .findFirst().get();

        noteEditButton.click();
    }
}
