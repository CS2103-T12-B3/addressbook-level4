# wshijing
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
        public void setPhoto(Photo photo) {
            this.photo = photo;
        }
```
###### \java\seedu\address\logic\commands\LocateCommand.java
``` java
package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Locates a person address identified using it's last displayed index from the address book.
 */
public class LocateCommand extends Command {

    public static final String COMMAND_WORD = "locate";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Locates the person address identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_LOCATE_PERSON_SUCCESS = "Located Person: %1$s";

    private final Index targetIndex;

    public LocateCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_LOCATE_PERSON_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LocateCommand // instanceof handles nulls
                && this.targetIndex.equals(((LocateCommand) other).targetIndex)); // state check
    }
}
```
###### \java\seedu\address\logic\parser\LocateCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.LocateCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new LocateCommand object
 */
public class LocateCommandParser implements Parser<LocateCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the LocateCommand
     * and returns an LocateCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public LocateCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new LocateCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, LocateCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\address\model\person\Person.java
``` java
    public void setPhoto(Photo photo) {
        this.photo.set(requireNonNull(photo));
    }

    @Override
    public ObjectProperty<Photo> photoProperty() {
        return photo;
    }

    @Override
    public Photo getPhoto() {
        return photo.get();
    }
```
###### \java\seedu\address\model\person\Photo.java
``` java
package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents the directory to a person's photo in the address book.
 */
public class Photo {

    public static final String MESSAGE_PHOTO_CONSTRAINTS = "Person's photo should be in format: nameOfFile.png";

    public static final String PHOTO_VALIDATION_REGEX = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    public static final int HEIGHT = 100;
    public static final int WIDTH = 75;

    public static final String BASE_DIR = System.getProperty("user.dir") + "/src/main/resources/person_photos/";

    private String photoDir;

    private String defaultPhoto = "template.png";

    /**
     * Validates given photo. If the parameter {@code photoDir} is Empty, uses the default photo.
     *
     * @throws IllegalValueException if given photo address string is invalid.
     */
    public Photo(Optional<String> photoDir) throws IllegalValueException {
        requireNonNull(photoDir);
        if (photoDir.isPresent()) {
            String trimmedPhoto = photoDir.get().trim();
            if (!isValidPhoto(trimmedPhoto)) {
                throw new IllegalValueException(MESSAGE_PHOTO_CONSTRAINTS);
            }
            this.photoDir = trimmedPhoto;
        } else {
            this.photoDir = defaultPhoto;
        }
    }

    public String getPhotoDir() {
        return photoDir;
    }

    public String getFullPhotoDir() {
        return BASE_DIR + photoDir;
    }

    public String getTemplatePhotoDir() {
        return BASE_DIR + defaultPhoto;
    }

    /**
     * Returns if a given string is a valid person photo.
     */
    public static boolean isValidPhoto(String test) {
        return test.matches(PHOTO_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return photoDir;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Photo // instanceof handles nulls
                && this.photoDir.equals(((Photo) other).photoDir)); // state check
    }

    @Override
    public int hashCode() {
        return photoDir.hashCode();
    }

}
```
###### \java\seedu\address\ui\MapsPanel.java
``` java
package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * The Maps Panel of the App.
 */
public class MapsPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String MAPS_DIR_URL_PREFIX = "https://www.google.com/maps/dir/?api=1";
    public static final String MAPS_DEFAULT_ORIGIN = "&origin=My+Location";
    public static final String MAPS_DEST_PREFIX = "&destination=";
    public static final String MAPS_SEARCH_ORIGIN = "&query=My+Location";
    public static final String MAPS_SEARCH_URL_PREFIX = "https://www.google.com/maps/search/?api=1";
    public static final String MAPS_SEARCH_URL_SUFFIX = "&dg=dbrw&newdg=1";

    private static final String FXML = "MapsPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private WebView maps;

    public MapsPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
    }


    private void loadPersonPage(ReadOnlyPerson person) {
        loadPage(MAPS_DIR_URL_PREFIX + MAPS_DEFAULT_ORIGIN + MAPS_DEST_PREFIX
                + person.getAddress().value.replaceAll(" ", "+") + MAPS_SEARCH_URL_SUFFIX);
    }

    public void loadPage(String url) {
        Platform.runLater(() -> maps.getEngine().load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        loadPage(MAPS_SEARCH_URL_PREFIX + MAPS_SEARCH_ORIGIN + MAPS_SEARCH_URL_SUFFIX);
    }

    /**
     * Frees resources allocated to the maps.
     */
    public void freeResources() {
        maps = null;
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection().person);
    }
}
```
###### \java\seedu\address\ui\PersonCard.java
``` java
    /**
     * Initialise each image and assign a person a photo
     */
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

```
###### \resources\view\MapsPanel.fxml
``` fxml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.layout.StackPane?>
<?import javafx.scene.web.WebView?>

<StackPane xmlns:fx="http://javafx.com/fxml/1">
    <WebView fx:id="maps"/>
</StackPane>
```
