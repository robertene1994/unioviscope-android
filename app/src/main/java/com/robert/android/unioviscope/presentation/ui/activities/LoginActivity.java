package com.robert.android.unioviscope.presentation.ui.activities;

import android.annotation.SuppressLint;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.robert.android.unioviscope.R;
import com.robert.android.unioviscope.domain.executor.impl.ThreadExecutor;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.network.ServiceGenerator;
import com.robert.android.unioviscope.network.service.StudentService;
import com.robert.android.unioviscope.presentation.presenters.LogInPresenter;
import com.robert.android.unioviscope.presentation.presenters.impl.LogInPresenterImpl;
import com.robert.android.unioviscope.presentation.ui.activities.base.AbstractActivity;
import com.robert.android.unioviscope.presentation.ui.utils.SnackbarUtil;
import com.robert.android.unioviscope.storage.SessionRepositoryImpl;
import com.robert.android.unioviscope.storage.SettingsRepositoryImpl;
import com.robert.android.unioviscope.threading.MainThreadImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Activity que extiende la clase AbstractActivity e implementa la interfaz del view del presenter LogInPresenter.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.ui.activities.base.AbstractActivity
 * @see com.robert.android.unioviscope.presentation.presenters.LogInPresenter.View
 */
@SuppressLint({"Registered", "NonConstantResourceId"})
@EActivity(R.layout.activity_login)
public class LoginActivity extends AbstractActivity implements LogInPresenter.View {

    public static final String KEY_IS_HOME_SCREEN = "IS_HOME_SCREEN";

    // vistas que se ocultan al realizar alguna operación (cargando)
    @ViewById(R.id.activityLoginLayout)
    View mActivityLoginLayout;
    @ViewById(R.id.logoImageView)
    View mLogoImageView;
    @ViewById(R.id.userNameTextInputLayout)
    View mUserNameTextInputLayout;
    @ViewById(R.id.passwordTextInputLayout)
    View mPasswordTextInputLayout;

    // vista del spinner (cargando)
    @ViewById(R.id.progressBarConstraintLayout)
    View mProgressBarConstraintLayout;

    @ViewById(R.id.userNameEditText)
    EditText mUserNameEditText;
    @ViewById(R.id.passwordEditText)
    EditText mPasswordEditText;
    @ViewById(R.id.loginButton)
    Button mLoginButton;

    private LogInPresenter mLogInPresenter;

    @Override
    protected void onResume() {
        mLogInPresenter.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mLogInPresenter.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mLogInPresenter.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mLogInPresenter.destroy();
        super.onDestroy();
    }

    @AfterViews
    void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        setProgressBarLayout(mProgressBarConstraintLayout);
        addViewsToHide(mLogoImageView, mUserNameTextInputLayout, mPasswordTextInputLayout, mLoginButton);

        mLogInPresenter = new LogInPresenterImpl(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this,
                getContext(), ServiceGenerator.createService(getContext(), StudentService.class),
                new SessionRepositoryImpl(getContext()), new SettingsRepositoryImpl(getContext()));

        mLogInPresenter.checkUserSession();
    }

    @Click(R.id.loginButton)
    public void onClickLogIn() {
        String userName = mUserNameEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        if (userName.isEmpty()) {
            SnackbarUtil.makeShort(mUserNameEditText, R.string.msg_userName_required);
        } else if (password.isEmpty()) {
            SnackbarUtil.makeShort(mPasswordEditText, R.string.msg_password_required);
        } else {
            mLogInPresenter.logIn(userName, password);
        }
    }

    @Override
    public void onSuccessLogIn(User user) {
        CertifyAttendanceActivity_.intent(LoginActivity.this).start();
    }

    @Override
    public void onUserLoggedIn() {
        mLogInPresenter.checkHomeScreen();
    }

    @Override
    public void onUserSessionExpired() {
        SnackbarUtil.makeShort(mActivityLoginLayout, R.string.msg_expired_session);
    }

    @Override
    public void onHomeScreenRetrieved(Boolean isHomeScreen) {
        CertifyAttendanceActivity_.intent(this).extra(KEY_IS_HOME_SCREEN, isHomeScreen).start();
    }

    @Override
    public void onFailureLogIn() {
        SnackbarUtil.makeShort(mActivityLoginLayout, R.string.msg_invalid_userName_or_password);
    }

    @Override
    public void onRoleNotAllowedLogIn() {
        SnackbarUtil.makeShort(mActivityLoginLayout, R.string.msg_only_students_allowed);
    }

    @Override
    public void noInternetConnection() {
        SnackbarUtil.makeLong(mActivityLoginLayout, R.string.msg_no_internet_connection);
    }

    @Override
    public void serviceNotAvailable() {
        SnackbarUtil.makeLong(mActivityLoginLayout, R.string.msg_service_not_avabile);
    }
}
