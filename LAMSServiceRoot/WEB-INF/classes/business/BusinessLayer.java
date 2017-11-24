package business;

import javax.jws.*;
import components.data.*;
import java.util.*;

public class BusinessLayer {
	public static final String OPEN_TIME = "8:00:00";
	public static final String CLOSE_TIME = "17:00:00";
	public static final String APPT_LENGTH = "00:15:00";
	public static final String APPT_GAP = "00:30:00";

	/**
	 * Check that appointment time does not conflict with any others.
	 */
	public static boolean isAvailable(Appointment newAppt, List<Object> sameDayAppts){
		boolean available = false;
		
		if(sameDayAppts != null && !sameDayAppts.isEmpty()){
			// find new appt time
			java.sql.Time start1 = newAppt.getAppttime();
			String newPhlebId = ((Appointment)newAppt).getPhlebid().getId();
			for (Object obj : sameDayAppts){
				// see if appts have the same phleb
				if(newPhlebId == ((Appointment)obj).getPhlebid().getId()){
					java.sql.Time start2 = ((Appointment)obj).getAppttime();
					System.out.println("New appointment time: "+start1.toString()+", This appointment time: "+start2.toString());
				}
				
			}
			
		}
		
		return available;
	}

	private static boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
    	return !start1.after(end2) && !start2.after(end1);
	}
	
}