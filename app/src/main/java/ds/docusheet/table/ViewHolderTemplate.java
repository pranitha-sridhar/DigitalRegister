package ds.docusheet.table;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderTemplate extends RecyclerView.ViewHolder  {
    TextView heading;
    RecyclerView recyclerView;
    int position2;
    ViewHolderTemplate(View itemView, final AdapterTemplate.OnItemClickListener listener) {
        super(itemView);
        heading = itemView.findViewById(R.id.template_heading);
        recyclerView=itemView.findViewById(R.id.category_template_recyclerView);
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
