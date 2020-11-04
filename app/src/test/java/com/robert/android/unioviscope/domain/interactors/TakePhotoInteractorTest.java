package com.robert.android.unioviscope.domain.interactors;

import android.os.Environment;
import android.test.mock.MockContext;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.impl.TakePhotoInteractorImpl;
import com.robert.android.unioviscope.utilTest.threading.TestMainThread;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Clase test para la clase TakePhotoInteractorImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.impl.TakePhotoInteractorImpl
 */
@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class TakePhotoInteractorTest {

    private static final String PHOTO_NAME = "test";
    private static final String PHOTO_FORMAT = ".jpg";

    @Mock
    private Executor mExecutor;
    @Mock
    private TakePhotoInteractor.Callback mCallback;
    @Mock
    private MockContext mContext;

    private MainThread mMainThread;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
    }

    @Test
    public void testTakePhotoPrepared() {
        File storageDirectory = new File("");

        when(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)).thenReturn(storageDirectory);

        new TakePhotoInteractorImpl(mExecutor, mMainThread, mCallback, mContext).run();

        verify(mCallback).onTakePhotoError();
    }
}
