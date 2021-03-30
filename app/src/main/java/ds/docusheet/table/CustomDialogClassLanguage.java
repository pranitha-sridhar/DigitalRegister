package ds.docusheet.table;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.Locale;


public class CustomDialogClassLanguage extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public RadioButton english, hindi;
    Button cancel;
    public CustomDialogClassLanguage(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_language);
        english =findViewById(R.id.radioButtonEnglish);
        hindi = findViewById(R.id.radioButtonHindi);
        cancel = (Button) findViewById(R.id.language_cancel);
        cancel.setOnClickListener(this);
        english.setOnClickListener(this);
        hindi.setOnClickListener(this);

        SharedPreferences prefs = c.getSharedPreferences("LangSettings", c.MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", "");
        if(lang.equals("hi")){
            hindi.setChecked(true);
            english.setChecked(false);
        }
        else{
            english.setChecked(true);
            hindi.setChecked(false);
        }


    }

    @Override
    public void onClick(View v) {
        Log.d("dialogCheck", "onClick: "+v.getId());
        switch (v.getId()) {
            case R.id.radioButtonEnglish:
                setLocale("");
                c.recreate();
                break;
            case R.id.radioButtonHindi:
                setLocale("hi");
                c.recreate();
                break;
            default:
                dismiss();
                break;
        }
        dismiss();
    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale=locale;
        c.getBaseContext().getResources().updateConfiguration(configuration,c.getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = c.getSharedPreferences("LangSettings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }
}
