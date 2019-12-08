package com.green_red.workcheckin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AppintmentListAdeptar extends RecyclerView.Adapter<AppintmentListAdeptar.MyViewHolder>{

    private List<Appointment> appointmentList;
    public onItemClickListener listener;

    public AppintmentListAdeptar(List<Appointment> userList, onItemClickListener listener) {
        this.appointmentList = userList;
        this.listener =listener;
    }

    public void setData(List<Appointment> userList){
        this.appointmentList = userList;
        notifyDataSetChanged();

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Appointment movie = appointmentList.get(position);
        holder.name.setText(movie.getName());
        holder.app_date.setText(movie.app_date);
        holder.address.setText(movie.address);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });
       // holder.role.setText(movie.role_id);

    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public Appointment  getItem(int position){
        return appointmentList.get(position);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, app_date, address;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            app_date = (TextView) view.findViewById(R.id.app_date);
            address = (TextView) view.findViewById(R.id.address);
        }
    }
}
