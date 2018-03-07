package com.example.su.readphonecontact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by su on 6/11/17.
 */

public class SelectedContactAdapter extends RecyclerView.Adapter<SelectedContactAdapter.MyViewHolder> {

    Context ctx;


    ArrayList<Contact> alist=new ArrayList<Contact>();

    public SelectedContactAdapter(Context ctx, ArrayList<Contact> alist) {
        this.ctx = ctx;
        this.alist = alist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.selectedcontact,parent,false);
        MyViewHolder vhold=new MyViewHolder(v);
        return vhold;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    holder.contactname.setText(alist.get(position).get_name());
        holder.contactnumber.setText(alist.get(position).get_phone_number());

    }

    @Override
    public int getItemCount() {
      //  Log.v("alist size",String.valueOf(alist.size()));
        return alist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView contactname,contactnumber;
        public MyViewHolder(View itemView) {
            super(itemView);
            contactname=(TextView)itemView.findViewById(R.id.tv_contactname);
            contactnumber=(TextView)itemView.findViewById(R.id.tv_contactnumber);
        }
    }
}
