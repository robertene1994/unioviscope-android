package com.robert.android.unioviscope.presentation.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.robert.android.unioviscope.R;
import com.robert.android.unioviscope.domain.executor.impl.ThreadExecutor;
import com.robert.android.unioviscope.domain.model.Session;
import com.robert.android.unioviscope.domain.model.Subject;
import com.robert.android.unioviscope.domain.model.types.GroupType;
import com.robert.android.unioviscope.network.ServiceGenerator;
import com.robert.android.unioviscope.network.service.StudentService;
import com.robert.android.unioviscope.presentation.presenters.SessionPresenter;
import com.robert.android.unioviscope.presentation.presenters.impl.SessionPresenterImpl;
import com.robert.android.unioviscope.presentation.ui.adapters.SectionsPagerAdapter;
import com.robert.android.unioviscope.presentation.ui.fragments.base.AbstractFragment;
import com.robert.android.unioviscope.presentation.ui.utils.SnackbarUtil;
import com.robert.android.unioviscope.storage.SessionRepositoryImpl;
import com.robert.android.unioviscope.threading.MainThreadImpl;

/**
 * Fragment que extiende la clase AbstractFragment e implementa la interfaz del callback del presenter SessionPresenter.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.ui.fragments.base.AbstractFragment
 * @see com.robert.android.unioviscope.presentation.presenters.SessionPresenter.View
 */
public class TheoryFragment extends AbstractFragment implements SessionPresenter.View {

    // vistas que se ocultan al realizar alguna operación (cargando)
    private ListView mTheorySessionsListView;

    private View mTheoryLayout;
    private SessionPresenter mSessionPresenter;
    private Subject mSubject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mTheoryLayout = inflater.inflate(R.layout.fragment_theory, container, false);
        mTheorySessionsListView = mTheoryLayout.findViewById(R.id.theorySessionsListView);

        View mProgressBarLayout = mTheoryLayout.findViewById(R.id.progressBarConstraintLayout);
        setProgressBarLayout(mProgressBarLayout);
        addViewsToHide(mTheorySessionsListView);

        mSessionPresenter.getSessions(mSubject, GroupType.THEORY);
        return mTheoryLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubject = getArguments().getParcelable(SectionsPagerAdapter.KEY_SUBJECT);
        mSessionPresenter = new SessionPresenterImpl(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(),
                this, getContext(), ServiceGenerator.createService(getContext(), StudentService.class),
                new SessionRepositoryImpl(getContext()));
    }

    @Override
    public void onResume() {
        mSessionPresenter.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mSessionPresenter.pause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mSessionPresenter.stop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mSessionPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public void showSessions(ArrayAdapter<Session> adapter) {
        mTheorySessionsListView.setAdapter(adapter);
    }

    @Override
    public void onSessionsEmpty() {
        if (this.getUserVisibleHint())
            SnackbarUtil.makeLong(mTheoryLayout, R.string.msg_no_sheduled_theory_sessions);
    }

    @Override
    public void noInternetConnection() {
        SnackbarUtil.makeLong(mTheoryLayout, R.string.msg_no_internet_connection);
    }

    @Override
    public void serviceNotAvailable() {
        SnackbarUtil.makeLong(mTheoryLayout, R.string.msg_service_not_avabile);
    }
}
