package ds.docusheet.table;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class TemplateDocument extends AppCompatActivity {
    int temp_num,cat_num,temp_pos;
    List<cell> cells,cells2,cells3,cells4,cells5;
    int rows=5,column=5,height=50,col_num,setting;
    Button use;
    String id,doc_name,heading;
    RecyclerView recyclerView,recyclerView2,recyclerView3,recyclerView4;
    static RelativeLayout relativeLayout;
    Adapter adapter;
    AdapterTitle adapter2;
    AdapterTitleRow adapter3;
    AdapterTotal adapter4;
    FirebaseAuth mAuth;
    ArrayList<dataCell> dataCellArrayList;
    ArrayList<cellColumn> cellColumnArrayList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_document);
        mAuth=FirebaseAuth.getInstance();
        heading = getIntent().getStringExtra("heading").toString();
        cat_num=getIntent().getIntExtra("cat_num",-1);
        temp_num=getIntent().getIntExtra("temp_num",-1);
        //temp_pos=cat_num+temp_num;
        //Toast.makeText(this, cat_num+" "+temp_num, Toast.LENGTH_SHORT).show();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(heading);

        mAuth=FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.recycle1);

        recyclerView2=findViewById(R.id.recycler2);
        recyclerView3=findViewById(R.id.recycle3);
        recyclerView4=findViewById(R.id.recycler4);
        use=findViewById(R.id.use);

        cells=new ArrayList<>();
        cells2=new ArrayList<>();
        cells3=new ArrayList<>();
        cells4=new ArrayList<>();
        cells5=new ArrayList<>();

        layout();


    }

    public void saving(){
        String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/save-document-create";
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("rows_num", rows);
            jsonBody.put("cols_num", column);
            jsonBody.put("user_id",mAuth.getUid());
            jsonBody.put("document_name",heading);
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
            for (int i = 0; i < cells5.size(); i++){
                cell tempCell = cells5.get(i);
                String string = tempCell.getData();
                json4.put(string);
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
                        Intent intent=new Intent(TemplateDocument.this,GetDocument.class);
                        intent.putExtra("doc_id",id);
                        intent.putExtra("doc_name",heading);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(TemplateDocument.this, " "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(request);
            //getID();
            //Toast.makeText(NewDocument.this, id, Toast.LENGTH_SHORT).show();



        }
        catch (JSONException e){
            Toast.makeText(TemplateDocument.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void layout(){
        if(temp_num==0){
            if(cat_num==0){
                cells2.add(new cell("Cash In"));
                cells2.add(new cell("Cash Out"));
                cells2.add(new cell("Date"));
                cells2.add(new cell("Remark"));
                cells5.add(new cell("rupees"));cells5.add(new cell("rupees"));cells5.add(new cell("date"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("\u20B9 100"));cells.add(new cell(" "));cells.add(new cell("16/01/2021"));cells.add(new cell("Tea"));
                cells.add(new cell(" "));cells.add(new cell("\u20B9 150"));cells.add(new cell("16/01/2021"));cells.add(new cell("Luggage"));
                for(int i=9;i<=20;i++)cells.add(new cell(""));
            }
            else if(cat_num==1){
                cells2.add(new cell("Date"));
                cells2.add(new cell("Payment Mode"));
                cells2.add(new cell("Amount"));
                cells2.add(new cell("Mobile Number"));
                cells5.add(new cell("date"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("phone"));
                column=4;
                cells.add(new cell("16/01/2021"));cells.add(new cell("PhonePay"));cells.add(new cell("\u20B9 1000"));cells.add(new cell("9999999999"));
                cells.add(new cell("16/01/2021"));cells.add(new cell("Paytm"));cells.add(new cell("\u20B9 2500"));cells.add(new cell("9999999999"));
                cells.add(new cell("16/01/2021"));cells.add(new cell("Cash"));cells.add(new cell("\u20B9 1090"));cells.add(new cell("9999999999"));
                for(int i=13;i<=20;i++)cells.add(new cell(""));

            }
            else if(cat_num==2){
                cells2.add(new cell("Date"));
                cells2.add(new cell("Item"));
                cells2.add(new cell("Amount"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("date"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("16/01/2021"));cells.add(new cell("Transport"));cells.add(new cell("\u20B9 1000"));cells.add(new cell("Delhi transport"));
                cells.add(new cell("16/01/2021"));cells.add(new cell("Goods Purchasing"));cells.add(new cell("\u20B9 2500"));cells.add(new cell("Various Bills"));
                cells.add(new cell("16/01/2021"));cells.add(new cell("Broadband"));cells.add(new cell("\u20B9 1090"));cells.add(new cell("Paid on PhonePe"));
                for(int i=13;i<=20;i++)cells.add(new cell(""));
            }
            else if(cat_num==3){
                cells2.add(new cell("Date of Order"));
                cells2.add(new cell("Name"));
                cells2.add(new cell("Item"));
                cells2.add(new cell("Quantity(in Kg)"));
                cells2.add(new cell("Address"));
                cells2.add(new cell("Payment Received"));
                cells2.add(new cell("Order Received"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("date"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=8;
                cells.add(new cell("16/01/2021"));cells.add(new cell("Karan"));cells.add(new cell("Cement"));cells.add(new cell("2"));cells.add(new cell("Jaipur Rd"));cells.add(new cell("Yes"));cells.add(new cell("Yes"));cells.add(new cell("-"));
                //cells.add(new cell("16/01/2021"));cells.add(new cell("Abdul"));cells.add(new cell(""));cells.add(new cell("Various Bills"));
                for(int i=9;i<=40;i++)cells.add(new cell(""));
            }
            else if(cat_num==4){
                cells2.add(new cell("Date"));
                cells2.add(new cell("Item"));
                cells2.add(new cell("Quantity"));
                cells2.add(new cell("Amount"));
                cells2.add(new cell("Payment type"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("date"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=6;
                cells.add(new cell("31/1/2021"));cells.add(new cell("Cement"));cells.add(new cell("2 Bags"));cells.add(new cell("\u20B9 600"));cells.add(new cell("Cash"));cells.add(new cell("Deliver Tomorrow"));
                for(int i=7;i<=30;i++)cells.add(new cell(""));
            }
        }
        else if(temp_num==1){
            if(cat_num==0){
                cells2.add(new cell("Item"));
                cells2.add(new cell("Quantity"));
                cells2.add(new cell("Purchased"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("Tea"));cells.add(new cell("1 kg"));cells.add(new cell("Yes"));cells.add(new cell(""));
                cells.add(new cell("Red Chilli Powder"));cells.add(new cell("500 g"));cells.add(new cell("Yes"));cells.add(new cell("Buy the same"));
                cells.add(new cell("Sugar"));cells.add(new cell("2 kg"));cells.add(new cell("No"));cells.add(new cell("Immediate need"));
                for(int i=13;i<=20;i++)cells.add(new cell(""));
            }
            else if(cat_num==1){
                cells2.add(new cell("Date"));
                cells2.add(new cell("Item"));
                cells2.add(new cell("Amount"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("date"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("16/01/2021"));cells.add(new cell("Transport"));cells.add(new cell("\u20B9 1000"));cells.add(new cell("Delhi transport"));
                cells.add(new cell("16/01/2021"));cells.add(new cell("Goods Purchasing"));cells.add(new cell("\u20B9 2500"));cells.add(new cell("Various Bills"));
                cells.add(new cell("16/01/2021"));cells.add(new cell("Broadband"));cells.add(new cell("\u20B9 1090"));cells.add(new cell("Paid on PhonePe"));
                for(int i=13;i<=20;i++)cells.add(new cell(""));
            }
        }
        else if(temp_num==2){
            if(cat_num==0){
                cells2.add(new cell("Student Name"));
                cells2.add(new cell("Father Name"));
                cells2.add(new cell("Course"));
                cells2.add(new cell("Payment Received"));
                cells2.add(new cell("Payment Pending"));
                cells2.add(new cell("Date"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("rupees"));cells5.add(new cell("date"));
                column=6;
                cells.add(new cell("Sheela Singh"));cells.add(new cell("Mukesh Singh"));cells.add(new cell("8th Class"));cells.add(new cell("\u20B9 2500"));cells.add(new cell("\u20B9 500"));cells.add(new cell("16/01/2021"));
                cells.add(new cell("Karan Ram"));cells.add(new cell("Diwakar Ram"));cells.add(new cell("8th Class"));cells.add(new cell("\u20B9 2700"));cells.add(new cell("\u20B9 300"));cells.add(new cell("16/01/2021"));
                cells.add(new cell("Vaibav Kumar"));cells.add(new cell("Mayank Kumar"));cells.add(new cell("8th Class"));cells.add(new cell("\u20B9 2400"));cells.add(new cell("\u20B9 600"));cells.add(new cell("16/01/2021"));
                for(int i=19;i<=30;i++)cells.add(new cell(""));
            }
            else if(cat_num==1){
                cells2.add(new cell("Item Name"));
                cells2.add(new cell("Quantity"));
                cells2.add(new cell("Order Placed"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=3;
                cells.add(new cell("Science Books"));cells.add(new cell("120"));cells.add(new cell("Yes"));
                cells.add(new cell("Notebooks"));cells.add(new cell("150"));cells.add(new cell("No"));
                cells.add(new cell("Pencils"));cells.add(new cell("200"));cells.add(new cell("Yes"));
                for(int i=10;i<=15;i++)cells.add(new cell(""));
            }
            else if(cat_num==2){
                cells2.add(new cell("Name"));
                cells2.add(new cell("First Exam"));
                cells2.add(new cell("Second Exam"));
                cells2.add(new cell("Half-Year"));
                cells2.add(new cell("Annual"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=5;
                cells.add(new cell("Mayank"));cells.add(new cell("90.5%"));cells.add(new cell("95.4%"));cells.add(new cell("93%"));cells.add(new cell("93%"));
                cells.add(new cell("Karan"));cells.add(new cell("80.5%"));cells.add(new cell("85.4%"));cells.add(new cell("83%"));cells.add(new cell("85.8%"));
                cells.add(new cell("Nitin"));cells.add(new cell("94.5%"));cells.add(new cell("93.4%"));cells.add(new cell("96%"));cells.add(new cell("96.8%"));
                for(int i=16;i<=25;i++)cells.add(new cell(""));
            }
        }
        else if(temp_num==3){
            if(cat_num==0){
                cells2.add(new cell("Topic"));
                cells2.add(new cell("Book"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=3;
                cells.add(new cell("Basic Formulae"));cells.add(new cell("Maths"));cells.add(new cell("Revise class work"));
                cells.add(new cell("Page 80 in Chapter 6"));cells.add(new cell("Science"));cells.add(new cell("Solve example questions"));
                cells.add(new cell("Ramnath poetries"));cells.add(new cell("Hindi"));cells.add(new cell("Practice learning"));
                for(int i=10;i<=15;i++)cells.add(new cell(""));
            }
            else if(cat_num==1){
                cells2.add(new cell("Subject"));
                cells2.add(new cell("Date"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("text"));cells5.add(new cell("date"));cells5.add(new cell("text"));
                column=3;
                cells.add(new cell("English"));cells.add(new cell("23/1/2021"));cells.add(new cell("Learn 3 poems"));
                cells.add(new cell("Maths"));cells.add(new cell("23/1/2021"));cells.add(new cell("Revise Q1-Q5 in C-2"));
                cells.add(new cell("Science"));cells.add(new cell("23/1/2021"));cells.add(new cell("Revise Pg-16 in C-3"));
                for(int i=10;i<=15;i++)cells.add(new cell(""));

            }
        }
        else if(temp_num==4){
            if(cat_num==0){
                cells2.add(new cell("Item"));
                cells2.add(new cell("Type"));
                cells2.add(new cell("Quantity"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("Boiled green veg"));cells.add(new cell("fresh"));cells.add(new cell("200 gm"));cells.add(new cell("boil mildly"));
                cells.add(new cell("Dry fruits"));cells.add(new cell("any"));cells.add(new cell("20 pieces"));cells.add(new cell("eat daily"));
                cells.add(new cell("Chapati"));cells.add(new cell("mix grain"));cells.add(new cell("3 daily"));cells.add(new cell("1 at a time"));
                for(int i=13;i<=20;i++)cells.add(new cell(""));
            }
            else if(cat_num==1){
                cells2.add(new cell("Exercise"));
                cells2.add(new cell("Repetitions"));
                cells2.add(new cell("Sets"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("Squats"));cells.add(new cell("10"));cells.add(new cell("2"));cells.add(new cell("daily"));
                cells.add(new cell("Pushups"));cells.add(new cell("10"));cells.add(new cell("2"));cells.add(new cell("alternate day"));
                cells.add(new cell("Cycling"));cells.add(new cell("5 Minutes"));cells.add(new cell("2"));cells.add(new cell("daily"));
                for(int i=13;i<=20;i++)cells.add(new cell(""));
            }
        }
        else if(temp_num==5){
            if(cat_num==0){
                cells2.add(new cell("Pick Up"));
                cells2.add(new cell("Drop"));
                cells2.add(new cell("Amount"));
                cells2.add(new cell("Payment Mode"));
                cells2.add(new cell("Time"));
                cells2.add(new cell("Date"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("text"));cells5.add(new cell("time"));cells5.add(new cell("date"));
                column=6;
                cells.add(new cell("Dhola Kuan"));cells.add(new cell("Haus Khas"));cells.add(new cell("\u20B9 200"));cells.add(new cell("Online"));cells.add(new cell("5:00 pm"));cells.add(new cell("23/1/2021"));
                cells.add(new cell("Haus Khas"));cells.add(new cell("Green Park"));cells.add(new cell("\u20B9 500"));cells.add(new cell("Online"));cells.add(new cell("6:00 pm"));cells.add(new cell("23/1/2021"));
                cells.add(new cell("Haus Khas"));cells.add(new cell(" Vasant Kunj"));cells.add(new cell("\u20B9 350"));cells.add(new cell("Online"));cells.add(new cell("6:40 pm"));cells.add(new cell("23/1/2021"));
                for(int i=19;i<=30;i++)cells.add(new cell(""));
            }
            else if(cat_num==1){
                cells2.add(new cell("Date"));
                cells2.add(new cell("Work"));
                cells2.add(new cell("Amount"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("date"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("21/01/2021"));cells.add(new cell("Engine service"));cells.add(new cell("\u20B9 6000"));cells.add(new cell("Service/5000 km"));
                cells.add(new cell("22/01/2021"));cells.add(new cell("Tyre change"));cells.add(new cell("\u20B9 8000"));cells.add(new cell(""));
                for(int i=9;i<=20;i++)cells.add(new cell(""));
            }
        }
        else if(temp_num==6){
            if(cat_num==0){
                cells2.add(new cell("Name"));
                cells2.add(new cell("No of people"));
                cells2.add(new cell("Invite Sent"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("Ravi"));cells.add(new cell("1"));cells.add(new cell("Yes"));cells.add(new cell("will reach on same day"));
                cells.add(new cell("Mayank"));cells.add(new cell("2"));cells.add(new cell("Yes"));cells.add(new cell("Not sure"));
                cells.add(new cell("Utkarsh"));cells.add(new cell("1"));cells.add(new cell("No"));cells.add(new cell(" not decided"));
                for(int i=9;i<=20;i++)cells.add(new cell(""));
            }
            else if(cat_num==1){
                cells2.add(new cell("Name"));
                cells2.add(new cell("Type"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=3;
                cells.add(new cell("Rabdi Jamun"));cells.add(new cell("fresh Rabdi"));cells.add(new cell(""));
                cells.add(new cell("Paneer"));cells.add(new cell("Tomato gravy"));cells.add(new cell("use the best quality"));
                cells.add(new cell("Chapati"));cells.add(new cell("Tandoori"));cells.add(new cell("use 3 Tandoor Bhatti"));
                for(int i=10;i<=15;i++)cells.add(new cell(""));
            }
            else if(cat_num==2){
                cells2.add(new cell("Date"));
                cells2.add(new cell("Item"));
                cells2.add(new cell("Amount"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("date"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("21/01/2021"));cells.add(new cell("Shervani"));cells.add(new cell("On Rent"));cells.add(new cell("return by next day"));
                cells.add(new cell("22/01/2021"));cells.add(new cell("Hotel stay for guests"));cells.add(new cell("\u20B9 1500"));cells.add(new cell("per room/day"));
                cells.add(new cell("22/01/2021"));cells.add(new cell("Catering"));cells.add(new cell("\u20B9 80000"));cells.add(new cell(""));
                for(int i=9;i<=20;i++)cells.add(new cell(""));
            }
        }

        cells3.add(new cell(""));
        for(int i=1;i<=(rows);i++){
            cells3.add(new cell(""+i));
        }
        adapter3=new AdapterTitleRow(getApplicationContext(),cells3);
        recyclerView3.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recyclerView3.setAdapter(adapter3);
        ViewCompat.setNestedScrollingEnabled(recyclerView3, false);

        adapter2=new AdapterTitle(getApplicationContext(),cells2);
        recyclerView2.setLayoutManager(new GridLayoutManager(getApplicationContext(),column));
        recyclerView2.setAdapter(adapter2);
        ViewCompat.setNestedScrollingEnabled(recyclerView2, false);

        adapter=new Adapter(getApplicationContext(),cells);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),column));
        recyclerView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                use.setEnabled(false);
                saving();
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