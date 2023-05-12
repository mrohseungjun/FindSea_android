package com.fndsea.main;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailSignUp, passwordSignUp, nameSignUp;
    private Button signUp;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        nameSignUp = (EditText) findViewById(R.id.nameSignUp);
        emailSignUp = (EditText) findViewById(R.id.emailSignUp);
        passwordSignUp = (EditText) findViewById(R.id.passwordSignUp);
        signUp = (Button) findViewById(R.id.signUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailSignUp.getText().toString().equals("") &&
                        !passwordSignUp.getText().toString().equals("") &&
                        !nameSignUp.getText().toString().equals("")) {
                    // 이메일과 비밀번호가 공백이 아닌 경우
                    Map<String, Object> user = new HashMap<>();
                    firebaseAuth.createUserWithEmailAndPassword(emailSignUp.getText().toString(), passwordSignUp.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // 회원가입 성공시
                                    user.put("name", nameSignUp.getText().toString());
                                    user.put("createdTime", new Timestamp(new Date()));
                                    user.put("google", 0);
                                    firebaseFirestore.collection("users")
                                            .document(authResult.getUser().getUid())
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                                    Log.d("결과", "성공");
                                                }
                                            });
                                    Log.d("아이디", authResult.getUser().getUid());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    finish();
                } else if (passwordSignUp.getText().toString().length() >= 8) {
                    // 비밀번호가 8자 미만인 경우
                    Toast.makeText(SignUpActivity.this,
                            "비밀번호는 8자 이상이 되어야합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(SignUpActivity.this,
                            "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void createUser(String email, String password, String name) {
        Map<String, Object> user = new HashMap<>();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("아이디", firebaseAuth.getCurrentUser().getUid());
                            // 회원가입 성공시
//                            user.put("name", name);
//                            user.put("createdTime", new Timestamp(new Date()));
//                            firebaseFirestore.collection("users")
//                                    .document(firebaseAuth.getCurrentUser().getUid())
//                                    .set(user)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void unused) {
//                                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
//                                            Log.d("결과", "성공");
//                                        }
//                                    });
                        } else {
                            // 계정이 중복된 경우
                            Toast.makeText(SignUpActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                            Log.d("에러", task.getException().toString());
                        }
                    }
                });
    }
}
