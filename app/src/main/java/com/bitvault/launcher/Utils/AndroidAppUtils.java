package com.bitvault.launcher.Utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitvault.launcher.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Utils of this application use static methods of application.
 *
 * @author Shruti
 */
public class AndroidAppUtils {
    private static final String PATTERN_IP_ADDRESS = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    @SuppressWarnings("unused")
    private static final String MAC_PATTERN = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
    private static final String EMAIL_ID_PATTERN = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
    // ^ # Start of the line
    // [a-z0-9_-] # Match characters and symbols in the list, a-z, 0-9,
    // underscore, hyphen
    // {3,15} # Length at least 3 characters and maximum length of 15
    // $ # End of the line
    // ^[a-z0-9_-]{3,15}$
    private static final String USER_NAME_PATTERN = "^[a-z0-9_-]{2,20}$";
    /**
     * Object instance of progress bar
     */
    private static boolean ISDEBUGGING = true;
    private static ProgressDialog mProgressDialog;
    private static String TAG = AndroidAppUtils.class.getSimpleName();


    /*List of packages which we need to open from luncher directly*/
    public static String PRAYER_ROOM_APP_PACKAGE = "com.biblezon.community", PRAYER_ROOM_APP_NAME = "Prayer Room";
    public static String NOTES_APP_PACKAGE = "com.bz.parallaxnotes", NOTES_APP_NAME = "Notes";
    public static String BOOK_APP_PACKAGE = "com.biblezon.shelves", BOOK_APP_NAME = "Book";
    public static String MESSAGE_APP_PACKAGE = "com.messages.messages", MESSAGE_APP_NAME = "Message";
    public static String ADULT_DAILY_ACTIVITIES_APP_PACKAGE = "com.biblezon.DailyActivitiesAdult", ADULT_DAILY_ACTIVITIES_APP_NAME = "Adult Bible";
    public static String KIDS_DAILY_ACTIVITIES_APP_PACKAGE = "com.biblezon.kidsdailyactivities", KIDS_DAILY_ACTIVITIES_APP_NAME = "Kids Bible";

    public static String NAVIGATOR_APP_PACKAGE = "com.biblezon.navigator", NAVIGATOR_APP_NAME = "Navigator";
    public static String STORE_APP_PACKAGE = "com.biblezon.appstore", STORE_APP_NAME = "BibleZon Store";
    public static String LITURGICAL_CALENDAR_APP_PACKAGE = "com.biblezon.saintsandcalendar", LITURGICAL_CALENDAR_APP_NAME = "Liturgical Calendar";
    public static String HELP_APP_PACKAGE = "com.biblezon.support", HELP_APP_NAME = "Help";
    public static String VIDEO_APP_PACKAGE = "com.kids.youtubeplay", VIDEO_APP_NAME = "Videos";
    public static String SONGS_APP_PACKAGE = "com.biblezon.songs", SONGS_APP_NAME = "Songs";
    public static String GAME_APP_PACKAGE = "com.app.biblezon", GAME_APP_NAME = "Games";

    /**
     * Show debug Message into logcat
     *
     * @param TAG
     * @param msg
     */
    public static void showLog(String TAG, String msg) {
        if (ISDEBUGGING) {
            try {
                Log.d(TAG, msg);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }

    }

    /**
     * Show debug Error Message into logcat
     *
     * @param TAG
     * @param msg
     */
    public static void showErrorLog(String TAG, String msg) {
        if (ISDEBUGGING) {
            Log.e(TAG, msg);
        }

    }

    /**
     * Show debug Verbose into logcat
     *
     * @param TAG
     * @param msg
     */
    public static void showVerboseLog(String TAG, String msg) {
        if (ISDEBUGGING) {
            Log.v(TAG, msg);
        }

    }

    /**
     * Show debug Info into logcat
     *
     * @param TAG
     * @param msg
     */
    public static void showInfoLog(String TAG, String msg) {
        if (ISDEBUGGING) {
            Log.i(TAG, msg);
        }

    }

    /**
     * Show debug Warning into logcat
     *
     * @param TAG
     * @param msg
     */
    public static void showWarningLog(String TAG, String msg) {
        if (ISDEBUGGING) {
            Log.w(TAG, msg);
        }

    }

    /**
     * Show Toast
     *
     * @param mActivity
     * @param msg
     */
    public static void showToast(final Activity mActivity, final String msg) {
        try {
            if (mActivity != null && msg != null && msg.length() > 0) {
                mActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Showing progress dialog
     *
     * @param msg
     */
    public static void showProgressDialog(final Activity mActivity,
                                          final String msg, final boolean isCancelable) {
        try {
            if (mActivity != null && mProgressDialog != null
                    && mProgressDialog.isShowing()) {
                try {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            mProgressDialog = null;
            if (mProgressDialog == null && mActivity != null) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog = new ProgressDialog(mActivity);
                        mProgressDialog.setMessage(msg);
                        mProgressDialog.setCancelable(isCancelable);
                    }
                });

            }
            if (mActivity != null && mProgressDialog != null
                    && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide progress dialog
     */
    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
                mProgressDialog = null;

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        } else {
            showErrorLog(TAG, "mProgressDialog is null");
        }
    }

    /**
     * Check device have internet connection or not
     *
     * @param activity
     * @return
     */
    public static boolean isOnline(Activity activity) {
        {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = (ConnectivityManager) activity
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                    if (ni.isConnected()) {
                        haveConnectedWifi = true;
                        showInfoLog(TAG, "WIFI CONNECTION : AVAILABLE");
                    } else {
                        showInfoLog(TAG, "WIFI CONNECTION : NOT AVAILABLE");
                    }
                }
                if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                    if (ni.isConnected()) {
                        haveConnectedMobile = true;
                        showInfoLog(TAG,
                                "MOBILE INTERNET CONNECTION : AVAILABLE");
                    } else {
                        showInfoLog(TAG,
                                "MOBILE INTERNET CONNECTION : NOT AVAILABLE");
                    }
                }
            }
            return haveConnectedWifi || haveConnectedMobile;
        }

    }

    /**
     * Down Keyboard
     *
     * @param mActivity
     */
    public static void keyboardDown(Activity mActivity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) mActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(mActivity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    /**
     * Get Data & time from timestamp
     *
     * @param timeStamp
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String convertTimeStampToDate(long timeStamp) {

        try {
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date netDate = (new Date(timeStamp * 1000));
            return sdf.format(netDate);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";

        }
    }

    /**
     * Converts string to MD5 format.
     *
     * @param string_expression : String to be converted.
     * @return
     */

    public static String convertStringToMD5(String string_expression) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(string_expression.getBytes(), 0,
                    string_expression.length());
            String hash = new BigInteger(1, digest.digest()).toString(16);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Convert Date into DD MMM formate
     *
     * @param indate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String convertDateFormteToDDMMM(String indate) {
        String dateResult = null;
        try {
            SimpleDateFormat sdf = null;
            if (indate.contains("/")) {
                sdf = new SimpleDateFormat("dd/MM/yyyy");
            } else if (indate.contains("-")) {
                sdf = new SimpleDateFormat("dd-MM-yyyy");
            }

            Date date = sdf.parse(indate);
            sdf = new SimpleDateFormat("dd MMM");
            dateResult = sdf.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return dateResult;
    }

    /**
     * Convert Time to AM/PM
     *
     * @param mtime
     * @return
     */
    @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
    public static String convertTimeFormteToAMPM(String mtime) {
        String resultTime = "00:00";
        try {
            DateFormat f1 = new SimpleDateFormat("HH:mm:ss"); // HH for hour of
            // the
            Date d = f1.parse(mtime);
            DateFormat f2 = new SimpleDateFormat("hh:mma");
            resultTime = f2.format(d).toLowerCase(); // "12:18am"
            return resultTime;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultTime;
    }

    /**
     * Make string perfect remove space from start and end
     *
     * @param Str
     * @return
     */
    public static String trimStringFromStartAndEnd(String Str) {
        Str = Str.replaceAll("^\\s+|\\s+$", "");
        return Str;

    }

    /**
     * Validate IP address
     *
     * @param ip
     * @return
     */
    public static boolean isIPAddressValidate(final String ip) {
        Pattern pattern = Pattern.compile(PATTERN_IP_ADDRESS);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * Validate password
     *
     * @return
     */
    public static boolean isPasswordValid(final String password) {
        String password_validate = password;
        if (password_validate.length() > 5) {
            return true;
        }
        return false;
    }

    /**
     * Validate mobile number
     *
     * @return
     */
    public static boolean isMobileValid(final String mobile) {
        String mobile_number_validate = mobile;
        if (mobile_number_validate.length() == 10) {
            return true;
        }
        return false;
    }

    /**
     * Validate MAC Address
     *
     * @param mac
     * @return
     */
    public static boolean isMACAddressValidate(final String mac) {
        Pattern pattern = Pattern.compile(PATTERN_IP_ADDRESS);
        Matcher matcher = pattern.matcher(mac);
        return matcher.matches();
    }

    /**
     * Validate Email Id of user
     *
     * @param emailID
     * @return
     */
    public static boolean isEmailIDValidate(String emailID) {
        Pattern pat = Pattern.compile(EMAIL_ID_PATTERN,
                Pattern.CASE_INSENSITIVE);
        Matcher match = pat.matcher(emailID);
        if (match.matches()) {
            return isEmailValidForDot(emailID);
        }
        return match.matches();
    }

    /*******************************************************************************
     * Function Name - isEmailValidForCom
     * <p>
     * Description - Check if email is not having .com.com more than one time.
     *
     * @param emailId email id on which validations is to be checked
     * @return true if .com is present only one time, otherwise returns false.
     ******************************************************************************/
    public static boolean isEmailValidForDot(String emailId) {
        boolean emailValid = false;
        String findStr = ".";
        int lastIndex = emailId.lastIndexOf("@");
        int count = 0;

        while (lastIndex != -1) {

            lastIndex = emailId.indexOf(findStr, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        if (count == 1) {
            emailValid = true;
        } else {
            emailValid = false;
        }
        return emailValid;
    }

    /**
     * Validate USER Name
     *
     * @param name
     * @return
     */
    public static boolean isUserNameValidate(String name) {
        Pattern pat = Pattern.compile(USER_NAME_PATTERN,
                Pattern.CASE_INSENSITIVE);
        Matcher match = pat.matcher(name);
        return match.matches();
    }

    /**
     * Check URI is present or not
     *
     * @param mContext
     * @param mUri
     * @return
     */
    public static Uri checkUriExists(Context mContext, Uri mUri) {
        Drawable d = null;
        if (mUri != null) {
            if ("content".equals(mUri.getScheme())) {
                try {
                    d = Drawable.createFromStream(mContext.getContentResolver()
                            .openInputStream(mUri), null);
                } catch (Exception e) {
                    showWarningLog(TAG,
                            "checkUriExists -> Unable to open content: " + mUri
                                    + " Error is -> " + e);
                    mUri = null;
                }
            } else {
                d = Drawable.createFromPath(mUri.toString());
            }

            if (d == null) {
                // Invalid uri
                mUri = null;

            }
        }

        return mUri;
    }

    /**
     * Check both Array list is equal or not
     *
     * @param one
     * @param two
     * @return
     */
    public static boolean isEqualLists(ArrayList<String> one,
                                       ArrayList<String> two) {
        if (one == null && two == null) {
            return true;
        }

        if ((one == null && two != null) || one != null && two == null
                || one.size() != two.size()) {
            return false;
        }

        // to avoid messing the order of the lists we will use a copy
        // as noted in comments by A. R. S.
        one = new ArrayList<String>(one);
        two = new ArrayList<String>(two);

        Collections.sort(one);
        Collections.sort(two);
        return one.equals(two);
    }

    /**
     * to convert string to hexadecimal
     *
     * @param row
     * @return
     */
    public static String convertInHex(String row) {
        Float abc;
        int pqr;
        String final_row = "";
        // TODO Auto-generated method stub
        if (row != "" && row != null) {
            abc = Float.parseFloat(row);
            pqr = (int) Math.round(abc);
            final_row = Integer.toHexString(pqr);
        }
        return final_row;

    }

    /**
     * to get id of relative layout by passing string
     *
     * @param resourceName
     * @param c
     * @return
     */
    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }

    /**
     * capitalized the first character of the string passed to this method and
     * return the new string
     *
     * @param req_text
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String capitalizeFirstCharacter(String req_text) {
        String textIs = "";
        if (req_text != null && req_text.length() > 0) {
            textIs = req_text.substring(0, 1).toUpperCase()
                    + req_text.substring(1);
        }
        return textIs;

    }

    /**
     * Get Cropped Image -> Crop bitmap
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        // Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        // return _bmp;
        return output;
    }

    /**
     * Get Resized Bitmap
     *
     * @param bm
     * @param newHeight
     * @param newWidth
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, true);
        return resizedBitmap;
    }

    /**
     * Convert string duration into hours mins and secs format.
     *
     * @param seconds second fetched from API call
     * @return hour, mins, secs formatted string
     */
    public static String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        String result = " " + twoDigitString(hours, "h")
                + twoDigitString(minutes, "m") + twoDigitString(seconds, "s");
        return result;
    }

    /**
     * converts string value in two digit format like 7 secs to 07 secs
     *
     * @param number
     * @param type   hour, min , sec
     * @return
     */
    private static String twoDigitString(int number, String type) {
        String two_digits = String.valueOf(number);
        if (number == 0) {
            two_digits = "";
            return two_digits;
        }

        if (number / 10 == 0) {
            two_digits = "0" + number;
        }

        if (type != null) {
            if (type.equalsIgnoreCase("h")) {
                two_digits = two_digits + "hours, ";
            } else if (type.equalsIgnoreCase("m")) {
                two_digits = two_digits + "mins, ";
            } else {
                two_digits = two_digits + "secs";
            }
        }
        return two_digits;
    }


    /**
     * Saved Image Path
     *
     * @param finalBitmap
     * @param imageName
     */
    public static void saveBitmapToSDCard(Bitmap finalBitmap,
                                          String imageName) {
        new SaveImageToStorageTask(finalBitmap, imageName).execute();

    }


    /**
     * Show Profile Image Task
     */
    public static class SaveImageToStorageTask extends AsyncTask<Void, Void, Void> {
        Bitmap finalBitmap = null;
        String imageName = "";

        public SaveImageToStorageTask(Bitmap finalBitmap,
                                      String imageName) {
            this.finalBitmap = finalBitmap;
            this.imageName = imageName;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/" + "Download");
                myDir.mkdirs();
                String fname = imageName + ".png";
                File file = new File(myDir, fname);
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }

    /**
     * Request Focus to edit text
     *
     * @param mActivity
     * @param view
     */
    public static void requestFocus(Activity mActivity, View view) {
        if (view.requestFocus()) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static double checkDimension(Context context) {
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        // since SDK_INT = 1;
        int mWidthPixels = displayMetrics.widthPixels;
        int mHeightPixels = displayMetrics.heightPixels;

        // includes window decorations (statusbar bar/menu bar)
        try {
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
            mWidthPixels = realSize.x;
            mHeightPixels = realSize.y;
        } catch (Exception ignored) {
        }

        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(mWidthPixels / dm.xdpi, 2);
        double y = Math.pow(mHeightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Log.d("debug", "Screen inches : " + screenInches);
        return screenInches;
    }

    /**
     * Load Assest Data
     *
     * @param inFile
     * @return
     */
    public static String LoadData(String inFile, Activity mActivity) {
        String tContents = "";
        try {
            InputStream stream = mActivity.getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;

    }

    /**
     * Craete HTML Page
     *
     * @param title
     * @param description
     * @return
     */
    public static String createHTMLContent(String title, String description) {

        String data = String.valueOf(Html
                .fromHtml("<![CDATA[<body style=\"text-align:justify;color:#222222; \">"
                        + (description)
                        /*+ (description.replaceAll("\r\n", "<br/>"))*/
                        + "</body>]]>"));
        return description;
    }

    /**
     * Open App
     */
    public static void openAppNow(String pckOfApp, String appName, Activity mActivity) {
        try {
            Intent launchIntent = mActivity.getPackageManager().getLaunchIntentForPackage(pckOfApp);
            if (launchIntent != null)
                mActivity.startActivity(launchIntent);//null pointer check in case package name was not found
            else
                AppDialogUtils.showAlertDialog(mActivity, appName + " " + mActivity.getString(R.string.app_not_found),
                        mActivity.getString(R.string.ok), null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Check App is installed or not
     *
     * @param mActivity
     * @param uri
     * @return
     */
    public static boolean appInstalledOrNot(Activity mActivity, String uri) {
        PackageManager pm = mActivity.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    /**
     * This will set the status bar color as of background color of the layout
     **/
    public static void statusBarColor(Activity mActivity, int statusColor) {
        // black status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mActivity.getResources().getColor(statusColor));
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(mActivity, true);
        }

        SystemBarTintManager mTintManager = new SystemBarTintManager(mActivity);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setTintColor(mActivity.getResources().getColor(statusColor));
    }

    @TargetApi(19)
    private static void setTranslucentStatus(Activity mActivity, boolean on) {
        Window win = mActivity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    /**
     * Update Views  GUI Theme
     */
    public static void updateGUIViewsTheme(Button mButton, TextView mTextView, RelativeLayout mRelativeLayout) {
        if (mButton != null)
            mButton.setBackgroundResource(R.drawable.button_selector_white);
        if (mTextView != null)
            mTextView.setBackgroundResource(R.drawable.button_selector_white);
        if (mRelativeLayout != null)
            mRelativeLayout.setBackgroundResource(R.drawable.button_selector_white);
    }
}
