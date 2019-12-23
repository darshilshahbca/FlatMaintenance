package com.example.flatmaintenance.Adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flatmaintenance.Model.Block;
import com.example.flatmaintenance.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MyBlockAdapter extends RecyclerView.Adapter<MyBlockAdapter.MyViewHolder> {

    Context context;
    List<Block> blockList;


    public MyBlockAdapter(Context context, List<Block> blockList) {
        this.context = context;
        this.blockList = blockList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_block, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.txt_block_name.setText(new StringBuilder(blockList.get(i).getBlockName())
                .append(" - ")
                .append(blockList.get(i).getOwerName())
        );

        myViewHolder.txt_owner_contact.setText(blockList.get(i).getOwnerContact());

        if (blockList.get(i).getMaint_amount() != 0) {
            myViewHolder.txt_maintenance.setText(new StringBuilder("₹ ").append(blockList.get(i).getMaint_amount()));
        } else {
            myViewHolder.txt_maintenance.setText(new StringBuilder("₹ ").append("0.0"));
        }

        if (blockList.get(i).getRenterName() != null) {
            myViewHolder.txt_renter_name.setText(blockList.get(i).getRenterName());
            myViewHolder.txt_renter_contact.setText(blockList.get(i).getRenterContact());
        } else {
            myViewHolder.txt_renter_info.setVisibility(View.GONE);
            myViewHolder.txt_renter_name.setVisibility(View.GONE);
            myViewHolder.txt_renter_contact.setVisibility(View.GONE);
            myViewHolder.img_person.setVisibility(View.GONE);
            myViewHolder.img_renter_contact.setVisibility(View.GONE);
        }

        myViewHolder.txt_owner_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + myViewHolder.txt_owner_contact.getText().toString()));
                if (ContextCompat.checkSelfPermission(context,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                context.startActivity(intent);
            }
        });

        myViewHolder.txt_renter_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + myViewHolder.txt_renter_contact.getText().toString()));
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blockList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView txt_block_name,txt_renter_name, txt_renter_contact, txt_renter_info,txt_owner_contact,txt_maintenance;
        ImageView img_person, img_renter_contact;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_block_name = (TextView)itemView.findViewById(R.id.txt_block_name);
            txt_renter_name = (TextView)itemView.findViewById(R.id.txt_renter_name);
            txt_renter_contact = (TextView)itemView.findViewById(R.id.txt_renter_contact);
            txt_renter_info = (TextView)itemView.findViewById(R.id.txt_renter_info);
            txt_owner_contact = (TextView)itemView.findViewById(R.id.txt_owner_contact);
            txt_maintenance = (TextView)itemView.findViewById(R.id.txt_maintenance);
            img_person = (ImageView) itemView.findViewById(R.id.img_person);
            img_renter_contact = (ImageView) itemView.findViewById(R.id.img_renter_contact);


        }

    }
}
