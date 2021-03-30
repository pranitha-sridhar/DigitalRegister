package ds.docusheet.table;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class FingerPrint extends AppCompatActivity {
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);


        BiometricManager biometricManager=BiometricManager.from(FingerPrint.this);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                //Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "No Fingerprint sensor", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "Biometric Error", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this, "None Enrolled", Toast.LENGTH_SHORT).show();
                break;

        }
        Executor executor= ContextCompat.getMainExecutor(this);
        biometricPrompt=new BiometricPrompt(FingerPrint.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),errString,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                getSharedPreferences("Lock_pref",MODE_PRIVATE).edit().putBoolean("stop",false).apply();
                    Intent intent=new Intent(FingerPrint.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });


        promptInfo=new BiometricPrompt.PromptInfo.Builder().setTitle("Login").setDescription("Place your finger").setNegativeButtonText("Cancel".toUpperCase()).build();
        biometricPrompt.authenticate(promptInfo);
    }
}