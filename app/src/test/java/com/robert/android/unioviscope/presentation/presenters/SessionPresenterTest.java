package com.robert.android.unioviscope.presentation.presenters;

import android.content.Context;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.model.Session;
import com.robert.android.unioviscope.domain.model.Subject;
import com.robert.android.unioviscope.domain.model.types.GroupType;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.network.service.StudentService;
import com.robert.android.unioviscope.presentation.presenters.impl.SessionPresenterImpl;
import com.robert.android.unioviscope.presentation.ui.adapters.SessionAdapter;
import com.robert.android.unioviscope.utilTest.threading.TestMainThread;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Clase test para la clase SessionPresenterImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.presenters.impl.SessionPresenterImpl
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionPresenterTest {

    @Mock
    private Executor mExecutor;
    @Mock
    private SessionPresenter.View mCallback;
    @Mock
    private SessionRepository mSessionRepository;

    private MainThread mMainThread;
    private Context mContext;
    private StudentService mService;
    private Subject mSubject;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
        mContext = mock(Context.class);
        mService = mock(StudentService.class);

        mSubject = new Subject(1L);
    }

    @Test
    public void testSessionsRetrieved() {
        SessionPresenterImpl presenter = new SessionPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository);
        presenter.getSessions(mSubject, GroupType.THEORY);

        spy(presenter).onSessionsRetrieved(new ArrayList<Session>());

        verify(mSessionRepository).getUser();
        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).showSessions(any(SessionAdapter.class));
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testSessionsEmpty() {
        SessionPresenterImpl presenter = new SessionPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository);
        presenter.getSessions(mSubject, GroupType.GROUP_TUTORSHIP);

        spy(presenter).onSessionsEmpty();

        verify(mSessionRepository).getUser();
        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).onSessionsEmpty();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testNoInternetConnection() {
        SessionPresenterImpl presenter = new SessionPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository);
        presenter.getSessions(mSubject, GroupType.SEMINAR);

        spy(presenter).onNoInternetConnection();

        verify(mSessionRepository).getUser();
        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).noInternetConnection();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testServiceNotAvailable() {
        SessionPresenterImpl presenter = new SessionPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository);
        presenter.getSessions(mSubject, GroupType.PRACTICE);

        spy(presenter).onServiceNotAvailable();

        verify(mSessionRepository).getUser();
        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).serviceNotAvailable();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }
}
