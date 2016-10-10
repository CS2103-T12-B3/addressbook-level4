package seedu.address.model;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.tag.UniqueTagList.DuplicateTagException;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.core.ComponentManager;

import java.util.Set;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskScheduler taskScheduler;
    private final FilteredList<Task> filteredTasks;

    /**
     * Initializes a ModelManager with the given AddressBook
     * AddressBook and its variables should not be null
     */
    public ModelManager(TaskScheduler src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with task scheduler: " + src + " and user prefs " + userPrefs);

        taskScheduler = new TaskScheduler(src);
        filteredTasks = new FilteredList<>(taskScheduler.getTasks());
    }

    public ModelManager() {
        this(new TaskScheduler(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskScheduler initialData, UserPrefs userPrefs) {
        taskScheduler = new TaskScheduler(initialData);
        filteredTasks = new FilteredList<>(taskScheduler.getTasks());
    }

    @Override
    public void resetData(ReadOnlyTaskScheduler newData) {
        taskScheduler.resetData(newData);
        indicateTaskSchedulerChanged();
    }

    @Override
    public ReadOnlyTaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateTaskSchedulerChanged() {
        raise(new AddressBookChangedEvent(taskScheduler));
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        taskScheduler.removeTask(target);
        indicateTaskSchedulerChanged();
    }

    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        taskScheduler.addTask(task);
        updateFilteredListToShowAll();
        indicateTaskSchedulerChanged();
    }

    @Override
    public void editTask(ReadOnlyTask oldTask, Task newTask) 
            throws TaskNotFoundException, UniqueTaskList.DuplicateTaskException {
        taskScheduler.editTask(oldTask, newTask);
        updateFilteredListToShowAll();
        indicateTaskSchedulerChanged();
        
    }   

    @Override
    public void markTask(ReadOnlyTask task) 
            throws TaskNotFoundException, DuplicateTagException {
        taskScheduler.markTask(task);
        updateFilteredListToShowAll();
        indicateTaskSchedulerChanged();
        
    }
    //=========== Filtered Task List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredTasks.setPredicate(null);
    }
    
    @Override
    public void updateFilteredTaskList(Set<String> keywords){
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }

    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask task);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask task);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(task.getName().fullName, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }


}
