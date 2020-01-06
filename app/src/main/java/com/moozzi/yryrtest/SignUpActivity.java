package com.moozzi.yryrtest;


import android.content.Intent;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText email_join,pwd_join,name_join;;

    Button btn,남버튼,여버튼;
    int 남녀=-1;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore 유저정보;
    String email,pwd,이름;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email_join = (EditText) findViewById(R.id.sign_up_email);
        pwd_join = (EditText) findViewById(R.id.sign_up_pwd);
        name_join = (EditText) findViewById(R.id.sign_up_name);
        btn = (Button) findViewById(R.id.sign_up_btn);
        남버튼 = findViewById(R.id.남);
        여버튼 = findViewById(R.id.여);

        firebaseAuth = FirebaseAuth.getInstance();
        유저정보 = FirebaseFirestore.getInstance();

        View.OnClickListener Listener = new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                switch (view.getId()) {
                    case R.id.남:
                         남녀=0;
                        break;
                    case R.id.여:
                         남녀=1;
                        break;

                }
            }
        };
        남버튼.setOnClickListener(Listener);
        여버튼.setOnClickListener(Listener);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = email_join.getText().toString().trim();
                pwd = pwd_join.getText().toString().trim();
                이름 = name_join.getText().toString().trim();

                if(남녀!=-1){
                    firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        CollectionReference 유저 = 유저정보.collection("솔로");

                                        Map<String, Object> data1 = new HashMap<>();
                                        data1.put("이메일", email);
                                        data1.put("이름", 이름);
                                        if(남녀==0){
                                            data1.put("성별", "남");
                                        }else if(남녀==1){
                                            data1.put("성별", "여");
                                        }
                                        data1.put("연결여부","false");
                                        data1.put("상대방이메일","false");
                                        data1.put("상대방이름","false");
                                        data1.put("커플이름","false");
                                        data1.put("상대방성별","false");
                                        data1.put("수신요청","false");
                                        data1.put("발신요청","false");

                                        유저.document(email).set(data1);
                                        Intent intent = new Intent(SignUpActivity.this, login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "아이디가 중복되었거나 가입형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
                }else{
                    Toast.makeText(SignUpActivity.this, "성별을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}