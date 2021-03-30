package ds.docusheet.table;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class DocxList extends AppCompatActivity {
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    FirebaseAuth mAuth;
    LinearLayout whatsapp;
    static AdapterList adaptermain;
    static List<Document> documents;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docx_list);

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = "+91 9981278197";
                String url = "https://api.whatsapp.com/send?phone=" + number;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        relativeLayout = findViewById(R.id.new_document);
        mAuth = FirebaseAuth.getInstance();
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DocxList.this, NewDocument.class);
                startActivity(intent);
                finish();
            }
        });

        getDocList();
    }

    public void getDocList() {
        String user_id = mAuth.getUid();
        final String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-all-document/" + user_id;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        documents = new ArrayList<>();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("document");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                documents.add(new Document(jsonObject.getString("doc_id"), jsonObject.getString("document_name"),jsonObject.getString("update_document")));
                            }
                            makelist(documents);
                        } catch (JSONException e) {
                            Toast.makeText(DocxList.this, "" + e, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DocxList.this, "" + error, Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }


    public void makelist(final List<Document> list) {
        adaptermain = new AdapterList(list, getApplication());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adaptermain);

        layoutManager = new LinearLayoutManager(DocxList.this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        //recyclerView.setLayoutManager(new LinearLayoutManager(DocxList.this));
        adaptermain.setOnItemClickListener(new AdapterList.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(DocxList.this, "HAHA", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DocxList.this, MainActivity.class);
                intent.putExtra("doc_id", list.get(position).getDoc_id());
                intent.putExtra("doc_name", list.get(position).getDoc_name());
                intent.putExtra("update",list.get(position).getUpdate());
                startActivity(intent);
            }
        });
        adaptermain.setOnMoreListener(new AdapterList.OnMoreListener() {
            @Override
            public void onMoreClick(int position) {
                Intent intent = new Intent(DocxList.this, DocumentSetting.class);
                intent.putExtra("doc_id", list.get(position).getDoc_id());
                intent.putExtra("doc_name", list.get(position).getDoc_name());
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder alert = new AlertDialog.Builder(DocxList.this);
        alert.setTitle("EXIT");
        alert.setMessage("Do you want to exit the application?");
        alert.setCancelable(false);
        alert.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
