package com.robert.android.unioviscope.domain.interactors;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.impl.CertifyAttendanceInteractorImpl;
import com.robert.android.unioviscope.domain.model.AttendanceCertificate;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.model.types.Role;
import com.robert.android.unioviscope.network.MockServiceGenerator;
import com.robert.android.unioviscope.network.service.StudentService;
import com.robert.android.unioviscope.utilTest.threading.TestMainThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
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
 * Clase test para la clase CertifyAttendanceInteractorImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.impl.CertifyAttendanceInteractorImpl
 */
@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class CertifyAttendanceInteractorTest {

    private static final Integer SERVER_PORT = 64395;
    private static final Long RESPONSE_TIMEOUT = 3000L;

    @Mock
    private Executor mExecutor;
    @Mock
    private CertifyAttendanceInteractor.Callback mCallback;
    @Mock
    private ConnectivityManager mConnectivityManager;
    @Mock
    private NetworkInfo mNetworkInfo;

    private MainThread mMainThread;
    private Gson mGson;
    private Context mContext;
    private MockWebServer mMockWebServer;
    private StudentService mService;
    private Bitmap mPhoto;
    private File mPhotoFile;

    private AttendanceCertificate mAttendanceCertificate;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
        mGson = new Gson();
        mContext = mock(Context.class);

        mMockWebServer = new MockWebServer();
        mMockWebServer.start(SERVER_PORT);

        mService = MockServiceGenerator.createService(mMockWebServer, StudentService.class);

        mPhotoFile = File.createTempFile("temp", ".jpg");
        mPhoto = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        User user = new User(1L);
        user.setUserName("UOe1");
        user.setFirstName("Nombre E1");
        user.setLastName("Apelidos E1");
        user.setDni("DNI1");
        user.setRole(Role.STUDENT);

        String qrCodeToken = "Bearer token";
        mAttendanceCertificate = new AttendanceCertificate(user.getUserName(), qrCodeToken, new Date().getTime());
    }

    @Test
    public void testSuccessCertifyAttendance() {
        MockResponse response = new MockResponse();
        response.setBody(mGson.toJson(true));
        mMockWebServer.enqueue(response);

        new CertifyAttendanceInteractorImpl(mExecutor, mMainThread, mCallback,
                mContext, mService, mAttendanceCertificate, mPhoto, mPhotoFile).run();

        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onSucessCertifyAttendance();
        verifyNoMoreInteractions(mCallback);
    }

    @Test
    public void testFailureCertifyAttendance() {
        MockResponse response = new MockResponse();
        response.setBody(mGson.toJson(false));
        mMockWebServer.enqueue(response);

        new CertifyAttendanceInteractorImpl(mExecutor, mMainThread, mCallback,
                mContext, mService, mAttendanceCertificate, mPhoto, mPhotoFile).run();

        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onFailureCertifyAttendance();
        verifyNoMoreInteractions(mCallback);
    }

    @Test
    public void testCertifyAttendanceError() {
        mPhoto = mock(Bitmap.class);

        MockResponse response = new MockResponse();
        response.setBody(mGson.toJson(false));
        mMockWebServer.enqueue(response);

        new CertifyAttendanceInteractorImpl(mExecutor, mMainThread, mCallback,
                mContext, mService, mAttendanceCertificate, mPhoto, mPhotoFile).run();

        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onCertifyAttendanceError();
        verifyNoMoreInteractions(mCallback);
    }

    @Test
    public void testCertifyAttendanceInternetConnection() {
        MockResponse response = new MockResponse();
        response.setBody(mGson.toJson(false));
        response.setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST);
        mMockWebServer.enqueue(response);


        new CertifyAttendanceInteractorImpl(mExecutor, mMainThread, mCallback,
                mContext, mService, mAttendanceCertificate, mPhoto, mPhotoFile).run();

        when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mConnectivityManager);
        when((mConnectivityManager.getActiveNetworkInfo())).thenReturn(mNetworkInfo);
        when(mNetworkInfo.isConnectedOrConnecting()).thenReturn(false);

        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onNoInternetConnection();
        verifyNoMoreInteractions(mCallback);
    }

    @Test
    public void testVerifyAttendanceCertificateServiceNotAvailable() {
        MockResponse response = new MockResponse();
        response.setBody(mGson.toJson(false));
        response.setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST);
        mMockWebServer.enqueue(response);

        new CertifyAttendanceInteractorImpl(mExecutor, mMainThread, mCallback,
                mContext, mService, mAttendanceCertificate, mPhoto, mPhotoFile).run();

        when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mConnectivityManager);
        when((mConnectivityManager.getActiveNetworkInfo())).thenReturn(mNetworkInfo);
        when(mNetworkInfo.isConnectedOrConnecting()).thenReturn(true);

        verify(mCallback, timeout(RESPONSE_TIMEOUT)).onServiceNotAvailable();
        verifyNoMoreInteractions(mCallback);
    }

    @After
    public void tearDown() throws Exception {
        mMockWebServer.shutdown();
    }
}