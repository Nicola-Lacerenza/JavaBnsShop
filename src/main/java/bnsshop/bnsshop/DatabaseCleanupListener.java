package bnsshop.bnsshop;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

@WebListener
public final class DatabaseCleanupListener implements ServletContextListener{
    public DatabaseCleanupListener(){}

    @Override
    public void contextDestroyed(ServletContextEvent sce){
        //when the application stopped, it unregisters all the drivers loaded in the memory.
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while(drivers.hasMoreElements()){
            Driver driver = drivers.nextElement();
            try{
                DriverManager.deregisterDriver(driver);
                System.out.println("Deregistered JDBC driver: " + driver);
            }catch(SQLException exception){
                //this isn't an error, this is a warning.
                exception.printStackTrace();
            }
        }

        //when the application stopped, it closes the thread of mysql connections abandoned (closed).
        com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        System.out.println("Thread of mysql connections abandoned (closed) stopped correctly.");
    }
}