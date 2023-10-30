package at.ac.tuwien.inso.peso;

import at.ac.tuwien.inso.peso.exception.NotEnoughSpaceException;
import at.ac.tuwien.inso.peso.exception.StorageEmptyException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the implementation of {@link MobileStorage}
 */
public class MobileStorageTest {
    /**
     * Tests constructor ***********************************************************************************************
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mobileStorageConstructor_TestIfStorageCanBeNegative() {
        MobileStorage mobileStorage = new MobileStorage(-10);
    }

    @Test
    public void mobileStorageConstructor_TestIfStorageCanBeOne() {
        MobileStorage mobileStorage = new MobileStorage(1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mobileStorageConstructor_TestIfStorageCanBeZero() {
        MobileStorage mobileStorage = new MobileStorage(0);
    }

    @Test(expectedExceptions = NotEnoughSpaceException.class)
    public void saveMessage_CheckIfMessageCanBeTooLong() {
        MobileStorage mobileStorage = new MobileStorage(2);
        String message = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
        mobileStorage.saveMessage(message);

    }

    /**
     * Tests saveMessages method ***************************************************************************************
     */
    @Test
    public void saveMessage_CheckIfStorageSizeIsActuallyMessageSize() {
        MobileStorage mobileStorage = new MobileStorage(3);
        mobileStorage.saveMessage("Test-String");
        mobileStorage.saveMessage("Test-String");
       mobileStorage.saveMessage("Test-String");
        int actualStorageSize = mobileStorage.getOccupied();
        int expectedStorageSize = 3;
        Assert.assertEquals(actualStorageSize, expectedStorageSize);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void saveMessage_CheckEmptyMessage() {
        MobileStorage mobileStorage = new MobileStorage(10);
        mobileStorage.saveMessage("");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void saveMessage_CheckNullMessage() {
        MobileStorage mobileStorage = new MobileStorage(10);
        mobileStorage.saveMessage(null);
    }

    @Test(expectedExceptions = NotEnoughSpaceException.class)
    public void saveMessage_CheckRequiredStorageCalculation() {
        String s = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
        MobileStorage mobileStorage = new MobileStorage(1);
        mobileStorage.saveMessage(s);
    }

    @Test(expectedExceptions = NotEnoughSpaceException.class)
    public void saveMessage_CheckRequiredStorageCalculationWithMultipleMessages() {
        String message1 = "This";
        String message2 = "Booooom.";
        MobileStorage mobileStorage = new MobileStorage(1);
        mobileStorage.saveMessage(message1);
        mobileStorage.saveMessage(message2);

    }

    @Test(expectedExceptions = NotEnoughSpaceException.class)
    public void saveMessage_CheckIfStorageToSmall() {
        String s = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
        MobileStorage mobileStorage = new MobileStorage(1);
        mobileStorage.saveMessage(s);
    }

    /**
     * Tests deleteMessage method ***************************************************************************************
     */
    @Test
    public void deleteMessage_CheckIfTheActualMessageGetsDeleted() {
        MobileStorage mobileStorage = new MobileStorage(10);
        mobileStorage.saveMessage("Test-String");
        mobileStorage.saveMessage("Test-String 2");
        String actualDeletedMessage = mobileStorage.deleteMessage();
        String expectedDeletedMessage = "Test-String";
        Assert.assertEquals(actualDeletedMessage, expectedDeletedMessage);
    }

    //(bug deleter)
    @Test
    public void deleteMessage_CheckInboxAfterDeletion() {
        MobileStorage mobileStorage = new MobileStorage(10);
        mobileStorage.saveMessage("Test-String");
        mobileStorage.saveMessage("Test-String 2");
        mobileStorage.saveMessage("Test-String 3");
        mobileStorage.deleteMessage();
        String actualDeletedMessage = mobileStorage.listMessages();
        String expectedDeletedMessage = "Test-String 2\nTest-String 3";
        Assert.assertEquals(actualDeletedMessage, expectedDeletedMessage);


    }

    @Test(expectedExceptions = StorageEmptyException.class)
    public void deleteMessage_CheckIfErrorIsThrownWhileOccupiedZero() {
        MobileStorage mobileStorage = new MobileStorage(1);
        mobileStorage.deleteMessage();
    }

    @Test
    public void deleteMessage_CheckIfMessageCanBeDeletedIfOnly1Message() {
        MobileStorage mobileStorage = new MobileStorage(1);
        mobileStorage.saveMessage("message");
        mobileStorage.deleteMessage();

    }

    @Test
    public void deleteMessage_CheckIfOccupiedIsCorrect() {
        MobileStorage mobileStorage = new MobileStorage(10);
        mobileStorage.saveMessage("message");
        mobileStorage.deleteMessage();
        int actualOccupied = mobileStorage.getOccupied();
        int expectedOccupied = 0;
        Assert.assertEquals(actualOccupied, expectedOccupied);
    }

    @Test
    public void listMessages_CheckIfReturnsMessagesSmallerThan160Chars() {
        MobileStorage mobileStorage = new MobileStorage(10);
        mobileStorage.saveMessage("This");
        mobileStorage.saveMessage("is");
        mobileStorage.saveMessage("a");
        mobileStorage.saveMessage("Test");
        String actualReturn = mobileStorage.listMessages();
        String expectedReturn = "This\nis\na\nTest";
        Assert.assertEquals(actualReturn, expectedReturn);
    }

    @Test
    public void listMessages_should_listNoMessages_when_noMessagesStored() throws Exception {
        MobileStorage mobileStorage = new MobileStorage(2);
        String result = mobileStorage.listMessages();
        Assert.assertEquals(result, "");
    }

    @Test
    public void listMessage_CheckIfStringsBiggerThan160CharsAreReturnedCorrectly() {
        MobileStorage mobileStorage = new MobileStorage(10);
        String message = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

        mobileStorage.saveMessage(message);
        mobileStorage.saveMessage("is");
        mobileStorage.saveMessage("a");
        mobileStorage.saveMessage("Test");
        String actualReturn = mobileStorage.listMessages();
        String expectedReturn = message + "\nis\na\nTest";
        Assert.assertEquals(actualReturn, expectedReturn);
    }

    @Test
    public void searchOccurrences_CheckIfSearchCriteriaIsFoundMultipleTimesInAString() {
        String message = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At v/ero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit am/et, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et ju/sto duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
        MobileStorage mobileStorage = new MobileStorage(10);
        mobileStorage.saveMessage(message);
        mobileStorage.saveMessage("is");
        mobileStorage.saveMessage("a");
        mobileStorage.saveMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        mobileStorage.saveMessage("No test at all");
        int actualOccurrence = mobileStorage.searchOccurrences("Lorem");
        int expectedOccurrence = 4;
        Assert.assertEquals(actualOccurrence, expectedOccurrence);

    }


    @Test
    public void searchOccurrences_CheckIfSearchCriteriaIsFound() {
        MobileStorage mobileStorage = new MobileStorage(10);
        mobileStorage.saveMessage("Test number 1 is a Test, which tests the testing result.");
        mobileStorage.saveMessage("is");
        mobileStorage.saveMessage("a");
        mobileStorage.saveMessage("Test number 2");
        mobileStorage.saveMessage("No test at all");
        int actualOccurrence = mobileStorage.searchOccurrences("Test");
        int expectedOccurrence = 2;
        Assert.assertEquals(actualOccurrence, expectedOccurrence);

    }

    @Test
    public void search_IfSearchCriteriaIsFoundOnce() {
        MobileStorage mobileStorage = new MobileStorage(10);
        String message1 = "Test number 1 is a tttttttest, which tests the testing result.";
        String message2 = "is";
        String message3 = "a";
        String message4 = "Test number 2";
        String message5 = "No test at all";

        mobileStorage.saveMessage(message1);
        mobileStorage.saveMessage(message2);
        mobileStorage.saveMessage(message3);
        mobileStorage.saveMessage(message4);
        mobileStorage.saveMessage(message5);
        String actualOccurrence = mobileStorage.search("Test");
        String expectedOccurrence = message1 + "\n" + message4;
        Assert.assertEquals(actualOccurrence, expectedOccurrence);

    }

    @Test
    public void search_IfSearchCriteriaIsNotFoundTwice() {
        MobileStorage mobileStorage = new MobileStorage(10);
        String message1 = "Test number 1 is a Test, which tests the testing result.";
        String message2 = "is";
        String message3 = "a";
        String message4 = "Test number 2";
        String message5 = "No test at all";

        mobileStorage.saveMessage(message1);
        mobileStorage.saveMessage(message2);
        mobileStorage.saveMessage(message3);
        mobileStorage.saveMessage(message4);
        mobileStorage.saveMessage(message5);
        String actualOccurrence = mobileStorage.search("Test");
        String expectedOccurrence = message1 + "\n" + message4;
        Assert.assertEquals(actualOccurrence, expectedOccurrence);

    }

    @Test
    public void search_IfSearchCriteriaIsFoundInMultipleLines() {
        MobileStorage mobileStorage = new MobileStorage(100);
        String message1 = "is";
        String message2 = "a";
        String message3 = "Test number 2";
        String message4 = "Test number 1 is a Test, which tests the testing result.";
        String message5 = "No Test at all. Unfortunaly we have to created a long message so after this senteces it won't make any sense anymore.153681941536819415368194153681941536819415368194153681941536819415368194153Test681941536819415368194153Test681941536819415368194153681941536819415368194153681941536819415368194153681941536819415368194153681941536819415368194153681941536819415368194";

        mobileStorage.saveMessage(message1);
        mobileStorage.saveMessage(message2);
        mobileStorage.saveMessage(message3);
        mobileStorage.saveMessage(message4);
        mobileStorage.saveMessage(message5);
        String actualOccurrence = mobileStorage.search("Test");
        String expectedOccurrence = message3 + "\n" + message4 + "\n" + message5;
        Assert.assertEquals(actualOccurrence, expectedOccurrence);

    }


}






