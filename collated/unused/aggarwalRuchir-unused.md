# aggarwalRuchir-unused
###### /java/seedu/address/ui/LoginWindow.java
``` java
package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FxViewUtil;


/**
 * Controller for a login dialogue
 */
public class LoginWindow extends UiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(LoginWindow.class);
    private static final String ICON = "/images/login_icon.png";
    private static final String FXML = "LoginWindow.fxml";
    private static final String TITLE = "Login";

    private boolean okClicked = false;

    @FXML
    private WebView browser;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    private final Stage dialogStage;

    public LoginWindow() {
        super(FXML);
        Scene scene = new Scene(getRoot());
        //Null passed as the parent stage to make it non-modal.
        dialogStage = createDialogStage(TITLE, null, scene);
        dialogStage.setMaximized(false); //TODO: set a more appropriate initial size
        FxViewUtil.setStageIcon(dialogStage, ICON);
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     * <ul>
     *     <li>
     *         if this method is called on a thread other than the JavaFX Application Thread.
     *     </li>
     *     <li>
     *         if this method is called during animation or layout processing.
     *     </li>
     *     <li>
     *         if this method is called on the primary stage.
     *     </li>
     *     <li>
     *         if {@code dialogStage} is already showing.
     *     </li>
     * </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        dialogStage.showAndWait();
    }


    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (performLoginAttempt()) {
            okClicked = true;
            dialogStage.close();
        }
    }
    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


    /**
     * Sets the login dialogue style to use the default style.
     */
    private void setStyleToDefault() {
        //TODO - restore if user restarts entering details
        ;
    }

    /**
     * Sets the login dialogue style to indicate a failed login
     */
    private void setMotionToindicateLoginFailure() {
        //TODO - change look/shake of dialog if user enters wrong details
        ;
    }

    /**
     * Performs a check whether the username and password entered by the user are correct or not
     * @return true if log in details are correct, else false
     */
    public boolean performLoginAttempt() {
        boolean loginAttemptBool = false;
        try {
            String errorMessage = "";
            String correctUsername = "admin";
            String correctPassword = "password";
            String enteredUsername = usernameTextField.getText().toString();
            String enteredPassword = passwordTextField.getText().toString();

            logger.info("username entered:" + enteredUsername);
            logger.info("password entered:" + enteredPassword);

            if ((enteredUsername.equals(correctUsername)) && (enteredPassword.equals(correctPassword))) {
                //TODO - send event for closing the login dialogue
                loginAttemptBool = true;
            } else {
                //TODO - In case wrong details entered
                errorMessage += "Incorrect username or password. Please enter correct details to start the app.";
            }

            if (errorMessage.length() != 0) {
                // Show the error message.
                Alert alert = new Alert(AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("Incorrect Login");
                alert.setHeaderText("Error");
                alert.setContentText(errorMessage);

                alert.showAndWait();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //TODO - Any exceptions?
        }

        return loginAttemptBool;
    }
}
```
###### /java/seedu/address/ui/MainWindow.java
``` java
    /**
     * Opens the Login window.
     */
    @FXML
    public void handleLogin() {
        logger.info("Login: Enter username and password to open address book");
        LoginWindow loginWindow = new LoginWindow();
        loginWindow.show();
    }
```
###### /java/seedu/address/ui/PersonDetailsCard.java
``` java
package seedu.address.ui;

import java.io.File;
import java.io.FileInputStream;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.Random;

import com.google.common.eventbus.Subscribe;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.ReadOnlyPerson;

public class PersonDetailsCard extends UiPart<Region> {

    private static final String ADDRESS_ICON_IMAGE_NAME = "address_icon.png";
    private static final String BIRTHDAY_ICON_IMAGE_NAME = "birthday_icon.png";
    private static final String EMAIL_ICON_IMAGE_NAME = "email_icon.png";
    private static final String PHOTO_ICON_IMAGE_NAME = "phone_icon.png";

    private static final String FXML = "PersonDetailsCard.fxml";
    private static String[] colors = {"red", "green", "blue", "pink",
            "grey", "orange", "brown", "purple", "magenta", "indigo"};
    private static HashMap<String, String> tagColors = new HashMap<>();
    private static Random random = new Random();

    private static final String BASE_DIR = System.getProperty("user.dir") + "/src/main/resources/images/";
    private final Logger logger = LogsCenter.getLogger(this.getClass());

    public ReadOnlyPerson person;

    @FXML
    private VBox personPanel;
    @FXML
    private HBox phoneBox;
    @FXML
    private HBox addressBox;
    @FXML
    private HBox emailBox;
    @FXML
    private HBox birthdayBox;
    @FXML
    private HBox tagsBox;
    @FXML
    private ImageView photo;
    @FXML
    private Label name;
    @FXML
    private ImageView phoneIcon;
    @FXML
    private Label phone;
    @FXML
    private ImageView addressIcon;
    @FXML
    private Label address;
    @FXML
    private ImageView emailIcon;
    @FXML
    private Label email;
    @FXML
    private ImageView birthdayIcon;
    @FXML
    private Label birthday;
    @FXML
    private FlowPane tags;


    public PersonDetailsCard(ObservableList<ReadOnlyPerson> personList) {
        super(FXML);
        this.person = getFirstPersonInTheList(personList);
        if (person != null) {
            initPhoto(person);
            initIcons();
            bindListeners(person);
            initTags(person);
        } else {
            initIcons();
            bindListeners(null);
        }
        registerAsAnEventHandler(this);
    }
```
###### /java/seedu/address/ui/PersonDetailsCard.java
``` java
    /**
     * Initialise each tag and assign a unique color
     */
    private void initTags(ReadOnlyPerson person) {
        tags.getChildren().clear();
        person.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.setStyle("-fx-background-color: " + getColorForTag(tag.tagName));
            tags.getChildren().add(tagLabel);

        });
    }

    /**
     * returns a random color for the tag
     */
    private static String getColorForTag(String tagValue) {
        if (!tagColors.containsKey(tagValue)) {
            tagColors.put(tagValue, colors[random.nextInt(colors.length)]);
        }

        return tagColors.get(tagValue);
    }

    /**
     * Initialises images for the various fields about the persons like phone, address etc
     */
    private void initIcons() {
        try {
            initFieldIcon( PHOTO_ICON_IMAGE_NAME, phoneIcon);
            initFieldIcon( ADDRESS_ICON_IMAGE_NAME, addressIcon);
            initFieldIcon( EMAIL_ICON_IMAGE_NAME, emailIcon);
            initFieldIcon( BIRTHDAY_ICON_IMAGE_NAME, birthdayIcon);
        } catch (Exception e) {
            System.out.println("Icon not found in directory");
        }
    }

    /**
     * Initialises image icon for a particular field
     * @param filename name of the icon image file name
     * @param fieldImageView name of the image view to which it corresponds to
     * @throws Exception FileNotFoundException() if file not found
     */
    private void initFieldIcon(String filename, ImageView fieldImageView) {
        try {
            File photoFile = new File(BASE_DIR + filename);
            if (photoFile.exists()) {
                Image iconPhoto = returnIconImage(photoFile);
                fieldImageView.imageProperty().setValue(iconPhoto);
            } else {
                Image defaultPhoneIconPhoto = returnDefaultIconImage();
                fieldImageView.imageProperty().setValue(defaultPhoneIconPhoto);
            }
        } catch (Exception e) {
            System.out.println("Icon image not found in directory");
        }
    }

    /**
     * returns the icon image file for a particular field like phone, address etc
     * @param photoFile name of the icon image file
     * @throws Exception FileNotFoundException() if file not found
     */
    private Image returnIconImage(File photoFile) throws Exception {
        FileInputStream fileStream = new FileInputStream(photoFile);
        return new Image(fileStream);
    }

    /**
     * returns the default icon image in case the icon for a field is not found
     * @throws Exception FileNotFoundException() if file not found
     */
    private Image returnDefaultIconImage() throws Exception {
        File defaultPhotoFile = new File(BASE_DIR + "defaultIcon");
        FileInputStream defaultFileStream = new FileInputStream(defaultPhotoFile);
        return new Image(defaultFileStream);
    }

    /**
     * returns the first person in the person list of the address book
     * @return
     */
    private ReadOnlyPerson getFirstPersonInTheList(ObservableList<ReadOnlyPerson> personList) {
        if (!personList.isEmpty()) {
            return personList.get(0);
        }
            return null;
    }

    /**
     * Updates the person details card with details of the input person
     */
    private void updatePersonDetailsCard(ReadOnlyPerson person) {
        this.person = person;
        initPhoto(person);
        bindListeners(person);
        initTags(person);
        registerAsAnEventHandler(this);
    }


    /**
     * returns true if the address book is empty
     */
    private boolean isAddressbookEmpty(AddressBookChangedEvent event) {
        return event.data.getPersonList().isEmpty();
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        updatePersonDetailsCard(event.getNewSelection().person);
    }

    @Subscribe
    private void handleAddressBookChangedEvent(AddressBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (!isAddressbookEmpty(event)) {
            updatePersonDetailsCard(event.data.getPersonList().get(0));
        }
    }

}
```
###### /resources/view/LoginWindow.fxml
``` fxml
<AnchorPane maxHeight="170.0" maxWidth="400.0" prefHeight="170.0" prefWidth="400.0" xmlns="http://javafx.com/javafx/8.0.111" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <Label layoutX="14.0" layoutY="14.0" prefHeight="34.0" prefWidth="372.0" text="Address App requires the username and password to open" AnchorPane.bottomAnchor="134.0" AnchorPane.leftAnchor="14.0" AnchorPane.rightAnchor="14.0" AnchorPane.topAnchor="14.0" />
      <TextField fx:id="usernameTextField" layoutX="26.0" layoutY="62.0" prefHeight="27.0" prefWidth="372.0" promptText="Username..." style="-fx-border-width: 0;" AnchorPane.bottomAnchor="93.0" AnchorPane.leftAnchor="14.0" AnchorPane.rightAnchor="14.0" AnchorPane.topAnchor="62.0" />
      <TextField fx:id="passwordTextField" layoutX="26.0" layoutY="102.0" prefHeight="27.0" prefWidth="372.0" promptText="Password..." AnchorPane.bottomAnchor="53.0" AnchorPane.leftAnchor="14.0" AnchorPane.rightAnchor="14.0" AnchorPane.topAnchor="102.0" />
      <Button layoutX="242.0" layoutY="141.0" minWidth="61.0" mnemonicParsing="false" onAction="#handleOk" text="OK" />
      <Button layoutX="321.0" layoutY="141.0" mnemonicParsing="false" onAction="#handleCancel" text="Cancel" />
   </children>
</AnchorPane>
```
###### /resources/view/PersonDetailsCard.fxml
``` fxml
<VBox fx:id="personPanel" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="600.0" prefWidth="300.0" xmlns="http://javafx.com/javafx/8.0.111" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <ImageView fx:id="photo" fitHeight="200.0" fitWidth="200.0" pickOnBounds="true" preserveRatio="true">
         <VBox.margin>
            <Insets left="50.0" top="20.0" />
         </VBox.margin>
         <viewport>
            <Rectangle2D />
         </viewport>
      </ImageView>
      <Label fx:id="name" alignment="CENTER" prefHeight="26.0" prefWidth="200.0" text="Name" textAlignment="CENTER">
         <VBox.margin>
            <Insets bottom="10.0" left="50.0" top="10.0" />
         </VBox.margin>
         <font>
            <Font size="23.0" />
         </font>
      </Label>
      <Line endX="180.0" startX="-100.0" strokeWidth="0.5">
         <VBox.margin>
            <Insets left="10.0" right="10.0" />
         </VBox.margin>
      </Line>
      <HBox fx:id="phoneBox" prefHeight="40.0" prefWidth="288.0">
         <children>
            <ImageView fx:id="phoneIcon" fitHeight="40.0" fitWidth="40.0" pickOnBounds="true" preserveRatio="true" />
            <Label fx:id="phone" prefWidth="480.0" text="Phone">
               <padding>
                  <Insets left="10.0" />
               </padding>
               <font>
                  <Font size="16.0" />
               </font>
               <HBox.margin>
                  <Insets top="10.0" />
               </HBox.margin>
            </Label>
         </children>
         <VBox.margin>
            <Insets bottom="5.0" left="10.0" right="10.0" top="5.0" />
         </VBox.margin>
         <padding>
            <Insets bottom="5.0" left="10.0" top="5.0" />
         </padding>
      </HBox>
      <Line endX="180.0" startX="-100.0" stroke="#323232" strokeWidth="0.5">
         <VBox.margin>
            <Insets left="10.0" right="10.0" />
         </VBox.margin>
      </Line>
      <HBox fx:id="addressBox" prefHeight="40.0" prefWidth="288.0">
         <children>
            <ImageView fx:id="addressIcon" fitHeight="40.0" fitWidth="40.0" pickOnBounds="true" preserveRatio="true" />
            <Label fx:id="address" prefWidth="380.0" text="Address">
               <padding>
                  <Insets left="10.0" />
               </padding>
               <font>
                  <Font size="16.0" />
               </font>
               <HBox.margin>
                  <Insets top="10.0" />
               </HBox.margin>
            </Label>
         </children>
         <VBox.margin>
            <Insets bottom="5.0" left="10.0" right="10.0" top="5.0" />
         </VBox.margin>
         <padding>
            <Insets bottom="5.0" left="10.0" top="5.0" />
         </padding>
      </HBox>
      <Line endX="180.0" startX="-100.0" strokeWidth="0.5">
         <VBox.margin>
            <Insets left="10.0" right="10.0" />
         </VBox.margin>
      </Line>
      <HBox fx:id="emailBox" prefHeight="40.0" prefWidth="276.0">
         <children>
            <ImageView fx:id="emailIcon" fitHeight="40.0" fitWidth="40.0" pickOnBounds="true" preserveRatio="true" />
            <Label fx:id="email" prefWidth="380.0" text="Email">
               <padding>
                  <Insets left="10.0" />
               </padding>
               <font>
                  <Font size="16.0" />
               </font>
               <HBox.margin>
                  <Insets top="10.0" />
               </HBox.margin>
            </Label>
         </children>
         <VBox.margin>
            <Insets bottom="5.0" left="10.0" right="10.0" top="5.0" />
         </VBox.margin>
         <padding>
            <Insets bottom="5.0" left="10.0" top="5.0" />
         </padding>
      </HBox>
      <Line endX="180.0" startX="-100.0" strokeWidth="0.5">
         <VBox.margin>
            <Insets left="10.0" right="10.0" />
         </VBox.margin>
      </Line>
      <HBox fx:id="birthdayBox" prefHeight="40.0" prefWidth="328.0">
         <children>
            <ImageView fx:id="birthdayIcon" fitHeight="40.0" fitWidth="40.0" pickOnBounds="true" preserveRatio="true" />
            <Label fx:id="birthday" prefWidth="380.0" text="Birthday">
               <font>
                  <Font size="16.0" />
               </font>
               <padding>
                  <Insets left="10.0" />
               </padding>
               <HBox.margin>
                  <Insets top="10.0" />
               </HBox.margin>
            </Label>
         </children>
         <VBox.margin>
            <Insets bottom="5.0" left="10.0" right="10.0" top="5.0" />
         </VBox.margin>
         <padding>
            <Insets bottom="5.0" left="10.0" top="5.0" />
         </padding>
      </HBox>
      <Line endX="180.0" startX="-100.0" strokeWidth="0.5">
         <VBox.margin>
            <Insets left="10.0" right="10.0" />
         </VBox.margin>
      </Line>
      <HBox fx:id="tagsBox" prefHeight="40.0" prefWidth="300.0">
         <children>
            <Label fx:id="taglabel" prefHeight="29.0" prefWidth="51.0" text="Tags">
               <font>
                  <Font size="16.0" />
               </font>
               <padding>
                  <Insets right="10.0" />
               </padding>
               <HBox.margin>
                  <Insets />
               </HBox.margin>
            </Label>
            <FlowPane fx:id="tags" prefHeight="100.0" prefWidth="200.0">
               <HBox.margin>
                  <Insets top="5.0" />
               </HBox.margin>
            </FlowPane>
         </children>
         <VBox.margin>
            <Insets bottom="5.0" left="10.0" right="10.0" top="5.0" />
         </VBox.margin>
         <padding>
            <Insets bottom="5.0" left="10.0" top="5.0" />
         </padding>
      </HBox>
   </children>
</VBox>
```
