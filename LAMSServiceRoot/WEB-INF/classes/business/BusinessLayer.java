package business;

public class BusinessLayer {

	public String testBusinessLayer(){
		return "testing the business layer";
	}

	public static String makeTag(String name, String value){
		return String.format("<%s>%s</%s>", name, value, name);
	}
}