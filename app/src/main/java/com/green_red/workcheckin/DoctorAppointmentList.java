package com.green_red.workcheckin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class DoctorAppointmentList extends AppCompatActivity implements onItemClickListener{

    private List<Appointment> appointmentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AppintmentListAdeptar mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment_list);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mAdapter = new AppintmentListAdeptar(appointmentList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        try {
            prepareUserData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void prepareUserData() throws JSONException {
        //URL of the request we are sending
        String url = "http://10.0.2.2:8000/appointment_list/";
        final ProgressDialog pd = new ProgressDialog(DoctorAppointmentList.this);
        pd.setMessage("connecting");
        pd.show();
        JSONObject postparams = new JSONObject();
        postparams.put("user", "");
        postparams.put("password", "");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, postparams,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.d("response",response.toString());
                        Gson gson = new GsonBuilder().create();
                        AllApponitmentLIstPojo allQueuePOJO = gson.fromJson(response.toString(), AllApponitmentLIstPojo.class);
                        appointmentList = allQueuePOJO.appointments;
                        Log.d("userList",Integer.toString(appointmentList.size()));
                        mAdapter.setData(appointmentList);
                        pd.dismiss();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("response",error.toString());
                        // Toast.makeText(getApplicationContext(),"Please insert correct email or password",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });
// Adding the request to the queue along with a unique string tag
        MyApplication.getInstance().addToRequestQueue(jsonObjReq, "postRequest");
    }


    @Override
    public void onItemClick(int position) {
// create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("User Details");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.appointment_dialogue, null);
        TextView name, email;
        Button write;
        name = (TextView) customLayout.findViewById(R.id.name);
        email = (TextView) customLayout.findViewById(R.id.email);
        write = (Button) customLayout.findViewById(R.id.write);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorAppointmentList.this, WritePrescription.class);
                startActivity(intent);
            }
        });
        builder.setView(customLayout);
        name.setText(mAdapter.getItem(position).getName());
        email.setText(mAdapter.getItem(position).app_date);
        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                dialog.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
