package com.robert.android.unioviscope.presentation.presenters;

import android.content.Context;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.model.types.Role;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;
import com.robert.android.unioviscope.network.service.StudentService;
import com.robert.android.unioviscope.presentation.presenters.impl.LogInPresenterImpl;
import com.robert.android.unioviscope.utilTest.threading.TestMainThread;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Clase test para la clase LogInPresenterImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.presenters.impl.LogInPresenterImpl
 */
@RunWith(MockitoJUnitRunner.class)
public class LogInPresenterTest {

    @Mock
    private Executor mExecutor;
    @Mock
    private LogInPresenter.View mCallback;
    @Mock
    private SessionRepository mSessionRepository;
    @Mock
    private SettingsRepository mSettingsRepository;

    private MainThread mMainThread;
    private Context mContext;
    private StudentService mService;
    private String mUserName;
    private String mPassword;
    private User mUser;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
        mContext = mock(Context.class);
        mService = mock(StudentService.class);

        mUserName = "UOe1";
        mPassword = "e1";
        mUser = new User(1L);
        mUser.setUserName(mUserName);
    }

    @Test
    public void testCheckUserSession() {
        LogInPresenterImpl presenter = new LogInPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository, mSettingsRepository);
        presenter.checkUserSession();

        spy(presenter).onUserLoggedIn();

        verify(mCallback).showProgress();
        verify(mCallback).onUserLoggedIn();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testCheckHomeScreen() {
        LogInPresenterImpl presenter = new LogInPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository, mSettingsRepository);
        presenter.checkHomeScreen();

        spy(presenter).onHomeScreenRetrieved(anyBoolean());

        verify(mCallback).onHomeScreenRetrieved(anyBoolean());
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testSuccessLogIn() {
        LogInPresenterImpl presenter = new LogInPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository, mSettingsRepository);
        presenter.logIn(mUserName, mPassword);

        spy(presenter).onSuccessLogIn(mUser);

        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).onSuccessLogIn(mUser);
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testLogInSuccess() {
        LogInPresenterImpl presenter = new LogInPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository, mSettingsRepository);
        presenter.logIn(mUserName, mPassword);

        spy(presenter).onFailureLogIn();

        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).onFailureLogIn();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testRoleNotAllowedLogIn() {
        mUser.setRole(Role.TEACHER);

        LogInPresenterImpl presenter = new LogInPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository, mSettingsRepository);
        presenter.logIn(mUserName, mPassword);

        spy(presenter).onRoleNotAllowedLogIn();

        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).onRoleNotAllowedLogIn();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testUserSessionExpired() {
        mUser.setRole(Role.ADMIN);

        LogInPresenterImpl presenter = new LogInPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository, mSettingsRepository);
        presenter.logIn(mUserName, mPassword);

        spy(presenter).onUserSessionExpired();

        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).onUserSessionExpired();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testUserNotLoggedIn() {
        LogInPresenterImpl presenter = new LogInPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository, mSettingsRepository);
        presenter.logIn(mUserName, mPassword);

        spy(presenter).onUserNotLoggedIn();

        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testNoInternetConnection() {
        LogInPresenterImpl presenter = new LogInPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository, mSettingsRepository);
        presenter.logIn(mUserName, mPassword);

        spy(presenter).onNoInternetConnection();

        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).noInternetConnection();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testServiceNotAvailable() {
        LogInPresenterImpl presenter = new LogInPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository, mSettingsRepository);
        presenter.logIn(mUserName, mPassword);

        spy(presenter).onServiceNotAvailable();

        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).serviceNotAvailable();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }
}
