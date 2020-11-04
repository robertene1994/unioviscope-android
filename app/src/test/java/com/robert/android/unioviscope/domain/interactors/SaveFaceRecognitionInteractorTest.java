package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.impl.SaveFaceRecognitionInteractorImpl;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;
import com.robert.android.unioviscope.utilTest.threading.TestMainThread;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Clase test para la clase SaveFaceRecognitionInteractorImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.impl.SaveFaceRecognitionInteractorImpl
 */
@SuppressWarnings("ConstantConditions, unused")
@RunWith(MockitoJUnitRunner.class)
public class SaveFaceRecognitionInteractorTest {

    private MainThread mMainThread;
    @Mock
    private Executor mExecutor;
    @Mock
    private SaveSettingsInteractor.Callback mCallback;
    @Mock
    private SettingsRepository mSettingsRepository;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
    }

    @Test
    public void testSaveFaceRecognition() {
        Boolean isFaceRecognition = true;

        when(mSettingsRepository.getFaceRecognition()).thenReturn(isFaceRecognition);

        new SaveFaceRecognitionInteractorImpl(mExecutor, mMainThread,
                mCallback, mSettingsRepository, isFaceRecognition).run();

        verify(mSettingsRepository).saveFaceRecognition(isFaceRecognition);
        verify(mCallback).onSavedSettings();
        verifyNoMoreInteractions(mSettingsRepository, mCallback);

        Assert.assertEquals(isFaceRecognition, mSettingsRepository.getFaceRecognition());
    }
}
