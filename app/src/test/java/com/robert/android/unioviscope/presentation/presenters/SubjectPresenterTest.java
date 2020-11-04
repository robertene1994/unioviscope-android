package com.robert.android.unioviscope.presentation.presenters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.model.Subject;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.network.service.StudentService;
import com.robert.android.unioviscope.presentation.presenters.impl.SubjectPresenterImpl;
import com.robert.android.unioviscope.utilTest.threading.TestMainThread;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Clase test para la clase SubjectPresenterImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.presenters.impl.SubjectPresenterImpl
 */
@RunWith(MockitoJUnitRunner.class)
public class SubjectPresenterTest {

    @Mock
    private Executor mExecutor;
    @Mock
    private SubjectPresenter.View mCallback;
    @Mock
    private SessionRepository mSessionRepository;

    private MainThread mMainThread;
    private Context mContext;
    private StudentService mService;

    @Before
    public void setUp() throws Exception {
        mMainThread = new TestMainThread();
        mContext = mock(Context.class);
        mService = mock(StudentService.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSubjectsRetrieved() {

        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject(1L));

        SubjectPresenterImpl presenter = new SubjectPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository);
        presenter.getSubjects();

        spy(presenter).onSubjectsRetrieved(subjects);
        verify(mSessionRepository).getUser();
        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).showSubjects(any(ArrayAdapter.class));
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testSubjectEmpty() {
        SubjectPresenterImpl presenter = new SubjectPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository);
        presenter.getSubjects();

        spy(presenter).onSubjectsEmpty();

        verify(mSessionRepository).getUser();
        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).onSubjectsEmpty();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testNoInternetConnection() {
        SubjectPresenterImpl presenter = new SubjectPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository);
        presenter.getSubjects();

        spy(presenter).onNoInternetConnection();

        verify(mSessionRepository).getUser();
        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).noInternetConnection();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }

    @Test
    public void testServiceNotAvailable() {
        SubjectPresenterImpl presenter = new SubjectPresenterImpl(mExecutor, mMainThread,
                mCallback, mContext, mService, mSessionRepository);
        presenter.getSubjects();

        spy(presenter).onServiceNotAvailable();

        verify(mSessionRepository).getUser();
        verify(mCallback).showProgress();
        verify(mCallback).hideProgress();
        verify(mCallback).serviceNotAvailable();
        verifyNoMoreInteractions(mSessionRepository, mCallback);
    }
}
