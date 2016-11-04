package seedu.taskscheduler.storage;

import seedu.taskscheduler.commons.exceptions.IllegalValueException;
import seedu.taskscheduler.model.ReadOnlyTaskScheduler;
import seedu.taskscheduler.model.tag.Tag;
import seedu.taskscheduler.model.tag.UniqueTagList;
import seedu.taskscheduler.model.task.ReadOnlyTask;
import seedu.taskscheduler.model.task.UniqueTaskList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An Immutable Task Scheduler that is serializable to XML format
 */
@XmlRootElement(name = "taskscheduler")
public class XmlSerializableTaskScheduler implements ReadOnlyTaskScheduler {

    @XmlElement
    private List<XmlAdaptedTask> tasks;
    @XmlElement
    private List<Tag> tags;
    @XmlElement
    private Map<Tag, Integer> tagsCounter;

    {
        tasks = new ArrayList<>();
        tags = new ArrayList<>();
        tagsCounter = new HashMap<>();
    }

    /**
     * Empty constructor required for marshalling
     */
    public XmlSerializableTaskScheduler() {}

    /**
     * Conversion
     */
    public XmlSerializableTaskScheduler(ReadOnlyTaskScheduler src) {
        tasks.addAll(src.getTaskList().stream().map(XmlAdaptedTask::new).collect(Collectors.toList()));
        tags = src.getTagList();
        tagsCounter = src.getTagsCounter();
    }

    @Override
    public UniqueTagList getUniqueTagList() {
        try {
            return new UniqueTagList(tags);
        } catch (UniqueTagList.DuplicateTagException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UniqueTaskList getUniqueTaskList() {
        UniqueTaskList lists = new UniqueTaskList();
        for (XmlAdaptedTask p : tasks) {
            try {
                lists.add(p.toModelType());
            } catch (IllegalValueException e) {

            }
        }
        return lists;
    }

    @Override
    public List<ReadOnlyTask> getTaskList() {
        return tasks.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Tag> getTagList() {
        return Collections.unmodifiableList(tags);
    }

    @Override
    public Map<Tag, Integer> getTagsCounter() {
        return tagsCounter;
    }

}
