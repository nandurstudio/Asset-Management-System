package com.ndu.assetmanagementsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AlertDialog;

public class NandurLibs {
    /*https://stackoverflow.com/questions/23408756/create-a-general-class-for-custom-dialog-in-java-android*/
    /*https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android*/
    /*https://stackoverflow.com/questions/42926918/android-dialog-button-onclicklistener-how-is-the-id-value-determined*/
    public static void nduDialog(Context context,
                                 String title,
                                 String msg,
                                 boolean cancelable,
                                 Drawable icon,
                                 String positiveText,
                                 String negativeText,
                                 DialogInterface.OnClickListener ocListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(cancelable);
        builder.setIcon(icon);

        if (positiveText != null) builder.setPositiveButton(
                positiveText, ocListener);

        builder.setNegativeButton(
                negativeText,
                (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();
        alert.show();
    }

}
