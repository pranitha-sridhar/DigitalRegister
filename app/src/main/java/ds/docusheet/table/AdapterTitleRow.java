package ds.docusheet.table;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdapterTitleRow extends RecyclerView.Adapter<AdapterTitleRow.holder> {
    List<cell> list;
    Context context;
    cell cells;
    int height;
    int cellpos=-1,column=-1;

    public AdapterTitleRow(Context context, List<cell> list) {
        this.context = context;
        this.list = list;
    }

    public void setheight(int pos,int height){
        this.height=height;
        cellpos=pos;
        if(cellpos%column==0)cellpos=cellpos/column+1;
        else cellpos=cellpos/column+2;
    }

    public AdapterTitleRow(Context context, List<cell> list, int h,int celpos,int colum){
        this.context = context;
        this.list = list;
        this.height=h;
        this.cellpos=celpos;this.column=colum;
        if(cellpos%column==0)cellpos=cellpos/column+1;
        else cellpos=cellpos/column+2;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardtitlerow, parent, false);
        holder vH = new holder(view);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        cells = list.get(position);
        holder.editText.setText(cells.getData());
        if(position==cellpos)
        holder.cardView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class holder extends RecyclerView.ViewHolder {
        TextView editText;
        CardView cardView;

        public holder(@NonNull final View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.titlerow);
            cardView=itemView.findViewById(R.id.cardview1);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   Drawable highlight = view.getResources().getDrawable(R.drawable.highlight);
                    itemView.setBackground(highlight);
                }
            });


        }
    }
}