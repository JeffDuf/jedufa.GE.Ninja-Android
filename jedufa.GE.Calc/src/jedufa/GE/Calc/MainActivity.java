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

import jedufa.GE.Calc.R;

import android.os.Bundle;
import android.app.Activity;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

    //User Inputs
    Boolean	SC = false; Boolean	LC = false; Boolean	LF = false; Boolean HF = false; Boolean	CR = false; 
    Boolean	BS = false; Boolean COL = false;Boolean	REC = false;
    Boolean	SPY = false; Boolean BB = false; Boolean DS = false; Boolean RIP = false; Boolean BC = false;    
    String orig_gal = "";    String orig_sys = "";    String orig_pla = "";//8
    String targ_gal = "";    String targ_sys = "";    String targ_pla = "";
    boolean   	use_general = false; 			//1
    String		event_factor = ""; 			//2
    int Hyperspace_drive_level = 0; int Impulse_drive_level = 0; int Combustion_drive_level = 0;
    Time departureTime;
    Time remainingTime;
        

    int ReadUserInputs ()
    {
    	
        String departure_time_H = ((Spinner) findViewById (R.id.spinner_Departhours)).getSelectedItem().toString();
        String departure_time_M = ((Spinner) findViewById (R.id.spinner_Departminutes)).getSelectedItem().toString();
        String departure_time_S = ((Spinner) findViewById (R.id.spinner_Departseconds)).getSelectedItem().toString();

        String remaining_time_D = ((Spinner) findViewById (R.id.spinner_Remainingdays)).getSelectedItem().toString();
        String remaining_time_H = ((Spinner) findViewById (R.id.spinner_Remaininghours)).getSelectedItem().toString();
        String remaining_time_M = ((Spinner) findViewById (R.id.spinner_Remainingminutes)).getSelectedItem().toString();
        String remaining_time_S = ((Spinner) findViewById (R.id.spinner_Remainingseconds)).getSelectedItem().toString();
        
       Time now = new Time();
        now.setToNow();        
     	departureTime = new Time ();
    	departureTime.set(Integer.parseInt(departure_time_S), Integer.parseInt(departure_time_M), Integer.parseInt(departure_time_H), now.monthDay, now.month, now.year);        	  

    	remainingTime = new Time ();
    	remainingTime.set(Integer.parseInt(remaining_time_S), Integer.parseInt(remaining_time_M), Integer.parseInt(remaining_time_H), Integer.parseInt(remaining_time_D), now.month, now.year);    
    	
    	
        SC = ((CheckBox) findViewById (R.id.CheckBoxSC)).isChecked();
        LC = ((CheckBox) findViewById (R.id.CheckBoxLC)).isChecked();
        LF = ((CheckBox) findViewById (R.id.CheckBoxLF)).isChecked();
        HF = ((CheckBox) findViewById (R.id.CheckBoxHF)).isChecked();
        CR = ((CheckBox) findViewById (R.id.CheckBoxCR)).isChecked();
        BS = ((CheckBox) findViewById (R.id.CheckBoxBS)).isChecked();
        COL =((CheckBox) findViewById (R.id.CheckBoxCS)).isChecked();
        REC =((CheckBox) findViewById (R.id.CheckBoxRC)).isChecked();
        SPY = false;
        BB = ((CheckBox) findViewById (R.id.checkBoxBB)).isChecked();
        DS = ((CheckBox) findViewById (R.id.CheckBoxDS)).isChecked();
        RIP =((CheckBox) findViewById (R.id.CheckBoxRIP)).isChecked();
        BC = ((CheckBox) findViewById (R.id.CheckBoxBC)).isChecked();

        use_general    = ((CheckBox) findViewById (R.id.checkBoxGENERAL)).isChecked();
        
        Hyperspace_drive_level = Integer.parseInt(((Spinner) findViewById (R.id.spinner1)).getSelectedItem().toString());
        Impulse_drive_level = Integer.parseInt(((Spinner) findViewById (R.id.spinner2)).getSelectedItem().toString());      
        Combustion_drive_level = Integer.parseInt(((Spinner) findViewById (R.id.spinner3)).getSelectedItem().toString());
           
        event_factor = ((Spinner) findViewById (R.id.spinner4)).getSelectedItem().toString();


        orig_gal = ((Spinner) findViewById (R.id.spinner_orig_a)).getSelectedItem().toString();
        orig_sys = ((Spinner) findViewById (R.id.spinner_orig_b)).getSelectedItem().toString();
        orig_pla = ((Spinner) findViewById (R.id.spinner_orig_c)).getSelectedItem().toString();
        targ_gal = ((Spinner) findViewById (R.id.spinner_targ_a)).getSelectedItem().toString();
        targ_sys = ((Spinner) findViewById (R.id.spinner_targ_b)).getSelectedItem().toString();
        targ_pla = ((Spinner) findViewById (R.id.spinner_targ_c)).getSelectedItem().toString();
        
        return 0;
    } 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.InitialiseGUIAndEvents();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    Calculations calculationTool = new Calculations ();
    int CallCalculations ()
    {
        final TextView output = ((TextView) findViewById (R.id.textViewOUTPUT));
        final TextView outputArrival = ((TextView) findViewById (R.id.textViewARRIVAL));
    	int error = ReadUserInputs();
        if (error != 0)
        {
            output.setText("Please set your inputs");
    		outputArrival.setText ("");
            return 100;
        }    	
        
        calculationTool.Initialise(
        		SC,LC,LF,HF,CR,BS,COL,REC,BB,DS,RIP,BC,use_general, event_factor, 
        		Hyperspace_drive_level, Impulse_drive_level, Combustion_drive_level, remainingTime,
        		departureTime, orig_gal, orig_sys,orig_pla, targ_gal, targ_sys, targ_pla);	
    	
    	calculationTool.Calculate();
    	
        output.setText(calculationTool.output);
		outputArrival.setText (calculationTool.outputArrival);
    	return 0;
    }


	private void InitialiseGUIAndEvents ()
	{ 
        
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
	     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.levels, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
        spinner.setSelection(14);
	     
	
	     spinner = (Spinner) findViewById(R.id.spinner2);
		 adapter = ArrayAdapter.createFromResource(this, R.array.levels, android.R.layout.simple_spinner_item);
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 spinner.setAdapter(adapter);
	     spinner.setSelection(12);
	  
	
	     spinner = (Spinner) findViewById(R.id.spinner3);
		 adapter = ArrayAdapter.createFromResource(this, R.array.levels, android.R.layout.simple_spinner_item);
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 spinner.setAdapter(adapter);
	     spinner.setSelection(11);
	     
	     spinner = (Spinner) findViewById(R.id.spinner4);
	     adapter = ArrayAdapter.createFromResource(this, R.array.eventFactor, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	     spinner.setSelection(0);
	     
	     
	     
	     



	     spinner = (Spinner) findViewById(R.id.spinner_Remainingdays);
	     adapter = ArrayAdapter.createFromResource(this, R.array.seven, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	     spinner.setSelection(0);
	     spinner = (Spinner) findViewById(R.id.spinner_Remaininghours);
	     adapter = ArrayAdapter.createFromResource(this, R.array.twentyfour, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	     spinner.setSelection(0);
	     spinner = (Spinner) findViewById(R.id.spinner_Remainingminutes);
	     adapter = ArrayAdapter.createFromResource(this, R.array.sixty, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	     spinner.setSelection(0);
	     spinner = (Spinner) findViewById(R.id.spinner_Remainingseconds);
	     adapter = ArrayAdapter.createFromResource(this, R.array.sixty, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	     spinner.setSelection(0);
	     
	     
	     
	     
	     
	     
	     

	     spinner = (Spinner) findViewById(R.id.spinner_Departhours);
	     adapter = ArrayAdapter.createFromResource(this, R.array.twentyfour, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	     spinner.setSelection(0);
	     spinner = (Spinner) findViewById(R.id.spinner_Departminutes);
	     adapter = ArrayAdapter.createFromResource(this, R.array.sixty, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	     spinner.setSelection(0);
	     spinner = (Spinner) findViewById(R.id.spinner_Departseconds);
	     adapter = ArrayAdapter.createFromResource(this, R.array.sixty, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	     spinner.setSelection(0);

	     spinner = (Spinner) findViewById(R.id.spinner_orig_a);
	     adapter = ArrayAdapter.createFromResource(this, R.array.seven, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	     spinner.setSelection(3);
	     spinner = (Spinner) findViewById(R.id.spinner_orig_b);
	     adapter = ArrayAdapter.createFromResource(this, R.array.fivehundred, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	     spinner.setSelection(251);
	     spinner = (Spinner) findViewById(R.id.spinner_orig_c);
	     adapter = ArrayAdapter.createFromResource(this, R.array.fifteen, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	     spinner.setSelection(7);
       

	     spinner = (Spinner) findViewById(R.id.spinner_targ_a);
	     adapter = ArrayAdapter.createFromResource(this, R.array.seven, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	     spinner.setSelection(3);
	     spinner = (Spinner) findViewById(R.id.spinner_targ_b);
	     adapter = ArrayAdapter.createFromResource(this, R.array.fivehundred, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	     spinner.setSelection(251);
	     spinner = (Spinner) findViewById(R.id.spinner_targ_c);
	     adapter = ArrayAdapter.createFromResource(this, R.array.fifteen, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);
	     spinner.setSelection(7);
       

       
       ((Spinner) findViewById (R.id.spinner1)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
				}});
       ((Spinner) findViewById (R.id.spinner2)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});
       ((Spinner) findViewById (R.id.spinner3)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});

       

       ((Spinner) findViewById (R.id.spinner_orig_a)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});
       ((Spinner) findViewById (R.id.spinner_orig_b)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});
       ((Spinner) findViewById (R.id.spinner_orig_c)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});
       ((Spinner) findViewById (R.id.spinner_targ_a)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});
       ((Spinner) findViewById (R.id.spinner_targ_b)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});
       ((Spinner) findViewById (R.id.spinner_targ_c)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});
			
			
       
       
       
       
       
       
       
       


       ((Spinner) findViewById (R.id.spinner_Remainingdays)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }
           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});
       ((Spinner) findViewById (R.id.spinner_Remaininghours)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }
           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});
       ((Spinner) findViewById (R.id.spinner_Remainingminutes)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }
           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});
       ((Spinner) findViewById (R.id.spinner_Remainingseconds)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }
           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});
       
       
       
       
       
       
       
       
       
       
			
			
			
       ((Spinner) findViewById (R.id.spinner_Departhours)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }
           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});
       ((Spinner) findViewById (R.id.spinner_Departminutes)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }
           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});
       ((Spinner) findViewById (R.id.spinner_Departseconds)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }
           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
		}});
       
       
       
       ((Spinner) findViewById (R.id.spinner4)).setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	   CallCalculations ();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parentView) {           
				}});
       

       ((CheckBox) findViewById (R.id.CheckBoxSC)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CallCalculations ();
		}});
       ((CheckBox) findViewById (R.id.CheckBoxLC)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CallCalculations ();
		}});
       ((CheckBox) findViewById (R.id.CheckBoxLF)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CallCalculations ();
		}});
       ((CheckBox) findViewById (R.id.CheckBoxHF)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CallCalculations ();
		}});
       ((CheckBox) findViewById (R.id.CheckBoxCR)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CallCalculations ();
		}});
       ((CheckBox) findViewById (R.id.CheckBoxBS)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CallCalculations ();
		}});
       ((CheckBox) findViewById (R.id.CheckBoxCS)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CallCalculations ();
		}});
       ((CheckBox) findViewById (R.id.CheckBoxRC)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CallCalculations ();
		}});
       ((CheckBox) findViewById (R.id.checkBoxBB)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CallCalculations ();
		}});
       ((CheckBox) findViewById (R.id.CheckBoxDS)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CallCalculations ();
		}});
       ((CheckBox) findViewById (R.id.CheckBoxRIP)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CallCalculations ();
		}});
       ((CheckBox) findViewById (R.id.CheckBoxBC)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CallCalculations ();
		}});

       ((CheckBox) findViewById (R.id.checkBoxGENERAL)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CallCalculations ();
		}});
       ((CheckBox) findViewById (R.id.CheckBoxBC)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CallCalculations ();
		}});
       

	
	}
}