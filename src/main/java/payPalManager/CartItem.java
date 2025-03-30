package payPalManager;

import models.ProdottiFull;
import org.json.JSONObject;

public class CartItem {

        private ProdottiFull prodottiFull;
        private int quantity;
        private String tagliaScelta;

        public ProdottiFull getProdottiFull() {
            return prodottiFull;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getTagliaScelta() {
            return tagliaScelta;
        }

        public CartItem(ProdottiFull prodottiFull, int quantity, String tagliaScelta) {
            this.prodottiFull = prodottiFull;
            this.quantity = quantity;
            this.tagliaScelta = tagliaScelta;
        }

        @Override
        public String toString() {
            JSONObject output = new JSONObject();
            output.put("prodotti_full",new JSONObject(prodottiFull.toString()));
            output.put("quantity",quantity);
            output.put("taglia_scelta",tagliaScelta);
            return output.toString(4);
        }
}
