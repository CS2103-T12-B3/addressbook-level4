package seedu.address.ui;

import java.io.File;
import java.io.FileInputStream;

import java.util.HashMap;
import java.util.Random;
import javafx.collections.ObservableList;

import java.util.logging.Logger;
import com.google.common.eventbus.Subscribe;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.ReadOnlyPerson;

import javafx.scene.shape.Circle;

public class PersonDetailsCard extends UiPart<Region> {

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
        this.person = personList.get(0);
        initPhoto(person);
        initIcons();
        bindListeners(person);
        initTags(person);
        registerAsAnEventHandler(this);
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
                final Circle clip = new Circle(100, 72, 72);
                photo.setClip(clip);
                photo.imageProperty().set(personPhoto);
            } else {
                File defaultPhotoFile = new File(person.getPhoto().getTemplatePhotoDir());
                FileInputStream defaultFileStream = new FileInputStream(defaultPhotoFile);
                Image defaultPersonPhoto = new Image(defaultFileStream);
                final Circle clip = new Circle(100, 72, 72);
                photo.setClip(clip);
                photo.imageProperty().set(defaultPersonPhoto);
            }
        } catch (Exception e) {
            System.out.println("Image not found in directory");
        }
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
        try {
            initPhoneIcon();
            initAddressIcon();
            initEmailIcon();
            initBirthdayIcon();
        } catch (Exception e) {
            System.out.println("Icon not found in directory");
        }
    }

    private void initPhoneIcon() throws Exception {
        File photoFile = new File(BASE_DIR + "phone_icon.png");
        if (photoFile.exists()) {
            FileInputStream fileStream = new FileInputStream(photoFile);
            Image phoneIconPhoto = new Image(fileStream);
            phoneIcon.imageProperty().setValue(phoneIconPhoto);
        } else {
            File defaultPhotoFile = new File(BASE_DIR + "defaultIcon");
            FileInputStream defaultFileStream = new FileInputStream(defaultPhotoFile);
            Image defaultPhoneIconPhoto = new Image(defaultFileStream);
            phoneIcon.imageProperty().setValue(defaultPhoneIconPhoto);
        }
    }

    private void initAddressIcon() throws Exception {
        File photoFile = new File(BASE_DIR + "address_icon.png");
        if (photoFile.exists()) {
            FileInputStream fileStream = new FileInputStream(photoFile);
            Image addressIconPhoto = new Image(fileStream);
            addressIcon.imageProperty().setValue(addressIconPhoto);
        } else {
            File defaultPhotoFile = new File(BASE_DIR + "defaultIcon");
            FileInputStream defaultFileStream = new FileInputStream(defaultPhotoFile);
            Image defaultAddressIconPhoto = new Image(defaultFileStream);
            addressIcon.imageProperty().setValue(defaultAddressIconPhoto);
        }
    }

    private void initEmailIcon() throws Exception {
        File photoFile = new File(BASE_DIR + "email_icon.png");
        if (photoFile.exists()) {
            FileInputStream fileStream = new FileInputStream(photoFile);
            Image emailIconPhoto = new Image(fileStream);
            emailIcon.imageProperty().setValue(emailIconPhoto);
        } else {
            File defaultPhotoFile = new File(BASE_DIR + "defaultIcon");
            FileInputStream defaultFileStream = new FileInputStream(defaultPhotoFile);
            Image defaultEmailIconPhoto = new Image(defaultFileStream);
            emailIcon.imageProperty().setValue(defaultEmailIconPhoto);
        }
    }

    private void initBirthdayIcon() throws Exception {
        File photoFile = new File(BASE_DIR + "birthday_icon.png");
        if (photoFile.exists()) {
            FileInputStream fileStream = new FileInputStream(photoFile);
            Image birthdayIconPhoto = new Image(fileStream);
            birthdayIcon.imageProperty().setValue(birthdayIconPhoto);
        } else {
            File defaultPhotoFile = new File(BASE_DIR + "defaultIcon");
            FileInputStream defaultFileStream = new FileInputStream(defaultPhotoFile);
            Image defaultBirthdayIconPhoto = new Image(defaultFileStream);
            birthdayIcon.imageProperty().setValue(defaultBirthdayIconPhoto);
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
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        getSelectedPerson(event.getNewSelection().person);
    }
}
