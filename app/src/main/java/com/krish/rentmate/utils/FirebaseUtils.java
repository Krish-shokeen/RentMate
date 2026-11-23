package com.krish.rentmate.utils;

import android.content.Context;
import com.google.firebase.FirebaseApp;

public class FirebaseUtils {

    public static boolean isFirebaseConfigured(Context context) {
        try {
            if (context == null) return false;
            return !FirebaseApp.getApps(context).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}