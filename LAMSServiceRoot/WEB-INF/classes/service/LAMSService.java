package service;

import javax.jws.*;
import business.*;
import components.data.*;
import java.util.*;

@WebService(serviceName="LAMSService")
public class LAMSService {

   private DBSingleton dbSingleton;

	@WebMethod(operationName="Initialize")
   public String initialize(){
         dbSingleton = DBSingleton.getInstance();
         dbSingleton.db.initialLoad("LAMS");
         return "Database Initialized";
   }
   
   @WebMethod(operationName="GetAllAppointments")
   public String GetAllAppointments(){
      DBSingleton dbSingleton = DBSingleton.getInstance();
      String appointments = "";
      dbSingleton.db.initialLoad("LAMS");
      System.out.println("All appointments");
      List<Object> objs = dbSingleton.db.getData("Appointment", "");
      for (Object obj : objs){
         appointments += obj;
      }
   }
   
   @WebMethod
   public double calcCircle(double r){
      return r*r*Math.PI;
   }
   
   public double calcRectangle(double w, double h){
      return w*h;
   }
   
   @WebMethod(exclude=true)
   public String someMethod(){
      return "";
   }

}