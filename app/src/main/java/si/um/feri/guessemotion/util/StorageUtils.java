package si.um.feri.guessemotion.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by:
 *
 * Diana Sellárová,
 * Marek Urban,
 * Stanislav Blaško
 *
 * as assignment for Mobile communication services subject on Erasmus+ study.
 */

public class StorageUtils {

    public static void saveStringPref(String key, String value, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
        Log.d("persistance", "saved '" + key + "': " + value);
    }

    public static String loadStringPref(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String ret = prefs.getString(key, "");
        if (ret.equalsIgnoreCase("") || ret == null)
            return "";
        else
            return ret;
    }

}
