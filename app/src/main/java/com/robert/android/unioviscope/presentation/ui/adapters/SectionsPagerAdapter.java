package com.robert.android.unioviscope.presentation.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.robert.android.unioviscope.R;
import com.robert.android.unioviscope.domain.model.Subject;
import com.robert.android.unioviscope.presentation.ui.fragments.GroupTutorshipFragment;
import com.robert.android.unioviscope.presentation.ui.fragments.PracticeFragment;
import com.robert.android.unioviscope.presentation.ui.fragments.SeminarFragment;
import com.robert.android.unioviscope.presentation.ui.fragments.TheoryFragment;
import com.robert.android.unioviscope.utils.LocaleUtil;

/**
 * Clase adapter que muestra al usuario las sesiones planificadas para una determinada asignatura en función de la
 * pantalla (tab) en la que se encuentra.
 *
 * @author Robert Ene
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public static final String KEY_SUBJECT = "SUBJECT";
    private static final Integer NUM_SECTIONS = 4;

    private final Context mContext;
    private final Subject mSubject;

    /**
     * Contructor que instancia un nuevo adapter.
     *
     * @param fm      el fragment manager.
     * @param context el contexto de la aplicación.
     * @param subject la asignatura seleccionada por el estudiante.
     */
    public SectionsPagerAdapter(FragmentManager fm, Context context, Subject subject) {
        super(fm);
        mContext = context;
        mSubject = subject;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUBJECT, mSubject);
        Fragment fragment;

        switch (position) {
            case 0:
                fragment = new TheoryFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                fragment = new PracticeFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 2:
                fragment = new SeminarFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 3:
                fragment = new GroupTutorshipFragment();
                fragment.setArguments(bundle);
                return fragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_SECTIONS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return LocaleUtil.getStringByLocale(mContext, R.string.tab_title_theory);
            case 1:
                return LocaleUtil.getStringByLocale(mContext, R.string.tab_title_practice);
            case 2:
                return LocaleUtil.getStringByLocale(mContext, R.string.tab_title_seminar);
            case 3:
                return LocaleUtil.getStringByLocale(mContext, R.string.tab_title_group_tutorship);
        }
        return null;
    }
}
