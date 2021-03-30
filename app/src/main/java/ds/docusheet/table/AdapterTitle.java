package ds.docusheet.table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class AdapterTitle extends RecyclerView.Adapter<AdapterTitle.holder> {
    List<cell> list;
    private OnItemClickListener3 mlistener;
    Context context;
    cell cells;

    public AdapterTitle(Context context, List<cell> list) {
        this.context = context;
        this.list = list;
    }
    public interface OnItemClickListener3 {
        void onItemClick(int position,View view);
    }
    public void setOnItemClickListener(OnItemClickListener3 listener) {
        mlistener = listener;
    }
    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardtitle, parent, false);
        holder vH = new holder(view,mlistener);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        cells = list.get(position);
        holder.editText.setText(cells.getData());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class holder extends RecyclerView.ViewHolder {
        TextView editText;

        public holder(@NonNull View itemView,OnItemClickListener3 listener) {
            super(itemView);
            editText = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        listener.onItemClick(getAdapterPosition(),view);
                    }
                }
            });

        }
    }
}