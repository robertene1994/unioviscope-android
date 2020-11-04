package com.robert.android.unioviscope.domain.interactors;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.impl.SessionInteractorImpl;
import com.robert.android.unioviscope.domain.model.Attendance;
import com.robert.android.unioviscope.domain.model.Session;
import com.robert.android.unioviscope.domain.model.Subject;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.model.types.GroupType;
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
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Clase test para la clase SessionInteractorImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.impl.SessionInteractorImpl
 */
@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class SessionInteractorTest {

    private static final Integer SERVER_PORT = 1000;
    private static final Long RESPONSE_TIMEOUT = 3000L;

    @Mock
    private Executor mExecutor;
    @Mock
    private SessionInteractor.Callback mCallback;
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

    private User mUser;
    private Subject mSubject;
    private List<Session> mSessions;
    private Attendance mAttendance;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
        mGson = new Gson();
        mContext = mock(Context.class);

        mMockWebServer = new MockWebServer();
        mMockWebServer.start(SERVER_PORT);

        mService = MockServiceGenerator.createService(mMockWebServer, StudentService.class);

        mUser = new User(1L);
        mUser.setUserName("UOe1");
        mUser.setFirstName("Nombre E1");
        mUser.setLastName("Apelidos E1");
        mUser.setDni("DNI1");
        mUser.setRole(Role.STUDENT);

        mSubject = new Subject(1L);

        mSessions = new ArrayList<>();
        Session session = new Session(1L);
        mSessions.add(session);

        mAttendance = new Attendance(1L);
    }

    @Test
    public void testSessionsRetrieved() {
        MockResponse response = new MockResponse();
        response.setBody(mGson.toJson(mSessions));
        mMockWebServer.enqueue(response);

        response = new MockResponse();
        response.setBody(mGson.toJson(mAttendance));
        mMockWebServer.enqueue(response);

        when(mSessionRepository.getUser()).thenReturn(mUser);

        new SessionInteractorImpl(mExecutor, mMainThread, mCallback,
                mContext, mService, mSessionRepository, mSubject, GroupType.THEORY).run();

        verify(mSessionRepository).getUser();
        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onSessionsRetrieved(mSessions);
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testSessionsEmpty() {
        MockResponse response = new MockResponse();
        mSessions.clear();
        response.setBody(mGson.toJson(mSessions));
        mMockWebServer.enqueue(response);

        when(mSessionRepository.getUser()).thenReturn(mUser);

        new SessionInteractorImpl(mExecutor, mMainThread, mCallback,
                mContext, mService, mSessionRepository, mSubject, GroupType.PRACTICE).run();

        verify(mSessionRepository).getUser();
        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onSessionsEmpty();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }


    @Test
    public void testSessionsInternetConnection() {
        MockResponse response = new MockResponse();
        response.setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST);
        mMockWebServer.enqueue(response);

        when(mSessionRepository.getUser()).thenReturn(mUser);

        new SessionInteractorImpl(mExecutor, mMainThread, mCallback,
                mContext, mService, mSessionRepository, mSubject, GroupType.GROUP_TUTORSHIP).run();

        when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mConnectivityManager);
        when((mConnectivityManager.getActiveNetworkInfo())).thenReturn(mNetworkInfo);
        when(mNetworkInfo.isConnectedOrConnecting()).thenReturn(false);


        verify(mSessionRepository).getUser();
        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onNoInternetConnection();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testSessionsServiceNotAvailable() {
        MockResponse response = new MockResponse();
        response.setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST);
        mMockWebServer.enqueue(response);

        when(mSessionRepository.getUser()).thenReturn(mUser);

        new SessionInteractorImpl(mExecutor, mMainThread, mCallback,
                mContext, mService, mSessionRepository, mSubject, GroupType.SEMINAR).run();

        when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mConnectivityManager);
        when((mConnectivityManager.getActiveNetworkInfo())).thenReturn(mNetworkInfo);
        when(mNetworkInfo.isConnectedOrConnecting()).thenReturn(true);

        verify(mSessionRepository).getUser();
        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onServiceNotAvailable();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testSessionsGetAttendancesServiceNotAvailable() {
        MockResponse response = new MockResponse();
        response.setBody(mGson.toJson(mSessions));
        mMockWebServer.enqueue(response);

        MockResponse responseAttendance = new MockResponse();
        responseAttendance.setBody(mGson.toJson(mAttendance));
        responseAttendance.setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST);
        mMockWebServer.enqueue(responseAttendance);

        when(mSessionRepository.getUser()).thenReturn(mUser);

        new SessionInteractorImpl(mExecutor, mMainThread, mCallback,
                mContext, mService, mSessionRepository, mSubject, GroupType.PRACTICE).run();

        when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mConnectivityManager);
        when((mConnectivityManager.getActiveNetworkInfo())).thenReturn(mNetworkInfo);
        when(mNetworkInfo.isConnectedOrConnecting()).thenReturn(true);

        verify(mSessionRepository).getUser();
        verify(mCallback, timeout(RESPONSE_TIMEOUT * 2)).onServiceNotAvailable();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @After
    public void tearDown() throws Exception {
        mMockWebServer.shutdown();
    }
}
