package ds.docusheet.table;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.Locale;
import java.util.concurrent.Executor;


public class MainActivity extends AppCompatActivity {
   BottomNavigationView bottomNavigationView;
   NavController navController;
   ImageView more_vertical;
   BiometricPrompt biometricPrompt;
   androidx.appcompat.widget.Toolbar toolbar;
   BiometricPrompt.PromptInfo promptInfo;
   boolean finger_lock,stop;
   FingerprintManager fingerprintManager;



    @Override
    protected void onStart() {

        //Toast.makeText(this, ""+stop+" "+finger_lock, Toast.LENGTH_SHORT).show();

        super.onStart();
    }

    @Override
    protected void onStop() {
        if(!getSharedPreferences("Lock_pref", MODE_PRIVATE).getBoolean("stop", true)){
            getSharedPreferences("Lock_pref",MODE_PRIVATE).edit().putBoolean("stop",true).apply();
            stop=true;
            //Toast.makeText(this, "Stopped "+stop, Toast.LENGTH_SHORT).show();
        }
        super.onStop();
    }

   /* @Override
    protected void onDestroy() {
        if(!getSharedPreferences("Lock_pref", MODE_PRIVATE).getBoolean("stop", true)){
            getSharedPreferences("Lock_pref",MODE_PRIVATE).edit().putBoolean("stop",true).apply();
            stop=true;
            Toast.makeText(this, "Destroyed "+stop, Toast.LENGTH_SHORT).show();
        }

        super.onDestroy();
    }*/

    public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale=locale;
        this.getResources().updateConfiguration(configuration,this.getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = this.getSharedPreferences("LangSettings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    private void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("LangSettings", Activity.MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", "");
        setLocale(lang);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);
        loadLocale();

        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(this,"0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume",null);
        mixpanel.flush();

        toolbar=findViewById(R.id.toolbar);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();
        setSupportActionBar(toolbar);
        invalidateOptionsMenu();
        toolbar.setOnMenuItemClickListener(toolListener);

        finger_lock = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("finger_lock", false);
        stop = getSharedPreferences("Lock_pref", MODE_PRIVATE).getBoolean("stop", true);
        //toolbar.inflateMenu(R.menu.settings_menu);
        BiometricManager biometricManager=BiometricManager.from(MainActivity.this);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS: //Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                                                     break;
                                                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:Toast.makeText(this, "No Sensor", Toast.LENGTH_SHORT).show();
                                                                                                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE: Toast.makeText(this, "the hardware is unavailable. Try again later.", Toast.LENGTH_SHORT).show();
                        break;
                        case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:Toast.makeText(this, "No fingerprint Registered. Please register", Toast.LENGTH_SHORT).show();
                            break;

        }
        Executor executor= ContextCompat.getMainExecutor(this);
        biometricPrompt=new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if(errString.equals("BIOMETRIC_ERROR_HW_NOT_PRESENT"))
                Toast.makeText(MainActivity.this, "device does not have a biometric sensor", Toast.LENGTH_SHORT).show();
                else if(errString.equals("BIOMETRIC_ERROR_NO_BIOMETRICS"))
                    Toast.makeText(MainActivity.this, "user does not have any biometrics enrolled.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, errString, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                if(finger_lock){
                    //biometricPrompt.authenticate(promptInfo);
                    //menuItem.setTitle("Disable Fingerprint Lock");
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("finger_lock",false).apply();
                    finger_lock=false;
                }
                else {
                    //biometricPrompt.authenticate(promptInfo);
                    //menuItem.setTitle("Enable Fingerprint Lock");
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("finger_lock",true).apply();
                    finger_lock=true;
                }

            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        promptInfo=new BiometricPrompt.PromptInfo.Builder().setTitle("Login").setDescription("Place your finger").setNegativeButtonText("Cancel").build();
    }



    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        loadLocale();
        getMenuInflater().inflate(R.menu.settings_menu, menu);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        loadLocale();
        MenuItem item = menu.findItem(R.id.finger_print);
        if (finger_lock) {
            item.setTitle(getResources().getString(R.string.disablefpl));
        } else {
            item.setTitle(getResources().getString(R.string.enablefpl));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private androidx.appcompat.widget.Toolbar.OnMenuItemClickListener toolListener=new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.change_lang: CustomDialogClassLanguage cdd = new CustomDialogClassLanguage(MainActivity.this);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                    //getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();
                    break;

                case R.id.help_feedback:String number = "+91 9981278197";
                    //getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();
                    String url = "https://api.whatsapp.com/send?phone=" + number;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    //onPause();
                    startActivity(i);
                    break;
                case R.id.share_app://getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Digital Register");
                        String shareMessage= "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "Choose One"));
                    } catch(Exception e) {
                        //e.toString();
                    }
                    break;
                case R.id.finger_print: biometricPrompt.authenticate(promptInfo);
                    /*if(finger_lock){

                        menuItem.setTitle("Disable Fingerprint Lock");
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("finger_lock",false).apply();
                        finger_lock=false;
                    }
                    else {
                        biometricPrompt.authenticate(promptInfo);
                        menuItem.setTitle("Enable Fingerprint Lock");
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("finger_lock",true).apply();
                        finger_lock=true;
                    }*/
                    break;
                default://getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();
                    break;
            }
            return false;
        }


    };


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
     @Override
     public boolean onNavigationItemSelected(@NonNull MenuItem item) {
         loadLocale();
         Fragment selectedFragment=null;

         switch (item.getItemId())
         {
             case R.id.home:
                 selectedFragment=new HomeFragment();
                 break;
             case R.id.template:
                 selectedFragment=new TemplateFragment();
                 break;
         }
         getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selectedFragment).commit();
         return true;
     }
 };

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("EXIT");
        alert.setMessage("Do you want to exit the application?");
        alert.setCancelable(false);
        alert.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(a);

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();

    }

}