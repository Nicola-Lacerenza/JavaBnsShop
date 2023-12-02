package bnsshop.bnsshop;

import controllers.ProdottiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.annotation.WebServlet;
import java.io.*;
import java.util.HashMap;

@WebServlet(name = "ProdottiServlet", value = "/ProdottiServlet")
public class ProdottiServlet extends HttpServlet{
    ProdottiController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new ProdottiController();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        response.getWriter().println("Hello world!");
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        this.controller.insertObject(new HashMap<>());
        response.getWriter().println("Hello world!");
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    public void doPut(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        response.getWriter().println("Hello world!");
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        response.getWriter().println("Hello world!");
        response.getWriter().flush();
        response.getWriter().close();
    }
}