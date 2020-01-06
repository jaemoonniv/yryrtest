package com.moozzi.yryrtest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class link extends AppCompatActivity {
    FirebaseFirestore 유저정보;
    EditText 상대방이메일텍스트,커플명텍스트;
    Button 요청,수락,거절;
    FrameLayout 요청수락레이아웃;
    TextView 테스트,요청이름,요청이메일,요청커플명,요청성별;
    String 상대방이메일,커플명,내이메일,생성커플,상대방이름,상대방성별,내이름,내성별;

    public void onStart(){
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link);
        유저정보 = FirebaseFirestore.getInstance();
        테스트 =findViewById(R.id.테스트텍스트);
        요청이름=findViewById(R.id.요청이름);
        요청이메일 =findViewById(R.id.요청이메일);
        요청커플명 =findViewById(R.id.요청커플명);
        요청성별 =findViewById(R.id.요청성별);
        상대방이메일텍스트 = findViewById(R.id.상대방이메일);
        커플명텍스트 = findViewById(R.id.커플이름);
        요청 = findViewById(R.id.요청버튼);
        수락 = findViewById(R.id.수락);
        거절 = findViewById(R.id.거절);
        요청수락레이아웃 = findViewById(R.id.요청수락레이아웃);


        SharedPreferences sf = getSharedPreferences("접속이메일",MODE_PRIVATE);
        내이메일 = sf.getString("내이메일",내이메일);



        보류();
        버튼();

    }
    void 버튼(){

        View.OnClickListener Listener = new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                switch (view.getId()) {
                    case R.id.요청버튼:
                        요청();
                        테스트.setText("요청중");
                        break;
                    case R.id.수락:
                        수락거절("수락");
                        요청수락레이아웃.setVisibility(View.GONE);
                        break;
                    case R.id.거절:
                        수락거절("거절");
                        요청수락레이아웃.setVisibility(View.GONE);
                        break;
                }
            }
        };
        요청.setOnClickListener(Listener);
        수락.setOnClickListener(Listener);
        거절.setOnClickListener(Listener);

    }
    void 수락거절(String 여부){

        if(여부.equals("수락")){
            CollectionReference 유저 = 유저정보.collection("커플");
            Map<String, Object> data1 = new HashMap<>();
            유저.document(생성커플).set(data1);

            CollectionReference 커포 = 유저정보.collection("커플");
            Map<String, Object> 커포데이터 = new HashMap<>();
            커포데이터.put("포인트",0);
            커포.document(생성커플).set(커포데이터);

            CollectionReference 유저2 = 유저정보.collection("커플").document(생성커플).collection("jaemoonniv@naver.com");
            Map<String, Object> data2 = new HashMap<>();
            data2.put("포인트",0);
            유저2.document("정보").set(data2);
            Map<String, Object> data4 = new HashMap<>();
            유저2.document("벌칙쿠폰").set(data4);

            CollectionReference 유저3 = 유저정보.collection("커플").document(생성커플).collection("jaemoonni@naver.com");
            Map<String, Object> data3 = new HashMap<>();
            data3.put("포인트",0);
            유저3.document("정보").set(data2);
            Map<String, Object> data5 = new HashMap<>();
            유저3.document("벌칙쿠폰").set(data5);



            DocumentReference 내정보 = 유저정보.collection("솔로").document(내이메일);
            내정보.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (snapshot != null && snapshot.exists()) {

                        내이름=snapshot.getData().get("이름").toString();
                        내성별=snapshot.getData().get("성별").toString();

                        CollectionReference 셋팅 = 유저정보.collection("솔로");

                        Map<String, Object> 내셋팅 = new HashMap<>();
                        내셋팅.put("상대방이메일",상대방이메일);
                        내셋팅.put("상대방이름",상대방이름);
                        내셋팅.put("상대방성별",상대방성별);
                        내셋팅.put("커플이름",생성커플);
                        내셋팅.put("수신요청","true");
                        내셋팅.put("발신요청","true");
                        내셋팅.put("연결여부","true");
                        셋팅.document(내이메일).update(내셋팅);







                        Map<String, Object> 상대셋팅 = new HashMap<>();
                        상대셋팅.put("상대방이메일",내이메일);
                        상대셋팅.put("상대방이름",내이름);
                        상대셋팅.put("상대방성별",내성별);
                        상대셋팅.put("커플이름",생성커플);
                        상대셋팅.put("수신요청","true");
                        상대셋팅.put("발신요청","true");
                        상대셋팅.put("연결여부","true");
                        셋팅.document(상대방이메일).update(상대셋팅);


                    } else {
                    }
                }
            });





            테스트.setText("수락성공");
        }else{

            CollectionReference 셋팅 = 유저정보.collection("솔로");
            Map<String, Object> data1 = new HashMap<>();
            data1.put("상대방이메일","false");
            data1.put("상대방이름","false");
            data1.put("상대방성별","false");
            data1.put("커플이름","false");
            data1.put("수신요청","false");
            셋팅.document(내이메일).update(data1);
            테스트.setText("수락거절");
        }
        요청수락레이아웃.setVisibility(View.GONE);
    }
    void 요청(){
        상대방이메일=상대방이메일텍스트.getText().toString().trim();
        커플명=커플명텍스트.getText().toString().trim();

        if(!내이메일.equals(상대방이메일)){

        DocumentReference docRef = 유저정보.collection("솔로").document(상대방이메일);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        if(document.getData().get("연결여부").equals("false")){


                            DocumentReference docRef = 유저정보.collection("커플").document(커플명);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {

                                            Toast.makeText(com.moozzi.yryrtest.link.this, "이미 사용중인 커플명입니다.", Toast.LENGTH_SHORT).show();

                                        } else {
                                            CollectionReference 유저 = 유저정보.collection("솔로");
                                            Map<String, Object> data1 = new HashMap<>();
                                            data1.put("상대방이메일","jaemoonniv@naver.com");
                                            data1.put("상대방이름","이재문");
                                            data1.put("상대방성별","남");
                                            data1.put("커플이름",커플명);
                                            data1.put("수신요청","true");
                                            유저.document(상대방이메일).update(data1);
                                            테스트.setText("요청완료");
                                        }
                                    } else {

                                    }
                                }
                            });

                        }
                        else{
                            Toast.makeText(com.moozzi.yryrtest.link.this, "이 이메일은 이미 커플입니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(com.moozzi.yryrtest.link.this, "이메일을 확인할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }
        });

        }else{
            Toast.makeText(com.moozzi.yryrtest.link.this, "자신과 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }






    }


    void 보류(){

        DocumentReference 요청사항 = 유저정보.collection("솔로").document(내이메일);
        요청사항.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    if(snapshot.getData().get("수신요청").equals("true")){
                     요청수락레이아웃.setVisibility(View.VISIBLE);
                     요청이름.setText(""+snapshot.getData().get("상대방이름"));
                     요청이메일.setText(""+snapshot.getData().get("상대방이메일"));
                     요청커플명.setText(""+snapshot.getData().get("커플이름"));
                     요청성별.setText(""+snapshot.getData().get("상대방성별"));
                     생성커플=snapshot.getData().get("커플이름").toString();
                     상대방이메일=snapshot.getData().get("상대방이메일").toString();
                     상대방이름=snapshot.getData().get("상대방이름").toString();
                     상대방성별=snapshot.getData().get("상대방성별").toString();
                    }
                } else {
                }
            }
        });
    }


}


