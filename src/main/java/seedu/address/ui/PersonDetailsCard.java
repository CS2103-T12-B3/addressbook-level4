//@@author aggarwalRuchir-unused
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
    //@@author

    /**
     * Binds the individual UI elements to observe their respective {@code Person} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners(ReadOnlyPerson person) {
        if (person != null) {
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

    //@@author aggarwalRuchir-unused
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
//@@author
