package ds.docusheet.table;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DocumentSetting extends AppCompatActivity {
    ListView listView;
    ListAdapter adapter;
    String heading,doc_id;
     List<SavedData> list=new ArrayList<>();;
    LoadingBar loadingBar;
    ProgressDialog progressBar;
    List<ColumnData> columnlist=new ArrayList<>();
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
        //setLocale("hi");
        setContentView(R.layout.activity_document_setting);
        doc_id= getIntent().getStringExtra("doc_id");
        heading=getIntent().getStringExtra("doc_name");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(heading);
        actionBar.show();
        list=new ArrayList<>();
        listView = findViewById(R.id.listview);
       progressBar= new ProgressDialog(DocumentSetting.this);
        final String[] listItem = new String[]{
                getResources().getString(R.string.rename_file), getResources().getString(R.string.share_pdf), getResources().getString(R.string.delete_file)
        };
        int[] imageItem = {
                R.drawable.ic_baseline_edit_24, R.drawable.ic_baseline_share_24, R.drawable.ic_baseline_delete_24
        };
        final int[] color = {Color.BLACK, Color.BLACK, Color.RED};

        listView = findViewById(R.id.listview);
        adapter = new MyDocumentAdapter(DocumentSetting.this, listItem, imageItem, color);
        listView.setAdapter(adapter);
       loadingBar = new LoadingBar(DocumentSetting.this);

        Toast.makeText(DocumentSetting.this,"yes",Toast.LENGTH_LONG);

        final String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-document-data/" +doc_id;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                list.add(new SavedData(jsonObject.getString("id"),
                                        jsonObject.getString("column1"),
                                        jsonObject.getString("column2"),
                                        jsonObject.getString("column3"),
                                        jsonObject.getString("column4")
                                        , jsonObject.getString("column5"),
                                        jsonObject.getString("column6"),
                                        jsonObject.getString("column7"),
                                        jsonObject.getString("column8"),
                                        jsonObject.getString("column9"),
                                        jsonObject.getString("column10"),
                                        jsonObject.getString("column11"),
                                        jsonObject.getString("column12"),
                                        jsonObject.getString("column13"),
                                        jsonObject.getString("column14"),
                                        jsonObject.getString("column15"),
                                        jsonObject.getString("column16")
                                        , jsonObject.getString("column17")
                                        , jsonObject.getString("column18")
                                        , jsonObject.getString("column19"),
                                        jsonObject.getString("column20"),
                                        jsonObject.getString("height"),
                                        jsonObject.getString("width")
                                ));
                            }

                        } catch (JSONException e) {
                            Toast.makeText(DocumentSetting.this, "" + e, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DocumentSetting.this, "" + error, Toast.LENGTH_LONG).show();
                    }
                });
        MySingleton.getInstance(DocumentSetting.this).addToRequestQueue(jsonObjectRequest);



        final String url2 = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-column-data/"+doc_id;

        final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                columnlist.add(new ColumnData(jsonObject.getString("id"),
                                        jsonObject.getString("column_names"),
                                        jsonObject.getString("column_type"),
                                        jsonObject.getString("column_nums"),
                                        jsonObject.getString("row_nums")
                                ));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(DocumentSetting.this, "" + e, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DocumentSetting.this, "" + error, Toast.LENGTH_LONG).show();
                    }
                });
        MySingleton.getInstance(DocumentSetting.this).addToRequestQueue(jsonObjectRequest2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {

                    List<SavedData> slist = list;
                    List<ColumnData> clist = columnlist;

                    if (isStoragePermissionGranted()) {
                        loadingBar.startLoading();
                        GeneratePdf generatePdf = new GeneratePdf();
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    Drawable d = getResources().getDrawable(R.drawable.icon34);
                                    BitmapDrawable bitDw = ((BitmapDrawable) d);
                                    Bitmap bmp = bitDw.getBitmap();
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    if (generatePdf.PdfCreation(slist, clist, heading, doc_id,stream)) {
                                        Toast.makeText(DocumentSetting.this, "Pdf succesfully created", Toast.LENGTH_LONG).show();
                                        if(Build.VERSION.SDK_INT>=24)
                                        {
                                            try{
                                                Method m= StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                                m.invoke(null);
                                            }
                                            catch (Exception e)
                                            {
                                                Toast.makeText(DocumentSetting.this,""+e,Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        String filename=heading+doc_id+".pdf";
                                        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/"+filename);
                                        Intent target=new Intent(Intent.ACTION_VIEW);
                                        target.setDataAndType(Uri.fromFile(folder),"application/pdf");
                                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY| Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        Intent intent=Intent.createChooser(target,"Open File");
                                        try {
                                            startActivity(intent);
                                        }
                                        catch (Exception e)
                                        {
                                            Toast.makeText(DocumentSetting.this,""+e,Toast.LENGTH_LONG).show();
                                        }
                                    } else {

                                        Toast.makeText(DocumentSetting.this, "Error", Toast.LENGTH_LONG).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    loadingBar.dismissDialog();
                    }
                }
                else if(position==0) {
                    Intent intent = new Intent(DocumentSetting.this, RenameDocument.class);
                    intent.putExtra("doc_id", doc_id);
                    intent.putExtra("heading", listItem[position]);
                    startActivity(intent);
                }
                else if(position==2)
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(DocumentSetting.this);
                    alert.setTitle("DELETE");
                    alert.setMessage("Do you want to delete the document?");
                    alert.setCancelable(false);
                    alert.setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/delete-document/"+doc_id;
                            RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
                            JsonObjectRequest request=new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Toast.makeText(DocumentSetting.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            queue.add(request);
                            startActivity(new Intent(DocumentSetting.this,MainActivity.class));
                            overridePendingTransition(0,0);
                            finish();
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
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }



        public boolean isStoragePermissionGranted()
    {
        String Tag="Permission Granted";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if(this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            {
                Log.v(Tag,"Permission is granted");
                return true;
            }
            else
            {
                Log.v(Tag,"Permission is revoked");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                return false;
            }
        }
        else
        {
            Log.v(Tag,"Permision is granted");
            return true;
        }
    }
}
class MyDocumentAdapter extends BaseAdapter {
    Context context;
    private String[] listItem;
    private int imageItem[];
    private int [] color;

    public MyDocumentAdapter(Context context, String[] listItem, int[] imageItem,int[] color) {
        this.context = context;
        this.listItem = listItem;
        this.imageItem = imageItem;
        this.color=color;
    }

    @Override
    public int getCount() {
        return listItem.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView =inflater.inflate(R.layout.columnmenuitem,parent,false);
            viewHolder.listname=convertView.findViewById(R.id.textItem);
            viewHolder.listname.setTextColor(color[position]);
            viewHolder.icon=convertView.findViewById(R.id.imageItem);
            viewHolder.icon.setColorFilter(color[position]);
            result = convertView;
            convertView.setTag(viewHolder);


        }
        else {
            viewHolder= (ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder.listname.setText(listItem[position]);
        viewHolder.icon.setImageResource(imageItem[position]);
        return convertView;
    }

    public static class ViewHolder{
        TextView listname;
        ImageView icon;
    }
}