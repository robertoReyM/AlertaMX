package com.smartplace.alerta.atlas;



import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.smartplace.alerta.MemoryServices;
import com.smartplace.alerta.R;
import com.smartplace.alerta.WebServices;
import com.smartplace.alerta.admin.AdminInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AtlasFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AtlasFragment extends Fragment {


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AtlasFragment.
     */

    private GoogleMap googleMap;
    MapView mMapView;
    private boolean mIsAttached = false;
    AdminInfo mAdminInfo;
    private int mPosition = 0;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ArrayList<TextView> mCapOptions;
    HorizontalScrollView mScrollView;

    public static AtlasFragment newInstance() {
        AtlasFragment fragment = new AtlasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public AtlasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_atlas, container, false);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mPullToRefreshLayout = (PullToRefreshLayout) v.findViewById(R.id.ptr_layout);
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.694857, -103.035121), 4));

         mScrollView = (HorizontalScrollView)v.findViewById(R.id.scrollview_atlas);

        mAdminInfo = new Gson().fromJson(MemoryServices.getAdminInfo(getActivity()), AdminInfo.class);

        // Now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                // Mark All Children as pull-able
                .allChildrenArePullable()
                        // Set the OnRefreshListener
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {

                        WebServices.getGeoJson(mAdminInfo.getAtlas().get(mPosition).getName(), mAdminInfo.getAtlas().get(mPosition).getUrl(), getGeoJsonHandler);
                    }
                })
                        // Finally commit the setup to our PullToRefreshLayout
                .setup(mPullToRefreshLayout);
        setAtlasButtons();

        mPullToRefreshLayout.setRefreshing(true);
        WebServices.getAdminInfo(mAdminInfoHandler);
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mIsAttached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mIsAttached = false;
    }
    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.atlas, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mPullToRefreshLayout.setRefreshing(true);
                WebServices.getAdminInfo(mAdminInfoHandler);
                //WebServices.getGeoJson(mAdminInfo.getAtlas().get(mPosition).getName(), mAdminInfo.getAtlas().get(mPosition).getUrl(), getGeoJsonHandler);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void setAtlasButtons(){
        mScrollView.removeAllViews();
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT,1f);
        params.gravity = Gravity.CENTER;
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER);


        mCapOptions = new ArrayList<TextView>();
        Typeface titleFont= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSansLight.ttf");
        for(int i = 0; i< mAdminInfo.getAtlas().size();i++){
            TextView textView = new TextView(getActivity());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            textView.setText(mAdminInfo.getAtlas().get(i).getName());
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(30, 0, 30, 0);
            textView.setTypeface(titleFont);
            textView.setLines(1);
            textView.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            textView.setTextColor(getResources().getColor(android.R.color.white));
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int index = 0; index<mCapOptions.size();index++){
                        if(index == finalI) {
                            mCapOptions.get(index).setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                        }else{
                            mCapOptions.get(index).setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        }
                    }
                    setAtlasInformation(finalI);
                }
            });
            mCapOptions.add(textView);
            linearLayout.addView(textView);
            if(i<(mAdminInfo.getAtlas().size()-1)){
                View view = new View(getActivity());
                view.setLayoutParams(new LinearLayout.LayoutParams(2, 40));
                view.setBackgroundColor(getResources().getColor(android.R.color.white));
                linearLayout.addView(view);
            }
        }
        mScrollView.addView(linearLayout);


        try {
            mCapOptions.get(0).setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            setAtlasInformation(0);
        }catch (IndexOutOfBoundsException e){

        }
    }
    private void setAtlasInformation(int position){

        if(mAdminInfo.getAtlas().size()!=0) {
            mPosition = position;
            mAdminInfo.getAtlas().get(position);
            googleMap.clear();
            setGeoJsonIntoMaps(MemoryServices.getAtlasInfo(getActivity(), mAdminInfo.getAtlas().get(position).getName()));
        }
    }
    private Handler getGeoJsonHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();

            if (mIsAttached) {

                if(mPullToRefreshLayout.isRefreshing()){
                    mPullToRefreshLayout.setRefreshComplete();
                }
                //mLoadingDialog.dismiss();
                String response = bundle != null ? bundle.getString("response") : "";
                String name = bundle != null ? bundle.getString("name") : "";
                if ((response.equals("")) || response.equals("no connection")) {
                /*There is an issue with the response, no data received*/
                    Toast.makeText(getActivity(),getString(R.string.data_not_available),Toast.LENGTH_SHORT).show();
                } else {
                    if(setGeoJsonIntoMaps(response)){
                        MemoryServices.setAtlasInfo(getActivity(),response,name);
                    }


                }
            }
        }
    };

    private boolean setGeoJsonIntoMaps(String geojson){
        try {
            JSONObject jsonObject = new JSONObject(geojson);
            JSONArray featuresArray = jsonObject.getJSONArray("features");
            googleMap.clear();
            if(featuresArray.length() == 0){
                mPullToRefreshLayout.setRefreshing(true);
                WebServices.getGeoJson(mAdminInfo.getAtlas().get(mPosition).getName(), mAdminInfo.getAtlas().get(mPosition).getUrl(), getGeoJsonHandler);
            }else {
                for (int i = 0; i < featuresArray.length(); i++) {
                    JSONObject jsonGeometry = featuresArray.getJSONObject(i).getJSONObject("geometry");
                    String type = jsonGeometry.getString("type");
                    if (type.equals("MultiPolygon")) {
                        JSONArray polygonCordsArray = jsonGeometry.getJSONArray("coordinates");
                        for (int i2 = 0; i2 < polygonCordsArray.length(); i2++) {
                            // Instantiates a new Polygon object and adds points to define a rectangle
                            PolygonOptions rectOptions = new PolygonOptions()
                                    .strokeColor(getResources().getColor(R.color.main_alerts))
                                    .fillColor(getResources().getColor(R.color.main_alerts_transparent));
                            JSONArray linearRingCordsArray = polygonCordsArray.getJSONArray(i2);
                            for (int i3 = 0; i3 < linearRingCordsArray.length(); i3++) {
                                JSONArray positionCordsArray = linearRingCordsArray.getJSONArray(i3);
                                for (int i4 = 0; i4 < positionCordsArray.length(); i4++) {
                                    JSONArray cordsArray = positionCordsArray.getJSONArray(i4);
                                    double lng = cordsArray.getDouble(0);
                                    double lat = cordsArray.getDouble(1);
                                    rectOptions.add(new LatLng(lat, lng));
                                    Log.v("alerta", "lat = " + lat + " , " + "lng = " + lng);
                                }

                                // Get back the mutable Polygon
                                Polygon polygon = googleMap.addPolygon(rectOptions);
                            }
                        }
                    }else if(type.equals("Point")){
                        JSONArray cordsArray = jsonGeometry.getJSONArray("coordinates");
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.valueOf(cordsArray.getString(1)), Double.valueOf(cordsArray.getString(0)))));
                    }
                }
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  false;
    }
    private Handler mAdminInfoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String response = bundle != null ? bundle.getString("response") : "";
            String name = bundle != null ? bundle.getString("name") : "";

                //mLoadingDialog.dismiss();
                if ((response.equals("")) || response.equals("no connection")) {
                /*There is an issue with the response, no data received*/
                } else {

                    try {

                        mAdminInfo = new Gson().fromJson(response,AdminInfo.class);
                        MemoryServices.setAdminInfo(getActivity(),response);
                        setAtlasButtons();
                        try {
                            WebServices.getGeoJson(mAdminInfo.getAtlas().get(mPosition).getName(), mAdminInfo.getAtlas().get(mPosition).getUrl(), getGeoJsonHandler);
                        }catch (IndexOutOfBoundsException e){

                            if(mPullToRefreshLayout.isRefreshing()){
                                mPullToRefreshLayout.setRefreshComplete();
                            }
                        }
                    } catch (JsonSyntaxException e) {

                    }
                }
            }

    };

}
