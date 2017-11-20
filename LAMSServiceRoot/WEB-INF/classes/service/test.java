package service;

import java.util.*;
import business.*;

public class test {

   public static void main(String[] args){
      // create service
      LAMSService service = new LAMSService();

      // init
      System.out.println(service.initialize());

      // get one appointment
      System.out.println("Appointment for Patient 220:");
      System.out.println(service.getAppointment("220"));
   }
}