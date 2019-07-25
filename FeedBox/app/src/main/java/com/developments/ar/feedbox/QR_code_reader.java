package com.developments.ar.feedbox;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.google.zxing.Result;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class QR_code_reader extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private String ip="";
    public Boolean server_con=false;
    private Sql_db db;
    private String fcode;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        db=new Sql_db(this);
        ip=getString(R.string.ip_address);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.d("result", rawResult.getText());
        fcode=rawResult.getText();
        new http_get_content().execute();
    }

    class http_get_content extends AsyncTask<Void, Void, javax.xml.transform.Result> {
        @Override
        protected javax.xml.transform.Result doInBackground(Void... params) {
            JSONObject object = null;
            Intent intent = new Intent();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+ ip +":8080/feedbox/feedbox?action=get_content");
                String json = "";
                object = new JSONObject("{'fcode' :'"+fcode+"'}");
                json = object.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpclient.execute(httpPost);
                String result = EntityUtils.toString(httpResponse.getEntity());
                JSONArray ja = new JSONArray(result);
                String fcode="";
                String f_icon="";
                String f_title="";
                String f_cont="";
                int f_pos = 0;
                int f_siz=0;
                int f_theme=0;
                JSONObject obj = ja.getJSONObject(0);
                String resp=obj.getString("result");
                if(resp.equals("success")){
                    JSONObject jo = ja.getJSONObject(0);
                    fcode=jo.getString("fcode");
                    f_icon=jo.getString("a_icon");
                    f_title=jo.getString("a_title");
                    f_pos=jo.getInt("a_pos");
                    f_siz=jo.getInt("a_siz");
                    f_theme=jo.getInt("a_theme");
                    try{
                        f_cont=jo.getString("a_cont");
                    }
                    catch (Exception e){
                        f_cont="";
                    }

                    Log.e("insert db", "insert ");
                    db.update_app_details(fcode, f_icon, f_title, f_pos, f_siz, f_theme, f_cont);
                    db.create_table();
                    setResult(RESULT_OK, intent);
                }
                else{
                    setResult(RESULT_CANCELED, intent);
                    Log.e("incorrect", "fcode ");
                }
                Log.e("log_tag", "connection success "+fcode);
                finish();
                server_con = true;
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                server_con = false;
            }
            return null;
        }


        protected void onPostExecute(javax.xml.transform.Result result) {

        }

    }

}