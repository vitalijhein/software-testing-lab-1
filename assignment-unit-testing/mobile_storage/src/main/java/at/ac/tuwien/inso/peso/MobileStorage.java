package at.ac.tuwien.inso.peso;

import at.ac.tuwien.inso.peso.exception.NotEnoughSpaceException;
import at.ac.tuwien.inso.peso.exception.StorageEmptyException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents the message inbox of a mobile phone.
 * Each storage position in the inbox can store a message with 160 characters at most.
 * Messages are stored with increasing order (oldest first).
 */
public class MobileStorage {

    private static final int MAX_MESSAGE_LENGTH = 160;

    private MobileMessage[] inbox;
    private int occupied = 0;

    /**
     * Creates a message inbox that can store {@code storageSize} mobile messages.
     *
     * @throws IllegalArgumentException in case the passed {@code storageSize} is zero or less
     */
    public MobileStorage(int storageSize) {
        if(storageSize < 1) {
            throw new IllegalArgumentException("Storage size must be greater than 0");
        }

        this.inbox = new MobileMessage[storageSize];
    }

    /**
     * Stores a new text message to the inbox.
     * In case the message text is longer than {@code MAX_MESSAGE_LENGTH}, the message is splitted and stored on multiple storage positions.
     *
     * @param message a non-empty message text
     * @throws IllegalArgumentException in case the given message is empty
     * @throws NotEnoughSpaceException in case the available storage is too small for storing the complete message text
     */
    public void saveMessage(String message) {
        if(StringUtils.isBlank(message)) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }

        int requiredStorage = (int) Math.ceil((double) message.length() / MAX_MESSAGE_LENGTH);

        if(requiredStorage > inbox.length || (inbox.length - occupied) < requiredStorage) {
            throw new NotEnoughSpaceException("Storage Overflow");
        }

        MobileMessage predecessor = null;
        for(int i = 0; i < requiredStorage; i++) {
            int from = i * MAX_MESSAGE_LENGTH;
            int to = Math.min((i+1) * MAX_MESSAGE_LENGTH, message.length());

            String messagePart = message.substring(from, to);
            MobileMessage mobileMessage = new MobileMessage(messagePart, predecessor);
            inbox[occupied] = mobileMessage;
            occupied++;
            predecessor = mobileMessage;
        }
    }

    /**
     * Returns the number of currently stored mobile messages.
     */
    public int getOccupied() {
        return occupied;
    }

    /**
     * Removes the oldest (first) mobile message from the inbox.
     *
     * @return the deleted message
     * @throws StorageEmptyException in case there are currently no messages stored
     */
    public String deleteMessage() {
        if(occupied == 0) {
            throw new StorageEmptyException("There are no messages in the inbox");
        }

        MobileMessage first = inbox[0];

        IntStream.range(1, occupied).forEach(index -> inbox[index-1] = inbox[index]);
        inbox[occupied-1] = null;
        if(inbox[0] != null){
        inbox[0].setPredecessor(null);}
        occupied--;

        return first.getText();
    }

    /**
     * Returns a readable representation of all currently stored messages, separated by a linebreak("\n").
     * Messages that were stored in multiple parts are joined together for representation.
     * returns an empty String in case there are currently no messages stored
     */
    public String listMessages() {
        return Arrays.stream(inbox)
                .filter(Objects::nonNull)
                .collect(StringBuilder::new, MobileStorage::foldMessage, StringBuilder::append)
                .toString();
    }

    /**
     * Counts how many messages contain the given {@code searchCriteria}.
     * In case the term is contained in a message more than once, it only counts once.
     * @param searchCriteria the search term to search for
     * @return the number of messages that match the search term or zero
     */
    public int searchOccurrences(String searchCriteria){
        return Arrays.stream(inbox)
                .filter(msg -> msg != null && msg.getText().contains(searchCriteria))
                .collect(Collectors.toList())
                .size();
    }

    /**
     * Prints a readable representation of all currently stored messages containing the specified search criteria, separated by a linebreak("\n").
     * If a message was splitted into multiple parts the whole message is printed.
     * In case a multipart message contains the specified search criteria multiple times
     * the entire message is  printed only once.
     */
    public String search(String searchCriteria) {
        List<MobileMessage> messages = Arrays.stream(inbox)
                .filter(msg -> msg != null && msg.getText().contains(searchCriteria))
                .map(this::getLastMessage)
                .distinct()
                .collect(Collectors.toList());

        return String.join("\n",
                messages.stream()
                        .map(this::getMessageText)
                        .collect(Collectors.toList())
        );
    }

    private String getMessageText(MobileMessage message) {
        StringBuilder builder = new StringBuilder();

        if(message.getPredecessor() == null){
            return builder.insert( 0, message.getText()).toString();
        }

        builder.insert( 0, getMessageText(message.getPredecessor()));
        return builder.append(message.getText()).toString();
    }

    private MobileMessage getLastMessage(MobileMessage message){
        List<MobileMessage> successors = Arrays.stream(inbox)
                .filter(msg -> msg != null && msg.getPredecessor() == message)
                .collect(Collectors.toList());

        if(!successors.isEmpty()){
            return getLastMessage(successors.get(0));
        }
        return message;
    }

    private static void foldMessage(StringBuilder builder, MobileMessage message) {
        if(message.getPredecessor() == null && builder.length() != 0) {
            builder.append('\n');
        }
        builder.append(message.getText());
    }
}