package com.ndu.assetmanagementsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.text.MessageFormat;

public class NandurLibs {

    private static final String SCHEME = "package";
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    private static final String APP_PKG_NAME_22 = "pkg";
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
    public static String versName;
    public static int versCode;

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

// --Commented out by Inspection START (14-Jan-21 15:25):
//    /**
//     * Get external sd card path using reflection
//     *
//     * @param mContext
//     * @param is_removable is external storage removable
//     * @return
//     */
//    @SuppressWarnings("JavaDoc")
//    private static String getExternalStoragePath(Context mContext, boolean is_removable) {
//
//        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
//        Class<?> storageVolumeClazz;
//        try {
//            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
//            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
//            @SuppressWarnings("JavaReflectionMemberAccess") Method getPath = storageVolumeClazz.getMethod("getPath");
//            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
//            Object result = getVolumeList.invoke(mStorageManager);
//            final int length = Array.getLength(Objects.requireNonNull(result));
//            for (int i = 0; i < length; i++) {
//                Object storageVolumeElement = Array.get(result, i);
//                String path = (String) getPath.invoke(storageVolumeElement);
//                //noinspection ConstantConditions
//                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
//                if (is_removable == removable) {
//                    return path;
//                }
//            }
//        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
// --Commented out by Inspection STOP (14-Jan-21 15:25)

    static void showInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) { // above 2.3
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(SCHEME, packageName, null);
            intent.setData(uri);
        } else { // below 2.3
            final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
                    : APP_PKG_NAME_21);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName(APP_DETAILS_PACKAGE_NAME,
                    APP_DETAILS_CLASS_NAME);
            intent.putExtra(appPkgName, packageName);
        }
        context.startActivity(intent);

    }

    public static void dialogInfoVersionName(MainActivity mainActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle(R.string.version_title);
        builder.setMessage("Version Name: " + versName + "\n" + "Version Code: " + versCode);
        builder.setIcon(R.drawable.ic_info_outline_black_24dp);
        AlertDialog diag = builder.create();
        //Display the message!
        diag.show();
    }

    public static void shareApp(Context context,
                                String share_via,
                                String app_name,
                                String version_title,
                                String version_name,
                                String build_title,
                                int version_code) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
//        String bitly = getResources().getString(R.string.bitly_share)+getResources().getString(R.string.bitly_dynamic);
//        String direct = getResources().getString(R.string.bitly_share)+getResources().getString(R.string.bitly_direct);
//        String hashtag = getResources().getString(R.string.hashtag);
//        String ofcWeb = getResources().getString(R.string.ofc_website);
//        String download = getResources().getString(R.string.direct_download);
//        String shareBody = tvResult.getText().toString()+"\n\n"+ofcWeb+bitly+"\n"+download+direct+"\n"+hashtag.trim();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, app_name + " " + version_title + " " + version_name + " " + build_title + " " + version_code);
//        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, share_via));
    }

    /**
     * Email client intent to send support mail
     * Appends the necessary device information to email body
     * useful when providing support
     */
    public static void sendFeedback(Context context,
                                    String pattern_mail,
                                    String feed_body,
                                    String app_name,
                                    String email_client,
                                    String target_mail,
                                    String version_name,
                                    int version_code) {
        /*
        Put this in string.xml
        \n\n-----------------------------\nPlease do not delete this section\nDevice OS: Android\nDevice OS version: {0}\nApp Version: {1} build {5}\nDevice Brand: {2}\nDevice Model: {3}\nDevice Manufacturer: {4}
        */
        String body = MessageFormat.format(pattern_mail, Build.VERSION.RELEASE, version_name, Build.BRAND, Build.MODEL, Build.MANUFACTURER, version_code);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{target_mail});
        intent.putExtra(Intent.EXTRA_SUBJECT, MessageFormat.format("{0} {1} v{2} b{3}", feed_body, app_name, version_name, version_code));
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, email_client));
    }

    public static void toaster(Context context, String msg, int duration) {
        Toast
                .makeText(context, msg, duration)
                .show();
    }
}
