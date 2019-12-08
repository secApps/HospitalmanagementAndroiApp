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

public class AddUser extends AppCompatActivity {
    EditText email, name, mobile, password;
    Button submit;
    int selected_role =0;
    Spinner role;
    private ArrayList<Role> roles_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);
        email = (EditText)findViewById(R.id.email);
        name = (EditText)findViewById(R.id.user_name);
        mobile = (EditText)findViewById(R.id.mobile);
        password = (EditText)findViewById(R.id.input_password);
        role = (Spinner)findViewById(R.id.role_list);
        submit = (Button)findViewById(R.id.submit);
        roles_list = new ArrayList<Role>();
        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              selected_role = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addRoles();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addUser(email.getText().toString(),name.getText().toString(),mobile.getText().toString(),password.getText().toString(),roles_list.get(selected_role).id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    public void addUser(String email, String name, String mobile, String password, int role) throws JSONException {


        //URL of the request we are sending
        String url = "http://10.0.2.2:8000/update_user/";
        final ProgressDialog pd = new ProgressDialog(AddUser.this);
        pd.setMessage("Connecting");
        pd.show();
        JSONObject postparams = new JSONObject();
        postparams.put("email", email);
        postparams.put("password", password);
        postparams.put("name", name);
        postparams.put("mobile", mobile);
        postparams.put("role", role);
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

                                Intent intent = new Intent(AddUser.this,Add_Patient.class);
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

    public void addRoles(){
        //URL of the request we are sending
        String url = "http://10.0.2.2:8000/role_list/";
        final ProgressDialog pd = new ProgressDialog(AddUser.this);
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
                        roles_list = rolespojo.role;
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


        for (int i = 0; i < roles_list.size(); i++) {
            lables.add(roles_list.get(i).name);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        role.setAdapter(spinnerAdapter);
    }
}
