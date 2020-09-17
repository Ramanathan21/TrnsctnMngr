package com.wordpress.echoesofram.trnsctnmngr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements MessageListener {
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.main_layout);

        // request for permission
        if (!checkIfPermissionsAreAvailable()) {
            Snackbar.make(mLayout, "Please provide permissions to read SMS",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 0);
                }
            }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 0) {
            // Request for camera permission.
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //Register sms listener
                SmsBroadcastReceiver.bindListener(this);
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Need permission to work! Provide it through app settings!",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void messageReceived(String message) {
        // TODO: here we can access the new sms text
        Snackbar.make(mLayout, "New Message Received: " + message, Snackbar.LENGTH_INDEFINITE)
                .show();
    }

    private boolean checkIfPermissionsAreAvailable() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }
}