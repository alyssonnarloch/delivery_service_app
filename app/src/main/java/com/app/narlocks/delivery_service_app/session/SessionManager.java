package com.app.narlocks.delivery_service_app.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.app.narlocks.delivery_service_app.activity.LoginActivity;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AndroidSessionPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PROFILE_IMAGE = "profile_image";
    public static final String KEY_ID = "id";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(int id, String name, String email, String profileImage) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PROFILE_IMAGE, profileImage);

        editor.commit();
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);
        }
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public int getUserId() {
        if (this.isLoggedIn()) {
            return pref.getInt(KEY_ID, 0);
        }
        return 0;
    }

    public String getUserName() {
        if (this.isLoggedIn()) {
            return pref.getString(KEY_NAME, "");
        }
        return "";
    }

    public String getUserEmail() {
        if (this.isLoggedIn()) {
            return pref.getString(KEY_EMAIL, "");
        }
        return "";
    }

    public String getUserProfileImage() {
        if (this.isLoggedIn()) {
            return pref.getString(KEY_PROFILE_IMAGE, "");
        }
        return "";
    }

}
