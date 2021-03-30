package ds.docusheet.table;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.holder> {
    SparseBooleanArray sSelectedItems=new SparseBooleanArray();

    int row_index=RecyclerView.NO_POSITION;
    int h=-1;
    /*List<cell> list;
    Context context;
    String row,column;
    ConnectionClass connectionClass;
    String message,s;
    cell cells;
    private OnItemClickListener mlistener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mlistener = listener;
    }

    public Adapter(Context context,List<cell> list) {
        this.context=context;
        this.list = list;
    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardcell, parent, false);
        Adapter.holder vH = new Adapter.holder(view,new MyTextWatcher(),mlistener);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        cells=list.get(position);
        holder.editText.setText(cells.getData());
        message=cells.getData();
        holder.myTextWatcher.updatePosition(holder.getAdapterPosition());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public class holder extends RecyclerView.ViewHolder {
        EditText editText;
        MyTextWatcher myTextWatcher;
        public holder(@NonNull View itemView, MyTextWatcher textWatcher,final OnItemClickListener listener) {
            super(itemView);
            editText=itemView.findViewById(R.id.edit);
            this.myTextWatcher=textWatcher;
            MyTextWatcher watcher=new MyTextWatcher();
            editText.addTextChangedListener(watcher);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        listener.onItemClick(position, v);
                    }
                }
            });
        }
    }

    public class MyTextWatcher implements TextWatcher {

        EditText editText;
        private int position;

        public MyTextWatcher() {

        }

        public void updatePosition(int position){
            this.position=position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            cells=list.get(position);
            s=charSequence.toString();

            cells.setData(s);

        }

        @Override
        public void afterTextChanged(Editable editable) {

            row=cells.getRow();
            column=cells.getColumn();
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Table");
            //cell cell=new cell(s,row,column);
            //String childid=databaseReference.push().getKey();
            databaseReference.child(row).child(column).setValue(s).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //Toast.makeText(context, "Submitted", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }*/

    List<cell> list;
    Context context;
    cell cells;
    private OnItemClickListener mlistener;
    private HeightCheck heightCheck;
    private onItemLongClickListener llistener;

    public Adapter(Context context, List<cell> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mlistener = listener;
    }
    public void setOnLongItemClick(onItemLongClickListener longItemClick){this.llistener=longItemClick;}
    public void heightcheck(HeightCheck heightCheck){this.heightCheck=heightCheck;}

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardcell, parent, false);
        holder vH = new holder(view,mlistener,heightCheck,llistener);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, final int position) {
        cells = list.get(position);
        holder.editText.setText(cells.getData());

        holder.editText.setSelected(row_index == position);
        h=holder.cardView.getMeasuredHeight();
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (40 * scale + 0.5f);
        holder.editText.setMinimumHeight(pixels);

        //setheight(position,h);

        /*holder.recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            holder.recyclerView.setBackgroundResource(R.drawable.highlight);
        }
        else{
            holder.recyclerView.setBackgroundResource(R.drawable.cell);
        }*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }
    public interface HeightCheck{
        void checker(int position, int h);
    }
    public interface onItemLongClickListener{
        void onLongItemClick(int position, View v);
    }


    public class holder extends RecyclerView.ViewHolder {
        TextView editText;
        CardView cardView;
        EditText editText5;
        RecyclerView recyclerView;

        public holder(@NonNull View itemView, final OnItemClickListener listener, HeightCheck checker,onItemLongClickListener longClickListener) {
            super(itemView);
            editText = itemView.findViewById(R.id.edit);
            cardView=itemView.findViewById(R.id.cardview);
            //editText5=itemView.findViewById(R.id.edittext5);
            //sSelectedItems=new SparseBooleanArray();
            //editText.setSelected(false);
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        listener.onItemClick(position, view);
                        notifyItemChanged(row_index);
                        row_index = getLayoutPosition();
                        notifyItemChanged(row_index);
                        //checker.checker(position,h);
                        //if (checker!=null)
                            //checker.checker(position,h);
                    }
                    if (checker!=null)
                        checker.checker(getAdapterPosition(),h);

                }
            });
            editText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (longClickListener!=null){
                        int position = getAdapterPosition();
                        longClickListener.onLongItemClick(position, view);
                        notifyItemChanged(row_index);
                        row_index = getLayoutPosition();
                        notifyItemChanged(row_index);
                    }
                    return true;
                }
            });


           /* itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        listener.onItemClick(position, view);
                    }
                    relativeLayout.setVisibility(View.VISIBLE);
                    sSelectedItems.put(getAdapterPosition(), true);
                    editText.setSelected(true);
                    return true;
                }
            });*/


        }
    }


}
