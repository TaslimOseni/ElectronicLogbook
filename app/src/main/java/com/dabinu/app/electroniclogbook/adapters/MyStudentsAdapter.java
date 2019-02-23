package com.dabinu.app.electroniclogbook.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.models.User;
import java.util.ArrayList;


public class MyStudentsAdapter extends RecyclerView.Adapter<MyStudentsAdapter.MyViewHolder>{


    Context context;
    ArrayList<User> objects;
    private static ClickListener clickListener;


    public MyStudentsAdapter(Context context, ArrayList<User> objects){
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.student_layout, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position){

        holder.name.setText(String.format("%s (%s)", objects.get(position).getFullname(), objects.get(position).getMatric()));
        holder.department.setText(objects.get(position).getDepartment());
        holder.faculty.setText(objects.get(position).getFaculty());

    }

    @Override
    public int getItemCount(){
        return objects.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{


        public TextView name, department, faculty;

        public MyViewHolder(View itemView){
            super(itemView);


            name = itemView.findViewById(R.id.name);
            department = itemView.findViewById(R.id.department);
            faculty = itemView.findViewById(R.id.faculty);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v){
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v){
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }


    }

    public void setOnItemClickListener(ClickListener clickListener){
        MyStudentsAdapter.clickListener = clickListener;
    }


    public interface ClickListener{
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }


}