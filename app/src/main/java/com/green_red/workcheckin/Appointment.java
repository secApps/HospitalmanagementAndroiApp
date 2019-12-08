package com.green_red.workcheckin;

public class Appointment {
         public int      id;
         public String   user_id;
         public String  current_phone;
         public String  ssn;
         public String   first_name;
         public String  last_name;
         public String   address;
         public String     dob;
         public String   created_at;
         public String   patient_id;
         public String  doctor_id;
         public String   app_date;
         public String   speciality;
         public String getName(){

             return first_name + " "+ last_name;

         }
}
