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

public class ViewBlockActivity extends AppCompatActivity implements IBlockLoadListener {

    CollectionReference blockRef;

    IBlockLoadListener iBlockLoadListener;

    @BindView(R.id.recycler_block)
    RecyclerView recycler_block;

    android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_block);

        ButterKnife.bind(this);

        init();
        initView();
        viewBlockDetails();
    }

    private void init() {
        iBlockLoadListener = this;
        alertDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        blockRef = FirebaseFirestore.getInstance().collection("Block");
    }

    private void initView(){
        recycler_block.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_block.setLayoutManager(layoutManager);
        recycler_block.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
    }

    private void viewBlockDetails() {

        alertDialog.show();

        blockRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<Block> blocks = new ArrayList<>();
                            for(DocumentSnapshot blockSnapshot :task.getResult() )
                            {
                                Block block = blockSnapshot.toObject(Block.class);
                                block.setBlockId(blockSnapshot.getId());
                                blocks.add(block);

                            }
                            iBlockLoadListener.onBlockLoadSuccess(blocks);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewBlockActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBlockLoadSuccess(List<Block> blockList) {
        MyBlockAdapter blockAdapter = new MyBlockAdapter(this, blockList);
        recycler_block.setAdapter(blockAdapter);
        alertDialog.dismiss();
    }

    @Override
    public void onBlockLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();

    }
}
