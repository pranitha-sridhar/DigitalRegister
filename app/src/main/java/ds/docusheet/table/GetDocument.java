package ds.docusheet.table;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.date.MonthAdapter;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.inflate;
import static java.util.Calendar.*;


public class GetDocument extends AppCompatActivity implements TextWatcher {
    static List<cell> cells,cells2,cells3,cells4;
    static int rows=2,column=5,height=50;
    int col_num,setting=0,mHour,mMinute,h=-1,m=-1,pos=0,datacell_num=0,column_id=0,posii=-1;
    Button rowbut,colbut;
    ImageView savebut;
    EditText et;
    boolean firstrun;
    String id="17",doc_name,format,update="n";
    RecyclerView recyclerView,recyclerView2,recyclerView3,recyclerView4;
    static RelativeLayout relativeLayout;
    Adapter adapter;
    AdapterTitle adapter2;
    AdapterTitleRow adapter3;
    AdapterTotal adapter4;
    FirebaseAuth mAuth;
    ArrayList<dataCell> dataCellArrayList;
    static List<cellColumn> cellColumnArrayList;
    ProgressDialog progressDialog;
    GetDocument instance;
    static List<cell> cells5=new ArrayList<>();
    TimePicker timePicker;
    cell cells_temp;
    Toolbar toolbar2,toolbar1;
    //ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_document);
        instance = this;


        //actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.show();

        //firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        id = getIntent().getStringExtra("doc_id");
        doc_name = getIntent().getStringExtra("doc_name");
        col_num = getIntent().getIntExtra("col_num", -1);
        setting = getIntent().getIntExtra("setting", 0);
        update = getIntent().getStringExtra("update_document");

        //actionBar.setTitle(doc_name);
        mAuth = FirebaseAuth.getInstance();

        toolbar2=findViewById(R.id.toolbar2);
        toolbar1=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(doc_name);


        rowbut = findViewById(R.id.rowbut);
        colbut = findViewById(R.id.colbut);
        savebut = findViewById(R.id.savebut);
        recyclerView = findViewById(R.id.recycle1);
        recyclerView2 = findViewById(R.id.recycler2);
        recyclerView3 = findViewById(R.id.recycle3);
        recyclerView4 = findViewById(R.id.recycler4);
        et = findViewById(R.id.edittext5);
        relativeLayout = findViewById(R.id.vislay);
        cells = new ArrayList<>();
        cells2 = new ArrayList<>();
        cells3 = new ArrayList<>();
        cells4 = new ArrayList<>();
        //cells5=new ArrayList<>();
        //Toast.makeText(GetDocument.this, ""+update+id, Toast.LENGTH_SHORT).show();


        if(update!=null) {
            if (update.equals("no")) {
                //Toast.makeText(GetDocument.this, "in no", Toast.LENGTH_SHORT).show();
                check();
            }
        }
        extract();

        adapter=new Adapter(getApplicationContext(),cells);
        adapter2=new AdapterTitle(getApplicationContext(),cells2);
        adapter3=new AdapterTitleRow(getApplicationContext(),cells3);
        adapter4 = new AdapterTotal(getApplicationContext(),cells4);

        //click(adapter);

        rowbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //rows++;
                //cells3.add(new cell(""+rows));
                final int[] row_id = new int[1];
               String rowurl="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/add-row/"+id;
                RequestQueue queue=Volley.newRequestQueue(getApplicationContext());
                JSONObject jsonObject=new JSONObject();
                final int[] data = new int[1];
                try {
                    JSONArray jsonArray1=new JSONArray();
                    for(int i=1;i<=column;i++){
                        jsonArray1.put("");
                    }
                    jsonObject.put("data",jsonArray1);
                    JSONArray jsonArray2=new JSONArray();
                    for(int i=1;i<=column;i++){
                        jsonArray2.put("10");
                    }
                    jsonObject.put("width",jsonArray2);
                    JSONArray jsonArray3=new JSONArray();
                    for(int i=1;i<=column;i++){
                        jsonArray3.put("10");
                    }
                    jsonObject.put("height",jsonArray3);
                    jsonObject.put("cols_num",column);
                    jsonObject.put("rows_num",rows);
                    jsonObject.put("current_row_number",rows+1);
                    JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.POST, rowurl, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                row_id[0] =Integer.parseInt(response.getString("row_id"));

                                //check();
                                //Toast.makeText(MainActivity.this, row_id, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    queue.add(objectRequest);
                    dataCellArrayList.add(new dataCell(row_id[0]));
                    dataCellArrayList.get(rows).setSerialno(rows+1);
                    //Toast.makeText(MainActivity.this, data[0], Toast.LENGTH_SHORT).show();
                    //cells.clear();
                    //cells2.clear();
                    //cells3.clear();
                    for(int i=1;i<=(column);i++){
                        cells.add(new cell(""));
                    }
                    cells3.add(new cell(""+(rows+1)));
                    rows++;

                    adapter.notifyDataSetChanged();
                    //adapter2.notifyDataSetChanged();
                    adapter3.notifyDataSetChanged();
                    //extract();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        colbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(column>=20){
                    Toast.makeText(GetDocument.this, "Cannot exceed more than 20", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/add-column/"+id;
                    RequestQueue queue=Volley.newRequestQueue(getApplicationContext());
                    JSONObject object=new JSONObject();
                    object.put("row_nums",String.valueOf(rows));
                    object.put("column_nums",String.valueOf(column));
                    object.put("column_type","text");
                    object.put("column_names","Column "+(column+1));
                    JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int col_id=Integer.parseInt(response.getString("column_id"));
                                cellColumnArrayList.add(new cellColumn(col_id,"Column "+(column+1),"text"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queue.add(request);
                    List<cell> temp=new ArrayList<>();
                    for(int j=0;j<rows*column;j++){
                        temp.add(new cell(cells.get(j).getData()));
                    }
                    cells.clear();
                    for(int i=0;i<column;i++)cells.add(new cell(temp.get(i).getData()));
                    String tem,tem1 = null;
                    for(int i=column,k=column,j=column;i<rows*(column+1);i++){
                        if(k==(column)){cells.add(new cell(""));k=0;}
                        else {if(j<rows*column)cells.add(new cell(temp.get(j).data));j++;k++;}
                    }
                    ++column;
                    cells2.add(new cell("Column "+column));
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),column));
                    recyclerView2.setLayoutManager(new GridLayoutManager(getApplicationContext(),column));
                    adapter.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                    if(recyclerView4.getVisibility()==View.VISIBLE){
                        cells4.add(new cell(""));
                        recyclerView4.setLayoutManager(new GridLayoutManager(getApplicationContext(),column));
                        adapter4.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    public void check(){
        String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/update-document-serial/" + id;
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(GetDocument.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Queue.add(jsonObjectRequest);
    }



    public void click(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(final int position, final View v) {

                toolbar1.setVisibility(View.VISIBLE);
                toolbar2.setVisibility(GONE);
                setSupportActionBar(toolbar1);
                toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        finish();
                        onBackPressed();
                    }
                });
                posii=-1;
                invalidateOptionsMenu();
                ConstraintLayout constraintLayout;
                constraintLayout=findViewById(R.id.layout);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(R.id.nest,ConstraintSet.TOP,R.id.toolbar1,ConstraintSet.BOTTOM,65);
                //constraintSet.connect(R.id.imageView,ConstraintSet.TOP,R.id.check_answer2,ConstraintSet.TOP,0);
                constraintSet.applyTo(constraintLayout);
                getSupportActionBar().setTitle(doc_name);
                cells_temp=cells.get(position);
                datacell_num=position/column;
                column_id=position%column;
                pos=position;
                //Toast.makeText(GetDocument.this, ""+dataCellArrayList.get(datacell_num).getSerialno(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, datacell_num, Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, position5, Toast.LENGTH_SHORT).show();
                if(cellColumnArrayList.get(column_id).getColumn_type().equals("text")) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    et.requestFocus();
                    et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    et.addTextChangedListener(GetDocument.this);
                    et.setText(cells_temp.getData());
                    et.setSelection(et.getText().length());
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    savebut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //String data = et.getText().toString();
                            //cells_temp.setData(data);
                            //et.getText().clear();
                            relativeLayout.setVisibility(GONE);
                            //postRequest(datacell_num);
                            //InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            //adapter.notifyItemChanged(position);
                        }
                    });
                }
                else if(cellColumnArrayList.get(column_id).getColumn_type().equals("number")){
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    relativeLayout.setVisibility(View.VISIBLE);
                    et.requestFocus();
                    et.setText(cells_temp.getData());
                    et.setSelection(et.getText().length());
                    et.addTextChangedListener(GetDocument.this);
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    savebut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            relativeLayout.setVisibility(GONE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    });
                }

                else if (cellColumnArrayList.get(column_id).getColumn_type().equals("time")){
                    //relativeLayout.setVisibility(View.GONE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    h=-1;m=-1;
                    Calendar c = Calendar.getInstance();
                    mHour=c.get(Calendar.HOUR_OF_DAY);
                    mMinute=c.get(Calendar.MINUTE);

                        format="AM";
                   TimePickerDialog timePickerDialog=new TimePickerDialog(GetDocument.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            Log.e("Time", String.valueOf(i)+" "+String.valueOf(i1));

                            if (i == 0) i += 12;
                            else if (i == 12) format = "PM";
                            else if (i > 12) {
                                i -= 12;
                                format = "PM";}
                            h=i;m=i1;

                        }
                    },mHour,mMinute,false);
                   timePickerDialog.setTitle("Set Time");
                    timePickerDialog.show();
                    if(h==-1 && m==-1){
                        relativeLayout.setVisibility(View.VISIBLE);
                        et.requestFocus();
                        et.setText(cells_temp.getData());
                        et.setSelection(et.getText().length());
                        et.addTextChangedListener(GetDocument.this);
                        et.setInputType(InputType.TYPE_CLASS_DATETIME| InputType.TYPE_DATETIME_VARIATION_TIME);
                        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        savebut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //String data = et.getText().toString();
                                //cells_temp.setData(data);
                                //et.getText().clear();
                                relativeLayout.setVisibility(GONE);
                                //postRequest(datacell_num);
                                //InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                //adapter.notifyItemChanged(position);
                            }
                        });
                    }
                    else{
                        if(m/10==0) cells_temp.setData(h+":0"+m+" "+format);
                        else cells_temp.setData(h+":"+m+" "+format);
                        postRequest(datacell_num);
                        adapter.notifyItemChanged(position);
                    }
                }

                else if (cellColumnArrayList.get(column_id).getColumn_type().equals("date")){
                    relativeLayout.setVisibility(GONE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    Calendar calendar=Calendar.getInstance();
                    int mYear=calendar.get(YEAR);
                    int mMonth=calendar.get(MONTH);
                    int mDate=calendar.get(DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog=new DatePickerDialog(GetDocument.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            cells_temp.setData(i2+"/"+(i1+1)+"/"+i);
                            postRequest(datacell_num);
                            adapter.notifyItemChanged(position);
                        }
                    },mYear,mMonth,mDate);
                    datePickerDialog.show();
                }

                else if (cellColumnArrayList.get(column_id).getColumn_type().equals("rupees")){
                    relativeLayout.setVisibility(View.VISIBLE);
                    et.requestFocus();
                    et.setText(cells_temp.getData());
                    et.setSelection(et.getText().length());
                    et.addTextChangedListener(GetDocument.this);
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    savebut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //String data = et.getText().toString();
                            //if(data.startsWith("\u20B9"))cells_temp.setData(data);
                            //else cells_temp.setData("\u20B9 "+data);
                            //et.getText().clear();
                            relativeLayout.setVisibility(GONE);
                            //postRequest(datacell_num);
                            //InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            //adapter.notifyItemChanged(position);
                        }
                    });
                }

                else if (cellColumnArrayList.get(column_id).getColumn_type().equals("phone")){
                    relativeLayout.setVisibility(View.VISIBLE);
                    et.requestFocus();
                    et.setText(cells_temp.getData());
                    et.setSelection(et.getText().length());
                    et.setInputType(InputType.TYPE_CLASS_PHONE);
                    et.addTextChangedListener(GetDocument.this);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    savebut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //String data = et.getText().toString();
                            //cells_temp.setData(data);
                            //et.getText().clear();
                            relativeLayout.setVisibility(GONE);
                            //postRequest(datacell_num);
                            //InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            //adapter.notifyItemChanged(position);
                        }
                    });
                }

                else if (cellColumnArrayList.get(column_id).getColumn_type().equals("toggle")){
                    Toast.makeText(GetDocument.this, "This feature will be added soon", Toast.LENGTH_SHORT).show();
                    //adapter.toggle(1);
                }

                else if (cellColumnArrayList.get(column_id).getColumn_type().equals("checkbox")){
                    Toast.makeText(GetDocument.this, "This feature will be added soon", Toast.LENGTH_SHORT).show();
                }
                //adapter.notifyAll();
            }
        });

        adapter2.setOnItemClickListener(new AdapterTitle.OnItemClickListener3() {
            @Override
            public void onItemClick(int position,View view) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Intent intent=new Intent(GetDocument.this,columnClicked.class);
                intent.putExtra("col_id",cellColumnArrayList.get(position).getCol_id());
                intent.putExtra("col_name",cellColumnArrayList.get(position).getColumn_name());
                intent.putExtra("col_type",cellColumnArrayList.get(position).getColumn_type());
                intent.putExtra("column_num",String.valueOf(position+1));
                intent.putExtra("total_col",String.valueOf(column));
                intent.putExtra("doc_id",id);
                intent.putExtra("doc_name",doc_name);
                startActivity(intent);
            }
        });
        adapter.setOnLongItemClick(new Adapter.onItemLongClickListener() {
            @Override
            public void onLongItemClick(int position, View v) {
                //actionBar.hide();
                posii=position;
                datacell_num=posii/column;
                toolbar1.setVisibility(GONE);
                toolbar2.setVisibility(View.VISIBLE);
                setSupportActionBar(toolbar2);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                ConstraintLayout constraintLayout;
                constraintLayout=findViewById(R.id.layout);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(R.id.nest,ConstraintSet.TOP,R.id.toolbar2,ConstraintSet.BOTTOM,65);
                //constraintSet.connect(R.id.imageView,ConstraintSet.TOP,R.id.check_answer2,ConstraintSet.TOP,0);
                constraintSet.applyTo(constraintLayout);
               getSupportActionBar().setTitle("");
               toolbar2.setOnMenuItemClickListener(toolListener);
               invalidateOptionsMenu();
               //toolbar2.inflateMenu(R.menu.cell_props);
                //Toast.makeText(GetDocument.this, ""+datacell_num, Toast.LENGTH_SHORT).show();

                relativeLayout.setVisibility(GONE);
                toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toolbar1.setVisibility(View.VISIBLE);
                        toolbar2.setVisibility(GONE);
                        setSupportActionBar(toolbar1);
                        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                                onBackPressed();
                            }
                        });
                        posii=-1;
                        invalidateOptionsMenu();
                        ConstraintLayout constraintLayout;
                        constraintLayout=findViewById(R.id.layout);
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(R.id.nest,ConstraintSet.TOP,R.id.toolbar1,ConstraintSet.BOTTOM,65);
                        //constraintSet.connect(R.id.imageView,ConstraintSet.TOP,R.id.check_answer2,ConstraintSet.TOP,0);
                        constraintSet.applyTo(constraintLayout);
                        getSupportActionBar().setTitle(doc_name);
                    }
                });
            }
        });

        
    }
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(posii!=-1){
            getMenuInflater().inflate(R.menu.cell_props, menu);

            if(menu instanceof MenuBuilder){
                MenuBuilder m = (MenuBuilder) menu;
                m.setOptionalIconsVisible(true);
            }
            return true;
        }
        else
        return false;
    }

    private Toolbar.OnMenuItemClickListener toolListener=new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            final int[] row_idabovebelow = new int[2];
            switch(item.getItemId()){
                case R.id.delete:cells_temp=cells.get(posii);
                                datacell_num=posii/column;
                                cells_temp.setData("");
                                postRequest(datacell_num);
                                adapter.notifyItemChanged(posii);
                                //posii=-1;
                                break;
                case R.id.above:

                    final String[] url = {"http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/add-row-above/"};
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("rows_num",String.valueOf(rows));
                        jsonObject.put("cols_num",String.valueOf(column));
                        jsonObject.put("current_row_number",datacell_num+1);
                        jsonObject.put("doc_id",String.valueOf(id));
                        JSONArray jsonArray=new JSONArray();
                        for(int i=0;i<column;i++)jsonArray.put("");
                        jsonObject.put("data",jsonArray);
                        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url[0], jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    row_idabovebelow[0] = Integer.parseInt(response.getString("row_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        queue.add(request);
                        List<dataCell> datatemp = new ArrayList<>();
                        for(int i=0;i<dataCellArrayList.size();i++)datatemp.add(dataCellArrayList.get(i));
                        dataCellArrayList.clear();
                        for (int i=0;i<datacell_num;i++)dataCellArrayList.add(datatemp.get(i));
                        dataCellArrayList.add(new dataCell(row_idabovebelow[0]));
                        for(int i=datacell_num;i<rows;i++)dataCellArrayList.add(datatemp.get(i));
                        List<cell> temp=new ArrayList<>();
                        for(int j=0;j<rows*column;j++){
                            temp.add(new cell(cells.get(j).getData()));
                        }
                        cells.clear();
                        for(int i=0;i<(datacell_num)*column;i++)cells.add(new cell(temp.get(i).getData()));
                        for(int i=(datacell_num)*column;i<(datacell_num)*column+column;i++)cells.add(new cell(""));
                        for (int i=(datacell_num)*column+column,k=(datacell_num)*column;i<(rows+1)*column;i++){
                            if(k<rows*column)cells.add(new cell(temp.get(k).data));k++;
                        }
                        ++rows;
                        cells3.add(new cell(""+rows));
                        adapter.notifyDataSetChanged();
                        adapter3.notifyDataSetChanged();
                        //rows++;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    
                    break;
                case R.id.below://posii=-1;
                    //Toast.makeText(GetDocument.this, "Building Soon", Toast.LENGTH_SHORT).show();
                    String addurl="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/add-row-below/";
                    RequestQueue requeue = Volley.newRequestQueue(getApplicationContext());
                    JSONObject jsonObject1=new JSONObject();
                    try {
                        jsonObject1.put("rows_num",String.valueOf(rows));
                        jsonObject1.put("cols_num",String.valueOf(column));
                        jsonObject1.put("current_row_number",datacell_num+1);
                        jsonObject1.put("doc_id",String.valueOf(id));
                        JSONArray jsonArray=new JSONArray();
                        for(int i=0;i<column;i++)jsonArray.put("");
                        jsonObject1.put("data",jsonArray);
                        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, addurl, jsonObject1, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    row_idabovebelow[1] = Integer.parseInt(response.getString("row_id"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        requeue.add(request);
                        ArrayList<dataCell> tempcell = new ArrayList<>(dataCellArrayList);
                        dataCellArrayList.clear();
                        for(int i=0;i<datacell_num+1;i++)dataCellArrayList.add(tempcell.get(i));
                        dataCellArrayList.add(new dataCell(row_idabovebelow[1]));
                        for(int i=datacell_num+1;i<rows;i++)dataCellArrayList.add(tempcell.get(i));
                        List<cell> temp=new ArrayList<>();
                        for(int j=0;j<rows*column;j++){
                            temp.add(new cell(cells.get(j).getData()));
                        }
                        cells.clear();
                        for(int i=0;i<(datacell_num+1)*column;i++)cells.add(new cell(temp.get(i).getData()));
                        for(int i=(datacell_num+1)*column;i<(datacell_num+1)*column+column;i++)cells.add(new cell(""));
                        for (int i=(datacell_num+1)*column+column,k=(datacell_num+1)*column;i<(rows+1)*column;i++){
                            if(k<rows*column)cells.add(new cell(temp.get(k).data));k++;
                        }
                        ++rows;
                        cells3.add(new cell(""+rows));
                        adapter.notifyDataSetChanged();
                        adapter3.notifyDataSetChanged();
                        //rows++;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                                break;
                case R.id.copy://posii=-1;
                    String data=cells.get(posii).getData();
                                ClipboardManager cm = (ClipboardManager)GetDocument.this.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData=ClipData.newPlainText("text",data);
                    cm.setPrimaryClip(clipData);
                                //cm.setPrimaryClip();
                                            Toast.makeText(GetDocument.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                                            break;

            }
            return false;
        }
    } ;

    public void getTotal(){
        for (int i = 1; i <= column; i++) {
                cells4.add(new cell(""));
            }

        //Toast.makeText(GetDocument.this," "+cells5.size(),Toast.LENGTH_SHORT).show();
            for (int j = 0; j < cells5.size(); j++) {
                int col_num = Integer.parseInt(cells5.get(j).getData());

                //Toast.makeText(this, " "+col_num, Toast.LENGTH_SHORT).show();
                int total = 0;
                int can = 0;
                String data = null;
                for (int i = col_num; i < rows * column; i += column) {
                    data = cells.get(i).getData().trim();
                    try {
                        if (data.equals("")) continue;
                        else if (data.startsWith("\u20B9")) {
                            total += Integer.parseInt(data.substring(2));
                        } else total += Integer.parseInt(data);
                    } catch (NumberFormatException e) {
                        can = -1;
                        break;
                    }
                }
                if (can == -1) {
                    Toast.makeText(this, " Cannot be converted as numbers", Toast.LENGTH_SHORT).show();
                } else {
                    if (cellColumnArrayList.get(col_num).getColumn_type().equals("rupees"))
                        cells4.get(col_num).setData("\u20B9 " + String.valueOf(total));
                    else cells4.get(col_num).setData(String.valueOf(total));
                    //adapter4.notifyItemChanged(col_num);
                }
            }
        adapter4 = new AdapterTotal(getApplicationContext(), cells4);
        recyclerView4.setLayoutManager(new GridLayoutManager(getApplicationContext(), column));
        recyclerView4.setVisibility(View.VISIBLE);
        recyclerView4.setAdapter(adapter4);
        ViewCompat.setNestedScrollingEnabled(recyclerView4, false);

    }

    public void postRequest(int datacell_num){
        String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/update-single-data/"+id;
        try {
            dataCell tempdatacell=dataCellArrayList.get(datacell_num);
            int row_id=tempdatacell.getRow_id();
            String row=String.valueOf(row_id);
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("rows_nums", rows);
            jsonBody.put("cols_nums", column);
            jsonBody.put("id",row);
            jsonBody.put("serialno",tempdatacell.getSerialno());
            JSONArray jsonArray = new JSONArray();
            for (int i = (datacell_num)*column; i < ((datacell_num)*column)+column; i++) {
                cell tempCell = cells.get(i);
                String string = tempCell.getData();
                jsonArray.put(string);
            }
            jsonBody.put("data",jsonArray);
            JSONArray json1 = new JSONArray();
            for(int i=1;i<=column;i++){
                json1.put("10");
            }
            jsonBody.put("width",json1);
            JSONArray json2 = new JSONArray();
            for(int i=1;i<=column;i++){
                json2.put("10");
            }
            jsonBody.put("height",json2);
            //final String requestBody = jsonBody.toString();
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.PUT, url,jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(request);
            if(recyclerView4.getVisibility()==View.VISIBLE){
                for (int j = 0; j < cells5.size(); j++) {
                    int col_num = Integer.parseInt(cells5.get(j).getData());

                    //Toast.makeText(this, " "+col_num, Toast.LENGTH_SHORT).show();
                    int total = 0;
                    int can = 0;
                    String data = null;
                    for (int i = col_num; i < rows * column; i += column) {
                        data = cells.get(i).getData().trim();
                        try {
                            if (data.equals("")) continue;
                            else if (data.startsWith("\u20B9")) {
                                total += Integer.parseInt(data.substring(2));
                            } else total += Integer.parseInt(data);
                        } catch (NumberFormatException e) {
                            can = -1;
                            break;
                        }
                    }
                    if (can == -1) {
                        Toast.makeText(this, " Cannot be converted as numbers", Toast.LENGTH_SHORT).show();
                    } else {
                        if (cellColumnArrayList.get(col_num).getColumn_type().equals("rupees"))
                            cells4.get(col_num).setData("\u20B9 " + String.valueOf(total));
                        else cells4.get(col_num).setData(String.valueOf(total));
                        //adapter4.notifyItemChanged(col_num);
                    }
                }
                adapter4.notifyDataSetChanged();
            }
        }
        catch (JSONException e){
            Toast.makeText(GetDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    public void extract(){
        //cells.clear();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        rowbut.setVisibility(GONE);
        colbut.setVisibility(GONE);
        dataCellArrayList=new ArrayList<>();
        String JsonURL="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-document-serial/"+id;
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.GET, JsonURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    //progressDialog.dismiss();

                    JSONArray array=response.getJSONArray("data");
                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject=array.getJSONObject(i);
                        //String data=jsonObject.getString("data");
                        dataCell datacell=new dataCell(jsonObject.getInt("id"),jsonObject.getString("column1"),jsonObject.getString("column2"),jsonObject.getString("column3"),jsonObject.getString("column4"),jsonObject.getString("column5"),jsonObject.getString("column6"),jsonObject.getString("column7"),jsonObject.getString("column8"),jsonObject.getString("column9"),jsonObject.getString("column10"),jsonObject.getString("column11"),jsonObject.getString("column12"),jsonObject.getString("column13"),jsonObject.getString("column14"),jsonObject.getString("column15"),jsonObject.getString("column16"),jsonObject.getString("column17"),jsonObject.getString("column18"),jsonObject.getString("column19"),jsonObject.getString("column20"),jsonObject.getString("height"),jsonObject.getString("width"),jsonObject.getInt("serialno"));
                        dataCellArrayList.add(datacell);
                        //cell.setData(array.getString(i));
                        //Toast.makeText(MainActivity.this, rows+column, Toast.LENGTH_SHORT).show();
                        //cells.add(cell);
                        if(datacell.getColumn1()!="null")
                            cells.add(new cell(datacell.getColumn1()));
                        if(datacell.getColumn2()!="null") cells.add(new cell(datacell.getColumn2()));
                        if(datacell.getColumn3()!="null")
                            cells.add(new cell(datacell.getColumn3()));
                        if(datacell.getColumn4()!="null")cells.add(new cell(datacell.getColumn4()));
                        if(datacell.getColumn5()!="null")cells.add(new cell(datacell.getColumn5()));
                        if(datacell.getColumn6()!="null")cells.add(new cell(datacell.getColumn6()));
                        if(datacell.getColumn7()!="null")cells.add(new cell(datacell.getColumn7()));
                        if(datacell.getColumn8()!="null")cells.add(new cell(datacell.getColumn8()));
                        if(datacell.getColumn9()!="null")cells.add(new cell(datacell.getColumn9()));
                        if(datacell.getColumn10()!="null")cells.add(new cell(datacell.getColumn10()));
                        if(datacell.getColumn11()!="null")cells.add(new cell(datacell.getColumn11()));
                        if(datacell.getColumn12()!="null")cells.add(new cell(datacell.getColumn12()));
                        if(datacell.getColumn13()!="null")cells.add(new cell(datacell.getColumn13()));
                        if(datacell.getColumn14()!="null")cells.add(new cell(datacell.getColumn14()));
                        if(datacell.getColumn15()!="null")cells.add(new cell(datacell.getColumn15()));
                        if(datacell.getColumn16()!="null")cells.add(new cell(datacell.getColumn16()));
                        if(datacell.getColumn17()!="null")cells.add(new cell(datacell.getColumn17()));
                        if(datacell.getColumn18()!="null")cells.add(new cell(datacell.getColumn18()));
                        if(datacell.getColumn19()!="null")cells.add(new cell(datacell.getColumn19()));
                        if(datacell.getColumn20()!="null")cells.add(new cell(datacell.getColumn20()));


                    }

                    String urlcol="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-column-data/"+id;
                    RequestQueue Queue=Volley.newRequestQueue(getApplicationContext());
                    cellColumnArrayList=new ArrayList<>();
                    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, urlcol, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONArray arraycol=response.getJSONArray("data");
                                JSONObject jsonObject1=arraycol.getJSONObject(0);
                                String row=jsonObject1.getString("row_nums");
                                rows=Integer.parseInt(row);
                                String c=jsonObject1.getString("column_nums");
                                column=Integer.parseInt(c);
                                for(int i=0;i<arraycol.length();i++) {
                                    JSONObject jsonObjectcol = arraycol.getJSONObject(i);
                                    cellColumn cellcol=new cellColumn(jsonObjectcol.getInt("id"),jsonObjectcol.getString("column_names"),jsonObjectcol.getString("column_type"));
                                    cellColumnArrayList.add(cellcol);
                                    cells2.add(new cell(cellcol.getColumn_name()));
                                }
                                adapter=new Adapter(getApplicationContext(),cells);
                                //adapter.notify();
                                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),column));
                                recyclerView.setAdapter(adapter);
                                ViewCompat.setNestedScrollingEnabled(recyclerView, false);
                                cells3.add(new cell(""));
                                for(int i=1;i<=(rows);i++){
                                    cells3.add(new cell(""+i));
                                }
                                progressDialog.dismiss();
                                rowbut.setVisibility(View.VISIBLE);
                                colbut.setVisibility(View.VISIBLE);
                                adapter3=new AdapterTitleRow(getApplicationContext(),cells3);
                                recyclerView3.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                                recyclerView3.setAdapter(adapter3);
                                ViewCompat.setNestedScrollingEnabled(recyclerView3, false);

                                recyclerView2.setLayoutManager(new GridLayoutManager(getApplicationContext(),column));
                                recyclerView2.setAdapter(adapter2);
                                ViewCompat.setNestedScrollingEnabled(recyclerView2, false);
                                click();
                                if(setting==-1){
                                    cells5.add(new cell(String.valueOf(col_num),Integer.parseInt(id)));
                                    getTotal();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(GetDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(GetDocument.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    Queue.add(jsonObjectRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(GetDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tagb", "onErrorResponse: "+ error.getMessage());
                Toast.makeText(GetDocument.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(objectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(GetDocument.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

   /* @Override
    public boolean onSupportNavigateUp()
    {
        Intent intent=new Intent(GetDocument.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        return true;
    }*/


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //Toast.makeText(GetDocument.this, "beforeText", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //Toast.makeText(GetDocument.this, ""+charSequence, Toast.LENGTH_SHORT).show();
        //if(charSequence.length()!=0)
        if (cellColumnArrayList.get(column_id).getColumn_type().equals("rupees")){
            String data=""+charSequence;
            if(data.startsWith("\u20B9"))cells_temp.setData(data);
            else cells_temp.setData("\u20B9 "+data);
        }
        else cells_temp.setData(""+charSequence);
            adapter.notifyItemChanged(pos);
            postRequest(datacell_num);


    }

    @Override
    public void afterTextChanged(Editable editable) {
        //Toast.makeText(GetDocument.this, "afterText", Toast.LENGTH_SHORT).show();
        //cells_temp.setData(""+editable);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        onBackPressed();
        return true;
    }
}