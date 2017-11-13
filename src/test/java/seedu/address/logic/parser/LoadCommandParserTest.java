package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.LoadCommand;

public class LoadCommandParserTest {

    private LoadCommandParser parser = new LoadCommandParser();

    @Test
    public void parse_failure() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoadCommand.MESSAGE_USAGE);

        // test with empty string
        assertParseFailure(parser, " ", expectedMessage);
    }

    @Test
    public void parse_success() throws Exception {
        LoadCommand expectedCommand = new LoadCommand("testbook.xml");

        assertParseSuccess(parser, "testbook.xml", expectedCommand);
    }


}
