package com.example.savaari.auth.biometric;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

public class BiometricUtils {

    public static boolean isBiometricAuthAvailable(Context context) {
        return isFingerprintAvailable(context) && isBiometricPromptEnabled() && isHardwareSupported(context)
                && isPermissionGranted(context) && isSdkVersionSupported();
    }


    public static boolean isBiometricPromptEnabled() {
        Log.d("BIOMETRICS", "isBiometricPromptEnabled: " + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P));
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P);
    }


    /*
     * Condition I: Check if the android version in device is greater than
     * Marshmallow, since fingerprint authentication is only supported
     * from Android 6.0.
     * Note: If your project's minSdkversion is 23 or higher,
     * then you won't need to perform this check.
     *
     * */
    public static boolean isSdkVersionSupported() {
        Log.d("BIOMETRICS", "isSdkVersionSupported: " + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M));
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }



    /*
     * Condition II: Check if the device has fingerprint sensors.
     * Note: If you marked android.hardware.fingerprint as something that
     * your app requires (android:required="true"), then you don't need
     * to perform this check.
     *
     * */
    public static boolean isHardwareSupported(Context context) {
        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(context);
        Log.d("BIOMETRICS", "isHardwareSupported: " + fingerprintManager.isHardwareDetected());
        return fingerprintManager.isHardwareDetected();
    }



    /*
     * Condition III: Fingerprint authentication can be matched with a
     * registered fingerprint of the user. So we need to perform this check
     * in order to enable fingerprint authentication
     *
     * */
    public static boolean isFingerprintAvailable(Context context) {
        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(context);
        Log.d("BIOMETRICS", "isFingerprintAvailable: " + fingerprintManager.hasEnrolledFingerprints());
        return fingerprintManager.hasEnrolledFingerprints();
    }



    /*
     * Condition IV: Check if the permission has been added to
     * the app. This permission will be granted as soon as the user
     * installs the app on their device.
     *
     * */
    public static boolean isPermissionGranted(Context context) {
        Log.d("BIOMETRICS", "isPermissionGranted: " + (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) ==
                PackageManager.PERMISSION_GRANTED));
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) ==
                PackageManager.PERMISSION_GRANTED;
    }
}
