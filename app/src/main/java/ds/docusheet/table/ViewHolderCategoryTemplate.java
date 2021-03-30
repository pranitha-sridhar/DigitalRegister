package ds.docusheet.table;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class ViewHolderCategoryTemplate extends RecyclerView.ViewHolder {
    TextView cards;
    ImageView img;
    RelativeLayout card_button;
    ViewHolderCategoryTemplate(View itemView, final AdapterCategoryTemplate.OnItemClickListener listener) {
        super(itemView);
        cards = itemView.findViewById(R.id.card_text);
        img=itemView.findViewById(R.id.card_img);
        card_button=itemView.findViewById(R.id.cards_button);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }
}
