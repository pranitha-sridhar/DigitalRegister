package ds.docusheet.table;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ds.docusheet.table.GetDocument.cellColumnArrayList;
import static ds.docusheet.table.GetDocument.cells;
import static ds.docusheet.table.GetDocument.column;
import static ds.docusheet.table.GetDocument.rows;

public class formulae extends AppCompatActivity {
Button buttonadd,buttonsub, buttondiv,buttonx,buttondeci,buttoncol,buttonC;
Button button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
Button save;
String doc_id,doc_name,col_num;
TextView edittext;
LinearLayout calc;
FrameLayout frame;
ListView listView;
ListAdapter adapter;
ArrayList<String> listItem,listIt,operator;
List<Integer>[] lists=new List[10];
int listno=0;
boolean muldiv=false;
ImageView back;
List<Integer> result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulae);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Set Formula");
        actionBar.show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Arrays.setAll(lists,ArrayList::new);
        }

        doc_id = getIntent().getStringExtra("doc_id");
        doc_name = getIntent().getStringExtra("doc_name");
        col_num=getIntent().getStringExtra("column_num");
        listView=findViewById(R.id.listview);
        back=findViewById(R.id.back);
        buttoncol=findViewById(R.id.buttoncol);
        button0=findViewById(R.id.button0);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        button4=findViewById(R.id.button4);
        button5=findViewById(R.id.button5);
        button6=findViewById(R.id.button6);
        button7=findViewById(R.id.button7);
        button8=findViewById(R.id.button8);
        button9=findViewById(R.id.button9);
        buttondeci=findViewById(R.id.buttondeci);
        buttonx=findViewById(R.id.buttonx);
        buttonadd=findViewById(R.id.buttonadd);
        buttonsub=findViewById(R.id.buttonsub);
        buttondiv=findViewById(R.id.buttondiv);
        buttonC=findViewById(R.id.buttonC);
        edittext=findViewById(R.id.editText3);
        save=findViewById(R.id.save_formulae);
        calc=findViewById(R.id.calc);
        frame=findViewById(R.id.frame);

        result=new ArrayList<>();
        listIt=new ArrayList<>();
        operator=new ArrayList<>();
        operator.add("+");
        listno=0;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calc.setVisibility(View.VISIBLE);
                frame.setVisibility(View.GONE);
            }
        });

        buttoncol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calc.setVisibility(View.GONE);
                frame.setVisibility(View.VISIBLE);
                listItem=new ArrayList<>();
                for(int i=0;i<cellColumnArrayList.size();i++){
                    listItem.add(cellColumnArrayList.get(i).getColumn_name());
                }
                adapter=new ColAdapter(formulae.this,listItem);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        edittext.setText(""+edittext.getText().toString()+cellColumnArrayList.get(i).getColumn_name());
                        calc.setVisibility(View.VISIBLE);
                        frame.setVisibility(View.GONE);
                        if(muldiv){
                            int can=0;
                            muldiv=false;
                            //Toast.makeText(formulae.this, ""+operator.get(operator.size()-1), Toast.LENGTH_SHORT).show();
                                if(listIt.get(listIt.size()-1).startsWith("c")){
                                    int col=Integer.parseInt(listIt.get(listIt.size()-1).substring(1));
                                    String data = null,data1=null;
                                    for (int m = i,p=col; m < GetDocument.rows * GetDocument.column && p<rows*column; m += GetDocument.column,p+=column) {
                                        data = GetDocument.cells.get(m).getData().trim();
                                        data1=cells.get(p).getData().trim();
                                        try {
                                            if (data.equals("") || data.equals(""))  {data="0";data1="0";}
                                            if (operator.get(operator.size()-1).equals("*")) {
                                                //Toast.makeText(formulae.this, "" +listno, Toast.LENGTH_SHORT).show();
                                                lists[listno].add(Integer.parseInt(data) * Integer.parseInt(data1));
                                                //listno++;
                                            }
                                            else{
                                                //Toast.makeText(formulae.this, ""+(Integer.parseInt(data)/Integer.parseInt(data1)), Toast.LENGTH_SHORT).show();
                                                lists[listno].add(Integer.parseInt(data)/Integer.parseInt(data1));
                                                //listno++;
                                            }
                                            //listno++;
                                        } catch (NumberFormatException e) {
                                            can = -1;
                                            break;
                                        }
                                    }
                                    listno++;
                                }
                                else if(listIt.get(listIt.size()-1).startsWith("list")){
                                    ArrayList<Integer> temp=new ArrayList<>(lists[(listno-1)]);
                                    lists[listno-1].clear();
                                    String data = null;
                                    for (int m = i,r=0; m < rows * column&& r<temp.size(); m += column,r++) {
                                        data = GetDocument.cells.get(m).getData().trim();
                                        try {
                                            if (data.equals("")) data="0";
                                            if (operator.get(operator.size()-1).equals("*")) {
                                                //Toast.makeText(formulae.this, "" + (Integer.parseInt(data) * Integer.parseInt(listIt.get(listIt.size() - 1))), Toast.LENGTH_SHORT).show();
                                                lists[listno-1].add(Integer.parseInt(data) * temp.get(r));

                                            }
                                            else{
                                                //Toast.makeText(formulae.this, ""+(Integer.parseInt(data)/Integer.parseInt(listIt.get(listIt.size()-1))), Toast.LENGTH_SHORT).show();
                                                lists[listno-1].add(temp.get(r)/Integer.parseInt(data));

                                            }
                                            //listno++;
                                        } catch (NumberFormatException e) {
                                            can = -1;
                                            break;
                                        }
                                    }

                                }
                                else{
                                    String data = null;
                                    for (int m = i; m < GetDocument.rows * GetDocument.column; m += GetDocument.column) {
                                        data = GetDocument.cells.get(m).getData().trim();
                                        try {
                                            if (data.equals("")) data="0";
                                            if (operator.get(operator.size()-1).equals("*")) {
                                                //Toast.makeText(formulae.this, "" + (Integer.parseInt(data) * Integer.parseInt(listIt.get(listIt.size() - 1))), Toast.LENGTH_SHORT).show();
                                                lists[listno].add(Integer.parseInt(data) * Integer.parseInt(listIt.get(listIt.size() - 1)));

                                            }
                                            else{
                                                //Toast.makeText(formulae.this, ""+(Integer.parseInt(data)/Integer.parseInt(listIt.get(listIt.size()-1))), Toast.LENGTH_SHORT).show();
                                                lists[listno].add(Integer.parseInt(data)/Integer.parseInt(listIt.get(listIt.size()-1)));

                                            }
                                            //listno++;
                                        } catch (NumberFormatException e) {
                                            can = -1;
                                            break;
                                        }
                                    }
                                    listno++;
                                }
                            operator.remove(operator.size()-1);
                            listIt.remove(listIt.size()-1);
                            listIt.add("list"+(listno-1));
                        }
                        else{
                            listIt.add("c"+i);
                        }
                    }
                });
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+"0");
                calc_num(Integer.parseInt("0"));
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+"1");
                calc_num(Integer.parseInt("1"));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+"2");
                calc_num(Integer.parseInt("2"));
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+"3");
                calc_num(Integer.parseInt("3"));

            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+"4");
                calc_num(Integer.parseInt("4"));
            }
        });button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+"5");
                calc_num(Integer.parseInt("5"));
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+"6");
                calc_num(Integer.parseInt("6"));
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+"7");
                calc_num(Integer.parseInt("7"));
            }
        });button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+"8");
                calc_num(Integer.parseInt("8"));
            }
        });button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+"9");
                calc_num(Integer.parseInt("9"));
            }
        });
        buttondeci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+".");
            }
        });
        buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+"+");
                operator.add("+");
            }
        });
        buttonsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+"-");
                operator.add("-");
            }
        });
        buttonx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+"*");
                muldiv=true;
                operator.add("*");
                //Toast.makeText(formulae.this, ""+muldiv, Toast.LENGTH_SHORT).show();
            }
        });
        buttondiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edittext.getText().toString();
                edittext.setText(text+"/");
                muldiv=true;
                operator.add("/");
            }
        });
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edittext.setText("");
                result.clear();
                operator.clear();
                listIt.clear();
                for(int i=0;i<listno;i++)lists[i].clear();
                listno=0;
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setEnabled(false);
                //Toast.makeText(formulae.this, "Building in progress", Toast.LENGTH_SHORT).show();
                int operpos = 0, listpos = 0;
                for (int i = 0; i < rows; i++) result.add(0);

                //Toast.makeText(formulae.this, "" + operator.size() + " " + listIt.size(), Toast.LENGTH_SHORT).show();

                //if(operator.size()>=2)
                {
                    for (int i = 0, q = 0, lipos = 0; i < listIt.size() && q < operator.size(); i++, q++) {
                        String op = operator.get(q);
                        if (listIt.get(i).startsWith("list")) {
                            if (op.equals("+")) {
                                for (int j = 0, k = 0; j < rows; j++) {
                                    result.set(j, result.get(j) + lists[lipos].get(j));
                                }
                            } else {
                                for (int j = 0; j < rows; j++) {
                                    result.set(j, result.get(j) - lists[lipos].get(j));
                                }
                            }
                            lipos++;
                        } else if (listIt.get(i).startsWith("c")) {
                            int col = Integer.parseInt(listIt.get(i).substring(1));
                            String data1 = null;
                            for (int p = col, j = 0; p < rows * column && j < rows; p += column, j++) {
                                data1 = cells.get(p).getData().trim();
                                try {
                                    if (data1.equals("")) data1 = "0";
                                    if (op.equals("+")) {
                                        result.set(j, result.get(j) + Integer.parseInt(data1));
                                    } else {
                                        result.set(j, result.get(j) - Integer.parseInt(data1));
                                    }
                                } catch (NumberFormatException e) {
                                    //can = -1;
                                    break;
                                }
                            }
                        } else {
                            if (op.equals("+")) {
                                for (int j = 0; j < rows; j++) {
                                    result.set(j, result.get(j) + Integer.parseInt(listIt.get(i)));
                                }

                            } else {
                                for (int j = 0; j < rows; j++)
                                    result.set(j, result.get(j) - Integer.parseInt(listIt.get(i)));
                            }
                        }
                        //operpos++;
                    }
                }


                String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/change-column-data";
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                try {
                    JSONObject object = new JSONObject();
                    object.put("current_col_number", col_num);
                    object.put("doc_id", String.valueOf(doc_id));
                    JSONArray jsonArray=new JSONArray();
                    for(int i=0;i<result.size();i++)
                        jsonArray.put(result.get(i));
                    object.put("data",jsonArray);
                    //Toast.makeText(ColumnClicked.this, object.toString(), Toast.LENGTH_SHORT).show();
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, object, new Response.Listener<JSONObject>() {
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
                    result.clear();
                    operator.clear();
                    listIt.clear();
                    for (int i = 0; i < listno; i++) lists[i].clear();
                    listno = 0;

                Intent intent=new Intent(formulae.this,GetDocument.class);
                intent.putExtra("doc_id",doc_id);
                intent.putExtra("doc_name",doc_name);
                finish();
                startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(ColumnClicked.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void calc_num(int num){
        if(muldiv){
            if(listIt.get(listIt.size()-1).startsWith("c")){
                int col=Integer.parseInt(listIt.get(listIt.size()-1).substring(1));
                String data1=null;
                for (int p=col; p<rows*column;p+=column) {
                    data1=cells.get(p).getData().trim();
                    try {
                        if (data1.equals("")) data1="0";
                        if (operator.get(operator.size()-1).equals("*")) {
                            //Toast.makeText(formulae.this, "" + (Integer.parseInt(data1)*num), Toast.LENGTH_SHORT).show();
                            lists[listno].add(Integer.parseInt(data1)*num);
                        }
                        else{
                            //Toast.makeText(formulae.this, "" + (Integer.parseInt(data1)/num), Toast.LENGTH_SHORT).show();
                            lists[listno].add(Integer.parseInt(data1)/num);
                        }

                    } catch (NumberFormatException e) {
                        //can = -1;
                        break;
                    }
                }
                listno++;
                operator.remove(operator.size()-1);
                listIt.remove(listIt.size()-1);
                listIt.add("list"+(listno-1));
            }
            else if(listIt.get(listIt.size()-1).startsWith("list")){
                ArrayList<Integer> temp=new ArrayList<>(lists[(listno-1)]);
                lists[listno-1].clear();
                String data = String.valueOf(num);
                for (int r=0; r<temp.size(); r++) {
                    try {
                        if (data.equals("")) data="0";
                        if (operator.get(operator.size()-1).equals("*")) {
                            //Toast.makeText(formulae.this, "" + (Integer.parseInt(data) * Integer.parseInt(listIt.get(listIt.size() - 1))), Toast.LENGTH_SHORT).show();
                            lists[listno-1].add(Integer.parseInt(data) * temp.get(r));

                        }
                        else{
                            //Toast.makeText(formulae.this, ""+(Integer.parseInt(data)/Integer.parseInt(listIt.get(listIt.size()-1))), Toast.LENGTH_SHORT).show();
                            lists[listno-1].add(temp.get(r)/Integer.parseInt(data));

                        }
                        //listno++;
                    } catch (NumberFormatException e) {
                        //can = -1;
                        break;
                    }
                }
            }
            else{
                String data = listIt.get(listIt.size()-1);
                if (operator.get(operator.size()-1).equals("*")) {
                    //Toast.makeText(formulae.this, "" + (Integer.parseInt(data)*num), Toast.LENGTH_SHORT).show();
                    operator.remove(operator.size()-1);
                    listIt.remove(listIt.size()-1);
                    listIt.add(String.valueOf(Integer.parseInt(data)*num));
                }
                else{
                    //Toast.makeText(formulae.this, "" + (Integer.parseInt(data)/num), Toast.LENGTH_SHORT).show();
                    operator.remove(operator.size()-1);
                    listIt.remove(listIt.size()-1);
                    listIt.add(String.valueOf(Integer.parseInt(data)/num));
                }
                //listno++;


            }

        }
        else{
            listIt.add(String.valueOf(num));
        }
    }
}
class ColAdapter extends BaseAdapter {
    Context context;
    private ArrayList<String> listItem;

    public ColAdapter(Context context, ArrayList<String> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @Override
    public int getCount() {
        return listItem.size();
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
            convertView = inflater.inflate(R.layout.columnmenuitem, parent, false);
            viewHolder.listname = convertView.findViewById(R.id.textItem);
            result = convertView;
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.listname.setText(listItem.get(position));
        return convertView;
    }

    public static class ViewHolder {
        TextView listname;
    }
}