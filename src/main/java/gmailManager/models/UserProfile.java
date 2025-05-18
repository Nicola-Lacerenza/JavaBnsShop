package gmailManager.models;

import models.Oggetti;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;


public class UserProfile implements Oggetti<UserProfile> {

    private final String emailAddress;

    private final int messagesNumber;

    private final int threadsNumber;

    private final String historyID;

    public UserProfile(String emailAddress, int messagesNumber, int threadsNumber, String historyID) {
        this.emailAddress = emailAddress;
        this.messagesNumber = messagesNumber;
        this.threadsNumber = threadsNumber;
        this.historyID = historyID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public int getMessagesNumber() {
        return messagesNumber;
    }

    public int getThreadsNumber() {
        return threadsNumber;
    }

    public String getHistoryID() {
        return historyID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(historyID, that.historyID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyID);
    }

    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("email_address",emailAddress);
        output.put("messages_number",messagesNumber);
        output.put("threads_number",threadsNumber);
        output.put("history_id",historyID);
        return output.toString(4);
    }

    @Override
    public Optional<UserProfile> convertDBToJava(ResultSet rs) throws SQLException {
        return Optional.empty();
    }
}
