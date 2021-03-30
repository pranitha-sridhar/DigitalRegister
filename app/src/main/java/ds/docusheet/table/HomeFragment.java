package ds.docusheet.table;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static ds.docusheet.table.Login.first;
import static ds.docusheet.table.Login.firstrun;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    RelativeLayout relativeLayout,youtube;
    ConstraintLayout constraintLayout;
    //WebView displayYoutubeVideo;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    FirebaseAuth mAuth;
    LinearLayoutManager layoutManager;
    int k=0;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home,container,false);
        relativeLayout=v.findViewById(R.id.new_document);
        mAuth=FirebaseAuth.getInstance();
        recyclerView= (RecyclerView)v.findViewById(R.id.recyclerView);
        youtube=v.findViewById(R.id.youtube);
        constraintLayout=v.findViewById(R.id.clayout);
        //displayYoutubeVideo=v.findViewById(R.id.youtube_web);
        YouTubePlayerView youTubePlayerView=v.findViewById(R.id.videoplayer);
        //boolean first = requireActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("first_run", true);
        //Toast.makeText(getContext(),""+first,Toast.LENGTH_SHORT).show();
        if(first){
            //Toast.makeText(getContext(),""+firstrun,Toast.LENGTH_SHORT).show();
            youtube.setVisibility(View.GONE);
            youTubePlayerView.setVisibility(View.VISIBLE);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.recyclerView,ConstraintSet.TOP,R.id.videoplayer,ConstraintSet.BOTTOM,0);
            //constraintSet.connect(R.id.imageView,ConstraintSet.TOP,R.id.check_answer2,ConstraintSet.TOP,0);
            constraintSet.applyTo(constraintLayout);
            getLifecycle().addObserver(youTubePlayerView);
        }
        else{
            youTubePlayerView.release();
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.recyclerView,ConstraintSet.TOP,R.id.youtube,ConstraintSet.BOTTOM,0);
            //constraintSet.connect(R.id.imageView,ConstraintSet.TOP,R.id.check_answer2,ConstraintSet.TOP,0);
            constraintSet.applyTo(constraintLayout);
            youtube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/3uIVlLEfah8\n"));
                    try {
                        getActivity().startActivity(webIntent);
                    } catch (ActivityNotFoundException ex) {
                    }
                }
            });
        }

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),NewDocument.class);
                startActivity(intent);
            }
        });
        String user_id=mAuth.getUid();
        final String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-all-document/"+user_id;

        final List<Document> documents=new ArrayList<>();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray=response.getJSONArray("document");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                k++;
                                documents.add(new Document(jsonObject.getString("doc_id"),jsonObject.getString("document_name"),jsonObject.getString("update_document") ));
                            }
                            makelist(documents);
                            //Toast.makeText(getContext(), ""+k, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            //Toast.makeText(getContext(),""+e,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getContext(),""+error,Toast.LENGTH_LONG).show();
                    }
                });
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

        return v;
    }
    public void makelist(final List<Document> list)
    {
        AdapterMain adapter=new AdapterMain(list,getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOnItemClickListener(new AdapterMain.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(getContext(), ""+list.get(position).getUpdate(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(),GetDocument.class);
                intent.putExtra("doc_id",list.get(position).getDoc_id());
                intent.putExtra("doc_name", list.get(position).getDoc_name());
                intent.putExtra("update_document",list.get(position).getUpdate());
                startActivity(intent);
            }});
    }
}