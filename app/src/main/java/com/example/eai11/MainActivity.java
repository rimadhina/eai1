package com.example.eai11;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import android.app.ProgressDialog;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ListView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    Button btn_sign_out;
    ListView fruitsList;
    private WebView wb;


    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        //dialog.show();

        Button submit = findViewById(R.id.button);
        final EditText editText = findViewById(R.id.editName);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String KEYWORD = (editText.getText().toString());
                String url = "https://www.googleapis.com/youtube/v3/search?type=video&part=snippet&q="+ KEYWORD +"&relevanceLanguage=id&key=AIzaSyBtHsWBx4QvSJtAtM758a5YkCiuV7BopQE";

                StringRequest request = new StringRequest(url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String string) {
                        parseJsonData(string);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
                rQueue.add(request);
            }
        });


        btn_sign_out = findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                signOut();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void signOut() {
        startActivity(new Intent(this, login.class));
        finish();
    }

    void parseJsonData(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray fruitsArray = object.getJSONArray("items");
            ArrayList al = new ArrayList();

            for(int i = 0; i < fruitsArray.length(); ++i) {
                //al.add(fruitsArray.getString(i));
                JSONObject c = fruitsArray.getJSONObject(0);
                JSONObject id = c.getJSONObject("id");
                String videoId = id.getString("videoId");
                JSONObject c2 = fruitsArray.getJSONObject(1);
                JSONObject id2 = c2.getJSONObject("id");
                String videoId2 = id2.getString("videoId");
                JSONObject c3 = fruitsArray.getJSONObject(2);
                JSONObject id3 = c3.getJSONObject("id");
                String videoId3 = id3.getString("videoId");
                String na = "https://www.youtube.com/embed/";
                //al.add(na + videoId);
                String de = (na + videoId );
                String de2 = (na + videoId2 );
                String de3 = (na + videoId3 );
                wb = findViewById(R.id.wb);
                wb.setWebChromeClient(new WebChromeClient());
                wb.getSettings().setJavaScriptEnabled(true);
                wb.getSettings().setAllowFileAccess(true);
                wb.loadUrl(de);
                wb.setWebChromeClient(new WebChromeClient());
                wb = findViewById(R.id.wb2);
                wb.getSettings().setJavaScriptEnabled(true);
                wb.getSettings().setAllowFileAccess(true);
                wb.loadUrl(de2);
                wb = findViewById(R.id.wb3);
                wb.setWebChromeClient(new WebChromeClient());
                wb.getSettings().setJavaScriptEnabled(true);
                wb.getSettings().setAllowFileAccess(true);
                wb.loadUrl(de3);





            }
            //ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, al);
            //fruitsList.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }

}