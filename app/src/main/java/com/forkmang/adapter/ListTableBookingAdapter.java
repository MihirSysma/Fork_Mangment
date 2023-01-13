package com.forkmang.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.activity.BookingTable_DetailView;
import com.forkmang.models.TableList;

import java.util.ArrayList;


public class ListTableBookingAdapter extends RecyclerView.Adapter<ListTableBookingAdapter.ListBookTableItemHolder> {
    Activity activity;
    ArrayList<TableList> listTableBooking;

    public ListTableBookingAdapter(Activity activity, ArrayList<TableList> listTableBooking) {
        this.activity = activity;
        this.listTableBooking = listTableBooking;
    }

    public ListTableBookingAdapter(Activity activity) {
        this.activity = activity;

    }


    @NonNull
    @Override
    public ListBookTableItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_list_cell, null);
        ListBookTableItemHolder listBookTableItemHolder = new ListBookTableItemHolder(v);
        return listBookTableItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListBookTableItemHolder holder, int position) {
        TableList tableList = listTableBooking.get(position);
        holder.txt_table_no.setText(tableList.getTable_no());
        holder.txt_noperson.setText(tableList.getNumber_of_person());
        holder.txt_type.setText(tableList.getType());
      }

    @Override
    public int getItemCount(){
       return listTableBooking.size();

    }

    public class ListBookTableItemHolder extends RecyclerView.ViewHolder {
        TextView txt_type, txt_noperson, txt_table_no;
        RelativeLayout relative_view;
        public ListBookTableItemHolder(@NonNull View itemView) {
            super(itemView);
            relative_view = itemView.findViewById(R.id.relative_view);
            txt_type = itemView.findViewById(R.id.txt_type);
            txt_noperson = itemView.findViewById(R.id.txt_noperson);
            txt_table_no = itemView.findViewById(R.id.txt_table_no);

            relative_view.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                TableList tableList = listTableBooking.get(position);
                ((BookingTable_DetailView)activity).showAlertView_tableselctionrule(tableList);

                //activity.finish();
            });
        }
    }

}
