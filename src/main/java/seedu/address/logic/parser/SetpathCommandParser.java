package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.commands.SetpathCommand;

public class SetpathCommandParser extends CommandParser {

    /**
     * Parses arguments in the context of the set save path command.
     * 
     * @param args full command args string
     * @return the custom saved path
     */

    public Command prepareCommand(String args) {
        args = args.trim();
        Matcher matcher = SETPATH_DATA_ARGS_FORMAT.matcher(args);
        // Validate arg string format
        if (matcher.matches()) {
            String path = "data/" + matcher.group("name").trim().replaceAll("/$","") +".xml";
            return new SetpathCommand(path); 
        }
        else {   
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetpathCommand.MESSAGE_USAGE));
        }
    }
}
