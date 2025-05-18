package gmailManager.utility;

import gmailManager.models.UserProfile;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserReadProfile extends GmailAPIRequest<UserProfile>{

    private final String userId;

    public UserReadProfile(String accessToken,String userId){
        super(accessToken);
        this.userId=userId;
    }

    @Override
    public String getEndpoint() {
        return "/users/" + userId + "/profile";
    }

    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<>();
    }

    @Override
    public String getBody() {
        return "";
    }

    @Override
    public Optional<UserProfile> parseResponse(JSONObject jsonResponse) {
        String emailAddress1 = jsonResponse.getString("emailAddress");
        int messagesNumber1 = jsonResponse.getInt("messagesTotal");
        int threadsNumber1 = jsonResponse.getInt("threadsTotal");
        String historyId1 = jsonResponse.getString("historyId");
        UserProfile profile = new UserProfile(emailAddress1,messagesNumber1,threadsNumber1,historyId1);
        return Optional.of(profile);
    }
}
