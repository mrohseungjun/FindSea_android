package com.fndsea.main;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

// x축을 원하는 값으로 표시.
public class CustomValueFormat implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String week = String.valueOf((int)(value));
        return week+"주";
    }
}