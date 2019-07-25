package com.developments.ar.feedbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Splash_screen extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);
        Intent mIntent = getIntent();
        String icon = mIntent.getStringExtra("icon");
        String title = mIntent.getStringExtra("title");
        int pos = mIntent.getIntExtra("pos", 0);
        int siz = mIntent.getIntExtra("siz", 0);
        int theme_no= mIntent.getIntExtra("colr", 1);

        Log.d("Intent",icon+" "+title+" "+pos+" "+siz);

        TextView txt_top=(TextView)findViewById(R.id.s_txt_top);
        TextView txt_middle=(TextView)findViewById(R.id.s_txt_middle);
        TextView txt_bottom=(TextView)findViewById(R.id.s_txt_bottom);
        LinearLayout background=(LinearLayout)findViewById(R.id.s_background);

        switch (theme_no)
        {
            case 1:
                txt_top.setTextColor(getResources().getColor(R.color.t_blue));
                txt_middle.setTextColor(getResources().getColor(R.color.t_blue));
                txt_bottom.setTextColor(getResources().getColor(R.color.t_blue));
                break;
            case 2:
                txt_top.setTextColor(getResources().getColor(R.color.t_red));
                txt_middle.setTextColor(getResources().getColor(R.color.t_red));
                txt_bottom.setTextColor(getResources().getColor(R.color.t_red));
                break;
            case 3:
                txt_top.setTextColor(getResources().getColor(R.color.t_green));
                txt_middle.setTextColor(getResources().getColor(R.color.t_green));
                txt_bottom.setTextColor(getResources().getColor(R.color.t_green));
                break;
            case 4:
                txt_top.setTextColor(getResources().getColor(R.color.t_pink));
                txt_middle.setTextColor(getResources().getColor(R.color.t_pink));
                txt_bottom.setTextColor(getResources().getColor(R.color.t_pink));
                break;
            case 5:
                txt_top.setTextColor(getResources().getColor(R.color.t_yellow));
                txt_middle.setTextColor(getResources().getColor(R.color.t_yellow));
                txt_bottom.setTextColor(getResources().getColor(R.color.t_yellow));
                break;
            case 6:
                txt_top.setTextColor(getResources().getColor(R.color.t_purple));
                txt_middle.setTextColor(getResources().getColor(R.color.t_purple));
                txt_bottom.setTextColor(getResources().getColor(R.color.t_purple));
                break;
            default:
                txt_top.setTextColor(getResources().getColor(R.color.t_green));
                txt_middle.setTextColor(getResources().getColor(R.color.t_green));
                txt_bottom.setTextColor(getResources().getColor(R.color.t_green));
        }

        switch (pos){
            case 1:
                txt_top.setText(title);
                txt_top.setTextSize((siz * 5) + 20);
                break;
            case 2:
                txt_middle.setText(title);
                txt_middle.setTextSize((siz * 5) + 20);
                break;
            case 3:
                txt_bottom.setText(title);
                txt_bottom.setTextSize((siz * 5) + 20);
                break;
        }

    }

    public void on_tap(View view){
        finish();
    }
}
