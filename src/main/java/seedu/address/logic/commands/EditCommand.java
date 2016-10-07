package seedu.address.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.DateFormatter;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Location;
import seedu.address.model.task.Name;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniquePersonList.DuplicatePersonException;
import seedu.address.model.task.UniquePersonList.PersonNotFoundException;

public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits a task in the scheduler. "
            + "Parameters: INDEX TASK s/START_DATE e/END_DATE a/LOCATION  [t/TAG]...\n"
            + "Example: " + COMMAND_WORD
            + " 1 MUST do CS2103 Pretut s/07102016 e/10102016 a/NUS COM1-B103 t/Priority";

    public static final String MESSAGE_SUCCESS = "Task edited: %1$s";
    
    public final int targetIndex;
    private final Task toCopy;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommand(int targetIndex, String name, String startDate, String endDate, String address, Set<String> tags)
            throws IllegalValueException {
 
        this.targetIndex = targetIndex;
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        this.toCopy = new Task(
                new Name(name),
                DateFormatter.convertStringToDate(startDate),
                DateFormatter.convertStringToDate(endDate),
                new Location(address),
                new UniqueTagList(tagSet)
        );
    }

    @Override
    public CommandResult execute() {
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredPersonList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        }

        ReadOnlyTask personToEdit = lastShownList.get(targetIndex - 1);

        try {
            model.editTask(personToEdit, toCopy);
        } catch (DuplicatePersonException dpe) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        } catch (PersonNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToEdit));
    }
}
