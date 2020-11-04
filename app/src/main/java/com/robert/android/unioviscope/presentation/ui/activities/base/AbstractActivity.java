package com.robert.android.unioviscope.presentation.ui.activities.base;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.robert.android.unioviscope.presentation.presenters.LanguagePresenter;
import com.robert.android.unioviscope.presentation.presenters.impl.LanguagePresenterImpl;
import com.robert.android.unioviscope.storage.SettingsRepositoryImpl;
import com.robert.android.unioviscope.utils.ContextWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Clase base abstracta para todas las activities que sirve como punto común para los accesos al contexto de la
 * aplicación y el manejo de las vistas antes y después de las operaciones realizadas.
 *
 * @author Robert Ene
 */
public abstract class AbstractActivity extends AppCompatActivity {

    private Context mContext;
    private List<View> mViewsToHide;
    private View mProgressBarLayout;

    @Override
    protected void attachBaseContext(Context context) {
        mViewsToHide = new ArrayList<>();
        wrapContextLanguage(context);
        super.attachBaseContext(getContext());
    }

    /**
     * Método que devuelve el contexto de la aplicación.
     *
     * @return el contexto de la aplicación.
     */
    protected Context getContext() {
        return mContext;
    }

    /**
     * Método que establece el layout del spinner (cargando).
     *
     * @param progressBarlayout el layout del spinner.
     */
    protected void setProgressBarLayout(View progressBarlayout) {
        mProgressBarLayout = progressBarlayout;
    }

    /**
     * Método que añade las vistas que se deben ocultar durante las operaciones realizadas.
     *
     * @param viewsToHide the views to hide
     */
    protected void addViewsToHide(View... viewsToHide) {
        mViewsToHide.addAll(Arrays.asList(viewsToHide));
    }

    /**
     * Método que oculta las vistas cuando la aplicación está realizando una operación.
     */
    public void showProgress() {
        for (View v : mViewsToHide)
            if (v != null)
                v.setVisibility(View.GONE);

        if (mProgressBarLayout != null)
            mProgressBarLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Método que muestra las vistas cuando la aplicación ha realizado una operación.
     */
    public void hideProgress() {
        if (mProgressBarLayout != null)
            mProgressBarLayout.setVisibility(View.GONE);

        for (View v : mViewsToHide)
            if (v != null)
                v.setVisibility(View.VISIBLE);
    }

    private void wrapContextLanguage(Context context) {
        LanguagePresenter languagePresenter = new LanguagePresenterImpl(new SettingsRepositoryImpl(context));
        String language = languagePresenter.getLanguage();
        mContext = ContextWrapper.wrap(context, new Locale(language));
    }
}
