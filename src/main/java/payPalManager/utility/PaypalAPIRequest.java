package payPalManager.utility;

import payPalManager.models.PaypalModels;
import utility.RichiesteAPIEsterne;
import java.io.Serial;

public abstract class PaypalAPIRequest<T extends PaypalModels<T>> extends RichiesteAPIEsterne<T> {
    @Serial
    private static final long serialVersionUID = 1L;

    public PaypalAPIRequest(String apiBaseURL,String accessToken){
        super(apiBaseURL,accessToken);
    }
}