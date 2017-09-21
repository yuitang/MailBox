package com.example.yui.mailbox.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Utils {
    private static float density;
    private static int deviceWidth;

    private Utils() {
    }

    static int dipToPx(Context context, int dip) {
        if(density <= 0.0F) {
            density = context.getResources().getDisplayMetrics().density;
        }

        return (int)((float)dip * density + 0.5F);
    }

    public static int pxToDip(Context context, int px) {
        if(density <= 0.0F) {
            density = context.getResources().getDisplayMetrics().density;
        }

        return (int)((float)px / density + 0.5F);
    }

    public static int designToDevice(Context context, int designScreenWidth, int designPx) {
        if(deviceWidth == 0) {
            int[] scrSize = getScreenSize(context);
            deviceWidth = scrSize[0] < scrSize[1]?scrSize[0]:scrSize[1];
        }

        return (int)((float)designPx * (float)deviceWidth / (float)designScreenWidth + 0.5F);
    }

    public static int designToDevice(Context context, float designScreenDensity, int designPx) {
        if(density <= 0.0F) {
            density = context.getResources().getDisplayMetrics().density;
        }

        return (int)((float)designPx * density / designScreenDensity + 0.5F);
    }

    public static int[] getScreenSize(Context context) {
        WindowManager windowManager;
        try {
            windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        } catch (Throwable var6) {
            windowManager = null;
        }

        if(windowManager == null) {
            return new int[]{0, 0};
        } else {
            Display display = windowManager.getDefaultDisplay();
            if(VERSION.SDK_INT < 13) {
                DisplayMetrics t1 = new DisplayMetrics();
                display.getMetrics(t1);
                return new int[]{t1.widthPixels, t1.heightPixels};
            } else {
                try {
                    Point t = new Point();
                    Method method = display.getClass().getMethod("getRealSize", new Class[]{Point.class});
                    method.setAccessible(true);
                    method.invoke(display, new Object[]{t});
                    return new int[]{t.x, t.y};
                } catch (Throwable var5) {
                    return new int[]{0, 0};
                }
            }
        }
    }

    public static int getScreenWidth(Context context) {
        return getScreenSize(context)[0];
    }

    public static int getScreenHeight(Context context) {
        return getScreenSize(context)[1];
    }

    public static void setResourceProvider(Object rp) {
        try {
            Method t = rp.getClass().getMethod("getResId", new Class[]{Context.class, String.class, String.class});
            if(t != null) {
                rp = rp;
            }
        } catch (Throwable var2) {
        }

    }

    public static int getResId(Context context, String resType, String resName) {
        int resId = 0;
        if(context != null && !TextUtils.isEmpty(resType) && !TextUtils.isEmpty(resName)) {

            if(resId <= 0) {
                String pck1 = context.getPackageName();
                if(TextUtils.isEmpty(pck1)) {
                    return resId;
                }

                if(resId <= 0) {
                    resId = context.getResources().getIdentifier(resName, resType, pck1);
                    if(resId <= 0) {
                        resId = context.getResources().getIdentifier(resName.toLowerCase(), resType, pck1);
                    }
                }

                if(resId <= 0) {
                    System.err.println("failed to parse " + resType + " resource \"" + resName + "\"");
                }
            }

            return resId;
        } else {
            return resId;
        }
    }

    public static int getBitmapRes(Context context, String resName) {
        return getResId(context, "drawable", resName);
    }

    public static int getStringRes(Context context, String resName) {
        return getResId(context, "string", resName);
    }

    public static int getStringArrayRes(Context context, String resName) {
        return getResId(context, "array", resName);
    }

    public static int getLayoutRes(Context context, String resName) {
        return getResId(context, "layout", resName);
    }

    public static int getStyleRes(Context context, String resName) {
        return getResId(context, "style", resName);
    }

    public static int getIdRes(Context context, String resName) {
        return getResId(context, "id", resName);
    }

    public static int getColorRes(Context context, String resName) {
        return getResId(context, "color", resName);
    }

    public static int getRawRes(Context context, String resName) {
        return getResId(context, "raw", resName);
    }

    public static int getPluralsRes(Context context, String resName) {
        return getResId(context, "plurals", resName);
    }

    public static int getAnimRes(Context context, String resName) {
        return getResId(context, "anim", resName);
    }

    public static void deleteFilesInFolder(File folder) throws Throwable {
        if(folder != null && folder.exists()) {
            if(folder.isFile()) {
                folder.delete();
            } else {
                String[] names = folder.list();
                if(names != null && names.length > 0) {
                    String[] arr$ = names;
                    int len$ = names.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        String name = arr$[i$];
                        File f = new File(folder, name);
                        if(f.isDirectory()) {
                            deleteFilesInFolder(f);
                        } else {
                            f.delete();
                        }
                    }

                }
            }
        }
    }

    public static void deleteFileAndFolder(File folder) throws Throwable {
        if(folder != null && folder.exists()) {
            if(folder.isFile()) {
                folder.delete();
            } else {
                String[] names = folder.list();
                if(names != null && names.length > 0) {
                    String[] arr$ = names;
                    int len$ = names.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        String name = arr$[i$];
                        File f = new File(folder, name);
                        if(f.isDirectory()) {
                            deleteFileAndFolder(f);
                        } else {
                            f.delete();
                        }
                    }

                    folder.delete();
                } else {
                    folder.delete();
                }
            }
        }
    }

    public static String toWordText(String text, int lenInWord) {
        char[] cText = text.toCharArray();
        int count = lenInWord * 2;
        StringBuilder sb = new StringBuilder();
        char[] arr$ = cText;
        int len$ = cText.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            char ch = arr$[i$];
            count -= ch < 256?1:2;
            if(count < 0) {
                return sb.toString();
            }

            sb.append(ch);
        }

        return sb.toString();
    }

    public static int getTextLengthInWord(String text) {
        char[] cText = text == null?new char[0]:text.toCharArray();
        int count = 0;

        for(int i = 0; i < cText.length; ++i) {
            count += cText[i] < 256?1:2;
        }

        return count;
    }

    public static long strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate.getTime();
    }

    public static long dateStrToLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate.getTime();
    }

    public static Date longToDate(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.getTime();
    }

    public static String longToTime(long time, int level) {
        String format = "yyyy-MM-dd kk:mm:ss";
        switch(level) {
            case 1:
                format = "yyyy";
                break;
            case 2:
                format = "yyyy-MM";
            case 3:
            case 4:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            default:
                break;
            case 5:
                format = "yyyy-MM-dd";
                break;
            case 10:
                format = "yyyy-MM-dd kk";
                break;
            case 12:
                format = "yyyy-MM-dd kk:mm";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(Long.valueOf(time));
    }

    public static long dateToLong(String date) {
        try {
            Date t = new Date(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(t);
            return cal.getTimeInMillis();
        } catch (Throwable var3) {
            return 0L;
        }
    }

    public static int[] covertTimeInYears(long time) {
        long delta = System.currentTimeMillis() - time;
        if(delta <= 0L) {
            return new int[]{0, 0};
        } else {
            delta /= 1000L;
            if(delta < 60L) {
                return new int[]{(int)delta, 0};
            } else {
                delta /= 60L;
                if(delta < 60L) {
                    return new int[]{(int)delta, 1};
                } else {
                    delta /= 60L;
                    if(delta < 24L) {
                        return new int[]{(int)delta, 2};
                    } else {
                        delta /= 24L;
                        if(delta < 30L) {
                            return new int[]{(int)delta, 3};
                        } else {
                            delta /= 30L;
                            if(delta < 12L) {
                                return new int[]{(int)delta, 4};
                            } else {
                                delta /= 12L;
                                return new int[]{(int)delta, 5};
                            }
                        }
                    }
                }
            }
        }
    }

    public static Uri pathToContentUri(Context context, String imagePath) {
        Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data=? ", new String[]{imagePath}, (String)null);
        if(cursor != null && cursor.moveToFirst()) {
            int imageFile1 = cursor.getInt(cursor.getColumnIndex("_id"));
            Uri values1 = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(values1, "" + imageFile1);
        } else {
            File imageFile = new File(imagePath);
            if(imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put("_data", imagePath);
                Uri baseUri = Media.EXTERNAL_CONTENT_URI;
                return context.getContentResolver().insert(baseUri, values);
            } else {
                return null;
            }
        }
    }

    public static String contentUriToPath(Context context, Uri uri) {
        if(uri == null) {
            return null;
        } else if((new File(uri.getPath())).exists()) {
            return uri.getPath();
        } else {
            String path = null;

            try {
                Cursor t = null;
                if(VERSION.SDK_INT >= 19) {
                    Class DocumentsContract = Class.forName("android.provider.DocumentsContract");
                    Method isDocumentUri = DocumentsContract.getMethod("isDocumentUri", new Class[]{Context.class, Uri.class});
                    isDocumentUri.setAccessible(true);
                    if(Boolean.TRUE.equals(isDocumentUri.invoke((Object)null, new Object[]{context, uri}))) {
                        Method getDocumentId = DocumentsContract.getMethod("getDocumentId", new Class[]{Uri.class});
                        getDocumentId.setAccessible(true);
                        String wholeID = String.valueOf(getDocumentId.invoke((Object)null, new Object[]{uri}));
                        String id = wholeID.split(":")[1];
                        String[] column = new String[]{"_data"};
                        String sel = "_id=?";
                        String[] args = new String[]{id};
                        t = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, column, sel, args, (String)null);
                    }
                }

                if(t == null) {
                    t = context.getContentResolver().query(uri, (String[])null, (String)null, (String[])null, (String)null);
                }

                if(t != null) {
                    if(t.moveToFirst()) {
                        path = t.getString(t.getColumnIndex("_data"));
                    }

                    t.close();
                }
            } catch (Throwable var12) {
                path = null;
            }

            return path;
        }
    }

    public static Bundle urlToBundle(String url) {
        int index = url.indexOf("://");
        if(index >= 0) {
            url = "http://" + url.substring(index + 1);
        } else {
            url = "http://" + url;
        }

        try {
            URL e = new URL(url);
            Bundle b = decodeUrl(e.getQuery());
            b.putAll(decodeUrl(e.getRef()));
            return b;
        } catch (Throwable var4) {
            return new Bundle();
        }
    }

    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if(s != null) {
            String[] array = s.split("&");
            String[] arr$ = array;
            int len$ = array.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String parameter = arr$[i$];
                String[] v = parameter.split("=");
                if(v.length >= 2 && v[1] != null) {
                    params.putString(URLDecoder.decode(v[0]), URLDecoder.decode(v[1]));
                } else {
                    params.putString(URLDecoder.decode(v[0]), "");
                }
            }
        }

        return params;
    }

    public static int parseInt(String string) throws Throwable {
        return parseInt(string, 10);
    }

    public static int parseInt(String string, int radix) throws Throwable {
        if(radix >= 2 && radix <= 36) {
            if(string == null) {
                throw invalidInt(string);
            } else {
                int length = string.length();
                int i = 0;
                if(length == 0) {
                    throw invalidInt(string);
                } else {
                    boolean negative = string.charAt(i) == 45;
                    if(negative) {
                        ++i;
                        if(i == length) {
                            throw invalidInt(string);
                        }
                    }

                    return parseInt(string, i, radix, negative);
                }
            }
        } else {
            throw new Throwable("Invalid radix: " + radix);
        }
    }

    private static int parseInt(String string, int offset, int radix, boolean negative) throws Throwable {
        int max = -2147483648 / radix;
        int result = 0;

        int next;
        for(int length = string.length(); offset < length; result = next) {
            int digit = digit(string.charAt(offset++), radix);
            if(digit == -1) {
                throw invalidInt(string);
            }

            if(max > result) {
                throw invalidInt(string);
            }

            next = result * radix - digit;
            if(next > result) {
                throw invalidInt(string);
            }
        }

        if(!negative) {
            result = -result;
            if(result < 0) {
                throw invalidInt(string);
            }
        }

        return result;
    }

    private static int digit(int codePoint, int radix) {
        if(radix >= 2 && radix <= 36) {
            int result = -1;
            if(48 <= codePoint && codePoint <= 57) {
                result = codePoint - 48;
            } else if(97 <= codePoint && codePoint <= 122) {
                result = 10 + (codePoint - 97);
            } else if(65 <= codePoint && codePoint <= 90) {
                result = 10 + (codePoint - 65);
            }

            return result < radix?result:-1;
        } else {
            return -1;
        }
    }

    private static Throwable invalidInt(String s) throws Throwable {
        throw new Throwable("Invalid int: \"" + s + "\"");
    }

    public static long parseLong(String string) throws Throwable {
        return parseLong(string, 10);
    }

    public static long parseLong(String string, int radix) throws Throwable {
        if(radix >= 2 && radix <= 36) {
            if(string == null) {
                throw new Throwable("Invalid long: \"" + string + "\"");
            } else {
                int length = string.length();
                int i = 0;
                if(length == 0) {
                    throw new Throwable("Invalid long: \"" + string + "\"");
                } else {
                    boolean negative = string.charAt(i) == 45;
                    if(negative) {
                        ++i;
                        if(i == length) {
                            throw new Throwable("Invalid long: \"" + string + "\"");
                        }
                    }

                    return parseLong(string, i, radix, negative);
                }
            }
        } else {
            throw new Throwable("Invalid radix: " + radix);
        }
    }

    private static long parseLong(String string, int offset, int radix, boolean negative) throws Throwable {
        long max = -9223372036854775808L / (long)radix;
        long result = 0L;

        long next;
        for(long length = (long)string.length(); (long)offset < length; result = next) {
            int digit = digit(string.charAt(offset++), radix);
            if(digit == -1) {
                throw new Throwable("Invalid long: \"" + string + "\"");
            }

            if(max > result) {
                throw new Throwable("Invalid long: \"" + string + "\"");
            }

            next = result * (long)radix - (long)digit;
            if(next > result) {
                throw new Throwable("Invalid long: \"" + string + "\"");
            }
        }

        if(!negative) {
            result = -result;
            if(result < 0L) {
                throw new Throwable("Invalid long: \"" + string + "\"");
            }
        }

        return result;
    }

    public static String toString(Object obj) {
        return obj == null?"":obj.toString();
    }

    public static boolean copyFile(String fromFilePath, String toFilePath) {
        if(!TextUtils.isEmpty(fromFilePath) && !TextUtils.isEmpty(toFilePath)) {
            if(!(new File(fromFilePath)).exists()) {
                return false;
            } else {
                try {
                    FileInputStream e = new FileInputStream(fromFilePath);
                    FileOutputStream fosto = new FileOutputStream(toFilePath);
                    copyFile(e, fosto);
                    return true;
                } catch (Throwable var4) {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public static void copyFile(FileInputStream src, FileOutputStream dst) throws Throwable {
        byte[] buf = new byte[65536];

        for(int len = src.read(buf); len > 0; len = src.read(buf)) {
            dst.write(buf, 0, len);
        }

        src.close();
        dst.close();
    }

    public static long getFileSize(String path) throws Throwable {
        if(TextUtils.isEmpty(path)) {
            return 0L;
        } else {
            File file = new File(path);
            return getFileSize(file);
        }
    }

    public static long getFileSize(File file) throws Throwable {
        if(!file.exists()) {
            return 0L;
        } else if(!file.isDirectory()) {
            return file.length();
        } else {
            String[] names = file.list();
            int size = 0;

            for(int i = 0; i < names.length; ++i) {
                File f = new File(file, names[i]);
                size = (int)((long)size + getFileSize(f));
            }

            return (long)size;
        }
    }

    public static boolean saveObjectToFile(String filePath, Object object) {
        if(!TextUtils.isEmpty(filePath)) {
            File cacheFile = null;

            try {
                cacheFile = new File(filePath);
                if(cacheFile.exists()) {
                    cacheFile.delete();
                }

                if(!cacheFile.getParentFile().exists()) {
                    cacheFile.getParentFile().mkdirs();
                }

                cacheFile.createNewFile();
            } catch (Throwable var6) {
                var6.printStackTrace();
                cacheFile = null;
            }

            if(cacheFile != null) {
                try {
                    FileOutputStream t = new FileOutputStream(cacheFile);
                    GZIPOutputStream gzos = new GZIPOutputStream(t);
                    ObjectOutputStream oos = new ObjectOutputStream(gzos);
                    oos.writeObject(object);
                    oos.flush();
                    oos.close();
                    return true;
                } catch (Throwable var7) {
                    var7.printStackTrace();
                }
            }
        }

        return false;
    }

    public static Object readObjectFromFile(String filePath) {
        if(!TextUtils.isEmpty(filePath)) {
            File cacheFile = null;

            try {
                cacheFile = new File(filePath);
                if(!cacheFile.exists()) {
                    cacheFile = null;
                }
            } catch (Throwable var6) {
                var6.printStackTrace();
                cacheFile = null;
            }

            if(cacheFile != null) {
                try {
                    FileInputStream t = new FileInputStream(cacheFile);
                    GZIPInputStream gzis = new GZIPInputStream(t);
                    ObjectInputStream ois = new ObjectInputStream(gzis);
                    Object object = ois.readObject();
                    ois.close();
                    return object;
                } catch (Throwable var7) {
                    var7.printStackTrace();
                }
            }
        }
        return null;
    }
}
