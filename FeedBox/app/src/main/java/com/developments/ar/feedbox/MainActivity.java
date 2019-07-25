package com.developments.ar.feedbox;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.zip.Inflater;

import javax.xml.transform.Result;

public class MainActivity extends Activity  {
    private String ip="";
    public Boolean server_con=false;
    private Sql_db db;
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;
    private String fcode="";
    private String[] app_details = new String[7];
    private String content="";
    private String single_content="";
    private String single_content_db="";
    private int qr_intent=11111;
    private int splash_intent=2222;
    private int offline_intent=3333;
    private boolean autenticate=false;
    private int theme_no=0;
    private LinearLayout summit_button;
    private LinearLayout field6;
    private LinearLayout field7;
    private LinearLayout notification_lay;
    private ArrayList fields_list;
    private Boolean online=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        db=new Sql_db(this);
        ip=getString(R.string.ip_address);
        app_details=db.get_app_details();
        fcode=app_details[0];

        if(fcode==null){
            // first run of app || no login found
            setContentView(R.layout.login_page);

        }
        else {
            content = app_details[6];
            Intent intent = new Intent(getApplicationContext(), Splash_screen.class);
            intent.putExtra("icon", app_details[1]);
            intent.putExtra("title", app_details[2]);
            intent.putExtra("pos", Integer.parseInt(app_details[3]));
            intent.putExtra("siz", Integer.parseInt(app_details[4]));
            intent.putExtra("colr", Integer.parseInt(app_details[5]));
            startActivityForResult(intent, splash_intent);
        }
        sharedPreferences = getSharedPreferences("com.developments.ar.feedbox", MODE_PRIVATE);
    }

    private void theme_activity(int theme_no) {
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.d("open","drawer");
                TextView textView=(TextView)findViewById(R.id.offline_counts);
                textView.setText(db.get_offline_count());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
            }
        });
        theme_no=theme_no;

        LinearLayout action_bar=(LinearLayout)findViewById(R.id.action_bar);
        LinearLayout drawer_title_bar=(LinearLayout)findViewById(R.id.drawer_title_bar);
        notification_lay= (LinearLayout) View.inflate(MainActivity.this, R.layout.notification_lay, null);
        summit_button=(LinearLayout) View.inflate(this, R.layout.summit_lay, null);
        View btn=summit_button.findViewById(R.id.summit_btn);
        field6= (LinearLayout) View.inflate(this, R.layout.field6_lay, null);
        field7= (LinearLayout) View.inflate(this, R.layout.field7_lay, null);
        View scan=notification_lay.findViewById(R.id.scan_btn2);
        ImageView ham=(ImageView)findViewById(R.id.ham_img);
        TextView offline_txt=(TextView)findViewById(R.id.offline_counts);
        RatingBar start_rating=(RatingBar)field7.findViewById(R.id.f7_star);
        LayerDrawable stars = (LayerDrawable) start_rating.getProgressDrawable();
        start_rating.setNumStars(5);
        stars.getDrawable(0).setColorFilter(Color.parseColor("#585858"), PorterDuff.Mode.SRC_ATOP);
        // for empty stars


        ham.setImageResource(R.drawable.ham_icon);

        switch (theme_no)
        {
            case 1:
                action_bar.setBackgroundColor(getResources().getColor(R.color.t_blue));
                drawer_title_bar.setBackgroundColor(getResources().getColor(R.color.t_blue));
                btn.setBackgroundColor(getResources().getColor(R.color.t_blue));
                scan.setBackgroundColor(getResources().getColor(R.color.t_blue));
                offline_txt.setTextColor(getResources().getColor(R.color.t_blue));
                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.t_blue), PorterDuff.Mode.SRC_ATOP); // for filled stars
                break;
            case 2:
                action_bar.setBackgroundColor(getResources().getColor(R.color.t_red));
                drawer_title_bar.setBackgroundColor(getResources().getColor(R.color.t_red));
                btn.setBackgroundColor(getResources().getColor(R.color.t_red));
                scan.setBackgroundColor(getResources().getColor(R.color.t_red));
                offline_txt.setTextColor(getResources().getColor(R.color.t_red));
                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.t_red), PorterDuff.Mode.SRC_ATOP); // for filled stars
                break;
            case 3:
                action_bar.setBackgroundColor(getResources().getColor(R.color.t_green));
                drawer_title_bar.setBackgroundColor(getResources().getColor(R.color.t_green));
                btn.setBackgroundColor(getResources().getColor(R.color.t_green));
                scan.setBackgroundColor(getResources().getColor(R.color.t_green));
                offline_txt.setTextColor(getResources().getColor(R.color.t_green));
                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.t_green), PorterDuff.Mode.SRC_ATOP);
                break;
            case 4:
                action_bar.setBackgroundColor(getResources().getColor(R.color.t_pink));
                drawer_title_bar.setBackgroundColor(getResources().getColor(R.color.t_pink));
                btn.setBackgroundColor(getResources().getColor(R.color.t_pink));
                scan.setBackgroundColor(getResources().getColor(R.color.t_pink));
                offline_txt.setTextColor(getResources().getColor(R.color.t_pink));
                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.t_pink), PorterDuff.Mode.SRC_ATOP);
                break;
            case 5:
                action_bar.setBackgroundColor(getResources().getColor(R.color.t_yellow));
                drawer_title_bar.setBackgroundColor(getResources().getColor(R.color.t_yellow));
                btn.setBackgroundColor(getResources().getColor(R.color.t_yellow));
                scan.setBackgroundColor(getResources().getColor(R.color.t_yellow));
                offline_txt.setTextColor(getResources().getColor(R.color.t_yellow));
                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.t_yellow), PorterDuff.Mode.SRC_ATOP);
                break;
            case 6:
                action_bar.setBackgroundColor(getResources().getColor(R.color.t_purple));
                drawer_title_bar.setBackgroundColor(getResources().getColor(R.color.t_purple));
                btn.setBackgroundColor(getResources().getColor(R.color.t_purple));
                scan.setBackgroundColor(getResources().getColor(R.color.t_purple));
                offline_txt.setTextColor(getResources().getColor(R.color.t_purple));
                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.t_purple), PorterDuff.Mode.SRC_ATOP);
                break;
            default:
                action_bar.setBackgroundColor(getResources().getColor(R.color.t_green));
                drawer_title_bar.setBackgroundColor(getResources().getColor(R.color.t_green));
                btn.setBackgroundColor(getResources().getColor(R.color.t_green));
                scan.setBackgroundColor(getResources().getColor(R.color.t_green));
                offline_txt.setTextColor(getResources().getColor(R.color.t_green));
                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.t_green), PorterDuff.Mode.SRC_ATOP);

        }
    }

    public void open_qr_scanner(View view){
        Intent intent = new Intent(getApplicationContext(), QR_code_reader.class);
        startActivityForResult(intent, qr_intent);
    }

    public void website_open(View view){
        TextView textView=(TextView)view.findViewById(R.id.txt_url);
        String v=textView.getText().toString();
        String url = "http://"+v;
        Log.d("url",url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void logout(View view){
        db.initial_db();
        setContentView(R.layout.login_page);
    }

    public void exit(View view){
        finish();
    }

    public void offline(View view){
        Intent intent = new Intent(getApplicationContext(), Offline.class);
        intent.putExtra("fcode", fcode);
        intent.putExtra("content", content);
        intent.putExtra("theme_no", theme_no);
        startActivityForResult(intent, offline_intent);
    }

    public void choice_button(View view){
        Log.d("summit", "summit");
        LinearLayout parent = (LinearLayout) view.getParent();
        Button yes=(Button)parent.findViewById(R.id.btn_yes);
        Button no=(Button)parent.findViewById(R.id.btn_no);
        TextView txt=(TextView)parent.findViewById(R.id.btn_txt);
        yes.setBackgroundResource(R.color.black);
        no.setBackgroundResource(R.color.black);

        if (view.getId()==R.id.btn_yes){
            txt.setText("Yes");
        }
        else{
            txt.setText("No");
        }
        switch (theme_no)
        {
            case 1:
                view.setBackgroundResource(R.color.t_blue);
                break;
            case 2:
                view.setBackgroundResource(R.color.t_red);
                break;
            case 3:
                view.setBackgroundResource(R.color.t_green);
                break;
            case 4:
                view.setBackgroundResource(R.color.t_pink);
                break;
            case 5:
                view.setBackgroundResource(R.color.t_yellow);
                break;
            case 6:
                view.setBackgroundResource(R.color.t_purple);
                break;
            default:
                view.setBackgroundResource(R.color.t_green);
        }

    }

    public void summit(View view){
       // Log.d("summit","summit ");
        int count=1;
        single_content="";
        single_content_db="";
        LinearLayout layout = (LinearLayout)findViewById(R.id.body_frame);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof LinearLayout) {
                LinearLayout layout1=(LinearLayout)v;
                View base = layout1.getChildAt(0);
                if(base instanceof LinearLayout) {
                    LinearLayout parent = (LinearLayout) base;
                    for (int j = 0; j < parent.getChildCount(); j++) {
                        View view1 = parent.getChildAt(j);


                        if (view1 instanceof EditText) {
                            EditText editText = ((EditText) view1);
                            String s=editText.getText().toString();
                            if(s.length()==0){
                                s="Null";
                            }
                            Log.d("edit text", s + " ");
                            single_content+="'col"+count+"':'"+s+"',";
                            single_content_db+="$col"+count+"$:$"+s+"$,";
                            count++;
                        }
                        else if (view1 instanceof LinearLayout) {
                            LinearLayout butn_lay=(LinearLayout)view1;
                            View txt = butn_lay.getChildAt(0);
                            if (txt instanceof TextView && txt.getId()==R.id.btn_txt){
                                TextView t = (TextView) txt;
                                String s= (String) t.getText();
                                if(s.length()==0){
                                    s="Null";
                                }
                                Log.d("button", s + " ");
                                single_content+="'col"+count+"':'"+s+"',";
                                single_content_db+="$col"+count+"$:$"+s+"$,";
                                count++;
                            }
                        }
                        else if(view1 instanceof RatingBar){
                            float s=((RatingBar) view1).getRating();
                            Log.d("rating", s + " ");
                            single_content+="'col"+count+"':'"+s+"',";
                            single_content_db+="$col"+count+"$:$"+s+"$,";
                            count++;
                        }

                    }
                }

                //validate your EditText here
            }
        }
        single_content=single_content.substring(0,single_content.length()-1);
        single_content_db=single_content_db.substring(0,single_content_db.length()-1);
        new single_insert().execute();
        Log.d("single_content",single_content);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == qr_intent) {
            if(resultCode == Activity.RESULT_OK){
                Intent intent = new Intent(getApplicationContext(), Splash_screen.class);
                app_details=db.get_app_details();
                intent.putExtra("icon", app_details[1]);
                intent.putExtra("title", app_details[2]);
                intent.putExtra("pos", Integer.parseInt(app_details[3]));
                intent.putExtra("siz", Integer.parseInt(app_details[4]));
                intent.putExtra("colr", Integer.parseInt(app_details[5]));
                setContentView(R.layout.login_page);
                startActivityForResult(intent, splash_intent);
//                setContentView(R.layout.activity_main);
//                Log.d("finally","ok");
//                app_details=db.get_app_details();
//                fcode=app_details[0];
//                content=app_details[6];
//                theme_no=Integer.parseInt(app_details[5]);
//                theme_activity(theme_no);
//                create_app_fields(content);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("finally","cancel");
            }
        }
        else if(requestCode==splash_intent){
            setContentView(R.layout.activity_main);
            app_details=db.get_app_details();
            fcode=app_details[0];
            content=app_details[6];
            if (app_details[5] != null) {
                theme_no = Integer.parseInt(app_details[5]);
                theme_activity(theme_no);
            }
            create_app_fields(content);
            new autentication().execute();
            Log.d("server...1", autenticate + " " + server_con + " ");
        }
    }

    public void create_app_fields(String cont){
        Log.d("create_app_fields", "" + cont);
        LinearLayout body=(LinearLayout)findViewById(R.id.body_frame);
        fields_list=new ArrayList();

        if(body.getChildCount() > 0){
            body.removeAllViews();
        }

        if(cont != null && !cont.isEmpty() && !cont.equals("null") ) {
            String[] re_arry1 = cont.split("\\$");
            String insert_values = "";
            String tab_col = "";
            int count = 1;
            for (String ans : re_arry1) {
                String[] re_arry2 = ans.split("\\|");
                String opt = re_arry2[0];
                String name = re_arry2[1];
                String f2 = re_arry2[2];
                String f3 = re_arry2[3];
                String f4 = re_arry2[4];
                String f0 = "field" + count;
                fields_list.add(f0);

                switch(Integer.parseInt(opt)){
                    case 1:
                        LinearLayout field1= (LinearLayout) View.inflate(this, R.layout.field1_lay, null);
                        ((TextView) field1.findViewById(R.id.f1_txt)).setText(name);
                        body.addView(field1);
                        break;
                    case 2:
                        LinearLayout field2= (LinearLayout) View.inflate(this, R.layout.field2_lay, null);
                        ((TextView) field2.findViewById(R.id.f2_txt)).setText(name);
                        body.addView(field2);
                        break;
                    case 3:
                        LinearLayout field3= (LinearLayout) View.inflate(this, R.layout.field3_lay, null);
                        ((TextView) field3.findViewById(R.id.f3_txt)).setText(name);
                        body.addView(field3);
                        break;
                    case 4:
                        LinearLayout field4= (LinearLayout) View.inflate(this, R.layout.field4_lay, null);
                        ((TextView) field4.findViewById(R.id.f4_txt)).setText(name);
                        body.addView(field4);
                        break;
                    case 5:
                        LinearLayout field5= (LinearLayout) View.inflate(this, R.layout.field5_lay, null);
                        ((TextView) field5.findViewById(R.id.f5_txt)).setText(name);
                        body.addView(field5);
                        count--;
                        break;
                    case 6:
                        LinearLayout field6=(LinearLayout) View.inflate(this, R.layout.field6_lay, null);
                        ((TextView) field6.findViewById(R.id.f6_txt)).setText(name);
                        Button btn=(Button)field6.findViewById(R.id.btn_no);
                        switch (theme_no)
                        {
                            case 1:
                                btn.setBackgroundResource(R.color.t_blue);
                                break;
                            case 2:
                                btn.setBackgroundResource(R.color.t_red);
                                break;
                            case 3:
                                btn.setBackgroundResource(R.color.t_green);
                                break;
                            case 4:
                                btn.setBackgroundResource(R.color.t_pink);
                                break;
                            case 5:
                                btn.setBackgroundResource(R.color.t_yellow);
                                break;
                            case 6:
                                btn.setBackgroundResource(R.color.t_purple);
                                break;
                            default:
                                btn.setBackgroundResource(R.color.t_green);
                        }
                        body.addView(field6);
                        break;
                    case 7:
                        LinearLayout field7=(LinearLayout) View.inflate(this, R.layout.field7_lay, null);
                        ((TextView) field7.findViewById(R.id.f7_txt)).setText(name);
                        RatingBar start_rating=(RatingBar)field7.findViewById(R.id.f7_star);
                        LayerDrawable stars = (LayerDrawable) start_rating.getProgressDrawable();
                        start_rating.setNumStars(5);
                        stars.getDrawable(0).setColorFilter(Color.parseColor("#585858"), PorterDuff.Mode.SRC_ATOP);
                        switch (theme_no)
                        {
                            case 1:
                                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.t_blue), PorterDuff.Mode.SRC_ATOP); // for filled stars
                                break;
                            case 2:
                                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.t_red), PorterDuff.Mode.SRC_ATOP); // for filled stars
                                break;
                            case 3:
                                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.t_green), PorterDuff.Mode.SRC_ATOP);
                                break;
                            case 4:
                                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.t_pink), PorterDuff.Mode.SRC_ATOP);
                                break;
                            case 5:
                                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.t_yellow), PorterDuff.Mode.SRC_ATOP);
                                break;
                            case 6:
                                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.t_purple), PorterDuff.Mode.SRC_ATOP);
                                break;
                            default:
                                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.t_green), PorterDuff.Mode.SRC_ATOP);
                        }
                        body.addView(field7);
                        break;
                    case 8:
                        LinearLayout field8= (LinearLayout) View.inflate(this, R.layout.field8_lay, null);
                        TextView txt=(TextView) field8.findViewById(R.id.f8_txt);
                        TextView txt2=(TextView) field8.findViewById(R.id.txt_url);
                        txt2.setText(name);
                        body.addView(field8);
                        switch (theme_no)
                        {
                            case 1:
                                txt.setTextColor(getResources().getColor(R.color.t_blue));
                                break;
                            case 2:
                                txt.setTextColor(getResources().getColor(R.color.t_red));
                                break;
                            case 3:
                                txt.setTextColor(getResources().getColor(R.color.t_green));
                                break;
                            case 4:
                                txt.setTextColor(getResources().getColor(R.color.t_pink));
                                break;
                            case 5:
                                txt.setTextColor(getResources().getColor(R.color.t_yellow));
                                break;
                            case 6:
                                txt.setTextColor(getResources().getColor(R.color.t_purple));
                                break;
                            default:
                                txt.setTextColor(getResources().getColor(R.color.t_green));
                        }
                        break;
                }
                count++;
            }


            body.addView(summit_button);
        }
    }


    class single_insert extends AsyncTask<Void, Void, javax.xml.transform.Result> {
        @Override
        protected javax.xml.transform.Result doInBackground(Void... params) {
            JSONObject object = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+ ip +":8080/feedbox/feedbox?action=put_data");
                String json = "";
                object = new JSONObject("{'fcode' :'"+fcode+"',"+single_content+"}");
                json = object.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpclient.execute(httpPost);
                String result = EntityUtils.toString(httpResponse.getEntity());
                JSONObject ja = new JSONObject(result);
                String resp=ja.getString("resp");
                if(resp.equals("success")){
                    Log.d("insert succes","1");
                    online=true;
                }
                else{
                    Log.d("insert failed","2");
                    online=false;
                }

            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                online=false;

            }
            Log.d("server...2 insert",autenticate+" "+server_con+" ");
            return null;
        }
        protected void onPostExecute(javax.xml.transform.Result result) {
            if(!online){
                db.insert_into_table(single_content_db);
                Log.d("counts",""+db.get_offline_count());
            }
            Intent intent = new Intent(getApplicationContext(), Splash_screen.class);
            intent.putExtra("icon", app_details[1]);
            intent.putExtra("title", app_details[2]);
            intent.putExtra("pos", Integer.parseInt(app_details[3]));
            intent.putExtra("siz", Integer.parseInt(app_details[4]));
            intent.putExtra("colr", Integer.parseInt(app_details[5]));
            startActivityForResult(intent, splash_intent);
            //single_content
        }
    }

    class autentication extends AsyncTask<Void, Void, javax.xml.transform.Result> {
        @Override
        protected javax.xml.transform.Result doInBackground(Void... params) {
            JSONObject object = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+ ip +":8080/feedbox/feedbox?action=authentication");
                String json = "";
                object = new JSONObject("{'fcode' :'"+fcode+"','content' :'"+content+"'}");
                json = object.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpclient.execute(httpPost);
                String result = EntityUtils.toString(httpResponse.getEntity());
                JSONObject ja = new JSONObject(result);
                String resp=ja.getString("result");
                if(resp.equals("success")){
                    autenticate = true;
                    server_con = true;
                }
                else{
                    autenticate=false;
                    server_con = true;
                }

            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                server_con = false;
                autenticate= false;
            }
            Log.d("server...2",autenticate+" "+server_con+" ");
            return null;
        }
        protected void onPostExecute(javax.xml.transform.Result result) {

            if (autenticate){
                create_app_fields(content);
            }
            else if(!server_con){
                app_details=db.get_app_details();
                fcode=app_details[0];
                content=app_details[6];
                theme_no=Integer.parseInt(app_details[5]);
                theme_activity(theme_no);
                create_app_fields(content);
            }
            else{
                LinearLayout body=(LinearLayout)findViewById(R.id.body_frame);
                if(body.getChildCount() > 0){
                    body.removeAllViews();
                }
                //LinearLayout field1= (LinearLayout) View.inflate(MainActivity.this, R.layout.notification_lay, null);
                body.addView(notification_lay);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sharedPreferences.getBoolean("firstRun", true)) {
            db.initial_db();
            editor = sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.commit();
        }
    }

    @Override
    public void onBackPressed(){

    }
}

