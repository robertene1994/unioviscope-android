package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.interactors.impl.GetLanguageSynchronousInteractorImpl;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Clase test para la clase GetLanguageSynchronousInteractorImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.impl.GetLanguageSynchronousInteractorImpl
 */
@SuppressWarnings("ConstantConditions, unused")
public class GetLanguageSynchronousInteractorTest {

    @Mock
    private SettingsRepository mSettingsRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetLanguageNull() {
        String language = null;
        String languageDefaultValue = Locale.getDefault().toString();

        when(mSettingsRepository.getLanguage()).thenReturn(language);

        GetLanguageSynchronousInteractorImpl interactor = new GetLanguageSynchronousInteractorImpl(mSettingsRepository);

        Assert.assertEquals(languageDefaultValue, interactor.getLanguage());
        verify(mSettingsRepository).getLanguage();
    }

    @Test
    public void testGetLanguageEmpty() {
        String language = "";
        String languageDefaultValue = Locale.getDefault().toString();

        when(mSettingsRepository.getLanguage()).thenReturn(language);

        GetLanguageSynchronousInteractorImpl interactor = new GetLanguageSynchronousInteractorImpl(mSettingsRepository);

        Assert.assertEquals(languageDefaultValue, interactor.getLanguage());
        verify(mSettingsRepository).getLanguage();
    }

    @Test
    public void testGetLanguageNotNull() {
        String language = "es_ES";

        when(mSettingsRepository.getLanguage()).thenReturn(language);

        GetLanguageSynchronousInteractorImpl interactor = new GetLanguageSynchronousInteractorImpl(mSettingsRepository);

        Assert.assertEquals(language, interactor.getLanguage());
        verify(mSettingsRepository).getLanguage();
    }
}
