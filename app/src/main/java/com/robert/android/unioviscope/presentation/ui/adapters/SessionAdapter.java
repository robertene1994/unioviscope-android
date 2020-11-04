package com.robert.android.unioviscope.presentation.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.CompoundButtonCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.robert.android.unioviscope.R;
import com.robert.android.unioviscope.domain.model.Attendance;
import com.robert.android.unioviscope.domain.model.Session;
import com.robert.android.unioviscope.presentation.ui.customViews.AttendanceViewItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase adapter para mostrar las sesiones al estudiante en función del layout que está en pantalla.
 *
 * @author Robert Ene
 */
public class SessionAdapter extends ArrayAdapter<Session> {

    private final Context mContext;
    private final List<Session> mSessions;

    /**
     * Contructor que instancia un nuevo adapter.
     *
     * @param context            el contexto de la aplicación.
     * @param textViewResourceId el id del layout.
     * @param sessions           las sesiones planificadas para la asignatura seleccionada.
     */
    public SessionAdapter(Context context, int textViewResourceId, List<Session> sessions) {
        super(context, textViewResourceId, sessions);
        mContext = context;
        mSessions = new ArrayList<>();
        mSessions.addAll(sessions);
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        AttendanceViewItem attendanceViewItem;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.attendance_item, null);

            attendanceViewItem = new AttendanceViewItem();
            attendanceViewItem.setSessionTextView((TextView) convertView.findViewById(R.id.sessionTextView));
            attendanceViewItem.setAttendanceCheckBox((CheckBox) convertView.findViewById(R.id.attendanceCheckBox));
            convertView.setTag(attendanceViewItem);
        } else {
            attendanceViewItem = (AttendanceViewItem) convertView.getTag();
        }

        Session session = mSessions.get(position);
        attendanceViewItem.getSessionTextView()
                .setText(formatStartDate(session.getStart()) + "-" + formatEndDate(session.getEnd()));
        Attendance attendance = session.getAttendance();
        attendanceViewItem.getAttendanceCheckBox().setChecked(attendance != null ? attendance.isConfirmed() : false);

        if (session.getStart().after(new Date())) {
            attendanceViewItem.getSessionTextView().setTextColor(Color.GRAY);
            int[][] states = {{android.R.attr.state_checkable}, {}};
            int[] colors = {Color.GRAY, Color.GRAY};
            CompoundButtonCompat.setButtonTintList(attendanceViewItem.getAttendanceCheckBox(),
                    new ColorStateList(states, colors));
        }

        return convertView;
    }

    @SuppressLint("SimpleDateFormat")
    private String formatStartDate(Date date) {
        return new SimpleDateFormat(mContext.getString(R.string.session_start_date_format)).format(date);
    }

    @SuppressLint("SimpleDateFormat")
    private String formatEndDate(Date date) {
        return new SimpleDateFormat(mContext.getString(R.string.session_end_date_format)).format(date);
    }
}
