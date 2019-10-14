package com.example.scrumproyect.view.ui.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.example.scrumproyect.BuildConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author @briansalvattore on 16/01/2018.
 */

public class FacebookHelper {

    @SuppressLint("PackageManagerGetSignatures")
    public static void init(Context context) {
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        }
        catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        }
        catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
}