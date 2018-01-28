package com.infosolution.dev.tfc.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infosolution.dev.tfc.Class.ConfigInfo;
import com.infosolution.dev.tfc.Class.RecyclerItemClickListener;
import com.infosolution.dev.tfc.R;
import com.infosolution.dev.tfc.activities.BuyNowActivity;
import com.infosolution.dev.tfc.activities.MainActivity;
import com.infosolution.dev.tfc.activities.ProductPageActivity;
import com.infosolution.dev.tfc.adapter.Homeadapter;
import com.infosolution.dev.tfc.model.Home;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.KeyManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  {

    private RecyclerView rcview;
    private Homeadapter homeadapter;
    //    private List<Home> homeList;
    private String city;
    private ArrayList<Home> homeList;
    private ProgressDialog pd;

    private String Proname;
    private String Proimage;
    private String Username;
    private String Timing;
    private String Price;
    private String Quantity;
    private String Availimg, CollectionTime, Logoo, Addresss;
    private String Count;
    private String Fav;
    private String resID, menuID, Quantityyy;
    private EditText etsearchcity;
    // private String qty,price;

    private String ressss, UserId, fav,resuser,Foodtype ;


    public HomeFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        final SharedPreferences prefss = getContext().getSharedPreferences("useriddsign", MODE_PRIVATE);
        UserId = prefss.getString("userid", null);




        Log.i("cityuser", "" + city);

        checkString();

       /* Intent intent=getActivity().getIntent();
        city=intent.getStringExtra("citttyy");*/

        rcview = v.findViewById(R.id.rc_home);






        rcview.setLayoutManager(new LinearLayoutManager(getContext()));
        rcview.setHasFixedSize(true);
        rcview.setAdapter(homeadapter);
        homeList = new ArrayList<>();
        homeadapter = new Homeadapter(homeList, getContext(), getActivity());


        etsearchcity = v.findViewById(R.id.et_searchcity);
        etsearchcity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                 //   city = etsearchcity.getText().toString();

                    SharedPreferences shared = getContext().getSharedPreferences("etcity", Context.MODE_PRIVATE);
                    SharedPreferences.Editor et = shared.edit();
                    et.putString("etcity", etsearchcity.getText().toString());
                    et.commit();




                    checkString();

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etsearchcity.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });





        return v;
    }


    public class FetchEventPreviousDataTask extends AsyncTask<Object, Object, Boolean> {

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(getContext());
            pd.setMessage("loading");
            pd.show();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {
                String dataurl = "http://thefoodcircle.co.uk/restaurant/demo/web-service/res_list.php";
                HttpPost httppost = new HttpPost(dataurl);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    JSONObject jsono = new JSONObject(resuser);
                    JSONArray jarray = jsono.getJSONArray("restaurents");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        Username = object.getString("name");
                        resID = object.getString("id");
                        Logoo = object.getString("logo");
                        Addresss = object.getString("address");
                        fav = object.getString("fav_staus");
                        int favv = Integer.parseInt(fav);


                        SharedPreferences sharedPreferencess = getContext().getSharedPreferences("resmenuDetailss", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferencess.edit();
                        editor.putString("log", Logoo);
                        editor.putString("add", Addresss);
                        editor.commit();

                        Log.i("resname", "" + Username);
                        Log.i("resId", "" + resID);
                        Home home = new Home();

                        if (fav.equals(1)) {

                            home.setFav(R.drawable.favselectedicon);
                        } else if (fav.equals(0)){

                            home.setFav(R.drawable.fav);
                        }
                        home.setUsername(Username);
                        home.setRes(resID);
                        home.setFavstatus(fav);


                        JSONObject jsono1 = new JSONObject(resuser);
                        jarray = jsono.getJSONArray("restaurents");
                        JSONArray jarray1 = object.getJSONArray("menu");

                        for (int j = 0; j < jarray1.length(); j++) {
                            JSONObject object1 = jarray1.getJSONObject(j);

                            Proname = object1.getString("menu_name");
                            Timing = object1.getString("collection_time");
                            Price = object1.getString("menu_rate");
                            Quantity = object1.getString("quantity_left");
                            Proimage = object1.getString("img1");
                            Availimg = object1.getString("img2");
                            Fav = object1.getString("img3");
                            menuID = object1.getString("id");
                            CollectionTime = object1.getString("collection_time");
                            Foodtype = object1.getString("food_type");
                            Log.i("menuId", "" + menuID);

                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("resmenuDetails", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorr = sharedPreferences.edit();
                            editorr.putString("name", Proname);
                            editorr.putString("resid", resID);
                            editorr.putString("menuid", menuID);
                            editorr.putString("quantityyy", Quantity);
                            editorr.putString("ct", CollectionTime);
                            editorr.commit();


                            home.setProname(Proname);
                            home.setTiming(Timing);
                            home.setPrice(Price);
                            home.setQuantity(Quantity);
                            home.setProimage(Proimage);
                           // home.setAvailimg(Availimg);

                            if (Foodtype.equals("Veg")){
                                home.setAvailimg(R.drawable.green);
                            }else if (Foodtype.equals("Non-Veg")){
                                home.setAvailimg(R.drawable.red);
                            }


                            //home.setFav(R.id.iv_fav);
                            homeList.add(home);

                        }


                    }


                    return true;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            rcview.setAdapter(homeadapter);
            homeadapter.notifyDataSetChanged();
        }
    }


    private void Fetchcity() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://thefoodcircle.co.uk/restaurant/demo/web-service/search_cityname.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response..........", response);
                        ressss = response;
                        new FetchEventPreviousData().execute();
                        // Toast.makeText(CommentsActivity.this, response.toString(), Toast.LENGTH_LONG).show();

                        Log.i("ressssss..", "" + response.toString());


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     //   Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("city", city);
                params.put("userid", UserId);

                Log.i("par", "" + params);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    public class FetchEventPreviousData extends AsyncTask<Object, Object, Boolean> {

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(getContext());
            pd.setMessage("loading");
            pd.show();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {
              /*  String dataurl = "http://thefoodcircle.co.uk/restaurant/demo/web-service/res_list.php";
                HttpPost httppost = new HttpPost(dataurl);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);*/
                JSONObject jsono = new JSONObject(ressss);
                JSONArray jarray = jsono.getJSONArray("restaurents");

                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject object = jarray.getJSONObject(i);

                    Username = object.getString("name");
                    resID = object.getString("id");
                    Logoo = object.getString("logo");
                    Addresss = object.getString("address");
                    fav = object.getString("fav_staus");
                    int favv = Integer.parseInt(fav);

                    SharedPreferences sharedPreferencess = getContext().getSharedPreferences("resmenuDetailss", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencess.edit();
                    editor.putString("log", Logoo);
                    editor.putString("add", Addresss);
                    editor.commit();

                    Log.i("resname", "" + Username);
                    Log.i("resId", "" + resID);
                    Home home = new Home();


                    if (favv == 1) {

                        home.setFav(R.drawable.favselectedicon);
                    } else {
                        home.setFav(R.drawable.fav);
                    }
                    home.setUsername(Username);
                    home.setRes(resID);
                    home.setFavstatus(fav);


                    JSONObject jsono1 = new JSONObject(ressss);
                    jarray = jsono.getJSONArray("restaurents");
                    JSONArray jarray1 = object.getJSONArray("menu");

                    for (int j = 0; j < jarray1.length(); j++) {
                        JSONObject object1 = jarray1.getJSONObject(j);

                        Proname = object1.getString("menu_name");
                        Timing = object1.getString("collection_time");
                        Price = object1.getString("menu_rate");
                        Quantity = object1.getString("quantity_left");
                        Proimage = object1.getString("img1");
                        Availimg = object1.getString("img2");
                        Fav = object1.getString("img3");
                        CollectionTime = object1.getString("collection_time");
                        Foodtype = object1.getString("food_type");
                        menuID = object1.getString("id");
                        Log.i("menuId", "" + menuID);

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("resmenuDetails", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorr = sharedPreferences.edit();
                        editorr.putString("name", Proname);
                        editorr.putString("resid", resID);
                        editorr.putString("menuid", menuID);
                        editorr.putString("ct", CollectionTime);
                        editorr.putString("quantityyy", Quantity);
                        editorr.commit();


                        home.setProname(Proname);
                        home.setTiming(Timing);
                        home.setPrice(Price);
                        home.setQuantity(Quantity);
                        home.setProimage(Proimage);
                       // home.setAvailimg(Availimg);

                        if (Foodtype.equals("Veg")){
                            home.setAvailimg(R.drawable.green);
                        }else if (Foodtype.equals("Non-Veg")){
                            home.setAvailimg(R.drawable.red);
                        }


                        //home.setFav(R.id.iv_fav);
                        homeList.add(home);

                    }


                }


                return true;
//                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            rcview.setAdapter(homeadapter);
            homeadapter.notifyDataSetChanged();
        }
    }

    public void checkString() {
        final SharedPreferences ct = getContext().getSharedPreferences("usercityy", MODE_PRIVATE);
        city = ct.getString("citttyy", null);

        final SharedPreferences ctt = getContext().getSharedPreferences("etcity", MODE_PRIVATE);
        city = ctt.getString("etcity", null);



        if (TextUtils.isEmpty(city)) {
           // Toast.makeText(getContext(),"City Not Found",Toast.LENGTH_LONG).show();
            SendId();



        } else {
         //   Toast.makeText(getContext(),"City Found",Toast.LENGTH_LONG).show();
            Fetchcity();


        }


    }


    private void SendId() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://thefoodcircle.co.uk/restaurant/demo/web-service/res_list.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Log.e("response..........", response.toString());
                        resuser = response;
                        new FetchEventPreviousDataTask().execute();
                        Log.i("resuserr",""+resuser);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", UserId);

                Log.i("parr", "" + params);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }




}

