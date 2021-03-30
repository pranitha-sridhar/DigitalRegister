package ds.docusheet.table;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

import java.util.Locale;
import java.util.concurrent.Executor;



public class Login extends AppCompatActivity {
    Button button;
    EditText phone;
    CountryCodePicker ccp;
    String code="91",number;
     BiometricPrompt biometricPrompt;
    androidx.appcompat.widget.Toolbar toolbar;
     BiometricPrompt.PromptInfo promptInfo;
    static boolean first,first_r,firstrun;

   @Override
    protected void onStart() {
       if (getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("finger_lock",false) && getSharedPreferences("Lock_pref",MODE_PRIVATE).getBoolean("stop",true) && FirebaseAuth.getInstance().getCurrentUser()!=null){
           Intent intent=new Intent(Login.this,FingerPrint.class);
           startActivity(intent);
           finish();
           //Toast.makeText(this, ""+stop+" "+finger_lock, Toast.LENGTH_SHORT).show();
       }
       else{
           if(FirebaseAuth.getInstance().getCurrentUser()!=null){
               Intent intent=new Intent(Login.this,MainActivity.class);
               startActivity(intent);
               finish();
           }
       }
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button=findViewById(R.id.button);
        phone=findViewById(R.id.editText2);
        ccp=findViewById(R.id.ccp);

        firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        //Toast.makeText(this, ""+firstrun, Toast.LENGTH_SHORT).show();
        first = getSharedPreferences("PREF", MODE_PRIVATE).getBoolean("first", true);
        //Toast.makeText(this, ""+first, Toast.LENGTH_SHORT).show();
        if (firstrun) {
            //CustomDialogClassLanguage cdd = new CustomDialogClassLanguage(this);
            //cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //cdd.show();
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", false).apply();
            firstrun=false;
        }
        else
        {
            getSharedPreferences("PREF", MODE_PRIVATE).edit().putBoolean("first", false).apply();
            first=false;
        }

        /*first = getSharedPreferences("PREF", MODE_PRIVATE).getBoolean("first_run", true);

        if (!first_r){
            getSharedPreferences("PREF",MODE_PRIVATE).edit().putBoolean("first_run",false).apply();
        }*/


        //loadLocale();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                code = ccp.getSelectedCountryCode();
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(phone.getText().toString().length()>0)
                {
                    button.setBackground(getResources().getDrawable(R.drawable.changablecorners));
                }
                else{
                    button.setBackground(getResources().getDrawable(R.drawable.corners));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = phone.getText().toString().trim();
                if (code == null) {
                    Toast.makeText(Login.this, "Number is Null", Toast.LENGTH_SHORT).show();
                    return;
                } else if (number.isEmpty()) {
                    phone.setError("Cannot be empty");
                    phone.requestFocus();
                    return;
                }
                    else if(number.length()>10||number.length()<10)
                    {
                        Toast.makeText(Login.this, "Number is not correct", Toast.LENGTH_SHORT).show();
                        return;
                    }
                 else {
                    String phoneNumber = "+" + code + number;
                    //Toast.makeText(Login.this, phoneNumber, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Otp.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);
                }
            }
        });
    }
    private void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("LangSettings", Activity.MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", "");
        setLocale(lang);
    }
    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("LangSettings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }
}