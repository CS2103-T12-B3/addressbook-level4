package seedu.taskscheduler.logic.parser;

import static seedu.taskscheduler.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;

import seedu.taskscheduler.logic.commands.Command;
import seedu.taskscheduler.logic.commands.IncorrectCommand;
import seedu.taskscheduler.logic.commands.SetpathCommand;

//@@author A0138696L

/**
* Parses setpath command user input.
*/
public class SetpathCommandParser extends CommandParser {

    /**
     * Parses arguments in the context of the set save path command.
     * 
     * @param args full command args string
     * @return the custom saved path
     */

    @Override
    public Command prepareCommand(String args) {
        Matcher matcher = SETPATH_DATA_ARGS_FORMAT.matcher(args);

        if (matcher.matches()) {
            String path = matcher.group("name").trim().replaceAll("/$","") +".xml";
            return new SetpathCommand(path); 
        }
        else {   
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetpathCommand.MESSAGE_USAGE));
        }
    }
}
