package seedu.tripbuddy.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.tripbuddy.exception.InvalidKeywordException;

import java.util.ArrayList;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParserTest {

    private Parser parser;
    private Logger testLogger;

    @BeforeEach
    void setUp() {
        // Create a logger instance for testing purposes.
        testLogger = Logger.getLogger("TestLogger");
        // Instantiate Parser with the injected logger.
        parser = Parser.getInstance(testLogger);
    }

    @Test
    void parseCommandTest_invalidKeyword_expectInvalidKeywordException() {
        assertThrows(InvalidKeywordException.class, () -> parser.parseCommand("lol"));
    }

    @Test
    void parseCommandTest_keywordOnly() throws InvalidKeywordException {
        Command cmd = parser.parseCommand("add-expense");
        assertEquals(Keyword.ADD_EXPENSE, cmd.getKeyword(), "keyword");
        assertEquals(0, cmd.getOptList().size(), "optList");
    }

    @Test
    void parseCommandTest_keywordWithExtraSpace() throws InvalidKeywordException {
        Command cmd = parser.parseCommand(" add-expense  ");
        assertEquals(Keyword.ADD_EXPENSE, cmd.getKeyword(), "keyword");
        assertEquals(0, cmd.getOptList().size(), "optList");
    }

    @Test
    void parseCommandTest_noOpt() throws InvalidKeywordException {
        Command cmd = parser.parseCommand("add-expense  bb ccc d");
        ArrayList<Option> optList = cmd.getOptList();
        assertEquals(Keyword.ADD_EXPENSE, cmd.getKeyword(), "keyword");
        assertEquals(1, optList.size(), "optList");
        assertTrue(optList.get(0).opt().isEmpty(), "opt");
        assertEquals("bb ccc d", optList.get(0).val(), "val");
    }

    @Test
    void parseCommandTest_noTokensBeforeFirstOpt() throws InvalidKeywordException {
        Command cmd = parser.parseCommand("add-expense -b bb");
        assertEquals(Keyword.ADD_EXPENSE, cmd.getKeyword(), "keyword");
        assertEquals(1, cmd.getOptList().size(), "optList");
    }

    @Test
    void parseCommandTest_extraTokensBeforeFirstOpt() throws InvalidKeywordException {
        Command cmd = parser.parseCommand("add-expense aa -b bb -c cc");
        assertEquals(Keyword.ADD_EXPENSE, cmd.getKeyword(), "keyword");
        assertEquals(3, cmd.getOptList().size(), "optList");
    }

    @Test
    void parseCommandTest_hasOptWithoutVal() throws InvalidKeywordException {
        Command cmd = parser.parseCommand("add-expense aa -d2 -b bb -d1 -c cc");
        assertEquals(Keyword.ADD_EXPENSE, cmd.getKeyword(), "keyword");
        assertEquals(5, cmd.getOptList().size(), "optList");
    }

    @Test
    void parseCommandTest_hasSingleMinus() throws InvalidKeywordException {
        Command cmd = parser.parseCommand("add-expense aa -d2 -");
        assertEquals(Keyword.ADD_EXPENSE, cmd.getKeyword(), "keyword");
        assertEquals(2, cmd.getOptList().size(), "optList");
    }

    @Test
    public void parseCommand_setTimeCommand_success() throws Exception {
        Parser parser = Parser.getInstance(null);
        Command cmd = parser.parseCommand("set-time lunch -t 2024-04-01 12:00:00");
        assertEquals(Keyword.SET_TIME, cmd.getKeyword());
        assertEquals("lunch", cmd.getOpt(""));
        assertEquals("2024-04-01 12:00:00", cmd.getOpt("t"));
    }
}
