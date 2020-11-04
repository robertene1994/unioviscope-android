package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.impl.SaveLanguageInteractorImpl;
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
 * Clase test para la clase SaveLanguageInteractorImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.impl.SaveLanguageInteractorImpl
 */
@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class SaveLanguageInteractorTest {

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
    public void testSaveHomeScreen() {

        String language = "en";

        when(mSettingsRepository.getLanguage()).thenReturn(language);

        new SaveLanguageInteractorImpl(mExecutor, mMainThread, mCallback, mSettingsRepository, language).run();

        verify(mSettingsRepository).saveLanguage(language);
        verify(mCallback).onSavedSettings();
        verifyNoMoreInteractions(mSettingsRepository, mCallback);
        Assert.assertEquals(language, mSettingsRepository.getLanguage());
    }
}
