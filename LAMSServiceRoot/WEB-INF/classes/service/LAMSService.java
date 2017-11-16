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
   public String getAllAppointments(){
      dbSingleton = DBSingleton.getInstance();
      String appointments = "";
      dbSingleton.db.initialLoad("LAMS");
      System.out.println("All appointments");
      List<Object> objs = dbSingleton.db.getData("Appointment", "");
      for (Object obj : objs){
         appointments += obj.toString()+"\n";
      }
      return appointments;
   }

   @WebMethod(operationName="GetAppointment")
   public String getAppointment(String appointmentID){
      dbSingleton = DBSingleton.getInstance();
      String appointment = "";
      List<Object> objs = dbSingleton.db.getData("Appointment", "patientid='"+appointmentID+"'");
      Patient patient = null;
      Phlebotomist phleb = null;
      PSC psc = null;
      for (Object obj : objs){
         appointment += obj.toString()+"\n";
         // patient = ((Appointment)obj).getPatientid();
         // phleb = ((Appointment)obj).getPhlebid();
         // psc = ((Appointment)obj).getPscid();
      }
      return appointment;
   }

   @WebMethod(operationName="AddAppointment")
   public String addAppointment(String xml){
      String response = "";

      System.out.println("^^^^^^^"+phleb.getId());
      Appointment newAppt = new Appointment("800",java.sql.Date.valueOf("2009-09-01"),java.sql.Time.valueOf("10:15:00"));
      //extra steps here due to persistence api and join, need to create objects in list
      List<AppointmentLabTest> tests = new ArrayList<AppointmentLabTest>();
      AppointmentLabTest test = new AppointmentLabTest("800","86900","292.9");
      test.setDiagnosis((Diagnosis)dbSingleton.db.getData("Diagnosis", "code='292.9'").get(0));
      test.setLabTest((LabTest)dbSingleton.db.getData("LabTest","id='86900'").get(0));
      tests.add(test);
      newAppt.setAppointmentLabTestCollection(tests);
      newAppt.setPatientid(patient);
      newAppt.setPhlebid(phleb);
      newAppt.setPscid(psc);
      boolean good = dbSingleton.db.addData(newAppt);
      objs = dbSingleton.db.getData("Appointment", "");
      for (Object obj : objs){
         response += obj.toString()+"\n";
      }

      // bad appointment
      // response = "<?xml version="1.0" encoding="UTF-8" standalone="no"?><AppointmentList><error>ERROR:Appointment is not available</error></AppointmentList>";
      return response;
   }

}