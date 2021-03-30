package ds.docusheet.table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterTotal extends RecyclerView.Adapter<AdapterTotal.holder>{
    List<cell> list;
    Context context;
    cell cells;

    public AdapterTotal(Context context, List<cell> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public AdapterTotal.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardtitle, parent, false);
        AdapterTotal.holder vH = new AdapterTotal.holder(view);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTotal.holder holder, int position) {
        cells = list.get(position);
        holder.editText.setText(cells.getData());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class holder extends RecyclerView.ViewHolder {
        TextView editText;
        public holder(@NonNull View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.title);
        }
    }
}
