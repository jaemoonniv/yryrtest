package com.moozzi.yryrtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {
    private Button join;
    private Button login;
    private EditText email_login;
    private EditText pwd_login;
    static String 내이메일,email,pwd;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore 유저정보;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        join = (Button) findViewById(R.id.main_join_btn);
        login = (Button) findViewById(R.id.main_login_btn);
        email_login = (EditText) findViewById(R.id.main_email);
        pwd_login = (EditText) findViewById(R.id.main_pwd);

        firebaseAuth = firebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = email_login.getText().toString().trim();
                pwd = pwd_login.getText().toString().trim();

                if(pwd.length()!=0&&email.length()!=0){

                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(com.moozzi.yryrtest.login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    SharedPreferences sharedPreferences = getSharedPreferences("접속이메일",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("내이메일",email); // key, value를 이용하여 저장하는 형태
                                    editor.commit();

                                    유저정보 = FirebaseFirestore.getInstance();
                                    DocumentReference docRef = 유저정보.collection("솔로").document(email);
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    if(document.getData().get("연결여부").equals("true")){
                                                        Intent intent = new Intent(login.this, main.class);

                                                        startActivity(intent);
                                                    }else {

                                                        Intent intent = new Intent(login.this, link.class);

                                                        startActivity(intent);
                                                    }


                                                } else {


                                                }}}});



                                } else {
                                    Toast.makeText(com.moozzi.yryrtest.login.this, "아이디나 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }else{
                    Toast.makeText(com.moozzi.yryrtest.login.this, "아이디나 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.moozzi.yryrtest.login.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    void 로그인(){

    }
}