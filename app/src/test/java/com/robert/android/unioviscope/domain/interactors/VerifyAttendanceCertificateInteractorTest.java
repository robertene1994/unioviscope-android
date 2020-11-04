package com.robert.android.unioviscope.domain.interactors;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.impl.VerifyAttendanceCertificateInteractorImpl;
import com.robert.android.unioviscope.domain.model.AttendanceCertificate;
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
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Clase test para la clase VerifyAttendanceCertificateInteractorImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.impl.VerifyAttendanceCertificateInteractorImpl
 */
@RunWith(MockitoJUnitRunner.class)
public class VerifyAttendanceCertificateInteractorTest {

    private static final Integer SERVER_PORT = 1000;
    private static final Long RESPONSE_TIMEOUT = 3000L;

    @Mock
    private Executor mExecutor;
    @Mock
    private VerifyAttendanceCertificateInteractor.Callback mCallback;
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
    }

    @Test
    public void testValidAttendanceCertificate() {
        String qrCodeToken = "token";

        AttendanceCertificate attendanceCertificate =
                new AttendanceCertificate(mUser.getUserName(), qrCodeToken, new Date().getTime());

        MockResponse response = new MockResponse();
        response.setBody(mGson.toJson(true));
        mMockWebServer.enqueue(response);

        when(mSessionRepository.getUser()).thenReturn(mUser);

        VerifyAttendanceCertificateInteractorImpl interactor =
                new VerifyAttendanceCertificateInteractorImpl(mExecutor, mMainThread, mCallback, mContext, mService,
                        mSessionRepository, qrCodeToken);
        interactor.run();

        verify(mSessionRepository).getUser();
        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onValidAttendanceCertificate(attendanceCertificate);
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testInvalidAttendanceCertificate() {
        String qrCodeToken = "token";

        MockResponse response = new MockResponse();
        response.setBody(mGson.toJson(false));
        mMockWebServer.enqueue(response);

        when(mSessionRepository.getUser()).thenReturn(mUser);

        new VerifyAttendanceCertificateInteractorImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository, qrCodeToken).run();

        verify(mSessionRepository).getUser();
        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onInvalidAttendanceCertificate();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testVerifyAttendanceCertificateInternetConnection() {
        String qrCodeToken = "token";

        MockResponse response = new MockResponse();
        response.setBody(mGson.toJson(false));
        response.setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST);
        mMockWebServer.enqueue(response);

        when(mSessionRepository.getUser()).thenReturn(mUser);

        new VerifyAttendanceCertificateInteractorImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository, qrCodeToken).run();

        when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mConnectivityManager);
        when((mConnectivityManager.getActiveNetworkInfo())).thenReturn(mNetworkInfo);
        when(mNetworkInfo.isConnectedOrConnecting()).thenReturn(false);

        verify(mSessionRepository).getUser();
        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onNoInternetConnection();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testVerifyAttendanceCertificateServiceNotAvailable() {
        String qrCodeToken = "token";

        MockResponse response = new MockResponse();
        response.setBody(mGson.toJson(false));
        response.setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST);
        mMockWebServer.enqueue(response);

        when(mSessionRepository.getUser()).thenReturn(mUser);

        new VerifyAttendanceCertificateInteractorImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository, qrCodeToken).run();

        when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mConnectivityManager);
        when((mConnectivityManager.getActiveNetworkInfo())).thenReturn(mNetworkInfo);
        when(mNetworkInfo.isConnectedOrConnecting()).thenReturn(true);

        verify(mSessionRepository).getUser();
        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onServiceNotAvailable();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @After
    public void tearDown() throws Exception {
        mMockWebServer.shutdown();
    }
}
