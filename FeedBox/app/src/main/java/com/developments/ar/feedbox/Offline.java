package com.developments.ar.feedbox;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.xml.transform.Result;

/**
 * Created by Admin on 12/14/2017.
 */
public class Offline extends Activity {
    private String ip="";
    public Boolean server_con=false;
    private boolean autenticate=false;
    private Sql_db db;
    private String fcode="";
    private String content="";
    private boolean online=false;
    private String single_content="";
    private String[] app_details = new String[7];
    private int theme_no=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_lay);
        db=new Sql_db(this);
        ip=getString(R.string.ip_address);
        Intent mIntent = getIntent();
        fcode = mIntent.getStringExtra("fcode");
        content= mIntent.getStringExtra("content");
        theme_no= mIntent.getIntExtra("theme_no", 1);
        Button view=(Button)findViewById(R.id.scan_btn2);
        switch (theme_no){
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
        new autentication().execute();
    }

    class autentication extends AsyncTask<Void, Void, Result> {
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
                online=false;
            }
            Log.d("server...2",autenticate+" "+server_con+" ");
            return null;
        }
        protected void onPostExecute(javax.xml.transform.Result result) {

            Button button=(Button)findViewById(R.id.scan_btn2);
            TextView textView=(TextView)findViewById(R.id.noti_txt);
            TextView noti_head=(TextView)findViewById(R.id.noti_head);
            if (autenticate && server_con){
                noti_head.setText("Online");
                textView.setText("Click to upload the feed to server");
                button.setText("Upload");
                online=true;
            }
            else{
                noti_head.setText("Offline");
                textView.setText("Try again later");
                button.setText("Upload");
                online=false;
            }
        }
    }

    public void open_qr_scanner(View view){
        if(online){
            ArrayList arrayList=new ArrayList();
            arrayList=db.get_offline_entry();
            for (int i = 0; i < arrayList.size(); i++) {
                String val=arrayList.get(i).toString();
                val=val.replace("$","'");
                single_content=val;
                new single_insert().execute();
                System.out.println(arrayList.get(i));
            }
            db.create_table();
            if(arrayList.size()>0){
                Toast.makeText(getApplicationContext(),"Uploaded Successfully !", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"No entries avaliable offline", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Offline !", Toast.LENGTH_SHORT).show();

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
//            if(!online){
//                db.insert_into_table(single_content_db);
//                Log.d("counts",""+db.get_offline_count());
//            }
//            Intent intent = new Intent(getApplicationContext(), Splash_screen.class);
//            intent.putExtra("icon", app_details[1]);
//            intent.putExtra("title", app_details[2]);
//            intent.putExtra("pos", Integer.parseInt(app_details[3]));
//            intent.putExtra("siz", Integer.parseInt(app_details[4]));
//            startActivityForResult(intent, splash_intent);
            //single_content
        }
    }

}
