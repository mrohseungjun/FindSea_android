package com.fndsea.main;



import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    public static Context mContext;
    // 변수 설정
    private ImageView flounders;
    private ImageView scombers;
    private ImageView squids;
    private ImageView tunas;
    private AutoCompleteTextView searchText;
    private ImageButton searchFish;
    private ImageButton LogSign;

    // 사용자 인증 불러오기
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Firestore fishes 컬렉션 불러오기
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference fishes = db.collection("fishes");

        mAuth = FirebaseAuth.getInstance();

        // json FireStore 데이터 추가
//        String fileName = "jsonData/raw_data.json";
//        inputDataForFireStore(fileName, fishes);

        // 위젯 바인딩
        flounders = (ImageView) findViewById(R.id.flounders);
        scombers = (ImageView) findViewById(R.id.scombers);
        squids = (ImageView) findViewById(R.id.squids);
        tunas = (ImageView) findViewById(R.id.tunas);
        searchText = (AutoCompleteTextView) findViewById(R.id.searchText);
        searchFish = (ImageButton) findViewById(R.id.searchFish);
        LogSign = (ImageButton)findViewById(R.id.log);

        // 검색어 설정
        String[] items = {
                "고등어", "참치", "오징어", "광어"
        };

        searchText.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, items));

        // 인텐트 설정
        Intent intent = new Intent(MainActivity.this, FishDetail.class);
        Intent loginSignUp = new Intent(MainActivity.this, UserLog.class);


        // 사용자 정보 버튼 설정
        LogSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() == null){
                    int Firstmsg = 1;
                    startActivityForResult(loginSignUp, Firstmsg);
                } else {
                    Intent userDetail = new Intent(MainActivity.this, UserDetail.class);
                    startActivity(userDetail);
                }
            }//onClick
        });//LogSign

        mContext = this;

        if(mAuth.getCurrentUser() != null) {
            LogSign.setImageResource(R.mipmap.material_account_icon_blue);
        } else if(mAuth.getCurrentUser() == null){
            LogSign.setImageResource(R.mipmap.material_account_icon);
        }

        // 검색 버튼 클릭 이벤트
        searchFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = searchText.getText().toString();
                if(keyword.equals("고등어")){
                    intent.putExtra("imgVal", "scombers");
                    startActivity(intent);
                } else if(keyword.equals("참치")){
                    intent.putExtra("imgVal", "scombers");
                    startActivity(intent);
                } else if(keyword.equals("오징어")){
                    intent.putExtra("imgVal", "scombers");
                    startActivity(intent);
                } else if(keyword.equals("광어")){
                    intent.putExtra("imgVal", "scombers");
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "검색한 어종의 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }//onClick
        });//searchFish

        // 각 이미지 클릭 이벤트(fish_detail로 이동)
        flounders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("imgVal", "scombers");
                startActivity(intent);
            }//onClick
        });//flounders.onClick

        scombers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("imgVal", "scombers");
                startActivity(intent);
            }//onClick
        });//scombers.onClick

        squids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("imgVal", "scombers");
                startActivity(intent);
            }//onClick
        });//squids.onClick

        tunas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("imgVal", "scombers");
                startActivity(intent);
            }//onClick
        });//tunas.onClick
    }//onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Log.d("넘어온 값", data.getStringExtra("LoginCode"));
            LogSign.setImageResource(R.mipmap.material_account_icon_blue);
        }
    }

    public void inputDataForFireStore(String fileName, CollectionReference collectionName) {
        String json = "";
        try {
            InputStream is = getAssets().open(fileName);
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            //json 파일 명을 가지와서 String 변수에 담음
            json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);

            // 배열로 된 자료 가져올 때
            JSONArray Array = jsonObject.getJSONArray("rawData");

            // 작성 일자 HashMap
            Map<String, Object> createDate = new HashMap<>();
            createDate.put("createDate", Timestamp.now());

            // json배열 추출
            for (int i = 0; i < Array.length(); i++) {
                JSONObject Object = Array.getJSONObject(i);

                // JSONObject Object의 key 값을 반복자(Iterator)로 선언
                Iterator<String> keys = Object.keys();

                // Firebase에 필드로 넣을 Map scombersData 초기화
                Map<String, Object> scombersData = new HashMap<>();

                // keys에 값이 있으면
                while (keys.hasNext()){
                    // Object의 key 값을 String k로 선언
                    String k = keys.next();

                    // date부터 sal까지 값을 새 HashMap scombersData에 담음
                    scombersData.put(k, Object.get(k));
                }

                // Map scombersData에 name, loc 키 값 삭제(필드로 입력하지 않기 때문)
                scombersData.remove("name");
                scombersData.remove("loc");

                // 만약 어종이 고등어일 경우 document 작성
                if (Object.getString("name").equals("scombers")) {
                    // scombers document 작성
                    DocumentReference scombers = collectionName.document(Object.getString("name"));

                    // 그 후 raw 컬렉션 그룹 작성
                    CollectionReference raw = scombers.collection("raw");

                    scombers.set(createDate);

                    // 만약 지역이 서해일 경우 document 작성
                    if (Object.getString("loc").equals("west")) {
                        // west document 작성
                        DocumentReference west = raw.document(Object.getString("loc"));

                        west.set(createDate);

                        Log.d("--  loc is west ", Object.getString("loc"));

                        // 년도별 월별 컬렉션, 도큐먼트 작성
                        makeCollectionYearMonth(Object, scombersData, west);

                        // 년도별로 컬렉션 그룹 작성 후 월별로 document 작성 후 필드에 scombersData을 입력
                    } else if (Object.getString("loc").equals("south")) {
                        DocumentReference south = raw.document(Object.getString("loc"));

                        south.set(createDate);

                        makeCollectionYearMonth(Object, scombersData, south);

                        Log.d("--  loc is south ", Object.getString("loc"));
                    } else {
                        DocumentReference east = raw.document(Object.getString("loc"));

                        east.set(createDate);

                        makeCollectionYearMonth(Object, scombersData, east);

                        Log.d("--  loc is east ", Object.getString("loc"));
                    }//ifLoc
                } else {
                    Log.d("--  name is ", Object.getString("name"));
                }//ifName
            }//forJSONArray
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        };
    };//inputDataForFireStore

    public void makeCollectionYearMonth(JSONObject Object, Map<String, Object> data, DocumentReference loc) throws JSONException {
        // 년도별로 컬렉션 그룹 작성 후 월별로 document 작성 후 필드에 data를 입력
        for (long year = 20131231; year < 20221232; year += 00010000) {
            if (Object.getLong("date") <= year) {
                // keyName을 year변수의 앞 4자리로 선언(ex 2013)
                String keyName = String.valueOf(Object.getLong("date")).substring(0, 4);
                Log.d("--  keyName is", keyName);

                // docName을 year변수의 앞 5~6자리로 선언(ex 01, 02, 03...)
                String docName = String.valueOf(Object.getLong("date")).substring(4, 6);
                Log.d("--  docName is", docName);

                // 년도별 컬렉션 그룹 작성
                CollectionReference colYear = loc.collection(keyName);
                // 년도별 컬렉션 내 월별 document 작성 후, data를 인풋
                colYear.document(docName).set(data);
            } else {
                String keyName = String.valueOf(Object.getLong("date")).substring(0, 4);
                Log.d("--  keyName is", keyName);

                String docName = String.valueOf(Object.getLong("date")).substring(4, 6);
                Log.d("--  docName is", docName);

                CollectionReference colYear = loc.collection(keyName);
                colYear.document(docName).set(data);
            }//ifYear
        }//forYear
    }//makeCollectionYearMonth
}