package ds.docusheet.table;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static ds.docusheet.table.GetDocument.cells5;


public class columnClicked extends AppCompatActivity {
    ListView listView;
    ListAdapter adapter;
    String heading;
    int col_id;
    String col_name,doc_name,doc_id,column_num,total_col,col_type;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column_clicked);
        listView=findViewById(R.id.listview);
        heading=getIntent().getStringExtra("col_name");
        //setLocale("hi");
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(heading);
        actionBar.show();
        col_id=getIntent().getIntExtra("col_id",-1);
        col_name=getIntent().getStringExtra("col_name");
        col_type=getIntent().getStringExtra("col_type");
        doc_id=getIntent().getStringExtra("doc_id");
        doc_name=getIntent().getStringExtra("doc_name");
        column_num=getIntent().getStringExtra("column_num");
        total_col=getIntent().getStringExtra("total_col");

        final String[] listItem=new String[]{
                getResources().getString(R.string.rename_column),getResources().getString(R.string.change_type),getResources().getString(R.string.show_total),getResources().getString(R.string.setformula),getResources().getString(R.string.delete_column)
        };
        int [] imageItem={
                R.drawable.ic_baseline_edit_24,R.drawable.ic_baseline_merge_type_24,R.drawable.ic_baseline_add_24,R.drawable.ic_baseline_calculate_24 ,R.drawable.ic_baseline_delete_24
        };
        int [] color={Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK,Color.RED};
        listView=findViewById(R.id.listview);
        adapter=new MyAdapter(columnClicked.this,listItem,imageItem,color);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                if(position==2)
                {
                    //Toast.makeText(columnClicked.this,"This feature is coming soon",Toast.LENGTH_LONG).show();


                    Intent intent=new Intent(columnClicked.this,GetDocument.class);
                    if(cells5.size()>=1){ if(cells5.get(cells5.size()-1).getDoc_id()!=Integer.parseInt(doc_id))cells5.clear();}
                    cells5.add(new cell (String.valueOf(Integer.parseInt(column_num)-1),Integer.parseInt(doc_id)));
                    intent.putExtra("doc_id",doc_id);
                    intent.putExtra("doc_name",doc_name);
                    intent.putExtra("col_num",Integer.parseInt(column_num)-1);
                    intent.putExtra("setting",-1);
                    finish();
                   //GetDocument.getInstance().getTotal(Integer.parseInt(column_num)-1);
                    startActivity(intent);

                }
                else if(position==1){
                    Intent intent = new Intent(columnClicked.this, columnsSetting.class);
                    intent.putExtra("heading", listItem[position]);
                    intent.putExtra("setting", String.valueOf(position));
                    intent.putExtra("col_id", col_id);
                    intent.putExtra("col_name", col_type);
                    intent.putExtra("doc_id", doc_id);
                    intent.putExtra("doc_name", doc_name);
                    intent.putExtra("total_col", total_col);
                    intent.putExtra("column_num", column_num);
                    startActivity(intent);

                }
                else
                if (position == 4) {

                    /*String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/delete-column/"+doc_id;
                RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
                try {
                    JSONObject object=new JSONObject();
                    object.put("column_nums",total_col);
                    object.put("deleted_column_num",column_num);
                    object.put("id",String.valueOf(col_id));
                    //Toast.makeText(ColumnClicked.this, object.toString(), Toast.LENGTH_SHORT).show();
                   JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,url,object,new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Toast.makeText(ColumnClicked.this, "Success", Toast.LENGTH_SHORT).show();

                            //dialog.dismiss();


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(ColumnClicked.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(request);


                }
                catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(ColumnClicked.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }*/
                    AlertDialog.Builder alert = new AlertDialog.Builder(columnClicked.this);
                    alert.setTitle("DELETE");
                    alert.setMessage("Do you want to delete the column?");
                    alert.setCancelable(false);
                    alert.setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/delete-column/"+doc_id;
                            RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
                            try {
                                JSONObject object=new JSONObject();
                                object.put("column_nums",total_col);
                                object.put("deleted_column_num",column_num);
                                object.put("id",String.valueOf(col_id));
                                //Toast.makeText(ColumnClicked.this, object.toString(), Toast.LENGTH_SHORT).show();
                                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,url,object,new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //Toast.makeText(ColumnClicked.this, "Success", Toast.LENGTH_SHORT).show();

                                        //dialog.dismiss();


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //Toast.makeText(ColumnClicked.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                queue.add(request);
                                Intent intent=new Intent(columnClicked.this,GetDocument.class);
                                intent.putExtra("doc_id",doc_id);
                                intent.putExtra("doc_name",doc_name);
                                finish();
                                startActivity(intent);



                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                                //Toast.makeText(ColumnClicked.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
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
                else if(position==3){
                    Intent intent=new Intent(columnClicked.this,formulae.class);
                    intent.putExtra("doc_id",doc_id);
                    intent.putExtra("doc_name",doc_name);
                    intent.putExtra("column_num", column_num);
                    startActivity(intent);
                    //Toast.makeText(columnClicked.this, "This feature will be added soon!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(columnClicked.this, columnsSetting.class);
                    intent.putExtra("heading", listItem[position]);
                    intent.putExtra("setting", String.valueOf(position));
                    intent.putExtra("col_id", col_id);
                    intent.putExtra("col_name", col_name);
                    intent.putExtra("doc_id", doc_id);
                    intent.putExtra("doc_name", doc_name);
                    intent.putExtra("total_col", total_col);
                    intent.putExtra("column_num", column_num);
                    startActivity(intent);
                }
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
class MyAdapter extends BaseAdapter {
    Context context;
    private String[] listItem;
    private int imageItem[];
    private int [] color;

    public MyAdapter(Context context, String[] listItem, int[] imageItem,int[] color) {
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