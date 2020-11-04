package com.robert.android.unioviscope.presentation.ui.fragments.base;

import android.content.Context;
import android.support.v4.app.Fragment;
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
 * Clase base abstracta para todos los fragments que sirve como punto común para los accesos al contexto de la
 * aplicación y el manejo de las vistas antes y después de las operaciones realizadas.
 *
 * @author Robert Ene
 */
public abstract class AbstractFragment extends Fragment {

    private Context mContext;
    private List<View> mViewsToHide;
    private View mProgressBarLayout;

    @Override
    public void onAttach(Context context) {
        mViewsToHide = new ArrayList<>();
        wrapContextLanguage(context);
        super.onAttach(getContext());
    }

    /**
     * Método que devuelve el contexto de la aplicación.
     *
     * @return el contexto de la aplicación.
     */
    @Override
    public Context getContext() {
        return mContext;
    }

    /**
     * Método que establece el layout del spinner (cargando).
     *
     * @param progressBarLayout el layout del spinner.
     */
    public void setProgressBarLayout(View progressBarLayout) {
        mProgressBarLayout = progressBarLayout;
    }

    /**
     * Método que añade las vistas que se deben ocultar durante las operaciones realizadas.
     *
     * @param viewsToHide the views to hide
     */
    public void addViewsToHide(View... viewsToHide) {
        mViewsToHide.addAll(Arrays.asList(viewsToHide));
    }

    /**
     * Método que oculta las vistas cuando la aplicación está realizando una operación.
     */
    public void showProgress() {
        for (View v : mViewsToHide)
            if (v != null)
                v.setVisibility(View.GONE);

        if (mProgressBarLayout != null) mProgressBarLayout.setVisibility(View.VISIBLE);
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
