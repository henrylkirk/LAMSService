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
      // System.out.println("Appointment for Appointment 790:");
      // System.out.println(service.getAppointment("790"));

      // all appointments
      // System.out.println("All Appointments:");
      // System.out.println(service.getAllAppointments());

      // add appointment
      service.addAppointment("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><appointment><date>2018-12-28</date><time>10:00</time><patientId>220</patientId><physicianId>20</physicianId><pscId>520</pscId><phlebotomistId>110</phlebotomistId><labTests><test id=\"86900\" dxcode=\"292.9\" /><test id=\"86609\" dxcode=\"307.3\" /></labTests></appointment>");
   }
}