package com.robert.android.unioviscope.storage;

import com.robert.android.unioviscope.domain.repository.SettingsRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Clase test para la clase SettingsRepositoryImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.storage.SettingsRepositoryImpl
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SettingsRepositoryTest {

    private SettingsRepository mSettingsRepository;
    private Boolean mIsFaceRecognition;
    private Boolean mIsHomeScreen;
    private String mLanguage;

    @Before
    public void setUp() throws Exception {
        mSettingsRepository = new SettingsRepositoryImpl(RuntimeEnvironment.application);

        mIsFaceRecognition = true;
        mIsHomeScreen = false;
        mLanguage = "es";
    }

    @Test
    public void testGetFaceRecognition() {
        assertNull(mSettingsRepository.getFaceRecognition());

        mSettingsRepository.saveFaceRecognition(mIsFaceRecognition);
        assertEquals(mIsFaceRecognition, mSettingsRepository.getFaceRecognition());
    }

    @Test
    public void testSaveFaceRecognition() {
        assertNull(mSettingsRepository.getFaceRecognition());

        mSettingsRepository.saveFaceRecognition(!mIsFaceRecognition);
        assertEquals(!mIsFaceRecognition, mSettingsRepository.getFaceRecognition());
    }

    @Test
    public void testGetHomeScreen() {
        assertNull(mSettingsRepository.getHomeScreen());

        mSettingsRepository.saveHomeScreen(mIsHomeScreen);
        assertEquals(mIsHomeScreen, mSettingsRepository.getHomeScreen());
    }

    @Test
    public void testSaveHomeScreen() {
        assertNull(mSettingsRepository.getHomeScreen());

        mSettingsRepository.saveHomeScreen(!mIsHomeScreen);
        assertEquals(!mIsHomeScreen, mSettingsRepository.getHomeScreen());
    }

    @Test
    public void testGetLanguage() {
        assertNull(mSettingsRepository.getLanguage());

        mSettingsRepository.saveLanguage(mLanguage);
        assertEquals(mLanguage, mSettingsRepository.getLanguage());
    }

    @Test
    public void testSaveLanguage() {
        mLanguage = "en";

        assertNull(mSettingsRepository.getLanguage());

        mSettingsRepository.saveLanguage(mLanguage);
        assertEquals(mLanguage, mSettingsRepository.getLanguage());
    }
}
