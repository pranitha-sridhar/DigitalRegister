package ds.docusheet.table;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

public class Otp extends AppCompatActivity {
    EditText editText;
    TextView resend;
    Button login;
    String verificationID;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        login=findViewById(R.id.button);
        editText=findViewById(R.id.editText3);
        resend=findViewById(R.id.resend);
        mAuth=FirebaseAuth.getInstance();

        final String phoneNumber=getIntent().getStringExtra("phoneNumber");
        sendVerificationCode(phoneNumber);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code=editText.getText().toString().trim();
                if(code.isEmpty()){
                    editText.setError("Empty");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
       editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.getText().toString().length()>0)
                {
                    login.setBackground(getResources().getDrawable(R.drawable.changablecorners));
                }
                else{
                    login.setBackground(getResources().getDrawable(R.drawable.corners));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Otp.this,"Resend otp",Toast.LENGTH_LONG).show();
                sendVerificationCode(phoneNumber);
            }
        });

    }

    private void verifyCode(String code){
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationID,code);
        signInwith(credential);
    }

    private void signInwith(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(Otp.this,MainActivity.class);
                    final String userid=mAuth.getUid();
                    String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/save-firebase-users";
                    RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

                    StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                                return userid == null ? null : userid.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", userid, "utf-8");
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


                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);

                }
                //else
                    //Toast.makeText(otp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendVerificationCode(String number){
        PhoneAuthOptions options=PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(number).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallBack).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code1=phoneAuthCredential.getSmsCode();
            if(code1!=null){
                editText.setText(code1);
                verifyCode(code1);

            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}
