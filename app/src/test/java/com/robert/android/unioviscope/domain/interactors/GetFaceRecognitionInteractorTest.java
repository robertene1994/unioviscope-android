package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.impl.GetFaceRecognitionInteractorImpl;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;
import com.robert.android.unioviscope.utilTest.threading.TestMainThread;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Clase test para la clase GetFaceRecognitionInteractorImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.impl.GetFaceRecognitionInteractorImpl
 */
@SuppressWarnings({"ConstantConditions", "unused"})
@RunWith(MockitoJUnitRunner.class)
public class GetFaceRecognitionInteractorTest {

    private MainThread mMainThread;
    @Mock
    private Executor mExecutor;
    @Mock
    private GetFaceRecognitionInteractor.Callback mCallback;
    @Mock
    private SettingsRepository mSettingsRepository;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
    }

    @Test
    public void testGetFaceRecognitionNull() {
        Boolean isFaceRecognition = null;
        Boolean isFaceRecognitionDefaultValue = true;
        when(mSettingsRepository.getFaceRecognition()).thenReturn(isFaceRecognition);

        new GetFaceRecognitionInteractorImpl(mExecutor, mMainThread, mCallback, mSettingsRepository).run();

        verify(mSettingsRepository).getFaceRecognition();
        verify(mCallback).onFaceRecognitionRetrieved(isFaceRecognitionDefaultValue);
    }

    @Test
    public void testGetFaceRecognitionTrue() {
        Boolean isFaceRecognition = true;
        when(mSettingsRepository.getFaceRecognition()).thenReturn(isFaceRecognition);

        new GetFaceRecognitionInteractorImpl(mExecutor, mMainThread, mCallback, mSettingsRepository).run();

        verify(mSettingsRepository).getFaceRecognition();
        verify(mCallback).onFaceRecognitionRetrieved(isFaceRecognition);
    }

    @Test
    public void testGetFaceRecognitionFalse() {
        Boolean isFaceRecognition = false;
        when(mSettingsRepository.getFaceRecognition()).thenReturn(isFaceRecognition);

        new GetFaceRecognitionInteractorImpl(mExecutor, mMainThread, mCallback, mSettingsRepository).run();

        verify(mSettingsRepository).getFaceRecognition();
        verify(mCallback).onFaceRecognitionRetrieved(isFaceRecognition);
    }
}
