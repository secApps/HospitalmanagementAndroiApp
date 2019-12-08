package com.green_red.workcheckin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.List;

public class WritePrescription extends AppCompatActivity {
    TextView name,age,email;
    EditText notes,advices;
    Button submit;
    int selected_diagnosis =0;
    int selected_medicine =0;
    Spinner diagnosis,medicine ;
    private ArrayList<Role> medicine_list;
    private ArrayList<Role> diagnosis_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_prescription);
        email = (TextView) findViewById(R.id.email);
        name = (TextView) findViewById(R.id.name);
        age  = (TextView) findViewById(R.id.age);
        advices  = (EditText) findViewById(R.id.advices);
        notes = (EditText)findViewById(R.id.notes);
        diagnosis = (Spinner)findViewById(R.id.diagnosis);
        medicine = (Spinner)findViewById(R.id.medicine);
        submit = (Button)findViewById(R.id.submit);
        medicine_list = new ArrayList<Role>();
        diagnosis_list = new ArrayList<Role>();
        medicine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_medicine = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        diagnosis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_diagnosis = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addDiagnosis();
        addMedicines();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addPrescription(advices.getText().toString(),diagnosis_list.get(selected_diagnosis).id,medicine_list.get(selected_diagnosis).id,notes.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    public void addPrescription(String advice, int diagnosis, int medicine, String notes) throws JSONException {


        //URL of the request we are sending
        String url = "http://10.0.2.2:8000/update_prescription/";
        final ProgressDialog pd = new ProgressDialog(WritePrescription.this);
        pd.setMessage("Connecting");
        pd.show();
        JSONObject postparams = new JSONObject();
        postparams.put("advice", advice);
        postparams.put("diagnosis", diagnosis);
        postparams.put("medicine", medicine);
        postparams.put("notes", notes);
        postparams.put("patient_id", 1);
        postparams.put("doctor_id", 1);
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

                            Intent intent = new Intent(WritePrescription.this,Add_Patient.class);
                            Log.d("iddd",Integer.toString(userpojo.id));
                            intent.putExtra("id",Integer.toString(userpojo.id));
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

    public void addDiagnosis(){
        //URL of the request we are sending
        String url = "http://10.0.2.2:8000/diagnosis_list/";
        final ProgressDialog pd = new ProgressDialog(WritePrescription.this);
        pd.setMessage("Collecting All Roles");
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
                        diagnosis_list = rolespojo.diagnosis;
                        populateDiagnosisSpinner();




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

    public void addMedicines(){
        //URL of the request we are sending
        String url = "http://10.0.2.2:8000/medicine_list/";
        final ProgressDialog pd = new ProgressDialog(WritePrescription.this);
        pd.setMessage("Collecting All Medicines");
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
                        medicine_list = rolespojo.medicines;
                        populateMedicineSpinner();




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

    private void populateDiagnosisSpinner() {
        List<String> lables = new ArrayList<String>();


        for (int i = 0; i < diagnosis_list.size(); i++) {
            lables.add(diagnosis_list.get(i).name);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        diagnosis.setAdapter(spinnerAdapter);
    }

    private void populateMedicineSpinner() {
        List<String> lables = new ArrayList<String>();


        for (int i = 0; i < diagnosis_list.size(); i++) {
            lables.add(diagnosis_list.get(i).name);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        diagnosis.setAdapter(spinnerAdapter);
    }
}
