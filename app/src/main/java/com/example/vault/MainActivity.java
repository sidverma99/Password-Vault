package com.example.vault;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private CancellationSignal mCancellationSignal;
    private Button authenticate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authenticate=(Button)findViewById(R.id.auth);
        authenticate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void authenticateUser(){
        BiometricPrompt biometricPrompt=new BiometricPrompt.Builder(this).setTitle("Biometric Authentication").setSubtitle("Authentication is required to continue").setDescription("This app uses biometric authentication to protect your data").setNegativeButton("Cancel", getMainExecutor(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notifyUser("Authentication Cancel");
            }
        }).build();
        biometricPrompt.authenticate(getCancellationSignal(),getMainExecutor(),getAuthenticationCallback());
    }
    private void notifyUser(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }
    private Boolean checkBiometricSupport(){
        KeyguardManager keyguardManager=(KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        PackageManager packageManager=this.getPackageManager();
        if(!keyguardManager.isKeyguardSecure()){
            notifyUser("Lock screen is not enabled in Settings");
            return false;
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC)!=packageManager.PERMISSION_GRANTED){
            notifyUser("Fingerprint authentication permission not enabled");
            return false;
        }
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) return true;

        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback(){
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                notifyUser("Authentication is successful");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        };
    }
    private CancellationSignal getCancellationSignal(){
        mCancellationSignal=new CancellationSignal();
        mCancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                notifyUser("Authentication cancelled");
            }
        });
        return mCancellationSignal;
    }
}