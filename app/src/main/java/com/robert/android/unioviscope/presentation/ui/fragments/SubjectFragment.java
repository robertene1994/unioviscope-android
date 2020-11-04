package com.robert.android.unioviscope.presentation.ui.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.robert.android.unioviscope.R;
import com.robert.android.unioviscope.domain.executor.impl.ThreadExecutor;
import com.robert.android.unioviscope.domain.model.Subject;
import com.robert.android.unioviscope.network.ServiceGenerator;
import com.robert.android.unioviscope.network.service.StudentService;
import com.robert.android.unioviscope.presentation.presenters.SubjectPresenter;
import com.robert.android.unioviscope.presentation.presenters.impl.SubjectPresenterImpl;
import com.robert.android.unioviscope.presentation.ui.activities.ReviewAttendancesActivity_;
import com.robert.android.unioviscope.presentation.ui.fragments.base.AbstractFragment;
import com.robert.android.unioviscope.presentation.ui.utils.SnackbarUtil;
import com.robert.android.unioviscope.storage.SessionRepositoryImpl;
import com.robert.android.unioviscope.threading.MainThreadImpl;

/**
 * Fragment que extiende la clase AbstractFragment e implementa la interfaz del callback del presenter SubjectPresenter.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.ui.fragments.base.AbstractFragment
 * @see com.robert.android.unioviscope.presentation.presenters.SubjectPresenter.View
 */
public class SubjectFragment extends AbstractFragment implements SubjectPresenter.View,
        AdapterView.OnItemClickListener {

    public static final String KEY_SUBJECT = "SUBJECT";

    // vistas que se ocultan al realizar alguna operación (cargando)
    private ListView mSubjectsListView;

    private View mSubjectsLayout;
    private SubjectPresenter mSubjectPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mSubjectsLayout = inflater.inflate(R.layout.fragment_subjects, container, false);
        mSubjectsListView = mSubjectsLayout.findViewById(R.id.subjectsListView);
        mSubjectsListView.setOnItemClickListener(this);

        View mProgressBarLayout = mSubjectsLayout.findViewById(R.id.progressBarConstraintLayout);
        setProgressBarLayout(mProgressBarLayout);
        addViewsToHide(mSubjectsListView);

        mSubjectPresenter.getSubjects();
        return mSubjectsLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSubjectPresenter = new SubjectPresenterImpl(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(),
                this, getContext(), ServiceGenerator.createService(getContext(), StudentService.class),
                new SessionRepositoryImpl(getContext()));
    }

    @Override
    public void onResume() {
        mSubjectPresenter.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mSubjectPresenter.pause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mSubjectPresenter.stop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mSubjectPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Subject subject = mSubjectPresenter.getSubjectByIndex(position);
        ReviewAttendancesActivity_.intent(this).extra(KEY_SUBJECT, (Parcelable) subject).start();
    }

    @Override
    public void showSubjects(ArrayAdapter<String> adapter) {
        mSubjectsListView.setAdapter(adapter);
    }

    @Override
    public void onSubjectsEmpty() {
        SnackbarUtil.makeLong(mSubjectsLayout, R.string.msg_no_enrolled_subjects);
    }

    @Override
    public void noInternetConnection() {
        SnackbarUtil.makeLong(mSubjectsLayout, R.string.msg_no_internet_connection);
    }

    @Override
    public void serviceNotAvailable() {
        SnackbarUtil.makeLong(mSubjectsLayout, R.string.msg_service_not_avabile);
    }
}
