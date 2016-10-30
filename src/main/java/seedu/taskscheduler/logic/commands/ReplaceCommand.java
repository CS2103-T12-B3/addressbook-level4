package seedu.taskscheduler.logic.commands;

import seedu.taskscheduler.commons.core.Messages;
import seedu.taskscheduler.commons.exceptions.IllegalValueException;
import seedu.taskscheduler.model.task.Task;
import seedu.taskscheduler.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.taskscheduler.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0148145E

/**
 * Replaces a task in the task scheduler.
 */
public class ReplaceCommand extends Command {

    public static final String COMMAND_WORD = "replace";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Replaces a task in the scheduler. "
            + "Parameters: INDEX TASK from START_TIME_DATE to END_TIME_DATE at LOCATION \n"
            + "Example: " + COMMAND_WORD
            + " 1 Must Do CS2103 Pretut\n"
            + "Example: " + COMMAND_WORD
            + " 2 new task name from 8am 10-Oct-2016 to 9am 10-Oct-2016 at NUS\n"
            + "Example: " + COMMAND_WORD
            + " 1 another new task name from 8am 11-Oct-2016 to 9am 11-Oct-2016 at there\n";

    public static final String MESSAGE_SUCCESS = "Task replaced: %1$s";
    
    public final int targetIndex;
    private final Task newTask;
    private Task oldTask;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public ReplaceCommand(Task toReplace) {
        this(EMPTY_INDEX, toReplace);
    }
    
    public ReplaceCommand(int targetIndex, Task toReplace) {
        this.targetIndex = targetIndex;
        this.newTask = toReplace;
    }

    @Override
    public CommandResult execute() {
        try {
            oldTask = new Task(getTaskFromIndexOrLastModified(targetIndex));
            model.replaceTask(oldTask, newTask);
            CommandHistory.addExecutedCommand(this);
            CommandHistory.setModifiedTask(newTask);
        } catch (DuplicateTaskException dpe) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        } catch (IndexOutOfBoundsException iobe) { 
            return new CommandResult(iobe.getMessage());
        } catch (TaskNotFoundException tnfe) {
            return new CommandResult(tnfe.getMessage());
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, oldTask));
    }

    @Override
    public CommandResult revert() {
        try {
            model.replaceTask(newTask, oldTask);
            CommandHistory.addRevertedCommand(this);
            CommandHistory.setModifiedTask(oldTask);
        } catch (DuplicateTaskException e) {
            assert false : Messages.MESSAGE_TASK_CANNOT_BE_DUPLICATED;
        } catch (TaskNotFoundException e) {
            assert false : Messages.MESSAGE_TASK_CANNOT_BE_MISSING;
        }
        return new CommandResult(String.format(MESSAGE_REVERT_COMMAND, COMMAND_WORD, "\n" + newTask));
    }
}
