package ds.docusheet.table;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterTemplate extends RecyclerView.Adapter<ViewHolderTemplate> {
    private OnItemClickListener mlistener;
    Context context;
    AdapterCategoryTemplate adapterCategoryTemplate;
    List<Templates> list = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(int position);}

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    public AdapterTemplate(List<Templates> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolderTemplate onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.category_templates, parent, false);
        ViewHolderTemplate viewHolder = new ViewHolderTemplate(photoView,mlistener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolderTemplate viewHolderTemplate, final int position) {
        viewHolderTemplate.heading.setText(list.get(position).getHeading());
        adapterCategoryTemplate=new AdapterCategoryTemplate(list.get(position).getList(),context,position);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        viewHolderTemplate.recyclerView.setAdapter(adapterCategoryTemplate);
        viewHolderTemplate.recyclerView.setLayoutManager(linearLayoutManager);


    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
