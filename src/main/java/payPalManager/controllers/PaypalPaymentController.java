package payPalManager.controllers;

import bnsshop.bnsshop.RegisterServlet;
import controllers.Controllers;
import payPalManager.models.PaypalPaymentsCreated;
import utility.Database;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PaypalPaymentController implements Controllers<PaypalPaymentsCreated>{
    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return false;
    }

    @Override
    public boolean updateObject(int id, Map<Integer, RegisterServlet.RegisterFields> request) {
        return false;
    }

    @Override
    public boolean deleteObject(int objectid) {
        return false;
    }

    @Override
    public Optional<PaypalPaymentsCreated> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<PaypalPaymentsCreated> getAllObjects() {
        return new LinkedList<>();
    }

    @Override
    public List<PaypalPaymentsCreated> executeQuery(String query) {
        return Database.executeGenericQuery("paypal_pagamento_creato",new PaypalPaymentsCreated(),query);
    }
}