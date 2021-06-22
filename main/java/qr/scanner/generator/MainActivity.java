package qr.scanner.generator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_TIMER = 5000;
    String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};
    private static final int PERMISSION_REQUEST_CODE = 200;

    // 3 Seconds = 3000 Milliseconds
    //5 Seconds = 5000 Milliseconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
        requestPermission();

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                SharedPreferences onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);

//                                if (isFirstTime) {
//                                    SharedPreferences.Editor editor = onBoardingScreen.edit();
//                                    editor.putBoolean("firstTime", false);
//                                    editor.apply();
//                                    Intent onBoardingIntent = new Intent(MainActivity.this, PaperOnboardingActivity.class);
//                                    startActivity(onBoardingIntent);
//                                } else {
                                    Intent intent = new Intent(MainActivity.this, QRCodeScanner.class);
                                    startActivity(intent);
//                                }
//                                finish();
                            }
                        }, SPLASH_TIMER);
                    }
                }


                break;
        }
    }
}