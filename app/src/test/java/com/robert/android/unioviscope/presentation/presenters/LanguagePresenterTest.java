package com.robert.android.unioviscope.presentation.presenters;

import com.robert.android.unioviscope.domain.repository.SettingsRepository;
import com.robert.android.unioviscope.presentation.presenters.impl.LanguagePresenterImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * Clase test para la clase LanguagePresenterImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.presenters.impl.LanguagePresenterImpl
 */
@RunWith(MockitoJUnitRunner.class)
public class LanguagePresenterTest {

    @Mock
    private SettingsRepository mSettingsRepository;

    @Test
    public void testCheckUserSession() {
        LanguagePresenterImpl presenter = new LanguagePresenterImpl(mSettingsRepository);
        presenter.getLanguage();

        verify(mSettingsRepository).getLanguage();
    }
}
