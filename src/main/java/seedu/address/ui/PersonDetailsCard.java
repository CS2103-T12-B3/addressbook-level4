package seedu.address.ui;

import java.io.File;
import java.io.FileInputStream;

import java.util.HashMap;
import java.util.Random;
import javafx.collections.ObservableList;

import com.google.common.eventbus.Subscribe;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.logic.Logic;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.ReadOnlyPerson;

public class PersonDetailsCard extends UiPart<Region> {

    private static final String FXML = "PersonDetailsCard.fxml";
    private static String[] colors = {"red", "yellow", "green", "blue", "pink",
            "grey", "orange", "brown", "purple", "magenta", "indigo"};
    private static HashMap<String, String> tagColors = new HashMap<>();
    private static Random random = new Random();

    private static final String BASE_DIR = System.getProperty("user.dir") + "/src/main/resources/images/";

    public ReadOnlyPerson person;

    @FXML
    private HBox cardPane;
    @FXML
    private GridPane grid;
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
        initPersonDetails(person);
        initTags(person);
        //bindListeners(person);
        initPhoto(person);
        initIcons();
    }

    /**
     * Binds the individual UI elements to observe their respective {@code Person} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners(ReadOnlyPerson person) {
        name.textProperty().bind(Bindings.convert(person.nameProperty()));
        phone.textProperty().bind(Bindings.convert(person.phoneProperty()));
        address.textProperty().bind(Bindings.convert(person.addressProperty()));
        email.textProperty().bind(Bindings.convert(person.emailProperty()));
        birthday.textProperty().bind(Bindings.convert(person.birthdayProperty()));
        person.tagProperty().addListener((observable, oldValue, newValue) -> {
            tags.getChildren().clear();
            initTags(person);
        });
    }

    /**
     * Initialise each image and assign a person a photo
    **/
    private void initPhoto(ReadOnlyPerson person) {
        try {
            File photoFile = new File(person.getPhoto().getFullPhotoDir());
            if (photoFile.exists()) {
                FileInputStream fileStream = new FileInputStream(photoFile);
                Image personPhoto = new Image(fileStream);
                photo = new ImageView(personPhoto);
                photo.setFitHeight(person.getPhoto().HEIGHT);
                photo.setFitWidth(person.getPhoto().WIDTH);
                cardPane.getChildren().add(photo);
            } else {
                File defaultPhotoFile = new File(person.getPhoto().getTemplatePhotoDir());
                FileInputStream defaultFileStream = new FileInputStream(defaultPhotoFile);
                Image defaultPersonPhoto = new Image(defaultFileStream);
                photo = new ImageView(defaultPersonPhoto);

                photo.setFitHeight(person.getPhoto().HEIGHT);
                photo.setFitWidth(person.getPhoto().WIDTH);
                cardPane.getChildren().add(photo);
            }
        } catch (Exception e) {
            System.out.println("Image not found in directory");
        }
    }

    private void initPersonDetails(ReadOnlyPerson person) {
        name.setText(person.getName().toString());
        //grid.getChildren().add(name);
    }

    /**
     * Initialise each tag and assign a unique color
     */
    private void initTags(ReadOnlyPerson person) {
        person.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.setStyle("-fx-background-color: " + getColorForTag(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    private static String getColorForTag(String tagValue) {
        if (!tagColors.containsKey(tagValue)) {
            tagColors.put(tagValue, colors[random.nextInt(colors.length)]);
        }

        return tagColors.get(tagValue);
    }

    private void initIcons() {
        int iconHeight = 50;
        int iconWidth = 50;
        try {
            initPhoneIcon(iconHeight, iconWidth);
            initAddressIcon(iconHeight, iconWidth);
            initEmailIcon(iconHeight, iconWidth);
            initBirthdayIcon(iconHeight, iconWidth);
        } catch (Exception e) {
            System.out.println("Icon not found in directory");
        }
    }

    private void initPhoneIcon(int iconHeight, int iconWidth) throws Exception {
        File photoFile = new File(BASE_DIR + "phone_icon.png");
        if (photoFile.exists()) {
            FileInputStream fileStream = new FileInputStream(photoFile);
            Image phoneIconPhoto = new Image(fileStream);
            phoneIcon = new ImageView(phoneIconPhoto);
            phoneIcon.setFitHeight(iconHeight);
            phoneIcon.setFitWidth(iconWidth);
            grid.getChildren().add(phoneIcon);
        } else {
            File defaultPhotoFile = new File(BASE_DIR + "defaultIcon");
            FileInputStream defaultFileStream = new FileInputStream(defaultPhotoFile);
            Image defaultPhoneIconPhoto = new Image(defaultFileStream);
            phoneIcon = new ImageView(defaultPhoneIconPhoto);
            phoneIcon.setFitHeight(iconHeight);
            phoneIcon.setFitWidth(iconWidth);
            grid.getChildren().add(phoneIcon);
        }
    }

    private void initAddressIcon(int iconHeight, int iconWidth) throws Exception {
        File photoFile = new File(BASE_DIR + "address_icon.png");
        if (photoFile.exists()) {
            FileInputStream fileStream = new FileInputStream(photoFile);
            Image addressIconPhoto = new Image(fileStream);
            addressIcon = new ImageView(addressIconPhoto);
            addressIcon.setFitHeight(iconHeight);
            addressIcon.setFitWidth(iconWidth);
            grid.getChildren().add(addressIcon);
        } else {
            File defaultPhotoFile = new File(BASE_DIR + "defaultIcon");
            FileInputStream defaultFileStream = new FileInputStream(defaultPhotoFile);
            Image defaultAddressIconPhoto = new Image(defaultFileStream);
            addressIcon = new ImageView(defaultAddressIconPhoto);
            addressIcon.setFitHeight(iconHeight);
            addressIcon.setFitWidth(iconWidth);
            grid.getChildren().add(addressIcon);
        }
    }

    private void initEmailIcon(int iconHeight, int iconWidth) throws Exception {
        File photoFile = new File(BASE_DIR + "email_icon.png");
        if (photoFile.exists()) {
            FileInputStream fileStream = new FileInputStream(photoFile);
            Image emailIconPhoto = new Image(fileStream);
            emailIcon = new ImageView(emailIconPhoto);
            emailIcon.setFitHeight(iconHeight);
            emailIcon.setFitWidth(iconWidth);
            grid.getChildren().add(emailIcon);
        } else {
            File defaultPhotoFile = new File(BASE_DIR + "defaultIcon");
            FileInputStream defaultFileStream = new FileInputStream(defaultPhotoFile);
            Image defaultEmailIconPhoto = new Image(defaultFileStream);
            emailIcon = new ImageView(defaultEmailIconPhoto);
            emailIcon.setFitHeight(iconHeight);
            emailIcon.setFitWidth(iconWidth);
            grid.getChildren().add(emailIcon);
        }
    }

    private void initBirthdayIcon(int iconHeight, int iconWidth) throws Exception {
        File photoFile = new File(BASE_DIR + "birthday_icon.png");
        if (photoFile.exists()) {
            FileInputStream fileStream = new FileInputStream(photoFile);
            Image birthdayIconPhoto = new Image(fileStream);
            birthdayIcon = new ImageView(birthdayIconPhoto);
            birthdayIcon.setFitHeight(iconHeight);
            birthdayIcon.setFitWidth(iconWidth);
            grid.getChildren().add(birthdayIcon);
        } else {
            File defaultPhotoFile = new File(BASE_DIR + "defaultIcon");
            FileInputStream defaultFileStream = new FileInputStream(defaultPhotoFile);
            Image defaultBirthdayIconPhoto = new Image(defaultFileStream);
            birthdayIcon = new ImageView(defaultBirthdayIconPhoto);
            birthdayIcon.setFitHeight(iconHeight);
            birthdayIcon.setFitWidth(iconWidth);
            grid.getChildren().add(birthdayIcon);
        }
    }

    private ReadOnlyPerson getFirstPersonInTheList(ObservableList<ReadOnlyPerson> personList) {
        return personList.get(0);
    }

    private void getSelectedPerson(ReadOnlyPerson person) {
        this.person = person;
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        //logger.info(LogsCenter.getEventHandlingLogMessage(event));
        getSelectedPerson(event.getNewSelection().person);
    }
}
