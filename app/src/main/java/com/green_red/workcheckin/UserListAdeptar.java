package com.green_red.workcheckin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserListAdeptar extends RecyclerView.Adapter<UserListAdeptar.MyViewHolder>{

    private List<User> userList;
    public onItemClickListener listener;

    public UserListAdeptar(List<User> userList, onItemClickListener listener) {
        this.userList = userList;
        this.listener =listener;
    }

    public void setData(List<User> userList){
        this.userList = userList;
        notifyDataSetChanged();

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        User movie = userList.get(position);
        holder.name.setText(movie.getName());
        holder.email.setText(movie.email);
        holder.role.setText(movie.name);
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
        return userList.size();
    }

    public User  getItem(int position){
        return userList.get(position);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email, role;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            email = (TextView) view.findViewById(R.id.email);
            role = (TextView) view.findViewById(R.id.role);
        }
    }
}
