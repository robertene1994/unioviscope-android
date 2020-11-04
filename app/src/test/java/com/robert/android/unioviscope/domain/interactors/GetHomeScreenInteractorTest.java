package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.impl.GetHomeScreenInteractorImpl;
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
 * Clase test para la clase GetHomeScreenInteractorImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.impl.GetHomeScreenInteractorImpl
 */
@SuppressWarnings("ConstantConditions, unused")
@RunWith(MockitoJUnitRunner.class)
public class GetHomeScreenInteractorTest {

    private MainThread mMainThread;
    @Mock
    private Executor mExecutor;
    @Mock
    private GetHomeScreenInteractor.Callback mCallback;
    @Mock
    private SettingsRepository mSettingsRepository;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
    }

    @Test
    public void testGetHomeScreenNull() {
        Boolean isHomeScreen = null;
        Boolean isHomeScreenDefaultValue = false;
        when(mSettingsRepository.getHomeScreen()).thenReturn(isHomeScreen);

        new GetHomeScreenInteractorImpl(mExecutor, mMainThread, mCallback, mSettingsRepository).run();

        verify(mSettingsRepository).getHomeScreen();
        verify(mCallback).onHomeScreenRetrieved(isHomeScreenDefaultValue);
    }

    @Test
    public void testGetHomeScreenTrue() {
        Boolean isHomeScreen = true;
        when(mSettingsRepository.getHomeScreen()).thenReturn(isHomeScreen);

        new GetHomeScreenInteractorImpl(mExecutor, mMainThread, mCallback, mSettingsRepository).run();

        verify(mSettingsRepository).getHomeScreen();
        verify(mCallback).onHomeScreenRetrieved(isHomeScreen);
    }

    @Test
    public void testGetHomeScreenFalse() {
        Boolean isHomeScreen = false;

        new GetHomeScreenInteractorImpl(mExecutor, mMainThread, mCallback, mSettingsRepository).run();

        verify(mSettingsRepository).getHomeScreen();
        verify(mCallback).onHomeScreenRetrieved(isHomeScreen);
    }
}
