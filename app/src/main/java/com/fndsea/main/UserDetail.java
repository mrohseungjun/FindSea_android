package com.fndsea.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDetail extends AppCompatActivity {

    private TextView emailUser,username;
    private EditText changePW;
    private Button changePassword, logout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail);

        username = (TextView) findViewById(R.id.username);
        emailUser = (TextView) findViewById(R.id.emailUser);
        changePassword = (Button) findViewById(R.id.changePassword);
        logout = (Button) findViewById(R.id.logout);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore
                .collection("users")
                .document(mAuth.getCurrentUser().getUid())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = documentSnapshot.getData().get("name").toString();
                        username.setText(name);
                        if((long)documentSnapshot.getData().get("google") == 1){
                            changePassword.setVisibility(View.INVISIBLE);
                        }
                    }
                });
        emailUser.setText(mAuth.getCurrentUser().getEmail());


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(UserDetail.this);
                dlg.setMessage("로그아웃 하시겠습니까?");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        ImageButton button = ((MainActivity) MainActivity.mContext).findViewById(R.id.log);
                        button.setImageResource(R.mipmap.material_account_icon);
                        finish();
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dlgView = (View) View.inflate(UserDetail.this,
                        R.layout.change_password, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(UserDetail.this);
                dlg.setView(dlgView);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        changePW = (EditText) dlgView.findViewById(R.id.changePW);
                        String newPassword = changePW.getText().toString();

                        user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mAuth.signOut();
                                            finish();
                                        }
                                    }
                                });
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });


    }
}


