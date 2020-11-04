package com.robert.android.unioviscope.presentation.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.face.FaceDetector;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.robert.android.unioviscope.R;
import com.robert.android.unioviscope.domain.executor.impl.ThreadExecutor;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.network.ServiceGenerator;
import com.robert.android.unioviscope.network.service.StudentService;
import com.robert.android.unioviscope.presentation.presenters.CertifyAttendancePresenter;
import com.robert.android.unioviscope.presentation.presenters.impl.CertifyAttendancePresenterImpl;
import com.robert.android.unioviscope.presentation.ui.activities.base.AbstractActivity;
import com.robert.android.unioviscope.presentation.ui.dialogs.FaceDetectorNotAvaibleDialog;
import com.robert.android.unioviscope.presentation.ui.dialogs.MoreThanOneFaceDetectedDialog;
import com.robert.android.unioviscope.presentation.ui.dialogs.NoFaceDetectedDialog;
import com.robert.android.unioviscope.presentation.ui.fragments.SubjectFragment;
import com.robert.android.unioviscope.presentation.ui.utils.SnackbarUtil;
import com.robert.android.unioviscope.storage.SessionRepositoryImpl;
import com.robert.android.unioviscope.storage.SettingsRepositoryImpl;
import com.robert.android.unioviscope.threading.MainThreadImpl;
import com.robert.android.unioviscope.utils.LocaleUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Activity que extiende la clase AbstractActivity e implementa la interfaz del view del presenter
 * CertifyAttendancePresenter.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.ui.activities.base.AbstractActivity
 * @see com.robert.android.unioviscope.presentation.presenters.CertifyAttendancePresenter.View
 */
@SuppressLint({"Registered", "NonConstantResourceId"})
@EActivity(R.layout.activity_navigation_drawer)
public class CertifyAttendanceActivity extends AbstractActivity implements CertifyAttendancePresenter.View,
        NavigationView.OnNavigationItemSelectedListener {

    private static final Integer MY_PERMISSIONS_REQUEST_CAMERA_RECOGNIZE = IntentIntegrator.REQUEST_CODE + 100;
    private static final Integer MY_PERMISSIONS_REQUEST_CAMERA_SCAN_QR = 1100;
    private static final Integer SCANNER_REQUEST_CODE = IntentIntegrator.REQUEST_CODE;
    private static final Integer CAMERA_REQUEST_CODE = 1000;

    // vistas que se ocultan al realizar alguna operación (cargando)
    @ViewById(R.id.fragmentSubjectLayout)
    View mSubjectsLayout;

    // vista del spinner (cargando)
    @ViewById(R.id.progressBarLayout)
    View mProgressBarConstraintLayout;

    @ViewById(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @ViewById(R.id.navigationView)
    NavigationView mNavigationView;
    @ViewById(R.id.loggedUserNameTextView)
    TextView mLoggedUserNameTextView;
    @ViewById(R.id.loggedUserEmailTextView)
    TextView mLoggedUserEmailTextView;

    private CertifyAttendancePresenter mCertifyAttendancePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCertifyAttendancePresenter = new CertifyAttendancePresenterImpl(ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(), this, getContext(), getFaceDetector(),
                ServiceGenerator.createService(getContext(), StudentService.class),
                new SessionRepositoryImpl(getContext()), new SettingsRepositoryImpl(getContext()));
        loadExtras();
    }

    @Override
    public void onResume() {
        setNavigationViewItems();
        mCertifyAttendancePresenter.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mCertifyAttendancePresenter.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mCertifyAttendancePresenter.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mCertifyAttendancePresenter.destroy();
        super.onDestroy();
    }

    @AfterViews
    void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        View headerLayout = mNavigationView.inflateHeaderView(R.layout.nav_header_navigation_drawer);

        mLoggedUserNameTextView = headerLayout.findViewById(R.id.loggedUserNameTextView);
        mLoggedUserEmailTextView = headerLayout.findViewById(R.id.loggedUserEmailTextView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new SubjectFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentSubjectLayout, fragment).commit();

        setProgressBarLayout(mProgressBarConstraintLayout);
        addViewsToHide(mSubjectsLayout);

        mCertifyAttendancePresenter.getUser();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == SCANNER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                if (scanResult != null)
                    mCertifyAttendancePresenter.processQrCode(scanResult.getContents());
            }
        } else if (requestCode == CAMERA_REQUEST_CODE)
            if (resultCode == Activity.RESULT_OK)
                mCertifyAttendancePresenter.processPhoto();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA_RECOGNIZE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                mCertifyAttendancePresenter.takePhoto();
            else
                SnackbarUtil.makeLong(mDrawerLayout, R.string.msg_camera_permission);
        } else if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA_SCAN_QR)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                scanQRCode();
            else
                SnackbarUtil.makeLong(mDrawerLayout, R.string.msg_camera_permission);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.certifyPresenceNavItem) {
            scanQRCode();
        } else if (id == R.id.reviewAttendancesNavItem) {
            Fragment fragment = new SubjectFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentSubjectLayout, fragment).commit();
        } else if (id == R.id.settingsNavItem) {
            SettingsActivity_.intent(this).start();
        } else if (id == R.id.logoutNavItem) {
            mCertifyAttendancePresenter.logOut();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void noInternetConnection() {
        SnackbarUtil.makeLong(mDrawerLayout, R.string.msg_no_internet_connection);
    }

    @Override
    public void serviceNotAvailable() {
        SnackbarUtil.makeLong(mDrawerLayout, R.string.msg_service_not_avabile);
    }

    @Override
    public void onUserRetrieved(User user) {
        mLoggedUserNameTextView.setText(user.getLastName().concat(" ").concat(user.getFirstName()));
        mLoggedUserEmailTextView.setText(getString(R.string.nav_user_email, user.getUserName()));
    }

    @Override
    public void onTakePhotoError() {
        SnackbarUtil.makeLong(mDrawerLayout, R.string.msg_prepare_take_photo_error);
    }

    @Override
    public void onTakePhotoPrepared(Uri photoUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void checkCameraPermissions() {
        if (ContextCompat.checkSelfPermission(CertifyAttendanceActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CertifyAttendanceActivity.this,
                    Manifest.permission.CAMERA)) {
                SnackbarUtil.makeLong(mDrawerLayout, R.string.msg_camera_permission);
            }
            ActivityCompat.requestPermissions(CertifyAttendanceActivity.this,
                    new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA_RECOGNIZE);
        } else {
            mCertifyAttendancePresenter.takePhoto();
        }
    }

    @Override
    public void onInvalidAttendanceCertificate() {
        SnackbarUtil.makeLong(mDrawerLayout, R.string.msg_invalid_qrcode);
    }

    @Override
    public void onProcessPhotoError() {
        SnackbarUtil.makeLong(mDrawerLayout, R.string.msg_process_photo_error);
    }

    @Override
    public void onFaceDetectorNotAvailable() {
        FaceDetectorNotAvaibleDialog.create(this, mCertifyAttendancePresenter).show();
    }

    @Override
    public void onMoreThanOneFaceDetected() {
        MoreThanOneFaceDetectedDialog.create(this, mCertifyAttendancePresenter).show();
    }

    @Override
    public void onNoFaceDetected() {
        NoFaceDetectedDialog.create(this, mCertifyAttendancePresenter).show();
    }

    @Override
    public void onSucessCertifyAttendance() {
        SnackbarUtil.makeLong(mDrawerLayout, R.string.msg_confirmed_attendance);
    }

    @Override
    public void onFailureCertifyAttendance() {
        SnackbarUtil.makeLong(mDrawerLayout, R.string.msg_certify_attendance_process_failed);
    }

    @Override
    public void onCertifyAttendanceError() {
        SnackbarUtil.makeLong(mDrawerLayout, R.string.msg_certify_attendance_error);
    }

    @Override
    public void onLogOut() {
        LoginActivity_.intent(this).start();
        finish();
    }

    private FaceDetector getFaceDetector() {
        return new FaceDetector.Builder(getContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.ACCURATE_MODE)
                .build();
    }

    private void setNavigationViewItems() {
        mNavigationView.setCheckedItem(R.id.reviewAttendancesNavItem);

        MenuItem item = mNavigationView.getMenu().findItem(R.id.certifyPresenceNavItem);
        item.setTitle(LocaleUtil.getStringByLocale(getContext(), R.string.nav_certify_presence));
        item = mNavigationView.getMenu().findItem(R.id.reviewAttendancesNavItem);
        item.setTitle(LocaleUtil.getStringByLocale(getContext(), R.string.nav_review_attendances));
        item = mNavigationView.getMenu().findItem(R.id.settingsNavItem);
        item.setTitle(LocaleUtil.getStringByLocale(getContext(), R.string.nav_settings));
        item = mNavigationView.getMenu().findItem(R.id.logoutNavItem);
        item.setTitle(LocaleUtil.getStringByLocale(getContext(), R.string.nav_logout));
    }

    private void loadExtras() {
        Bundle receivedBundle = getIntent().getExtras();

        if (receivedBundle != null) {
            Boolean isHomeScreen = receivedBundle.getBoolean(LoginActivity.KEY_IS_HOME_SCREEN);

            if (isHomeScreen) {
                getIntent().removeExtra(LoginActivity.KEY_IS_HOME_SCREEN);
                scanQRCode();
            }
        }
    }

    private void scanQRCode() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                SnackbarUtil.makeLong(mDrawerLayout, R.string.msg_camera_permission);
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA_SCAN_QR);
        } else {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setPrompt(LocaleUtil.getStringByLocale(getContext(), R.string.msg_qrcode_prompt));
            integrator.setBeepEnabled(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        }
    }
}
