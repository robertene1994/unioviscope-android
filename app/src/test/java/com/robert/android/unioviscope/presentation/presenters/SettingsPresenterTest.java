package com.robert.android.unioviscope.presentation.presenters;

import android.content.Context;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;
import com.robert.android.unioviscope.presentation.presenters.impl.SettingsPresenterImpl;
import com.robert.android.unioviscope.utilTest.threading.TestMainThread;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Clase test para la clase SettingsPresenterImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.presenters.impl.SettingsPresenterImpl
 */
@RunWith(MockitoJUnitRunner.class)
public class SettingsPresenterTest {

    @Mock
    private Executor mExecutor;
    @Mock
    private SettingsPresenter.View mCallback;
    @Mock
    private SettingsRepository mSettingsRepository;

    private MainThread mMainThread;
    private Context mContext;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
        mContext = mock(Context.class);
    }

    @Test
    public void testGetFaceRecognition() {
        SettingsPresenterImpl presenter = new SettingsPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mSettingsRepository);
        presenter.getFaceRecognition();

        spy(presenter).onFaceRecognitionRetrieved(anyBoolean());

        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).onFaceRecognitionRetrieved(anyBoolean());
        verifyNoMoreInteractions(mSettingsRepository, mCallback);
    }

    @Test
    public void testSaveFaceRecognition() {
        SettingsPresenterImpl presenter = new SettingsPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mSettingsRepository);
        presenter.saveFaceRecognition(true);

        spy(presenter).onSavedSettings();

        verifyNoMoreInteractions(mSettingsRepository, mCallback);
    }

    @Test
    public void testGetHomeScreen() {
        SettingsPresenterImpl presenter = new SettingsPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mSettingsRepository);
        presenter.getHomeScreen();

        spy(presenter).onHomeScreenRetrieved(false);

        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).onHomeScreenRetrieved(false);
        verifyNoMoreInteractions(mSettingsRepository, mCallback);
    }

    @Test
    public void testSaveHomeScreen() {
        SettingsPresenterImpl presenter = new SettingsPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mSettingsRepository);
        presenter.saveHomeScreen(true);

        spy(presenter).onSavedSettings();

        verifyNoMoreInteractions(mSettingsRepository, mCallback);
    }

    @Test
    public void testGetLanguage() {
        SettingsPresenterImpl presenter = new SettingsPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mSettingsRepository);
        presenter.getLanguage();

        spy(presenter).onLanguageRetrieved("es");

        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).onLanguageRetrieved("es");
        verifyNoMoreInteractions(mSettingsRepository, mCallback);
    }
}
