package seedu.address.model.task;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

public class DeadlineTask extends Task {

    public DeadlineTask(Name name, TaskDateTime endDateTime) throws IllegalValueException {
        super(
            name, 
            new TaskDateTime(), 
            endDateTime, 
            new Location(), 
            new UniqueTagList(new Tag("Deadline")));
    }

    public DeadlineTask(ReadOnlyTask source) {
        super(source);
    }

//    @Override
//    public String getParamOne() {
//        // TODO Auto-generated method stub
//        return "Due Date: " + getEndDate().getDisplayString();
//    }
//
//    @Override
//    public String getParamTwo() {
//        // TODO Auto-generated method stub
//        return "";
//    }
//
//    @Override
//    public String getParamThree() {
//        // TODO Auto-generated method stub
//        return "";
//    }

}
