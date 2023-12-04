package com.example.admissionaceapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admissionaceapplication.Activities.QuestionsActivity;
import com.example.admissionaceapplication.Activities.QuestionsActivityEng;
import com.example.admissionaceapplication.Activities.QuestionsActivityLog;
import com.example.admissionaceapplication.Activities.QuestionsActivitySc;
import com.example.admissionaceapplication.Models.SetModel;
import com.example.admissionaceapplication.R;
import com.example.admissionaceapplication.databinding.ItemSetsBinding;

import java.util.ArrayList;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.viewHolder>  {

    Context context;
    ArrayList<SetModel> list;
    String name;

    public SetAdapter(Context context, ArrayList<SetModel> list, String name) {
        this.context = context;
        this.list = list;
        this.name = name;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        if(name.equalsIgnoreCase("Math"))
            view = LayoutInflater.from(context).inflate(R.layout.item_sets,parent,false);
        else if(name.equalsIgnoreCase("Science"))
            view = LayoutInflater.from(context).inflate(R.layout.item_setssc,parent,false);
        else if(name.equalsIgnoreCase("English"))
            view = LayoutInflater.from(context).inflate(R.layout.item_setseng,parent,false);
        else if(name.equalsIgnoreCase("Logic"))
            view = LayoutInflater.from(context).inflate(R.layout.items_setslog,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        final SetModel model = list.get(position);

        holder.binding.setName.setText(model.getSetNmae());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = null;
                if(name.equalsIgnoreCase("Math"))
                {
                    intent = new Intent(context, QuestionsActivity.class);
                }
                else if(name.equalsIgnoreCase("Science"))
                {
                    intent = new Intent(context, QuestionsActivitySc.class);
                }
                else if(name.equalsIgnoreCase("English"))
                {
                    intent = new Intent(context, QuestionsActivityEng.class);
                }
                else if(name.equalsIgnoreCase("Logic"))
                {
                    intent = new Intent(context, QuestionsActivityLog.class);
                }
                intent.putExtra("set",model.getSetNmae());
                context.startActivity(intent);

            }
        })

    ;}

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ItemSetsBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSetsBinding.bind(itemView);

        }
    }
}
