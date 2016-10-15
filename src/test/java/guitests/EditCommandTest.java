package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.Command;
import seedu.address.commons.core.Messages;
import seedu.address.testutil.TestTask;
import seedu.address.testutil.TestUtil;


public class EditCommandTest extends TaskSchedulerGuiTest {

    @Test
    public void edit() {
        //edit a task
        TestTask[] currentList = td.getTypicalTasks();
        int indexToEdit = 1;
        TestTask taskToCopy = td.hoon;
        assertEditSuccess(indexToEdit, taskToCopy, currentList);
        currentList[indexToEdit - 1] = taskToCopy;
        currentList = TestUtil.addTasksToList(currentList);
        
        //edit another task
        taskToCopy = td.ida;
        indexToEdit = 3;
        assertEditSuccess(indexToEdit, taskToCopy, currentList);
        currentList[indexToEdit - 1] = taskToCopy;
        currentList = TestUtil.addTasksToList(currentList);

        //edit with a duplicate task
        indexToEdit = 5;
        commandBox.runCommand("edit " + indexToEdit + " " + td.hoon.getTaskString());
        assertResultMessage(Command.MESSAGE_DUPLICATE_TASK);
        assertTrue(taskListPanel.isListMatching(currentList));

        //edit in empty list
        commandBox.runCommand("clear");
        commandBox.runCommand("edit " + indexToEdit + " " + td.hoon.getTaskString());
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);

        //invalid command
        commandBox.runCommand("edit eee " + td.ida.getTaskString());
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    private void assertEditSuccess(int indexToEdit, TestTask taskToCopy, TestTask... currentList) {
        commandBox.runCommand("edit " + indexToEdit + " " + taskToCopy.getTaskString());

        //confirm the edited card contains the right data
        TaskCardHandle editedCard = taskListPanel.navigateToTask(indexToEdit - 1);
        
        assertMatching(taskToCopy, editedCard);

        //confirm the list now contains all previous tasks with the edited task
        TestTask[] expectedList = TestUtil.addTasksToList(currentList);
        expectedList[indexToEdit - 1] = taskToCopy;
        assertTrue(taskListPanel.isListMatching(expectedList));
    }
}
