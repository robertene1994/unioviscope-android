package com.robert.android.unioviscope.presentation.ui.customViews;

import android.widget.CheckBox;
import android.widget.TextView;

public class AttendanceViewItem {

    private TextView mSessionTextView;
    private CheckBox mAttendanceCheckBox;

    public AttendanceViewItem() {
        super();
    }

    public TextView getSessionTextView() {
        return mSessionTextView;
    }

    public void setSessionTextView(TextView sessionTextView) {
        mSessionTextView = sessionTextView;
    }

    public CheckBox getAttendanceCheckBox() {
        return mAttendanceCheckBox;
    }

    public void setAttendanceCheckBox(CheckBox attendanceCheckBox) {
        mAttendanceCheckBox = attendanceCheckBox;
    }
}
