package business;

import javax.jws.*;
import components.data.*;
import java.util.*;
import org.joda.time.*;
import java.text.SimpleDateFormat;

public class BusinessLayer {
	
	public static final String OPEN_TIME = "8:00:00";
	public static final String CLOSE_TIME = "17:00:00";
	public static final int APPT_LENGTH = 15;
	public static final int APPT_GAP = 30;

	/**
	 * Check that the appointment time does not conflict with any others.
	 */
	public static boolean isAvailable(Appointment newAppt, List<Object> sameDayAppts){
		if(sameDayAppts != null && !sameDayAppts.isEmpty()){
			// find new appt time
			Interval appt1 = dateAndTimeToInterval(newAppt.getApptdate(), newAppt.getAppttime());
			String newPhlebId = ((Appointment)newAppt).getPhlebid().getId();
			for (Object obj : sameDayAppts){
				// see if appts have the same phleb
				if(newPhlebId == ((Appointment)obj).getPhlebid().getId()){
					Interval appt2 = dateAndTimeToInterval(((Appointment)obj).getApptdate(), ((Appointment)obj).getAppttime());

					// see if new appointment conflicts with any that day
					if(appt1.overlaps(appt2) || !isValidGap(appt1, appt2)){
						System.out.println("the appointment time conflicts");
						return false;
					} 
				}
			}
		}
		return true;
	}

	/**
	 * Change a java.sql.Date and a java.sql.Time to an Interval of appointment length.
	 */
	private static Interval dateAndTimeToInterval(java.sql.Date date, java.sql.Time time) {
	    String myDate = date + " " + time;
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    java.util.Date utilDate = new java.util.Date();
	    try {
	        utilDate = sdf.parse(myDate);
	    } catch (Exception e){
	        e.printStackTrace();
	    }

	    // make an interval for appointment length
	    DateTime start = new DateTime(utilDate);
	    DateTime end = start.plusMinutes(APPT_LENGTH);
	    Interval interval = new Interval(start, end);

	    return interval;
	}

	/**
	 * See if the interval between two appointments is greater or equal to the required gap length.
	 */
	private static boolean isValidGap(Interval appt1, Interval appt2){
		Interval gap = appt1.gap(appt2);
		System.out.println("gap between the appointments: "+gap.toString());
		int minutes = gap.toPeriod().getMinutes();
		int hours = gap.toPeriod().getHours();
		int totalMinutes = (hours*60) + minutes;
		System.out.println("gap minutes: "+Integer.toString(minutes)+", gap hours: "+Integer.toString(hours)+", gap total minutes: "+Integer.toString(totalMinutes));
		return (totalMinutes >= APPT_GAP);
	}
	
}