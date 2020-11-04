package com.robert.android.unioviscope.presentation.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.robert.android.unioviscope.R;
import com.robert.android.unioviscope.presentation.presenters.CertifyAttendancePresenter;
import com.robert.android.unioviscope.utils.LocaleUtil;

/**
 * Clase que crea un dialog con información sobre el estado del detector de rostros utilizado.
 *
 * @author Robert Ene
 */
public class FaceDetectorNotAvaibleDialog {

    /**
     * Método que crea el dialog.
     *
     * @param context   el contexto de la aplicación.
     * @param presenter el presenter asociado a la activity del dialog.
     * @return el dialog creado.
     */
    public static Dialog create(Context context, final CertifyAttendancePresenter presenter) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setMessage(LocaleUtil.getStringByLocale(context, R.string.msg_face_detector_not_avabile));

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, LocaleUtil.getStringByLocale(context,
                R.string.btn_retry_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                presenter.takePhoto();
            }
        });

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, LocaleUtil.getStringByLocale(context,
                R.string.btn_cancel_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
