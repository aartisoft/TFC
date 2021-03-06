package com.dev.tfc.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.dev.tfc.R;
import com.dev.tfc.adapter.Homeadapter;
import com.dev.tfc.model.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.RECEIVER_VISIBLE_TO_INSTANT_APPS;

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

   /* private String Proname;
    private String Proimage;
    private String Username;
    private String Timing;
    private String Price;
    private String Quantity;
    private String Availimg, CollectionTime, Logoo, Addresss,UserName;
    private String Count;
    private String Fav;
    private String resID, menuID, Quantityyy;*/
    private EditText etsearchcity;
    // private String qty,price;
    private  TextView tvempty;

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



        rcview = v.findViewById(R.id.rc_home);
        tvempty=v.findViewById(R.id.tv_emptytext);






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

                    SharedPreferences preferences =getContext().getSharedPreferences("usercityy", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();


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





    private void Fetchcity() {

        pd = new ProgressDialog(getContext());
        pd.setMessage("loading");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://thefoodcircle.co.uk/restaurant/demo/web-service/search_cityname.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response..........", response);
                     //   ressss = response;
                        homeList.clear();
                        pd.dismiss();

                        try {


                            JSONObject jsono = new JSONObject(response);

                            String res = jsono.getString("restaurents");


                            if (res.equals("null")) {

                              //  Toast.makeText(getContext(), "NUll Values", Toast.LENGTH_SHORT).show();
                                rcview.setVisibility(View.GONE);
                                tvempty.setVisibility(View.VISIBLE);

                            }else {



                            JSONArray jarray = jsono.getJSONArray("restaurents");



                            rcview.setVisibility(View.VISIBLE);


                                for (int i = 0; i < jarray.length(); i++) {
                                JSONObject object = jarray.getJSONObject(i);

                              String  Username = object.getString("name");
                                String  resID = object.getString("id");
                                String Logoo = object.getString("logo");
                                String Addresss = object.getString("address");
                                String  fav = object.getString("fav_staus");
                             //   int favv = Integer.parseInt(fav);


                                SharedPreferences sharedPreferencess = getContext().getSharedPreferences("resmenuDetailss", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferencess.edit();
                                editor.putString("log", Logoo);
                                editor.putString("add", Addresss);
                                editor.commit();

                                Log.i("resname", "" + Username);
                                Log.i("resId", "" + resID);
                                /*Home home = new Home();


                                homeList.add(home);*/


                                JSONObject jsono1 = new JSONObject(response);
                                jarray = jsono.getJSONArray("restaurents");
                                JSONArray jarray1 = object.getJSONArray("menu");



                                for (int j = 0; j < jarray1.length(); j++) {
                                    JSONObject object1 = jarray1.getJSONObject(j);
                                    Home homeE = new Home();

                                String    Proname = object1.getString("menu_name");
                                    String   Timing = object1.getString("collection_time");
                                    String   Price = object1.getString("menu_rate");
                                    String   Quantity = object1.getString("quantity_left");
                                    String   Proimage = object1.getString("img1");
                                    String   Availimg = object1.getString("img2");
                                   // String  Fav = object1.getString("img3");
                                    String  menuID = object1.getString("id");
                                    String   CollectionTime = object1.getString("collection_time");
                                    String   Foodtype = object1.getString("food_type");



                                    Log.i("menuId", "" + menuID);

                                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("resmenuDetails", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editorr = sharedPreferences.edit();
                                    editorr.putString("name", Proname);
                                    editorr.putString("resid", resID);
                                    editorr.putString("menuid", menuID);
                                    editorr.putString("quantityyy", Quantity);
                                    editorr.putString("ct", CollectionTime);
                                    editorr.commit();




                                    if (fav.equals("1")) {

                                        homeE.setFav(R.drawable.favselectedicon);
                                    } else if (fav.equals("0")){

                                        homeE.setFav(R.drawable.fav);
                                    }
                                    homeE.setUsername(Username);
                                    homeE.setRes(resID);
                                    homeE.setFavstatus(fav);



                                    if (Quantity.equals("0")){
                                        homeE.setTrans(R.drawable.bgtrans);
                                    }

                                    homeE.setProname(Proname);
                                    homeE.setTiming(Timing);
                                    homeE.setPrice(Price);
                                    homeE.setQuantity(Quantity);
                                    homeE.setProimage(Proimage);
                                    // home.setAvailimg(Availimg);

                                    if (Foodtype.equals("Veg")){
                                        homeE.setAvailimg(R.drawable.green);
                                    }else if (Foodtype.equals("Non-Veg")){
                                        homeE.setAvailimg(R.drawable.red);
                                    }


                                    //home.setFav(R.id.iv_fav);
                                    homeList.add(homeE);

                                }
                            }}
                           /* }else{
                               rcview.setVisibility(View.GONE);
                               tvempty.setVisibility(View.VISIBLE);
                            }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i("ressssss..", "" + response.toString());

                        rcview.setAdapter(homeadapter);
                        homeadapter.notifyDataSetChanged();
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
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



    public void checkString() {

        final SharedPreferences ct = getContext().getSharedPreferences("usercityy", MODE_PRIVATE);
        city = ct.getString("citttyy", null);



        if (TextUtils.isEmpty(city)){
            final SharedPreferences ctt = getContext().getSharedPreferences("etcity", MODE_PRIVATE);
            city = ctt.getString("etcity", null);
        }

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

                        try {


                            JSONObject jsono = new JSONObject(response);
                            JSONArray jarray = jsono.getJSONArray("restaurents");

                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject object = jarray.getJSONObject(i);

                             String   Username = object.getString("name");
                                String resID = object.getString("id");
                                String  Logoo = object.getString("logo");
                                String  Addresss = object.getString("address");
                                String  fav = object.getString("fav_staus");
                                //   int favv = Integer.parseInt(fav);


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


                                JSONObject jsono1 = new JSONObject(response);
                                jarray = jsono.getJSONArray("restaurents");
                                JSONArray jarray1 = object.getJSONArray("menu");

                                for (int j = 0; j < jarray1.length(); j++) {
                                    JSONObject object1 = jarray1.getJSONObject(j);
                                    Home homeE = new Home();

                                    String   Proname = object1.getString("menu_name");
                                    String   Timing = object1.getString("collection_time");
                                    String   Price = object1.getString("menu_rate");
                                    String   Quantity = object1.getString("quantity_left");
                                    String   Proimage = object1.getString("img1");
                                    String  Availimg = object1.getString("img2");
                                    String  Fav = object1.getString("img3");
                                    String menuID = object1.getString("id");
                                    String  CollectionTime = object1.getString("collection_time");
                                    String  Foodtype = object1.getString("food_type");
                                    Log.i("menuId", "" + menuID);

                                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("resmenuDetails", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editorr = sharedPreferences.edit();
                                    editorr.putString("name", Proname);
                                    editorr.putString("resid", resID);
                                    editorr.putString("menuid", menuID);
                                    editorr.putString("quantityyy", Quantity);
                                    editorr.putString("ct", CollectionTime);
                                    editorr.commit();


                                    if (TextUtils.isEmpty(Proname)){
                                        tvempty.setVisibility(View.VISIBLE);
                                        rcview.setVisibility(View.GONE);
                                    }


                                    if (Quantity.equals("0")){
                                        homeE.setTrans(R.drawable.bgtrans);
                                    }

                                    homeE.setProname(Proname);
                                    homeE.setTiming(Timing);
                                    homeE.setPrice(Price);
                                    homeE.setQuantity(Quantity);
                                    homeE.setProimage(Proimage);
                                    // home.setAvailimg(Availimg);

                                    if (Foodtype.equals("Veg")){
                                        homeE.setAvailimg(R.drawable.green);
                                    }else if (Foodtype.equals("Non-Veg")){
                                        homeE.setAvailimg(R.drawable.red);
                                    }


                                    //home.setFav(R.id.iv_fav);
                                    homeList.add(homeE);

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i("ressssss..", "" + response.toString());

                        rcview.setAdapter(homeadapter);
                        homeadapter.notifyDataSetChanged();



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

