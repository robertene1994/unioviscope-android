package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.impl.GetUserInteractorImpl;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.utilTest.threading.TestMainThread;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Clase test para la clase GetUserInteractorImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.impl.GetUserInteractorImpl
 */
@SuppressWarnings("ConstantConditions, unused")
@RunWith(MockitoJUnitRunner.class)
public class GetUserInteractorTest {

    private MainThread mMainThread;
    @Mock
    private Executor mExecutor;
    @Mock
    private GetUserInteractor.Callback mCallback;
    @Mock
    private SessionRepository mSessionRepository;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
    }

    @Test
    public void testGetUserNotNull() {
        User user = new User(1L);

        when(mSessionRepository.getUser()).thenReturn(user);

        new GetUserInteractorImpl(mExecutor, mMainThread, mCallback, mSessionRepository).run();

        verify(mSessionRepository).getUser();
        verify(mCallback).onUserRetrieved(user);
    }

    @Test
    public void testGetUserNull() {
        User user = null;

        when(mSessionRepository.getUser()).thenReturn(user);

        new GetUserInteractorImpl(mExecutor, mMainThread, mCallback, mSessionRepository).run();

        verify(mSessionRepository).getUser();
        verify(mCallback).onUserRetrieved(user);
    }
}
