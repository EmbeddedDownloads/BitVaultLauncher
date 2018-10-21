package com.bitvault.launcher.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.bitvault.launcher.iHelper.AlertDialogClickListener;
import com.bitvault.launcher.iHelper.ChoiceDialogClickListener;


public class AppDialogUtils {
    @SuppressWarnings("unused")
    private static String TAG = AppDialogUtils.class.getSimpleName();
    Activity mActivity;

    /**
     * Alert dialog to show message to user
     *
     * @param mActivity
     * @param msg
     * @param positiveButtonText
     */
    public static void showAlertDialog(Activity mActivity, String msg,
                                       String positiveButtonText,
                                       final AlertDialogClickListener mAlertDialogClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mActivity);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton(positiveButtonText,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (mAlertDialogClickListener != null) {
                            mAlertDialogClickListener
                                    .onClickOfAlertDialogPositive();
                        }
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Multiple User choice dialog
     *
     * @param mActivity
     * @param msg
     * @param positiveButtonText
     * @param navgitaveButtonText
     */
    public static void showChoiceDialog(Activity mActivity, String msg,
                                        String positiveButtonText, String navgitaveButtonText,
                                        final ChoiceDialogClickListener mChoiceDialogClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mActivity);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton(positiveButtonText,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mChoiceDialogClickListener.onClickOfPositive();
                    }
                });
        alertDialogBuilder.setNegativeButton(navgitaveButtonText,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mChoiceDialogClickListener.onClickOfNegative();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
