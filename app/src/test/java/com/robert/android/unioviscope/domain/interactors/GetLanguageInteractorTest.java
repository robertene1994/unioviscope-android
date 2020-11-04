package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.impl.GetLanguageInteractorImpl;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;
import com.robert.android.unioviscope.utilTest.threading.TestMainThread;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Clase test para la clase GetLanguageInteractorImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.impl.GetLanguageInteractorImpl
 */
@SuppressWarnings("ConstantConditions, unused")
@RunWith(MockitoJUnitRunner.class)
public class GetLanguageInteractorTest {

    private MainThread mMainThread;
    @Mock
    private Executor mExecutor;
    @Mock
    private GetLanguageInteractor.Callback mCallback;
    @Mock
    private SettingsRepository mSettingsRepository;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
    }

    @Test
    public void testGetLanguageNull() {
        String language = null;
        String languageDefaultValue = Locale.getDefault().toString();

        when(mSettingsRepository.getLanguage()).thenReturn(language);

        new GetLanguageInteractorImpl(mExecutor, mMainThread, mCallback, mSettingsRepository).run();

        verify(mSettingsRepository).getLanguage();
        verify(mCallback).onLanguageRetrieved(languageDefaultValue);
    }


    @Test
    public void testGetLanguageEmpty() {
        String language = "";
        String languageDefaultValue = Locale.getDefault().toString();

        when(mSettingsRepository.getLanguage()).thenReturn(language);

        new GetLanguageInteractorImpl(mExecutor, mMainThread, mCallback, mSettingsRepository).run();

        verify(mSettingsRepository).getLanguage();
        verify(mCallback).onLanguageRetrieved(languageDefaultValue);
    }

    @Test
    public void testGetLanguageNotNull() {
        String language = "es_ES";

        when(mSettingsRepository.getLanguage()).thenReturn(language);

        new GetLanguageInteractorImpl(mExecutor, mMainThread, mCallback, mSettingsRepository).run();

        verify(mSettingsRepository).getLanguage();
        verify(mCallback).onLanguageRetrieved(language);
    }
}
