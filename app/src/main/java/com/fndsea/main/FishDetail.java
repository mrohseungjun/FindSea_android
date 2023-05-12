package com.fndsea.main;


import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.Typeface;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FishDetail extends AppCompatActivity {

    // 변수 설정
    private ImageButton btnReturn;
    private RadioGroup rGroup;
    private RadioButton lineGraph;
    private RadioButton barGraph;
    private LineChart lineChart;
    private BarChart barChart;
//    private RadioGroup rGroup2;
//    private RadioButton lineGraph2;
//    private RadioButton barGraph2;
//    private LineChart lineChart2;
//    private BarChart barChart2;

    private Spinner spinner;
    private Spinner spinner2;
    static String keyword;
    private FirebaseAuth mAuth;

    // 스피너에 들어갈 연도 배열
    String[] years = {
            "2013", "2014", "2015",
            "2016", "2017", "2018",
            "2019", "2020", "2021",
            "2022"};

    // 스피너에 들어갈 전반기 후반기 선택용 배열
    String[] month = {
            "01", "02", "03",
            "04", "05", "06",
            "07", "08", "09",
            "10", "11", "12"
    };

    private RecyclerView table;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fish_detail);

        mAuth = FirebaseAuth.getInstance();

        // main에서 가져오는 인텐트 값
        Intent intent = getIntent();
        // keyword에 따라 표시되는 결과가 달라짐.
        keyword = intent.getStringExtra("imgVal");

        // 위젯 바인딩
        btnReturn = (ImageButton) findViewById(R.id.btnReturn);

        rGroup = (RadioGroup) findViewById(R.id.rGroup);
        lineGraph = (RadioButton) findViewById(R.id.lineGraph);
        barGraph = (RadioButton) findViewById(R.id.barGraph);

        table = (RecyclerView) findViewById(R.id.table);

        lineGraph.setChecked(true);
//        lineGraph2.setChecked(true);

        // 뒤로가기
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 라디오 버튼 클릭
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.lineGraph) {
                    barChart.setVisibility(View.INVISIBLE);
                    lineChart.setVisibility(View.VISIBLE);
                    lineChart.animateX(1000, Easing.EasingOption.EaseInSine);
                } else if (checkedId == R.id.barGraph) {
                    lineChart.setVisibility(View.INVISIBLE);
                    barChart.setVisibility(View.VISIBLE);
                    barChart.animateY(1500, Easing.EasingOption.EaseOutBounce);
                }
            }
        });

        // 현재 날짜 가져오기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        // 가져온 현재 날짜를 yyyyMMdd 형태로 변환
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(date);
        // int 형태의 nowDate (date필드 색인용)
        int intNowDate = Integer.parseInt(nowDate);



        ArrayList<Entry> LineEntryList = new ArrayList<>();
        ArrayList<BarEntry> BarEntryList = new ArrayList<>();
        Typeface tf = Typeface.createFromAsset(getAssets(), "font2/kimm_light.ttf"); // 글꼴


        lineChart = (LineChart) findViewById(R.id.lineChart);
        barChart = (BarChart) findViewById(R.id.barChart);


        // 차트 데이터 설정.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
        DocumentReference predict = db.collection("fishes").document(keyword)
                .collection("future").document("south");
//
            //yyyyMMdd 형태의 현재 날짜 String에서 yyyy만 자르기
            String subYear = nowDate.substring(0, 4);
            int intSubYear = Integer.parseInt(subYear);
            // MM만 자르기
            String subMonth = nowDate.substring(4, 6);
            int intSubMonth = Integer.parseInt(subMonth);

            // 현재 년 기준 다음 년도
            int nextYear = Integer.parseInt(subYear) + 1;
            String strNextYear = String.valueOf(nextYear);

            float week[] = {1,2,3,4,5,6,7,8};

            Query getFish = predict.collection(String.valueOf(intSubYear))
                    .whereGreaterThanOrEqualTo("date", intNowDate)
                    .limit(8);
            getFish.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    int i = 0;
                    for (DocumentSnapshot result : queryDocumentSnapshots) {
                        double y = result.getDouble("predicted_mean");
                        if (result.getDouble("predicted_mean") <= 0.0) {
                            y = 0.0;
                        }
                        LineEntryList.add(
                                new Entry(week[i]
                                        , (float) (Math.round(y / 100 * 10) / 10.0)));
                        BarEntryList.add(new BarEntry(week[i]
                                , (float) (Math.round(y / 100 * 10) / 10.0)));
                        i++;
                    }
                    showLine(lineChart, LineEntryList, tf); // 라인 그래프 그리기
                    showBar(barChart, BarEntryList, tf); // 막대 그래프 그리기
                }
            });
        // 컬렉션 = 현재 년도(yyyy) -2, date 필드 >= 현재 날짜(yyyyMMdd) - 2년
        LinearLayoutManager manager; // 레이아웃매니저 설정
        manager = new LinearLayoutManager(FishDetail.this,
                RecyclerView.VERTICAL, false);
        table.setLayoutManager(manager); // 매니저 끼우기

        // 스피너 설정
        spinner = (Spinner) findViewById(R.id.yearSelect);
        spinner2 = (Spinner) findViewById(R.id.monthSelect);
//
        SpinnerAdapter spinnerYear = new SpinnerAdapter(years);
        SpinnerAdapter spinnerMonth = new SpinnerAdapter(month);
//
        spinner.setAdapter(spinnerYear);
        spinner2.setAdapter(spinnerMonth);

        final String[] selectedValue = {spinner.getSelectedItem().toString()};
        final String[] selectedValue2 = {spinner2.getSelectedItem().toString()};

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedValue[0] = parent.getItemAtPosition(position).toString();
                updateSearchVal(selectedValue[0], selectedValue2[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedValue2[0] = (String) parent.getItemAtPosition(position).toString();
                updateSearchVal(selectedValue[0], selectedValue2[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateSearchVal(String selectedValue, String selectedValue2) {
        List<Fishes> fishesList = new ArrayList<>();
        int searchVal = Integer.parseInt(selectedValue + selectedValue2);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference fishes = db.collection("fishes").document(keyword)
                .collection("raw").document("south");
        Query getExcel = fishes.collection(selectedValue);
        getExcel
                .whereGreaterThanOrEqualTo("date", searchVal * 100)
                .whereLessThanOrEqualTo("date", searchVal * 100 + 31)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        fishesList.clear();
                        for (DocumentSnapshot result : queryDocumentSnapshots) {
                            Fishes fish = new Fishes();
                            fish.setDate(result.getLong("date"));
                            fish.setWti(result.getDouble("wti"));
                            fish.setwTmp(result.getDouble("wTmp(C)"));
                            fish.setTmp(result.getDouble("tmp(C)"));
                            fish.setAtm(result.getDouble("atm(hPa)"));
                            fish.setCatchVal(result.getDouble("catch(kg)"));
                            fish.setWs(result.getDouble("ws(mPs)"));
                            fishesList.add(fish);
                        }
                        RecyclerAdapter tableAdp = new RecyclerAdapter(fishesList);
                        table.setAdapter(tableAdp);
                    }
                });
    }


    // 선 그래프 그리기 함수 정의
    public LineChart showLine(LineChart lineChart, ArrayList<Entry> entries, Typeface tf) {

        LineDataSet lineDataSet = new LineDataSet(entries, "월 별 어획량 예측(100톤)");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(4);
        lineDataSet.setCircleColor(Color.parseColor("#1F78B4"));
        lineDataSet.setCircleColorHole(Color.argb(1, 255, 255, 255));
        lineDataSet.setColor(Color.parseColor("#1F78B4"));
        lineDataSet.setValueTypeface(tf);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTypeface(tf);
        xAxis.setValueFormatter(new CustomValueFormat());
        xAxis.setLabelCount(entries.size());
        xAxis.setGranularity(1f);
        xAxis.setAxisMaximum(entries.get(7).getX() + 0.5f);
        xAxis.setAxisMinimum(entries.get(0).getX() - 0.5f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTypeface(tf);
        yLAxis.setDrawGridLines(false);
        yLAxis.setDrawAxisLine(false);
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Legend lineLegend = lineChart.getLegend();
        lineLegend.setTypeface(tf);
        lineLegend.setTextSize(15);
        lineLegend.setFormSize(0f);
        lineLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lineLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        lineChart.setTouchEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(null);
        lineChart.animateX(1000, Easing.EasingOption.EaseInSine);
        lineChart.invalidate();

        return lineChart;
    }

    // 막대 그래프 그리기 함수 정의
    public BarChart showBar(BarChart barChart, ArrayList<BarEntry> entry_chart, Typeface tf) {

        int[] colors = new int[entry_chart.size()];
        // 데이터의 y값이 평균치 보다 클 경우 다른 색깔로 표시.
        float total = 0;
        for (int i = 0; i < entry_chart.size(); i++) {
            float[] yVal = new float[entry_chart.size()];
            yVal[i] += entry_chart.get(i).getY();

            total += yVal[i];
        }


        for (int i = 0; i < entry_chart.size(); i++) {
            if (entry_chart.get(i).getY() >= (total / entry_chart.size())) {
                colors[i] += Color.parseColor("#BD5E6D");
            } else {
                colors[i] += Color.parseColor("#886785");
            }
        }

        BarDataSet barDataSet = new BarDataSet(entry_chart, "월 별 어획량 예측(100톤)"); // 데이터가 담긴 Arraylist 를 BarDataSet 으로 변환한다.
        barDataSet.setValueTypeface(tf);
        barDataSet.setColors(colors); // 해당 BarDataSet 색 설정 :: 각 막대 과 관련된 세팅은 여기서 설정한다.

        BarData barData = new BarData(); // 차트에 담길 데이터
        barData.setBarWidth(0.5f);
        barData.addDataSet(barDataSet); // 해당 BarDataSet 을 적용될 차트에 들어갈 DataSet 에 넣는다.

        XAxis barXaxis = barChart.getXAxis();
        barXaxis.setTypeface(tf);
        barXaxis.setValueFormatter(new CustomValueFormat());
        barXaxis.setDrawAxisLine(false);
        barXaxis.setDrawGridLines(false);
        barXaxis.setLabelCount(entry_chart.size());
        barXaxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis baryLAxis = barChart.getAxisLeft();
        baryLAxis.setTypeface(tf);
        baryLAxis.setDrawGridLines(false);
        baryLAxis.setDrawAxisLine(false);
        baryLAxis.setTextColor(Color.BLACK);

        YAxis baryRAxis = barChart.getAxisRight();
        baryRAxis.setDrawLabels(false);
        baryRAxis.setDrawAxisLine(false);
        baryRAxis.setDrawGridLines(false);

        Legend barLegend = barChart.getLegend();
        barLegend.setTypeface(tf);
        barLegend.setTextSize(15);
        barLegend.setFormSize(0f);
        barLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        barLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        barChart.setData(barData); // 차트에 위의 DataSet 을 넣는다.
        barChart.setDescription(null);

        barChart.invalidate(); // 차트 업데이트
        barChart.animateY(1500, Easing.EasingOption.EaseOutBounce);
        barChart.setTouchEnabled(false); // 차트 터치 불가능하게

        return barChart;
    }
}
