package ds.docusheet.table;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class NewDocument extends AppCompatActivity {
    List<cell> cells,cells2,cells3;
    int rows=5,column=3,height=50;
    String id;
    FirebaseAuth mAuth;

    /*public void setLocale(String lang){
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_document);
        mAuth=FirebaseAuth.getInstance();
        LoadingBar loadingBar=new LoadingBar(NewDocument.this);
        loadingBar.startLoading();
        cells=new ArrayList<>();
        cells2=new ArrayList<>();
        cells3=new ArrayList<>();
        //setLocale("hi");
        startingMethod();
        //getID();
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        //Intent intent=new Intent(NewDocument.this,MainActivity.class);
        //intent.putExtra("doc_id",id);
        //startActivity(intent);
        //finish();
        //extract();
        //click(adapter);

    }

    public void startingMethod(){
        cells3.add(new cell(""));
        for(int i=1;i<=(column);i++){
            cells2.add(new cell(""+getResources().getString(R.string.column)+" "+i));
        }

        for(int i=1;i<=(rows);i++){
            cells3.add(new cell(""+i));
        }
        for(int i=1;i<=rows*(column);i++){
            cells.add(new cell(""));
        }
        String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/save-document-create";
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("rows_num", rows);
            jsonBody.put("cols_num", column);
            jsonBody.put("user_id",mAuth.getUid());
            jsonBody.put("document_name",getResources().getString(R.string.new_register));
            JSONArray json1=new JSONArray();
            for (int i=1;i<=rows*column;i++){
                json1.put("10");
            }
            jsonBody.put("width",json1);
            JSONArray json2=new JSONArray();
            for (int i=1;i<=rows*column;i++){
                json2.put("10");
            }
            jsonBody.put("height",json2);
            JSONArray json3=new JSONArray();
            for (int i = 0; i < cells2.size(); i++) {
                cell tempCell = cells2.get(i);
                String string = tempCell.getData();
                json3.put(string);
            }
            jsonBody.put("column_name",json3);
            JSONArray json4=new JSONArray();
            for (int i=1;i<=rows*column;i++){
                json4.put("text");
            }
            jsonBody.put("column_type",json4);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < cells.size(); i++) {
                cell tempCell = cells.get(i);
                String string = tempCell.getData();
                jsonArray.put(string);
            }
            jsonBody.put("data",jsonArray);
            //final String requestBody = jsonBody.toString();
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url,jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //progressDialog.dismiss();
                    try {
                        id=response.getString("doc_id");
                        Intent intent=new Intent(NewDocument.this,GetDocument.class);
                        intent.putExtra("doc_id",id);
                        intent.putExtra("doc_name",getResources().getString(R.string.new_register));
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(NewDocument.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(request);
            //getID();
            //Toast.makeText(NewDocument.this, id, Toast.LENGTH_SHORT).show();



        }
        catch (JSONException e){
            Toast.makeText(NewDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

   /* public void getID(){
        String user_id=mAuth.getUid();
        //user_id="user1";
        final String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-all-document/"+user_id;
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        //final List<Document> documents=new ArrayList<>();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray=response.getJSONArray("document");
                            JSONObject jsonObject=jsonArray.getJSONObject(jsonArray.length()-1);
                            id=jsonObject.getString("doc_id") ;
                            doc_name=jsonObject.getString("document_name");

                            Toast.makeText(NewDocument.this, id, Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(NewDocument.this,MainActivity.class);
                            intent.putExtra("doc_id",id);
                            intent.putExtra("doc_name",doc_name);
                            finish();
                            startActivity(intent);


                        } catch (JSONException e) {
                            Toast.makeText(NewDocument.this,""+e,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewDocument.this,""+error,Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
    */
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }
}