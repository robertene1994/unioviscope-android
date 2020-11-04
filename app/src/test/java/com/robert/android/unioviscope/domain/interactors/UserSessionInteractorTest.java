package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.impl.UserSessionInteractorImpl;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.model.types.Role;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.utilTest.threading.TestMainThread;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Clase test para la clase UserSessionInteractorImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.impl.UserSessionInteractorImpl
 */
@SuppressWarnings("ConstantConditions, unused")
@RunWith(MockitoJUnitRunner.class)
public class UserSessionInteractorTest {

    @Mock
    private Executor mExecutor;
    @Mock
    private UserSessionInteractor.Callback mCallback;
    @Mock
    private SessionRepository mSessionRepository;

    private MainThread mMainThread;
    private User mUser;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
    }

    @Test
    public void testUserLoggedIn() {
        mUser = new User(1L);
        mUser.setUserName("UOe1");
        mUser.setRole(Role.STUDENT);

        String token = null;

        when(mSessionRepository.getUser()).thenReturn(mUser);
        when(mSessionRepository.getToken()).thenReturn(token);

        new UserSessionInteractorImpl(mExecutor, mMainThread, mCallback, mSessionRepository).run();

        verify(mSessionRepository).getUser();
        verify(mSessionRepository).getToken();
        verify(mCallback).onUserSessionExpired();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testSessionExpired() {
        mUser = new User(1L);
        mUser.setUserName("UOe1");
        mUser.setRole(Role.STUDENT);

        String token = null;
        when(mSessionRepository.getUser()).thenReturn(mUser);
        when(mSessionRepository.getToken()).thenReturn(token);

        new UserSessionInteractorImpl(mExecutor, mMainThread, mCallback, mSessionRepository).run();

        verify(mSessionRepository).getUser();
        verify(mSessionRepository).getToken();
        verify(mCallback).onUserSessionExpired();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testUserNotLoggedIn() {
        mUser = null;
        String token = null;

        when(mSessionRepository.getUser()).thenReturn(mUser);
        when(mSessionRepository.getToken()).thenReturn(token);

        new UserSessionInteractorImpl(mExecutor, mMainThread, mCallback, mSessionRepository).run();

        verify(mSessionRepository).getUser();
        verify(mSessionRepository).getToken();
        verify(mCallback).onUserNotLoggedIn();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }
}
