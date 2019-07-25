package com.developments.ar.feedbox;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Admin on 3/4/2017.
 */
public class http_server extends ContextWrapper {
    public String mem_id;
    public Boolean return_result=false;
    public Sql_db db;
    private String ip=null;
    public int s_id;
    public int ngo_id;
    public int ut_id;
    public http_server(Context context) {
        super(context);
        db=new Sql_db(this);
        ip=getString(R.string.ip_address);
    }

    public void server_deleteUT1(int S_id){
        s_id=S_id;
        new http_delete_UT1().execute();
    }



    class http_delete_UT1 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject object = null;
            try {
                object = new JSONObject("{'UT_id' : "+s_id+"}");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+ip+":8080/tap_to_plant/ttp?action=delete_UT1");
                String json = "";
                json = object.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpclient.execute(httpPost);
                InputStream inputStream = httpResponse.getEntity().getContent();
                String result = "";
                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

                Log.e("log_tag", "connection success ... "+result);

            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());


            }
            return null;
        }
    }




    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        String result = null;
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;
    }


}
