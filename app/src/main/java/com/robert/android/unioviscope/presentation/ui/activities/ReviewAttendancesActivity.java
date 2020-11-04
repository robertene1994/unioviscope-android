package com.robert.android.unioviscope.presentation.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.robert.android.unioviscope.R;
import com.robert.android.unioviscope.domain.model.Subject;
import com.robert.android.unioviscope.presentation.ui.activities.base.AbstractActivity;
import com.robert.android.unioviscope.presentation.ui.adapters.SectionsPagerAdapter;
import com.robert.android.unioviscope.presentation.ui.fragments.SubjectFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Activity que extiende la clase AbstractActivity.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.ui.activities.base.AbstractActivity
 */
@SuppressLint({"Registered", "NonConstantResourceId"})
@EActivity(R.layout.activity_review_attendances)
public class ReviewAttendancesActivity extends AbstractActivity {

    @ViewById(R.id.containerViewPager)
    ViewPager mContainerViewPager;
    @ViewById(R.id.tabLayout)
    TabLayout mTabLayout;

    private Subject mSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadExtras();
    }

    @AfterViews
    void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                getContext(), mSubject);
        mContainerViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mContainerViewPager);
    }

    private void loadExtras() {
        Bundle receivedBundle = getIntent().getExtras();

        if (receivedBundle != null)
            mSubject = receivedBundle.getParcelable(SubjectFragment.KEY_SUBJECT);
    }
}
