package com.smartplace.alerta.alerts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.melnykov.fab.FloatingActionButton;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.smartplace.alerta.MemoryServices;
import com.smartplace.alerta.R;
import com.smartplace.alerta.Utilities.TransparentProgressDialog;
import com.smartplace.alerta.WebServices;
import com.smartplace.alerta.admin.AdminInfo;
import com.smartplace.alerta.admin.CapInfo;
import com.smartplace.alerta.cap.CapEntry;
import com.smartplace.alerta.cap.CapFeed;
import com.smartplace.alerta.coverflow.FancyCoverFlow;
import com.smartplace.alerta.coverflow.FancyCoverFlowSampleAdapter;
import com.smartplace.alerta.coverflow.FancyCoverFlowStatusAdapter;
import com.smartplace.alerta.family.members.FamilyMember;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * to handle interaction events.
 * Use the {@link AlertsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AlertsFragment extends Fragment implements TransparentProgressDialog.OnProgressDialogListener {

    private boolean isAttached;
    private ListView mAlertsList;
    private CapFeed mCapFeed;
    private ArrayList mCapEntries;
    private SwingBottomInAnimationAdapter mAnimAlertsListAdapter;
    AdminInfo mAdminInfo;
    private TransparentProgressDialog mLoadingDialog;
    private AlertDialog mHelpDialog;
    private boolean mIsDialogActive = false;
    private PullToRefreshLayout mPullToRefreshLayout;
    private TextView mTxtGettingInfo;
    private int mPosition = 0;
    private ArrayList<TextView> mCapOptions;
    //private ProgressDialog mLoadingDialog;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlertsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlertsFragment newInstance() {
        AlertsFragment fragment = new AlertsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AlertsFragment() {
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_alerts, container, false);
        mAlertsList = (ListView) v.findViewById(R.id.list_entry_items);
        mPullToRefreshLayout = (PullToRefreshLayout) v.findViewById(R.id.ptr_layout);
        mTxtGettingInfo = (TextView)v.findViewById(R.id.txt_cap_empty);
        HorizontalScrollView scrollView = (HorizontalScrollView)v.findViewById(R.id.scrollview_caps);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT,1f);
        params.gravity = Gravity.CENTER;
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER);

        mAdminInfo = new Gson().fromJson(MemoryServices.getAdminInfo(getActivity()), AdminInfo.class);

        Typeface titleFont= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSansLight.ttf");
        mTxtGettingInfo.setTypeface(titleFont);

        mCapOptions = new ArrayList<TextView>();
        for(int i = 0; i< mAdminInfo.getCaps().size();i++){
            TextView textView = new TextView(getActivity());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            textView.setText(mAdminInfo.getCaps().get(i).getName());
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(30, 0, 30, 0);
            textView.setTypeface(titleFont);
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
                    setCAPInformation(finalI);
                }
            });
            mCapOptions.add(textView);
            linearLayout.addView(textView);
            if(i<(mAdminInfo.getCaps().size()-1)){
                View view = new View(getActivity());
                view.setLayoutParams(new LinearLayout.LayoutParams(2, 40));
                view.setBackgroundColor(getResources().getColor(android.R.color.white));
                linearLayout.addView(view);
            }
        }
        scrollView.addView(linearLayout);

        mCapOptions.get(0).setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        mCapEntries = new ArrayList();


        ImageButton helpImageBtn = (ImageButton) v.findViewById(R.id.button_floating_action);
        helpImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MemoryServices.getUserID(getActivity()).equals("")) {
                    showHelpDialog();
                }else{
                    Toast.makeText(getActivity(),getString(R.string.register_missing),Toast.LENGTH_LONG).show();
                }
            }
        });


        // Now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                // Mark All Children as pull-able
                .allChildrenArePullable()
                        // Set the OnRefreshListener
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {

                        WebServices.getCAP(mAdminInfo.getCaps().get(mPosition).getName(),mAdminInfo.getCaps().get(mPosition).getUrl(), getCapHandler);
                    }
                })
                        // Finally commit the setup to our PullToRefreshLayout
                .setup(mPullToRefreshLayout);

        setCAPInformation(0);

        mAlertsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), AlertDetailedActivity.class);
                String capEntry = new Gson().toJson(mCapEntries.get(i));
                intent.putExtra("entry", capEntry);
                startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isAttached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isAttached = false;
    }
    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.alerts, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:

                Intent i = new Intent(getActivity(),AlertsConfigActivity.class);
                startActivity(i);
                return true;
            case R.id.action_refresh:
                mPullToRefreshLayout.setRefreshing(true);
                WebServices.getCAP(mAdminInfo.getCaps().get(mPosition).getName(), mAdminInfo.getCaps().get(mPosition).getUrl(), getCapHandler);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void setCAPInformation(int position){

        mPosition = position;
        mCapEntries.clear();
        mCapFeed = new Gson().fromJson(MemoryServices.getCAPInfo(getActivity(), mAdminInfo.getCaps().get(position).getName()), CapFeed.class);
        setCapItems();
        if(mCapFeed.getEntries().size()==0){
            mTxtGettingInfo.setVisibility(View.VISIBLE);
            mPullToRefreshLayout.setRefreshing(true);
            WebServices.getCAP(mAdminInfo.getCaps().get(mPosition).getName(),mAdminInfo.getCaps().get(mPosition).getUrl(), getCapHandler);

        }else{
            mTxtGettingInfo.setVisibility(View.INVISIBLE);
        }

        AlertsListAdapter alertsListAdapter = new AlertsListAdapter(getActivity(), mCapEntries);
        mAnimAlertsListAdapter = new SwingBottomInAnimationAdapter(alertsListAdapter);
        mAnimAlertsListAdapter.setAbsListView(mAlertsList);
        mAlertsList.setAdapter(mAnimAlertsListAdapter);

    }

    public void showHelpDialog()
    {


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View promptsView = inflater.inflate(R.layout.prompt_help_dialog, null);
        final FancyCoverFlow fancyCoverFlow = (FancyCoverFlow) promptsView.findViewById(R.id.fancyCoverFlow);
        final TextView txtStatusType = (TextView) promptsView.findViewById(R.id.txt_status_type);
        alertDialogBuilder.setView(promptsView);


        final String[] statusTypes= getResources().getStringArray(R.array.status_types_array);

        fancyCoverFlow.setAdapter(new FancyCoverFlowStatusAdapter());
        fancyCoverFlow.setUnselectedAlpha(1.0f);
        fancyCoverFlow.setUnselectedSaturation(0.0f);
        fancyCoverFlow.setUnselectedScale(0.5f);
        fancyCoverFlow.setSpacing(10);
        fancyCoverFlow.setMaxRotation(0);
        fancyCoverFlow.setSelection(1);
        fancyCoverFlow.setScaleDownGravity(1f);
        fancyCoverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtStatusType.setText(statusTypes[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);

        Typeface titleFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSansLight.ttf");
        ((TextView)promptsView.findViewById(R.id.txt_header_help)).setTypeface(titleFont);
        ((TextView)promptsView.findViewById(R.id.txt_help_instructions  )).setTypeface(titleFont);
        txtStatusType.setTypeface(titleFont);

        // set dialog message
        alertDialogBuilder
                .setNegativeButton(getString(R.string.options_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }
                )
                .setPositiveButton(getString(R.string.confirm),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mIsDialogActive = true;
                                mLoadingDialog = new TransparentProgressDialog(getActivity(), R.drawable.ic_loading);
                                mLoadingDialog.show();
                                mLoadingDialog.tv.setText(getString(R.string.sending_alert));
                                String[] location = MemoryServices.getUserLocation(getActivity()).split(",");
                                WebServices.sendAlert(MemoryServices.getUserID(getActivity()),
                                        String.valueOf(fancyCoverFlow.getSelectedItemPosition()+1),location[0],location[1],mSendAlertHandler);
                            }

                        }
                );
        // create alert dialog
        mHelpDialog= alertDialogBuilder.create();
        // show it
        mHelpDialog.show();
    }
    private void setCapItems(){
        String[] selectedStates = MemoryServices.getConfigStates(getActivity()).split(",");
        //save references to local variables
        for (int i3 = 0; i3 < mCapFeed.getEntries().size(); i3++) {
            CapEntry capEntry = mCapFeed.getEntries().get(i3);
            //if (alertsAdded < Constants.ITEMS_PER_PAGE) {
            try {
                String area = capEntry.getContent().getAlert().getInfo().get(0).getArea().get(0).getAreaDesc();
                String[] states = area.split(",");
                //Go through all alert states
                for (int i = 0; i < states.length; i++) {
                    String currentState = states[i];
                    //Go through all selected states to check for a match
                    for (int i2 = 0; i2 < selectedStates.length; i2++) {
                        String currentSelectedState = selectedStates[i2];
                        //Check for a match
                        if (currentState.equals(currentSelectedState)) {
                            mCapEntries.add(capEntry);
                            i2 = selectedStates.length;
                            i = states.length;
                        }
                    }
                }
            } catch (NullPointerException e) {

            }
            //} else {
            //    i3 = mCapFeed.getEntries().size();
            //}
        }

    }
    private Handler getCapHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String response = bundle != null ? bundle.getString("response") : "";
            String name = bundle != null ? bundle.getString("name") : "";

            if (isAttached) {

                if(mPullToRefreshLayout.isRefreshing()){
                    mPullToRefreshLayout.setRefreshComplete();
                }

                mTxtGettingInfo.setVisibility(View.INVISIBLE);
                //mLoadingDialog.dismiss();
                if ((response.equals("")) || response.equals("no connection")) {
                /*There is an issue with the response, no data received*/
                } else {

                    try {
                        mCapFeed = new Gson().fromJson(response, CapFeed.class);
                        mCapEntries.clear();
                        for (CapEntry entry : mCapFeed.getEntries()) {
                            mCapEntries.add(entry);
                        }
                        mAnimAlertsListAdapter.notifyDataSetChanged();
                        MemoryServices.setCAPInfo(getActivity(), response, name);

                    } catch (JsonSyntaxException e) {

                    }
                }
            }
        }
    };

    private Handler mSendAlertHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String response = bundle != null ? bundle.getString("response") : "";
            String type = bundle != null ? bundle.getString("type") : "";

            if (mIsDialogActive) {

                mIsDialogActive = false;
                mLoadingDialog.dismiss();
                //mLoadingDialog.dismiss();
                if ((response.equals("")) || response.equals("no connection")) {
                /*There is an issue with the response, no data received*/
                } else if(response.equals("NOK1")) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.invalid_parameters),Toast.LENGTH_SHORT).show();

                }else if(response.equals("OK")){
                    mHelpDialog.dismiss();
                    FamilyMember userInfo = new Gson().fromJson(MemoryServices.getUserInfo(getActivity()),FamilyMember.class);
                    userInfo.setStatus(type);
                    MemoryServices.setUserInfo(getActivity(),new Gson().toJson(userInfo));
                    Toast.makeText(getActivity(),getString(R.string.succesfull_alert),Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    @Override
    public void onDialogCancelled() {
        mIsDialogActive = false;
    }
}
