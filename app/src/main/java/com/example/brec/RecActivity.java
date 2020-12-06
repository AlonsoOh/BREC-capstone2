package com.example.brec;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brec.model.Book;
import com.example.brec.model.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecActivity extends AppCompatActivity {
    Button btn_keyword;
    TextView tv_outPut;
    EditText key_input;
    String result;

    private BookAdapter mBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        SearchFragmentRec searchFragment = new SearchFragmentRec();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout_contents, searchFragment);
        transaction.commit();

        /*
        //tv_outPut = (TextView) findViewById(R.id.tv_outPut);
        key_input = (EditText) findViewById(R.id.key_input);

        btn_keyword = (Button) findViewById(R.id.keyword_button);
        btn_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String keywords = key_input.getText().toString();

                String url = "http://34.229.136.176:5000/recommend";
                ContentValues val = new ContentValues();
                val.put(keywords, "");
                //AsyncTask를통해 http수행 - arguments주의
                NetworkTask net = new NetworkTask(url, val);
                net.execute();

                //Toast t = Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT);
                //t.show();

            }
        });
    }
    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;
        public NetworkTask(String url, ContentValues values){
            this.url = url;
            this.values = values;
        }
        @Override
        protected String doInBackground(Void... params) {
            String result; //요청결과 저장
            RequestHttp requestHttp = new RequestHttp();
            result = requestHttp.request(url, values);
            return result;
        }
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            tv_outPut.setText(s);
            result = s;

        }*/
    }

}
