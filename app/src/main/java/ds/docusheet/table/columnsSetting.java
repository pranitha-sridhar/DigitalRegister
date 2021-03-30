package ds.docusheet.table;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class columnsSetting extends AppCompatActivity {
    Button button, save_radio;
    Button column;
    EditText cols_name;
    int col_id = -1;
    String heading, name, doc_id, doc_name, col_name, total_col, column_num,string;
    RelativeLayout relativeLayout3, relativeLayout4, relativeLayout5, relativeLayout6;
    RadioGroup radioGroup;

   /* public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale=locale;
        this.getResources().updateConfiguration(configuration,this.getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = this.getSharedPreferences("LangSettings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_columns_setting);
        String heading = getIntent().getStringExtra("heading");
        String setting = getIntent().getStringExtra("setting");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(heading);
        actionBar.show();
        //setLocale("hi");

        doc_id = getIntent().getStringExtra("doc_id");
        col_id = getIntent().getIntExtra("col_id", -1);
        doc_name = getIntent().getStringExtra("doc_name");
        col_name = getIntent().getStringExtra("col_name");
        //Toast.makeText(this, ""+col_name, Toast.LENGTH_SHORT).show();
        column_num = getIntent().getStringExtra("column_num");
        total_col = getIntent().getStringExtra("total_col");

        relativeLayout3 = findViewById(R.id.relativeLayout3);
        relativeLayout3.setVisibility(View.INVISIBLE);
        relativeLayout4 = findViewById(R.id.relativeLayout4);
        relativeLayout4.setVisibility(View.INVISIBLE);
        relativeLayout5 = findViewById(R.id.relativeLayout5);
        relativeLayout5.setVisibility(View.INVISIBLE);
        relativeLayout6 = findViewById(R.id.relativeLayout6);
        relativeLayout6.setVisibility(View.INVISIBLE);
        cols_name = findViewById(R.id.cols_name);
        button = findViewById(R.id.save);
        save_radio = findViewById(R.id.save_radio);
        radioGroup=findViewById(R.id.radiogroup);

        if(col_name.equals("text"))radioGroup.check(R.id.radioText);
        else if(col_name.equals("number"))radioGroup.check(R.id.radioNum);
        else if(col_name.equals("time"))radioGroup.check(R.id.radioTime);
        else if(col_name.equals("date"))radioGroup.check(R.id.radioDate);
        else if(col_name.equals("rupees"))radioGroup.check(R.id.radioRupees);
        else if(col_name.equals("phone"))radioGroup.check(R.id.radioMobile);
        else if(col_name.equals("toggle"))radioGroup.check(R.id.radioToggle);
        else if(col_name.equals("checkbox"))radioGroup.check(R.id.radioCheckbox);

        cols_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(cols_name.getText().toString().length()>0)
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

        if (setting.equals("0")) {
            relativeLayout4.setVisibility(View.VISIBLE);
            relativeLayout3.setVisibility(View.VISIBLE);

        }
        if (setting.equals("1")) {
            relativeLayout5.setVisibility(View.VISIBLE);
            relativeLayout6.setVisibility(View.VISIBLE);
        }

        save_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_radio.setEnabled(false);
                int selectedradio=radioGroup.getCheckedRadioButtonId();
                if (selectedradio==R.id.radioText)string="text";
                else if(selectedradio==R.id.radioNum)string="number";
                else if(selectedradio==R.id.radioTime)string="time";
                else if(selectedradio==R.id.radioDate)string="date";
                else if(selectedradio==R.id.radioRupees)string="rupees";
                else if(selectedradio==R.id.radioMobile)string="phone";
                else if(selectedradio==R.id.radioToggle)string="toggle";
                else if(selectedradio==R.id.radioCheckbox)string="checkbox";
                String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/change-column-type/"+col_id;
                RequestQueue queue= Volley.newRequestQueue(columnsSetting.this);
                StringRequest request=new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(otp.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(otp.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    public String getBodyContentType() {
                        return "text/plain;charset=UTF-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return string == null ? null : string.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", string, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                            // can get more details such as response.headers
                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };
                queue.add(request);
                Intent intent=new Intent(columnsSetting.this,GetDocument.class);
                intent.putExtra("doc_id",doc_id);
                intent.putExtra("doc_name",doc_name);
                finish();
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                String string = cols_name.getText().toString();
                if (string == null) {
                    cols_name.setError("Should not be empty");
                    cols_name.requestFocus();
                    return;
                }
                String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/change-column-name/" + col_id;
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(otp.this, response, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(columnsSetting.this, GetDocument.class);
                        intent.putExtra("doc_id", doc_id);
                        intent.putExtra("doc_name", doc_name);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        //finish();
                        finish();
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(otp.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return string == null ? null : string.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", string, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                            // can get more details such as response.headers
                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };
                queue.add(request);
                // request.setRetryPolicy(defaultRetryPolicy);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }
}