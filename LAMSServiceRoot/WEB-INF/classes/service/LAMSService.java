package service;

import javax.jws.*;
import business.*;
import components.data.*;
import java.util.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import org.xml.sax.InputSource;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

// @WebService(serviceName="LAMSService")
public class LAMSService {

   private DBSingleton dbSingleton;
   private final String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";

	// @WebMethod(operationName="Initialize")
   public String initialize(){
      dbSingleton = DBSingleton.getInstance();
      dbSingleton.db.initialLoad("LAMS");
      return "Database Initialized";
   }
   
   // @WebMethod(operationName="GetAllAppointments")
   public String getAllAppointments(){
      String xml = xmlHead + "<AppointmentList>";

      dbSingleton = DBSingleton.getInstance();
      dbSingleton.db.initialLoad("LAMS");
      System.out.println("All appointments");
      List<Object> objs = dbSingleton.db.getData("Appointment", "");
      for (Object obj : objs){
         xml += formatAppointment(obj);
      }

      return xml;
   }

   // @WebMethod(operationName="GetAppointment")
   public String getAppointment(String appointmentID){
      String xml = xmlHead + "<AppointmentList>";
      
      dbSingleton = DBSingleton.getInstance();
      dbSingleton.db.initialLoad("LAMS");
      List<Object> objs = dbSingleton.db.getData("Appointment", "id='"+appointmentID+"'");
      for (Object obj : objs){
         xml += formatAppointment(obj);
      }

      xml += "</AppointmentList>";
      return xml;
   }

   /**
    * Takes in Appointment and creates xml string from it.
    */
   private String formatAppointment(Object obj) {
      String date = ((Appointment)obj).getApptdate().toString();
      String id = ((Appointment)obj).getId();
      String time = ((Appointment)obj).getAppttime().toString();
      String xml = "<appointment date=\""+date+"\" id=\""+id+"\" time=\""+time+"\">";

      Patient patient = ((Appointment)obj).getPatientid();
      xml += tag("patient", patient.getId(), "id");
      xml += tag("name", patient.getName());
      xml += tag("address", patient.getAddress());
      xml += tag("insurance", Character.toString(patient.getInsurance()));
      xml += tag("dob", patient.getDateofbirth().toString());
      xml += "</patient>";

      Phlebotomist phleb = ((Appointment)obj).getPhlebid();
      xml += tag("phlebotomist", phleb.getId(), "id");
      xml += tag("name", phleb.getName());
      xml += "</phlebotomist>";

      PSC psc = ((Appointment)obj).getPscid();
      xml += tag("psc", psc.getId(), "id");
      xml += tag("name", psc.getName());
      xml += "</psc>";

      // lab tests
      List<AppointmentLabTest> tests = ((Appointment)obj).getAppointmentLabTestCollection();
      LabTest test = null;
      Diagnosis dx = null;
      xml += "<allLabTests>";
      for (AppointmentLabTest apptTest : tests) {
         test = apptTest.getLabTest();
         dx = apptTest.getDiagnosis();
         xml += "<appointmentLabTest appointmentId=\""+id+"\" dxcode=\""+dx.getCode()+"\" labTestId=\""+test.getKey()+"\"/>";
      }
      xml += "</allLabTests>";

      xml += "</appointment>";
      return xml;
   }

   private static String tag(String name, String value) {
      return String.format("<%s>%s</%s>\n", name, value, name);
   }

   private static String tag(String name, String value, String propertyName) {
      return String.format("<%s %s=\"%s\">\n", name, propertyName, value);
   }

   // @WebMethod(operationName="AddAppointment")
   public String addAppointment(String xml){
      String response = xmlHead + "<AppointmentList>";

      try {
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
         NodeList nList = doc.getElementsByTagName("appointment");
               
         Node nNode = nList.item(0); // there should be only one appointment
         System.out.println("\nCurrent Element: "+nNode.getNodeName());
              
         if(nNode.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element)nNode;
            // System.out.println("date: "+eElement.getAttribute("date"));
            System.out.println("date: "+eElement.getElementsByTagName("date").item(0).getTextContent());
            System.out.println("time: "+eElement.getElementsByTagName("time").item(0).getTextContent());
            
            NodeList tests = eElement.getElementsByTagName("test");
            for(int i = 0 ; i < tests.getLength() ; i++){
               System.out.println("Test: "+tests.item(i).getTextContent());  
            }
            System.out.println("\n\n");
         }

      } catch(ParserConfigurationException pce){
         System.out.println("ParserConfigurationException caught at addAppointment(String): ");
      } catch(Exception e){
         System.out.println("Exception caught at addAppointment(String): ");
         e.printStackTrace();
      }
      

      // // System.out.println("^^^^^^^"+phleb.getId());
      // Appointment newAppt = new Appointment("800",java.sql.Date.valueOf("2009-09-01"),java.sql.Time.valueOf("10:15:00"));
      // //extra steps here due to persistence api and join, need to create objects in list
      // List<AppointmentLabTest> tests = new ArrayList<AppointmentLabTest>();
      // AppointmentLabTest test = new AppointmentLabTest("800","86900","292.9");
      // test.setDiagnosis((Diagnosis)dbSingleton.db.getData("Diagnosis", "code='292.9'").get(0));
      // test.setLabTest((LabTest)dbSingleton.db.getData("LabTest","id='86900'").get(0));
      // tests.add(test);
      // newAppt.setAppointmentLabTestCollection(tests);
      // newAppt.setPatientid(patient);
      // newAppt.setPhlebid(phleb);
      // newAppt.setPscid(psc);
      // boolean good = dbSingleton.db.addData(newAppt);
      // objs = dbSingleton.db.getData("Appointment", "");
      // for (Object obj : objs){
      //    response += obj.toString()+"\n";
      // }

      // bad appointment
      // response = "<?xml version="1.0" encoding="UTF-8" standalone="no"?><AppointmentList><error>ERROR:Appointment is not available</error></AppointmentList>";
      
      response += "</AppointmentList>";
      return response;
   }

}