package com.green_red.workcheckin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    String id;
    Button login;
    EditText email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button)findViewById(R.id.btn_login) ;
        email = (EditText)findViewById(R.id.input_email) ;
        password = (EditText)findViewById(R.id.input_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!email.getText().toString().isEmpty()){
                        login(email.getText().toString(),password.getText().toString());
                    }

                } catch (JSONException e) {
                    Log.d("exception",e.toString());
                }
            }
        });





    }

    public void login(String email, String password) throws JSONException {


        //URL of the request we are sending
        String url = "http://10.0.2.2:8000/login/";
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("connecting and trying to logging in");
        pd.show();
        JSONObject postparams = new JSONObject();
        postparams.put("user", email);
        postparams.put("password", password);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, postparams,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Gson gson = new GsonBuilder().create();
                        UserPojo userpojo = gson.fromJson(response.toString(), UserPojo.class);
                        Log.d("response",response.toString());

                        pd.dismiss();
                        if(userpojo.status.equals("true")){
                            Toast.makeText(getApplicationContext(),"Successfully Logged in",Toast.LENGTH_SHORT).show();
                            if(userpojo.data.role_id == 1)
                            {
                                Intent intent = new Intent(MainActivity.this, UserList.class);
                                startActivity(intent);
                            }else if(userpojo.data.role_id == 6){
                                Intent intent = new Intent(MainActivity.this, PatientList.class);
                                startActivity(intent);
                            }else if(userpojo.data.role_id == 7){
                                Intent intent = new Intent(MainActivity.this, DoctorAppointmentList.class);
                                startActivity(intent);
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Login Failed!! Invalid Credentials",Toast.LENGTH_SHORT).show();
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

}
