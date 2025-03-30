package payPalManager;

import models.Ordine;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class OrdineCreato {
    private final List<LinkCreato> links;
    private final String orderId;
    private final Ordine order;

    public OrdineCreato(List<LinkCreato> links,String orderId,Ordine order){
        this.links= links;
        this.orderId= orderId;
        this.order= order;
    }

    public OrdineCreato(){
        this(new LinkedList<>(),"",new Ordine());
    }

    public List<LinkCreato> getLinks() {
        return links;
    }

    public String getOrderId() {
        return orderId;
    }

    public Ordine getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdineCreato that = (OrdineCreato) o;
        return Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("links",links);
        output.put("order_id",orderId);
        output.put("order",new JSONObject(order.toString()));
        return output.toString(4);
    }
}
