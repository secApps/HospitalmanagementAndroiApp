package com.green_red.workcheckin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddAppointment extends AppCompatActivity {
    TextView first_name, last_name;
    TextView a_date;
    Button submit;
    Calendar date;
    int selected_doctor =0;
    Spinner doctor;
    private ArrayList<Doctor> doctors_list;
    String patient_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_appointment);
        Intent intent = getIntent();
        patient_id = intent.getStringExtra("id");
        first_name = (TextView)findViewById(R.id.user_name);
        last_name = (TextView)findViewById(R.id.mobile);
        a_date = (TextView) findViewById(R.id.date);
        a_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });
        doctor = (Spinner)findViewById(R.id.doctor);
        submit = (Button)findViewById(R.id.submit);
        doctors_list = new ArrayList<Doctor>();
        doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_doctor = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addDoctors();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    addAppointment(a_date.getText().toString(), doctors_list.get(selected_doctor).user_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    public void addAppointment( String date, int doctor) throws JSONException {


        //URL of the request we are sending
        String url = "http://10.0.2.2:8000/update_appointments/";
        final ProgressDialog pd = new ProgressDialog(AddAppointment.this);
        pd.setMessage("Connecting");
        pd.show();
        JSONObject postparams = new JSONObject();
        postparams.put("patient_id", patient_id);
        postparams.put("doctor_id", doctor);
        postparams.put("date", date);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, postparams,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Gson gson = new GsonBuilder().create();
                        UserPojo userpojo = gson.fromJson(response.toString(), UserPojo.class);
                        Log.d("response",response.toString());
                        // Toast.makeText(getApplicationContext(),"Successfully Logged in",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        if(userpojo.status.equals("true")){

                            Intent intent = new Intent(AddAppointment.this,Add_Patient.class);
                            intent.putExtra("id",userpojo.id);
                            startActivity(intent);

                        }else{
                            Toast.makeText(getApplicationContext(),"Something went wrong! Check your input",Toast.LENGTH_SHORT).show();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("response",error.toString());
                        Toast.makeText(getApplicationContext(),"Please insert correct email or password",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });
// Adding the request to the queue along with a unique string tag
        MyApplication.getInstance().addToRequestQueue(jsonObjReq, "postRequest");

    }

    public void addDoctors(){
        //URL of the request we are sending
        String url = "http://10.0.2.2:8000/doctor_list/";
        final ProgressDialog pd = new ProgressDialog(AddAppointment.this);
        pd.setMessage("Collecting All Doctors");
        pd.show();
        JSONObject postparams = new JSONObject();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, postparams,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Gson gson = new GsonBuilder().create();
                        RolesPojo rolespojo = gson.fromJson(response.toString(), RolesPojo.class);
                        Log.d("response",response.toString());
                        // Toast.makeText(getApplicationContext(),"Successfully Logged in",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        doctors_list = rolespojo.doctor;
                        populateSpinner();




                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("response",error.toString());
                        Toast.makeText(getApplicationContext(),"Something went wrong in loading roles",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });
// Adding the request to the queue along with a unique string tag
        MyApplication.getInstance().addToRequestQueue(jsonObjReq, "postRequest");
    }

    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();


        for (int i = 0; i < doctors_list.size(); i++) {
            lables.add(doctors_list.get(i).getName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        doctor.setAdapter(spinnerAdapter);
    }


    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(AddAppointment.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(AddAppointment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.v("GOT THE DATE TIME", "The choosen one " + date.getTime());
                        a_date.setText(date.getTime().toString());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
}
