package ds.docusheet.table;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterCategoryTemplate extends RecyclerView.Adapter<ViewHolderCategoryTemplate> {

    private OnItemClickListener mlistener;
    Context context;
    List<Cards> list = new ArrayList<>();
    int position2;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    public AdapterCategoryTemplate(List<Cards> list, Context context,int position2) {
        this.list = list;
        this.context = context;
        this.position2=position2;
    }

    @Override
    public ViewHolderCategoryTemplate onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.template_cards, parent, false);
        ViewHolderCategoryTemplate viewHolder = new ViewHolderCategoryTemplate(photoView,mlistener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolderCategoryTemplate viewHolder, final int position) {
        viewHolder.cards.setText(list.get(position).getCards());
        viewHolder.img.setImageResource(list.get(position).getImg());
        //viewHolder.position2=position2;
        viewHolder.card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"This feature is coming soon",Toast.LENGTH_LONG).show();
                //List<Cards> cards=list.get(position2).getList();
                Cards card=list.get(position);
                Intent intent=new Intent(context,TemplateDocument.class);
                intent.putExtra("heading",card.getCards());
                intent.putExtra("cat_num",position);
                intent.putExtra("temp_num",position2);
                context.startActivity(intent);

            }
        });
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
