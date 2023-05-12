package com.fndsea.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter {

    // 드롭다운 목록 표 설정
    private String[] list;

    public SpinnerAdapter(String[] list) {
        this.list = list;
    }

    private TextView text;

    // 드롭다운 표시 갯수
    @Override
    public int getCount() {
        return list.length;
    }

    // 선택시 반환 값
    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 처음 보여줄 스피너 뷰
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spinner_adapter,parent, false);
        text = (TextView) view.findViewById(R.id.YYYY);
        text.setText(list[position]);
        return view;
    }

    // 스피너 클릭시 드롭 다운 뷰
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spinner_adapter,parent, false);
        text = (TextView) view.findViewById(R.id.YYYY);
        text.setText(list[position]);
        return view;
    }
}
