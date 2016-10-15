package guitests;

import guitests.guihandles.TaskCardHandle;
import org.junit.Test;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.MarkCommand;
import seedu.address.commons.core.Messages;
import seedu.address.testutil.TestTask;
import seedu.address.testutil.TestUtil;
import seedu.address.ui.TaskCard;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS;

public class MarkCommandTest extends TaskSchedulerGuiTest {

    @Test
    public void mark() {

        //mark the first in the list
        TestTask[] currentList = td.getTypicalTasks();
        int targetIndex = 1;
        assertMarkSuccess(targetIndex, currentList);
        
        //mark the last in the list
        targetIndex = currentList.length;
        assertMarkSuccess(targetIndex, currentList);
        
        //mark the middle in the list
        targetIndex = currentList.length/2;
        assertMarkSuccess(targetIndex, currentList);
        
        //invalid index
        commandBox.runCommand("mark " + currentList.length + 1);
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        
        //mark the first in the list again
        targetIndex = 1;
        commandBox.runCommand("mark " + targetIndex);
        assertResultMessage(MarkCommand.MESSAGE_MARK_TASK_FAIL);
        
        //mark empty list
        commandBox.runCommand("clear");
        commandBox.runCommand("mark " + currentList.length + 1);
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);

    }

    /**
     * Runs the mark command to mark the task at specified index as completed and confirms the result is correct.
     * @param targetIndexOneIndexed e.g. to mark the first task in the list, 1 should be given as the target index.
     * @param currentList A copy of the current list of tasks (before deletion).
     */
    public void assertMarkSuccess(int targetIndexOneIndexed, final TestTask[] currentList) {
        
        TestTask taskToMark = currentList[targetIndexOneIndexed-1]; //-1 because array uses zero indexing
        
        commandBox.runCommand("mark " + targetIndexOneIndexed);
        
        //confirm the task card is now marked completed.
        assertTrue(taskListPanel.navigateToTask(targetIndexOneIndexed - 1).getTags().contains("[Completed]"));
        assertTrue(taskListPanel.navigateToTask(targetIndexOneIndexed - 1).getHBoxStyle().equals(TaskCard.COMPLETED_INDICATION));
        assertFalse(taskListPanel.navigateToTask(targetIndexOneIndexed - 1).getHBoxStyle().equals(TaskCard.OVERDUE_INDICATION));
        //confirm the result message is correct
        assertResultMessage(String.format(MarkCommand.MESSAGE_MARK_TASK_SUCCESS, taskToMark));
    }
}