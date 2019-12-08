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

public class UserList extends AppCompatActivity implements onItemClickListener {
    private List<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserListAdeptar mAdapter;
    Button add_user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        add_user = (Button)findViewById(R.id.add_user);
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserList.this, AddUser.class);
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mAdapter = new UserListAdeptar(userList, this);
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
        String url = "http://10.0.2.2:8000/user_list/";
        final ProgressDialog pd = new ProgressDialog(UserList.this);
        pd.setMessage("connecting and trying to logging in");
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
                        AllUserLIstPojo allQueuePOJO = gson.fromJson(response.toString(), AllUserLIstPojo.class);
                        userList = allQueuePOJO.users;
                        Log.d("userList",Integer.toString(userList.size()));
                        mAdapter.setData(userList);
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
        final View customLayout = getLayoutInflater().inflate(R.layout.user_profile_dialogue, null);
        TextView name, email;
        name = (TextView) customLayout.findViewById(R.id.name);
        email = (TextView) customLayout.findViewById(R.id.email);
        builder.setView(customLayout);
        name.setText(mAdapter.getItem(position).user_name);
        email.setText(mAdapter.getItem(position).email);
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
