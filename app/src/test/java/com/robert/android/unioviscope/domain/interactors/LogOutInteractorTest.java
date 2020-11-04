package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.impl.LogOutInteractorImpl;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.utilTest.threading.TestMainThread;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Clase test para la clase LogOutInteractorImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.impl.LogOutInteractorImpl
 */
@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class LogOutInteractorTest {

    private MainThread mMainThread;
    @Mock
    private Executor mExecutor;
    @Mock
    private LogOutInteractor.Callback mCallback;
    @Mock
    private SessionRepository mSessionRepository;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
    }

    @Test
    public void testLogOut() {
        new LogOutInteractorImpl(mExecutor, mMainThread, mCallback, mSessionRepository).run();

        verify(mSessionRepository).deleteUser();
        verify(mSessionRepository).deleteToken();
        verify(mCallback).onLogOut();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }
}
