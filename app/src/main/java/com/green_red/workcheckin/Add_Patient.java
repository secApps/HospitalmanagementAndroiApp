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

public class Add_Patient extends AppCompatActivity {

    EditText ssn, first_name, last_name,address;
    Button submit;
    String user_id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_patient);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("id");
        Log.d("user_id","lk"+user_id);
        ssn = (EditText)findViewById(R.id.ssn);
        first_name = (EditText)findViewById(R.id.first_name);
        last_name = (EditText)findViewById(R.id.last_name);
        address = (EditText) findViewById(R.id.address);
        submit = (Button)findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    addPatient(ssn.getText().toString(),first_name.getText().toString(),last_name.getText().toString(),address.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    public void addPatient(String ssn, String first_name, String last_name, String address) throws JSONException {


        //URL of the request we are sending
        String url = "http://10.0.2.2:8000/add_people/";
        final ProgressDialog pd = new ProgressDialog(Add_Patient.this);
        pd.setMessage("Connecting");
        pd.show();
        JSONObject postparams = new JSONObject();
        postparams.put("user_id", user_id);
        postparams.put("f_name", first_name);
        postparams.put("l_name", last_name);
        postparams.put("address", address);
        postparams.put("ssn", ssn);
        postparams.put("who", "Patient");
        Log.d("parameters",postparams.toString());

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

                            Intent intent = new Intent(Add_Patient.this, AddAppointment.class);
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
                        Toast.makeText(getApplicationContext(),"Please insert correct input",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });
// Adding the request to the queue along with a unique string tag
        MyApplication.getInstance().addToRequestQueue(jsonObjReq, "postRequest");

    }



}
