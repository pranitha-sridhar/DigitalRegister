package ds.docusheet.table;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class RenameDocument extends AppCompatActivity {

    String name,doc_name;
    EditText newname;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_document);
        String heading=getIntent().getStringExtra("heading");
        String doc_id=getIntent().getStringExtra("doc_id");
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(heading);
        actionBar.show();
        newname=findViewById(R.id.cols_name);
        save=findViewById(R.id.save);

        newname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(newname.getText().toString().length()>0)
                {
                    save.setBackground(getResources().getDrawable(R.drawable.changablecorners));
                }
                else{
                    save.setBackground(getResources().getDrawable(R.drawable.corners));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        name=doc_name;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setEnabled(false);
                String string=newname.getText().toString();
                if(string==null){
                    newname.setError("Should not be empty");
                    newname.requestFocus();
                    return;
                }
                String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/change-document-name/"+doc_id;
                RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
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
                Intent intent=new Intent(RenameDocument.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.slide_in_bottom);
            }
        });
        newname.setText(name);
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }
}