package com.robert.android.unioviscope.domain.interactors;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.impl.LogInInteractorImpl;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.model.types.Role;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.network.MockServiceGenerator;
import com.robert.android.unioviscope.network.service.StudentService;
import com.robert.android.unioviscope.utilTest.threading.TestMainThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Clase test para la clase LogInInteractorImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.impl.LogInInteractorImpl
 */
@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class LogInInteractorTest {

    private static final Integer SERVER_PORT = 1000;
    private static final Long RESPONSE_TIMEOUT = 3000L;

    @Mock
    private Executor mExecutor;
    @Mock
    private LogInInteractor.Callback mCallback;
    @Mock
    private SessionRepository mSessionRepository;
    @Mock
    private ConnectivityManager mConnectivityManager;
    @Mock
    private NetworkInfo mNetworkInfo;

    private MainThread mMainThread;
    private Gson mGson;
    private Context mContext;
    private MockWebServer mMockWebServer;
    private StudentService mService;

    private String mUserName;
    private String mPassword;
    private User mUser;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
        mGson = new Gson();
        mContext = Mockito.mock(Context.class);

        mMockWebServer = new MockWebServer();
        mMockWebServer.start(SERVER_PORT);

        mService = MockServiceGenerator.createService(mMockWebServer, StudentService.class);

        mUserName = "UOe1";
        mPassword = "e1";

        mUser = new User(1L);
        mUser.setUserName(mUserName);
        mUser.setFirstName("Nombre E1");
        mUser.setLastName("Apelidos E1");
        mUser.setDni("DNI1");
    }

    @Test
    public void testLogInSuccess() {
        String token = "Bearer token";

        MockResponse response = new MockResponse();
        response.addHeader("Authorization", token);
        mMockWebServer.enqueue(response);

        response = new MockResponse();
        mUser.setRole(Role.STUDENT);
        response.setBody(mGson.toJson(mUser));
        mMockWebServer.enqueue(response);

        new LogInInteractorImpl(mExecutor, mMainThread, mCallback, mContext,
                mService, mSessionRepository, mUserName, mPassword).run();

        verify(mSessionRepository, timeout(RESPONSE_TIMEOUT)).saveUser(mUser);
        verify(mSessionRepository, timeout(RESPONSE_TIMEOUT)).saveToken(token);
        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onSuccessLogIn(mUser);
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }


    @Test
    public void testLogInRoleNotAllowed() {
        String token = "Bearer token";

        MockResponse response = new MockResponse();
        response.addHeader("Authorization", token);

        mMockWebServer.enqueue(response);

        response = new MockResponse();
        mUser.setRole(Role.TEACHER);
        response.setBody(mGson.toJson(mUser));

        mMockWebServer.enqueue(response);

        new LogInInteractorImpl(mExecutor, mMainThread, mCallback, mContext,
                mService, mSessionRepository, mUserName, mPassword).run();

        verify(mSessionRepository, timeout(RESPONSE_TIMEOUT)).saveToken(token);
        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onRoleNotAllowedLogIn();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testLogInFailure() {
        mMockWebServer.enqueue(new MockResponse());

        new LogInInteractorImpl(mExecutor, mMainThread, mCallback, mContext,
                mService, mSessionRepository, mUserName, mPassword).run();

        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onFailureLogIn();
        verifyNoMoreInteractions(mCallback);
        verifyZeroInteractions(mSessionRepository);
    }

    @Test
    public void testLogInNoInternetConnection() {
        MockResponse response = new MockResponse();
        response.setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST);
        mMockWebServer.enqueue(response);

        new LogInInteractorImpl(mExecutor, mMainThread, mCallback, mContext,
                mService, mSessionRepository, mUserName, mPassword).run();

        when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mConnectivityManager);
        when((mConnectivityManager.getActiveNetworkInfo())).thenReturn(mNetworkInfo);
        when(mNetworkInfo.isConnectedOrConnecting()).thenReturn(false);

        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onNoInternetConnection();
        verifyNoMoreInteractions(mCallback);
        verifyZeroInteractions(mSessionRepository);
    }

    @Test
    public void testLogInServiceNotAvailable() {
        MockResponse response = new MockResponse();
        response.setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST);
        mMockWebServer.enqueue(response);

        new LogInInteractorImpl(mExecutor, mMainThread, mCallback, mContext,
                mService, mSessionRepository, mUserName, mPassword).run();

        when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mConnectivityManager);
        when((mConnectivityManager.getActiveNetworkInfo())).thenReturn(mNetworkInfo);
        when(mNetworkInfo.isConnectedOrConnecting()).thenReturn(true);

        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onServiceNotAvailable();
        verifyNoMoreInteractions(mCallback);
        verifyZeroInteractions(mSessionRepository);
    }

    @Test
    public void testLogInGetUserDetailsServiceNotAvailable() {
        String token = "Bearer token";

        MockResponse response = new MockResponse();
        response.addHeader("Authorization", token);
        mMockWebServer.enqueue(response);

        response = new MockResponse();
        mUser.setRole(Role.STUDENT);
        response.setBody(mGson.toJson(mUser));
        response.setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST);
        mMockWebServer.enqueue(response);

        new LogInInteractorImpl(mExecutor, mMainThread, mCallback, mContext,
                mService, mSessionRepository, mUserName, mPassword).run();

        when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mConnectivityManager);
        when((mConnectivityManager.getActiveNetworkInfo())).thenReturn(mNetworkInfo);
        when(mNetworkInfo.isConnectedOrConnecting()).thenReturn(true);

        verify(mSessionRepository, timeout(RESPONSE_TIMEOUT)).saveToken(token);
        verify(mCallback, timeout(RESPONSE_TIMEOUT * 2)).onServiceNotAvailable();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @After
    public void tearDown() throws Exception {
        mMockWebServer.shutdown();
    }
}
