package seedu.address.testutil;

import java.util.Date;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.DateFormatter;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.*;

/**
 *
 */
public class TaskBuilder {

    private TestTask person;

    public TaskBuilder() {
        this.person = new TestTask();
    }

    public TaskBuilder withName(String name) throws IllegalValueException {
        this.person.setName(new Name(name));
        return this;
    }

    public TaskBuilder withTags(String ... tags) throws IllegalValueException {
        for (String tag: tags) {
            person.getTags().add(new Tag(tag));
        }
        return this;
    }

    public TaskBuilder withAddress(String address) throws IllegalValueException {
        this.person.setAddress(new Location(address));
        return this;
    }

    public TaskBuilder withStartDate(String startDate) throws IllegalValueException {
        this.person.setStartDate(DateFormatter.convertStringToDate(startDate));
        return this;
    }

    public TaskBuilder withEndDate(String endDate) throws IllegalValueException {
        this.person.setEndDate(DateFormatter.convertStringToDate(endDate));
        return this;
    }

    public TestTask build() {
        return this.person;
    }

}
