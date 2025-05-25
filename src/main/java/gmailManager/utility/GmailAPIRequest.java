package gmailManager.utility;

import models.Oggetti;
import org.json.JSONObject;
import utility.RichiesteAPIEsterne;

import java.io.Serial;
import java.util.Map;
import java.util.Optional;

public abstract class GmailAPIRequest <T extends Oggetti<T>> extends RichiesteAPIEsterne<T> {

    @Serial
    private static final long serialVersionUID = 1L;

    public GmailAPIRequest(String accessToken){
        super("",accessToken);
    }
    public GmailAPIRequest(String apiBaseURL,String accessToken) {
        super(apiBaseURL,accessToken);
    }


}
