package ds.docusheet.table;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingBar {
    Activity activity;
    AlertDialog alertDialog;
    LoadingBar(Activity activity)
    {
        this.activity=activity;
    }
    void startLoading()
    {

        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_window,null));
        builder.setCancelable(true);
        alertDialog=builder.create();
        alertDialog.show();
    }
    void dismissDialog()
    {
        alertDialog.dismiss();
    }
}
