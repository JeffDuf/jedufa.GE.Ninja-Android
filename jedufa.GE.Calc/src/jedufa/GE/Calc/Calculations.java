/*
    This file is part of jedufa.GE.Ninja

    jedufa.GE.Ninja is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jedufa.GE.Ninja is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jedufa.GE.Ninja.  If not, see <http://www.gnu.org/licenses/>.
*/


package jedufa.GE.Calc;

import java.util.Calendar;

import android.text.format.Time;

public class Calculations {

    Boolean	SC = false; Boolean	LC = false; Boolean	LF = false; Boolean HF = false; Boolean	CR = false; 
    Boolean	BS = false; Boolean COL = false;Boolean	REC = false;
    Boolean	SPY = false; Boolean BB = false; Boolean DS = false; Boolean RIP = false; Boolean BC = false;
    boolean use_general = false; 			//1
    double		event_factor; 			//2
    int    	Hyperspace_drive_level; int		Impulse_drive_level; int 	Combustion_drive_level; //8
    int orig_gal;    int orig_sys;    int orig_pla;    int targ_gal;    int targ_sys;    int targ_pla;
    double ship_speed_factor_hyp;    double ship_speed_factor_imp;    double ship_speed_factor_com;
    Time departureTime;
    Time remainingTime;

    // Constants
    int ship_speed_SC_low_impulse5 = 5000;		//1
    int ship_speed_SC_high_impulse5 = 10000;		//-
    int ship_speed_LC = 7500;				//2
    int ship_speed_LF = 12500;			//3
    int ship_speed_HF = 10000;			//4
    int ship_speed_CR = 15000;			//5
    int ship_speed_BS = 10000;			//6
    int ship_speed_COL = 2500;			//7
    int ship_speed_REC = 2000;			//8
    int ship_speed_SPY = 100000000;		//9
    int ship_speed_BB_low_hyper8 = 4000;	//10
    int ship_speed_BB_high_hyper8 = 5000;	//-
    int ship_speed_DS = 5000;				//11
    int ship_speed_RIP = 100;				//12
    int ship_speed_BC = 10000;			//13

    int universe_speed = 2;

    double	Hyperspace_drive_level_bonus 		= 0.3; 
    double	Impusle_drive_level_bonus 			= 0.2; 
    double	Combustion_drive_level_bonus 	    = 0.1; 
	
	//NOTE: DEPARTURE TIME IS NOT USED NOR INITIALISED ANYMORE!!!
		
	public void Initialise (Boolean sc, Boolean lc, Boolean lf, Boolean hf, Boolean cr, Boolean bs, 
			Boolean cs, Boolean rc, Boolean bb, Boolean ds,  Boolean rip,  Boolean bc,
			Boolean gen, String event, int hyp, int imp, int com, Time remaining_time, Time departure_time,
			String origgal, String origsys, String origpla, String destgal, String destsys, String destpla)
	{
		this.SC = sc; this.LC = lc; this.LF = lf; this.HF = hf; this.CR = cr; this.BS = bs;
		this.COL = cs; this.REC = rc; this.BB = bb; this.DS = ds; this.RIP = rip; this.BC = bc;
		this.use_general = gen; this.event_factor = tryParseDouble(event); this.Hyperspace_drive_level = hyp;
		this.Impulse_drive_level = imp; this.Combustion_drive_level = com;
		this.departureTime = departure_time;
		this.remainingTime = remaining_time;
		this.orig_gal = tryParse(origgal); this.orig_pla = tryParse(origpla); this.orig_sys = tryParse(origsys);
		this.targ_gal = tryParse(destgal); this.targ_pla = tryParse(destpla); this.targ_sys = tryParse(destsys);

        ship_speed_factor_hyp = (1 + (double)Hyperspace_drive_level * Hyperspace_drive_level_bonus);
        ship_speed_factor_imp = (1 + (double)Impulse_drive_level    * Impusle_drive_level_bonus);
        ship_speed_factor_com = (1 + (double)Combustion_drive_level * Combustion_drive_level_bonus);
	}
	

    public String output = "";
    public String outputArrival = "";

    int Calculate()
    {    	
        this.output = "";
        this.outputArrival = ""; 
        int slowest_ship_speed = Calculate_Slowest_Ship_Speed();
        if (slowest_ship_speed == 0)
        {
            this.output = "Please set your inputs";
            return 200;
        }
        double distance = Calculate_Distance();

        int flight_time100 = Calculate_Flight_Time(100, slowest_ship_speed, distance);
        int flight_time90  = Calculate_Flight_Time(90, slowest_ship_speed, distance);
        int flight_time80  = Calculate_Flight_Time(80, slowest_ship_speed, distance);
        int flight_time70  = Calculate_Flight_Time(70, slowest_ship_speed, distance);
        int flight_time60  = Calculate_Flight_Time(60, slowest_ship_speed, distance);
        int flight_time50  = Calculate_Flight_Time(50, slowest_ship_speed, distance);
        int flight_time40  = Calculate_Flight_Time(40, slowest_ship_speed, distance);
        int flight_time30  = Calculate_Flight_Time(30, slowest_ship_speed, distance);
        int flight_time20  = Calculate_Flight_Time(20, slowest_ship_speed, distance);
        int flight_time10  = Calculate_Flight_Time(10, slowest_ship_speed, distance);

        int flight_time_hours = Convert_TotalSeconds_To_Hours(flight_time100);
        int flight_time_minutes = Convert_TotalSeconds_To_Minutes(flight_time100, flight_time_hours);
        int flight_time_seconds = Convert_TotalSeconds_To_Seconds(flight_time100, flight_time_hours, flight_time_minutes);
        String outputText = "100%:" + this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, true);
        String outputTextArrival = "";        
        outputTextArrival += this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, false); 
        
        flight_time_hours = Convert_TotalSeconds_To_Hours(flight_time90);
        flight_time_minutes = Convert_TotalSeconds_To_Minutes(flight_time90, flight_time_hours);
        flight_time_seconds = Convert_TotalSeconds_To_Seconds(flight_time90, flight_time_hours, flight_time_minutes);
        outputText += "\n  90%:" + this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, true);
        outputTextArrival += this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, false);
        
        flight_time_hours = Convert_TotalSeconds_To_Hours(flight_time80);
        flight_time_minutes = Convert_TotalSeconds_To_Minutes(flight_time80, flight_time_hours);
        flight_time_seconds = Convert_TotalSeconds_To_Seconds(flight_time80, flight_time_hours, flight_time_minutes);
        outputText += "\n  80%:" + this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, true);
        outputTextArrival += this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, false);
        flight_time_hours = Convert_TotalSeconds_To_Hours(flight_time70);
        flight_time_minutes = Convert_TotalSeconds_To_Minutes(flight_time70, flight_time_hours);
        flight_time_seconds = Convert_TotalSeconds_To_Seconds(flight_time70, flight_time_hours, flight_time_minutes);
        outputText += "\n  70%:" + this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, true);
        outputTextArrival += this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, false);
        flight_time_hours = Convert_TotalSeconds_To_Hours(flight_time60);
        flight_time_minutes = Convert_TotalSeconds_To_Minutes(flight_time60, flight_time_hours);
        flight_time_seconds = Convert_TotalSeconds_To_Seconds(flight_time60, flight_time_hours, flight_time_minutes);
        outputText += "\n  60%:" + this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, true);
        outputTextArrival += this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, false);
        flight_time_hours = Convert_TotalSeconds_To_Hours(flight_time50);
        flight_time_minutes = Convert_TotalSeconds_To_Minutes(flight_time50, flight_time_hours);
        flight_time_seconds = Convert_TotalSeconds_To_Seconds(flight_time50, flight_time_hours, flight_time_minutes);
        outputText += "\n  50%:" + this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, true);
        outputTextArrival += this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, false);
        flight_time_hours = Convert_TotalSeconds_To_Hours(flight_time40);
        flight_time_minutes = Convert_TotalSeconds_To_Minutes(flight_time40, flight_time_hours);
        flight_time_seconds = Convert_TotalSeconds_To_Seconds(flight_time40, flight_time_hours, flight_time_minutes);
        outputText += "\n  40%:" + this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, true);
        outputTextArrival += this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, false);
        flight_time_hours = Convert_TotalSeconds_To_Hours(flight_time30);
        flight_time_minutes = Convert_TotalSeconds_To_Minutes(flight_time30, flight_time_hours);
        flight_time_seconds = Convert_TotalSeconds_To_Seconds(flight_time30, flight_time_hours, flight_time_minutes);
        outputText += "\n  30%:" + this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, true);
        outputTextArrival += this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, false);
        flight_time_hours = Convert_TotalSeconds_To_Hours(flight_time20);
        flight_time_minutes = Convert_TotalSeconds_To_Minutes(flight_time20, flight_time_hours);
        flight_time_seconds = Convert_TotalSeconds_To_Seconds(flight_time20, flight_time_hours, flight_time_minutes);
        outputText += "\n  20%:" + this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, true);
        outputTextArrival += this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, false);
        flight_time_hours = Convert_TotalSeconds_To_Hours(flight_time10);
        flight_time_minutes = Convert_TotalSeconds_To_Minutes(flight_time10, flight_time_hours);
        flight_time_seconds = Convert_TotalSeconds_To_Seconds(flight_time10, flight_time_hours, flight_time_minutes);
        outputText += "\n 10%:" + this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, true);
        outputTextArrival += this.GetStringFromRemainingTimeAtRecall(flight_time_hours, flight_time_minutes, flight_time_seconds, false);


        this.output = outputText;
        this.outputArrival = outputTextArrival;
        return 0;
    }

    ///////////////////////////////////////////////////
    /* Lowest Speed*/
    ///////////////////////////////////////////////////

    int Calculate_Slowest_Ship_Speed ()
    {
        double actual_ship_speed_SC = 0;  //comb-imp
        double actual_ship_speed_LC = 0;  //comb
        double actual_ship_speed_LF = 0;  //comb
        double actual_ship_speed_HF = 0;  //imp
        double actual_ship_speed_CR = 0;  //imp
        double actual_ship_speed_BS = 0;  //hyp
        double actual_ship_speed_COL = 0; //imp
        double actual_ship_speed_REC = 0; //comb
        double actual_ship_speed_SPY = 0; //comb
        double actual_ship_speed_BB = 0;  //imp-hyp
        double actual_ship_speed_DS = 0;  //hyp
        double actual_ship_speed_RIP = 0; //hyp
        double actual_ship_speed_BC = 0;  //hyp

        int	slowest_ship = 0;
        double slowest_ship_speed = 100000000; 	// 390

        if (SC)
        {
	        if (Impulse_drive_level >= 5) 	// Impulse drive used
	        {
		        actual_ship_speed_SC = ship_speed_SC_high_impulse5 * ship_speed_factor_imp ;
		        actual_ship_speed_SC = actual_ship_speed_SC + (use_general?1.0:0.0) * 0.2 * ship_speed_SC_high_impulse5;
	        }
	        else				 // Combustion drive used
	        {
		        actual_ship_speed_SC = ship_speed_SC_low_impulse5 * ship_speed_factor_com ;
		        actual_ship_speed_SC = actual_ship_speed_SC  + (use_general?1.0:0.0) * 0.2 * ship_speed_SC_low_impulse5;
	        }
	        slowest_ship_speed = actual_ship_speed_SC;
	        slowest_ship = 1;	
        }

        if (LC)
        {
	        actual_ship_speed_LC = ship_speed_LC * ship_speed_factor_com;
	        actual_ship_speed_LC = actual_ship_speed_LC  + (use_general?1.0:0.0) * 0.2 * ship_speed_LC;
	        if (actual_ship_speed_LC < slowest_ship_speed)
	        {
		        slowest_ship_speed = actual_ship_speed_LC;
		        slowest_ship = 2;
	        }

        }

        if (LF)
        {
	        actual_ship_speed_LF = ship_speed_LF * ship_speed_factor_com;
	        actual_ship_speed_LF = actual_ship_speed_LF  + (use_general?1.0:0.0) * 0.2* ship_speed_LF;
	        if (actual_ship_speed_LF < slowest_ship_speed)
	        {
		        slowest_ship_speed = actual_ship_speed_LF;
		        slowest_ship = 3;
	        }

        }
        if (HF)
        {
	        actual_ship_speed_HF = ship_speed_HF * ship_speed_factor_imp;
	        actual_ship_speed_HF = actual_ship_speed_HF  + (use_general?1.0:0.0) * 0.2 * ship_speed_HF;
	        if (actual_ship_speed_HF < slowest_ship_speed)
	        {
		        slowest_ship_speed = actual_ship_speed_HF;
		        slowest_ship = 4;
	        }
        }
        if (CR)
        {
	        actual_ship_speed_CR = ship_speed_CR * ship_speed_factor_imp;
	        actual_ship_speed_CR = actual_ship_speed_CR  + (use_general?1.0:0.0) * 0.2 * ship_speed_CR;
	        if (actual_ship_speed_CR < slowest_ship_speed)
	        {
		        slowest_ship_speed = actual_ship_speed_CR;
		        slowest_ship = 5;
	        }
        }
        if (BS)
        {
	        actual_ship_speed_BS = ship_speed_BS * ship_speed_factor_hyp;
	        actual_ship_speed_BS = actual_ship_speed_BS  + (use_general?1.0:0.0) * 0.2 * ship_speed_BS;
	        if (actual_ship_speed_BS < slowest_ship_speed)
	        {
		        slowest_ship_speed = actual_ship_speed_BS;
		        slowest_ship = 6;
	        }
        }
        if (COL)
        {
	        actual_ship_speed_COL = ship_speed_COL * ship_speed_factor_imp;
	        actual_ship_speed_COL = actual_ship_speed_COL  + (use_general?1.0:0.0) * 0.2 * ship_speed_COL;
	        if (actual_ship_speed_COL < slowest_ship_speed)
	        {
		        slowest_ship_speed = actual_ship_speed_COL;
		        slowest_ship = 7;
	        }
        }
        if (REC)
        {
	        actual_ship_speed_REC = ship_speed_REC * ship_speed_factor_com;
	        actual_ship_speed_REC = actual_ship_speed_REC  + (use_general?1.0:0.0) * 0.2 * ship_speed_REC;
	        if (actual_ship_speed_REC < slowest_ship_speed)
	        {
		        slowest_ship_speed = actual_ship_speed_REC;
		        slowest_ship = 8;
	        }
        }
        if (SPY)
        {
	        actual_ship_speed_SPY = ship_speed_SPY * ship_speed_factor_com;
	        actual_ship_speed_SPY = actual_ship_speed_SPY  + (use_general?1.0:0.0) * 0.2 * ship_speed_SPY;
	        if (actual_ship_speed_SPY < slowest_ship_speed)
	        {
		        slowest_ship_speed = actual_ship_speed_SPY ;
		        slowest_ship = 9;
	        }
        }
        if (BB)
        {
	        if (Hyperspace_drive_level >= 5) // Hyperspace drive used
	        {
		        actual_ship_speed_BB = ship_speed_BB_high_hyper8 * ship_speed_factor_hyp;
		        actual_ship_speed_BB = actual_ship_speed_BB  + (use_general?1.0:0.0) * 0.2 * ship_speed_BB_high_hyper8;
	        }
	        else				 // Impulse drive used
	        {
		        actual_ship_speed_BB = ship_speed_BB_low_hyper8 * ship_speed_factor_imp;
		        actual_ship_speed_BB = actual_ship_speed_BB  + (use_general?1.0:0.0) * 0.2 * ship_speed_BB_low_hyper8;
	        }
	        if (actual_ship_speed_BB < slowest_ship_speed)
	        {
		        slowest_ship_speed = actual_ship_speed_BB ;
		        slowest_ship = 10;
	        }

        }
        if (DS)
        {
	        actual_ship_speed_DS = ship_speed_DS * ship_speed_factor_hyp;
	        actual_ship_speed_DS = actual_ship_speed_DS  + (use_general?1.0:0.0) * 0.2 * ship_speed_DS;
	        if (actual_ship_speed_DS < slowest_ship_speed)
	        {
		        slowest_ship_speed = actual_ship_speed_DS ;
		        slowest_ship = 11;
	        }
        }
        if (RIP)
        {
	        actual_ship_speed_RIP = ship_speed_RIP * ship_speed_factor_hyp;
	        actual_ship_speed_RIP = actual_ship_speed_RIP  + (use_general?1.0:0.0) * 0.2 * ship_speed_RIP;
	        if (actual_ship_speed_RIP < slowest_ship_speed)
	        {
		        slowest_ship_speed = actual_ship_speed_RIP ;
		        slowest_ship = 12;
	        }
        }
        if (BC)
        {
	        actual_ship_speed_BC = ship_speed_BC * ship_speed_factor_hyp;
	        actual_ship_speed_BC = actual_ship_speed_BC  + (use_general?1.0:0.0) * 0.2 * ship_speed_BC;
	        if (actual_ship_speed_BC < slowest_ship_speed)
	        {
		        slowest_ship_speed = actual_ship_speed_BC ;
		        slowest_ship = 13;
	        }
        }
        
        if (slowest_ship == 0)
        	return 0;
        return (int)Math.round(slowest_ship_speed);
    }

    ///////////////////////////////////////////////////
    /* Distance calculation */
    ///////////////////////////////////////////////////

    double Calculate_Distance ()
    {
        double distance = 0;
        if (targ_gal != orig_gal)
        {
	        distance = 20000 * Math.abs(targ_gal - orig_gal);
        }
        else
        {
	        if (targ_sys != orig_sys)
	        {
		        distance = 2700 + (95 * Math.abs(targ_sys - orig_sys));
	        }
	        else
	        {
		        if (targ_pla != orig_pla)
		        {
			        distance = 1000 + (5 * Math.abs(targ_pla - orig_pla));
		        }
		        else
		        {
			        distance = 5;
		        }        		        	
	        }
        }
        return distance;
    }
    ///////////////////////////////////////////////////
    /* Flight time */
    ///////////////////////////////////////////////////
    int Calculate_Flight_Time (int fleet_Speed_Fraction, int slowest_ship_speed, double distance)
    {
        double flight_time = 0;
        flight_time = (35000 / fleet_Speed_Fraction * Math.pow(distance * 1000 / slowest_ship_speed, 0.5) + 10) / universe_speed / event_factor; 

        return  (int) Math.round(flight_time);// in seconds
    }

    int Convert_TotalSeconds_To_Hours (int totalseconds)
    {
        return (int)Math.floor ((double)(totalseconds / 3600));
    }
    int Convert_TotalSeconds_To_Minutes (int totalseconds, int flight_time_hours)
    {
        return (int)Math.floor ((double)(totalseconds / 60 - flight_time_hours * 60));
    }
    int Convert_TotalSeconds_To_Seconds (int totalseconds, int flight_time_hours, int flight_time_minutes)
    {
        return (int) Math.floor ((double)(totalseconds - 60*flight_time_minutes - 3600*flight_time_hours));
    }




    
    public String GetStringFromDepartureTime (int durationDays, int flight_time_hours, int flight_time_minutes, int flight_time_seconds)
    {
        Time now = new Time();
        now.setToNow();        
        
        Calendar calendar_departureTime = Calendar.getInstance(); 
        calendar_departureTime.set(now.year, now.month, now.monthDay,  departureTime.hour, departureTime.minute, departureTime.second);
	        
			Calendar tmp = (Calendar) calendar_departureTime.clone();
			tmp.add(Calendar.DAY_OF_MONTH, durationDays);
			tmp.add(Calendar.HOUR_OF_DAY, flight_time_hours);
			tmp.add(Calendar.MINUTE, flight_time_minutes);
			tmp.add(Calendar.SECOND, flight_time_seconds);

        Time arrivalTime = new Time();       
		arrivalTime.set(tmp.get(Calendar.SECOND), tmp.get(Calendar.MINUTE), tmp.get(Calendar.HOUR), tmp.get(Calendar.DAY_OF_MONTH), tmp.get(Calendar.MONTH), tmp.get(Calendar.YEAR));
		
		int daysDiff = arrivalTime.monthDay - departureTime.monthDay;
		if (tmp.get (Calendar.HOUR_OF_DAY) >= 12)
		{
			if (arrivalTime.hour == 0)
				return (" " + arrivalTime.format("12:%M:%SPM (+") + daysDiff + ")\n");
			else
				return (" " + arrivalTime.format("%H:%M:%SPM (+") + daysDiff + ")\n");				
		}
		else
			return (" " + arrivalTime.format("%H:%M:%SAM (+") + daysDiff + ")\n");
    }
    
    


    
    public String GetStringFromRemainingTimeAtRecall (int flight_time_hours, int flight_time_minutes, int flight_time_seconds, Boolean returnDuration)
    {
        Time now = new Time();
        now.setToNow();        
        
        int flightDurationInSeconds = flight_time_seconds + 60*flight_time_minutes + 3600*flight_time_hours;
        int remainingSecondsAtRecall = remainingTime.second + 60*remainingTime.minute + 3600*remainingTime.hour + 86400*remainingTime.monthDay;
        int remainingSecondsBeforeReturn = flightDurationInSeconds - remainingSecondsAtRecall;
        
		if (remainingSecondsBeforeReturn <= 0 && returnDuration)
			return ("--------");
		else if (remainingSecondsBeforeReturn <= 0 && !returnDuration)
			return ("--------\n");

        Calendar aCalendar = Calendar.getInstance(); 
        aCalendar.set(now.year, now.month, now.monthDay,  0, 0, 0);
		Calendar tmp = (Calendar) aCalendar.clone();
		tmp.add(Calendar.SECOND, remainingSecondsBeforeReturn);
		
        Time remainingTimeToArrival = new Time();       
        remainingTimeToArrival.set(tmp.get(Calendar.SECOND), tmp.get(Calendar.MINUTE), tmp.get(Calendar.HOUR), tmp.get(Calendar.DAY_OF_MONTH), tmp.get(Calendar.MONTH), tmp.get(Calendar.YEAR));

        int durD=(int) Math.floor(remainingSecondsBeforeReturn/86400);
        int durH=(int) Math.floor((remainingSecondsBeforeReturn-durD*86400)/3600);
        int durM=(int) Math.floor((remainingSecondsBeforeReturn-durH*3600-durD*86400)/60);
        int durS=(int) Math.round(remainingSecondsBeforeReturn - durM*60-durH*3600-durD*86400);
        
		int daysDiff = remainingTimeToArrival.monthDay - now.monthDay;
        if (returnDuration)
        	return durD + "d" + String.format("%02d", durH) +"h"+ String.format("%02d", durM)+"m"+ String.format("%02d", durS) +"s";
        else
        	return GetStringFromDepartureTime (durD,durH,durM,durS);	
    
		//return ("\t\t" + remainingTimeToArrival.format("%H:%M:%S\n"));
    }
    

    public Integer tryParse(String number) {
    	  Integer retVal;
    	  try {
    	    retVal = Integer.parseInt(number);
    	  } catch (NumberFormatException nfe) {
    	    retVal = -1; // or null if that is your preference
    	  }
    	  return retVal;
    	}    

    public double tryParseDouble(String number) {
    	  double retVal;
    	  try {
    	    retVal = Double.parseDouble(number);
    	  } catch (NumberFormatException nfe) {
    	    retVal = -1; // or null if that is your preference
    	  }
    	  return retVal;
    	}    
    
    
}
