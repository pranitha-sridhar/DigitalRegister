package ds.docusheet.table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolderList> {
    private OnItemClickListener mlistener;
    private OnMoreListener moreListener;
    Context context;
    List<Document> list;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnMoreListener{
        void onMoreClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }
    public void setOnMoreListener(OnMoreListener moreListener){this.moreListener=moreListener;}

    public AdapterList(List<Document> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolderList onCreateViewHolder(ViewGroup parent, int viewType) {
        //Context context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.docx_name, parent, false);
        ViewHolderList vH = new ViewHolderList(view,mlistener,moreListener);
        return vH;
    }

    @Override
    public void onBindViewHolder(final ViewHolderList viewHolder, final int position) {
        viewHolder.Name.setText(list.get(position).getDoc_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolderList extends RecyclerView.ViewHolder{
        TextView Name;
        ImageView more;
        public ViewHolderList(@NonNull View itemView, final OnItemClickListener listener, final OnMoreListener moreListener) {
            super(itemView);
            Name=itemView.findViewById(R.id.doc_id);
            more=itemView.findViewById(R.id.more);
            Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null){
                        int position=getAdapterPosition();
                        listener.onItemClick(position);
                    }
                }
            });
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (moreListener!=null){
                        moreListener.onMoreClick(getAdapterPosition());
                    }
                }
            });

        }
    }
}

