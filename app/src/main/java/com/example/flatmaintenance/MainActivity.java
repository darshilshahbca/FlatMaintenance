package com.example.flatmaintenance;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.flatmaintenance.Common.Common;
import com.example.flatmaintenance.Common.CustomLoginDialog;
import com.example.flatmaintenance.Interface.IDialogClickListner;
import com.example.flatmaintenance.Interface.IGetUserListener;
import com.example.flatmaintenance.Interface.IUserLoginRememberListener;
import com.example.flatmaintenance.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements IDialogClickListner, IUserLoginRememberListener, IGetUserListener {

    IUserLoginRememberListener iUserLoginRememberListener;
    IGetUserListener iGetUserListener;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CALL_PHONE
                )
                .withListener(new MultiplePermissionsListener(){

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        FirebaseInstanceId.getInstance()
                                .getInstanceId()
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                            }
                        });

                        Paper.init(MainActivity.this);
                        String user = Paper.book().read(Common.LOGGED_KEY);

                        if(TextUtils.isEmpty(user)) //If User Not Login Before
                        {
                            setContentView(R.layout.activity_main);

                            ButterKnife.bind(MainActivity.this);
                            init();
                            showLoginDialog();

                        } else{
                            //If Use already Login
                            Gson gson  = new Gson();
                            Common.currentUser = gson.fromJson(Paper.book().read(Common.USER_KEY, ""),
                                    new TypeToken<User>(){}.getType());

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();


                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();

    }

    private void showLoginDialog() {
        CustomLoginDialog.getInstance()
                .showLoginDialog("OWNER LOGIN", "Login", "Cancel",
                        this, this);
    }

    @Override
    public void onClickPositiveButton(DialogInterface dialogInterface, String userName, String password) {
        final AlertDialog loading = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();

        loading.show();

        // /FlatUser/7TMW06BhhkY5ii6JpdLf
        FirebaseFirestore.getInstance()
                .collection("FlatUser")
                .whereEqualTo("username", userName)
                .whereEqualTo("password", password)
                .limit(1)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            if(task.getResult().size() > 0)
                            {
                                dialogInterface.dismiss();
                                loading.dismiss();

                                iUserLoginRememberListener.onUserLoginSuccess(userName);

                                //Create User
                                User userDoc = new User();
                                for(DocumentSnapshot userSnapshot : task.getResult())
                                {
                                    userDoc = userSnapshot.toObject(User.class);
                                }

                                iGetUserListener.onGetUserSuccess(userDoc);


                                //We will navigate Home and Clear all Previous Activity
                                Intent Home = new Intent(MainActivity.this, HomeActivity.class);
                                Home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(Home);



                            }else{
                                loading.dismiss();
                                Toast.makeText(MainActivity.this,"Wrong userName/password or Wrong salon",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    @Override
    public void onClickNegativeButton(DialogInterface dialogInterface) {
        dialogInterface.dismiss();
    }

    private void init() {
        iUserLoginRememberListener = this;
        iGetUserListener = this;

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

    }


    @Override
    public void onUserLoginSuccess(String user) {
        Paper.init(this);
        Paper.book().write(Common.LOGGED_KEY, user);

    }

    @Override
    public void onGetUserSuccess(User userDoc) {
        Common.currentUser = userDoc;
        Paper.book().write(Common.USER_KEY, new Gson().toJson(userDoc));
    }
}
