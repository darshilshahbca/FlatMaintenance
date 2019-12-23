package com.example.flatmaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flatmaintenance.Adapter.MyBlockAdapter;
import com.example.flatmaintenance.Common.Common;
import com.example.flatmaintenance.Interface.IBlockLoadListener;
import com.example.flatmaintenance.Model.Block;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    TextView txt_owner_name;
    CollectionReference blockRef;

    @BindView(R.id.activity_main)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    ActionBarDrawerToggle actionBarDrawerToggle;
    BottomSheetDialog bottomSheetDialog;
    android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        init();
        initView();
    }

    private void init() {
        alertDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        blockRef = FirebaseFirestore.getInstance().collection("Block");
    }

    private void initView(){

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.menu_exit)
                {
                    logOut();
                }
                else if(menuItem.getItemId() == R.id.menu_addBlock)
                {
                    addBlockDetails();
                }
                else if(menuItem.getItemId() == R.id.menu_viewBlock){
                    startActivity(new Intent(HomeActivity.this, ViewBlockActivity.class));
                }
                return true;
            }
        });

        View headerView = navigationView.getHeaderView(0);
        txt_owner_name = (TextView)headerView.findViewById(R.id.txt_owner_name);
        txt_owner_name.setText(Common.currentUser.getName());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void addBlockDetails() {

        drawerLayout.closeDrawers();

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setTitle("Add Block");
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);

        View sheetView = getLayoutInflater().inflate(R.layout.layout_update_information, null);

        Button btn_update = (Button)sheetView.findViewById(R.id.btn_update);
        TextInputEditText edt_block_name = (TextInputEditText)sheetView.findViewById(R.id.edt_block_name);
        RadioButton rdi_no_use = (RadioButton) sheetView.findViewById(R.id.rdi_no_use);
        RadioButton rdi_yes_use = (RadioButton) sheetView.findViewById(R.id.rdi_yes_use);

        LinearLayout layout_owner_information = (LinearLayout) sheetView.findViewById(R.id.layout_owner_information);
        TextInputEditText edt_owner_name = (TextInputEditText)sheetView.findViewById(R.id.edt_owner_name);
        TextInputEditText edt_maint_amount = (TextInputEditText)sheetView.findViewById(R.id.edt_maint_amount);
        TextInputEditText edt_owner_contact = (TextInputEditText)sheetView.findViewById(R.id.edt_owner_contact);

        LinearLayout layout_renter_information = (LinearLayout) sheetView.findViewById(R.id.layout_renter_information);
        RadioButton rdi_no_rent = (RadioButton) sheetView.findViewById(R.id.rdi_no_rent);
        RadioButton rdi_yes_rent = (RadioButton) sheetView.findViewById(R.id.rdi_yes_rent);
        TextInputEditText edt_renter_name = (TextInputEditText)sheetView.findViewById(R.id.edt_renter_name);
        TextInputEditText edt_renter_contact = (TextInputEditText)sheetView.findViewById(R.id.edt_renter_contact);

        rdi_yes_use.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                      layout_owner_information.setVisibility(View.VISIBLE);
            }
        });

        rdi_no_use.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    layout_owner_information.setVisibility(View.GONE);
            }
        });

        rdi_yes_rent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                     layout_renter_information.setVisibility(View.VISIBLE);
            }
        });

        rdi_no_rent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                     layout_renter_information.setVisibility(View.GONE);
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!alertDialog.isShowing())
                    alertDialog.show();

                Block block = null;

                if(rdi_no_use.isChecked()) {
                    block = new Block (edt_block_name.getText().toString(), false);
                    block.setBlockId(UUID.randomUUID().toString());
                }
                else{
                    if(rdi_no_rent.isChecked()) {
                        block = new Block(edt_block_name.getText().toString(),
                                        edt_owner_name.getText().toString(),
                                        edt_owner_contact.getText().toString(),
                                        Double.valueOf(edt_maint_amount.getText().toString()),
                                        true,
                                        false);

                        block.setBlockId(UUID.randomUUID().toString());

                    } else if(rdi_yes_rent.isChecked())
                    {
                        block = new Block(edt_block_name.getText().toString(),
                                edt_owner_name.getText().toString(),
                                edt_owner_contact.getText().toString(),
                                edt_renter_name.getText().toString(),
                                edt_renter_contact.getText().toString(),
                                Double.valueOf(edt_maint_amount.getText().toString()),
                                true,
                                true);

                        block.setBlockId(UUID.randomUUID().toString());
                    }

                }

                blockRef.document(block.getBlockId())
                        .set(block)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                bottomSheetDialog.dismiss();
                                if(alertDialog.isShowing())
                                    alertDialog.dismiss();

                                Toast.makeText(HomeActivity.this, "Blocked Added Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                bottomSheetDialog.dismiss();
                                if(alertDialog.isShowing())
                                    alertDialog.dismiss();
                                Toast.makeText(HomeActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }) ;



            }
        });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();


    }

    private void logOut() {


        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit ?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Just Delete All Rememberance Key and Start Main Activity
                        Paper.init(HomeActivity.this);
                        Paper.book().delete(Common.LOGGED_KEY);
                        Paper.book().delete(Common.USER_KEY);

                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        }).show();
    }

}
