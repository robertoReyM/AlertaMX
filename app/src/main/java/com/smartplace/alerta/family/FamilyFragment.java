package com.smartplace.alerta.family;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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
import com.smartplace.alerta.coverflow.FancyCoverFlow;
import com.smartplace.alerta.coverflow.FancyCoverFlowSampleAdapter;
import com.smartplace.alerta.coverflow.FancyCoverFlowStatusAdapter;
import com.smartplace.alerta.family.members.FamilyInfo;
import com.smartplace.alerta.family.members.FamilyMember;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FamilyFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FamilyFragment extends Fragment implements TransparentProgressDialog.OnProgressDialogListener{


    private ListView mPeopleList;
    private ArrayList mPeopleItems;
    private SwingBottomInAnimationAdapter mAnimPeopleListAdapter;
    private ScrollView mLayoutLogin;
    private ScrollView mLayoutNoFamily;
    private LinearLayout mLayoutList;
    private ImageView mImageStatus;
    private ImageView mImageUser;
    private TextView mStatusUpdate;
    private TextView mUserName;

    private FormEditText mEditName;
    private FormEditText mEditMobile;
    private FancyCoverFlow mFancyCoverFlow;
    private FamilyMember mUserInfo;

    private TransparentProgressDialog mLoadingDialog;
    private AlertDialog mAddFamilyAlertDialog;
    private boolean mIsDialogActive = false;
    private AlertDialog mHelpDialog;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FamilyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FamilyFragment newInstance() {
        FamilyFragment fragment = new FamilyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public FamilyFragment() {
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
        View v = inflater.inflate(R.layout.fragment_family, container, false);
        mPeopleList = (ListView) v.findViewById(R.id.list_people_items);
        mLayoutList = (LinearLayout) v.findViewById(R.id.layout_list);
        mLayoutLogin = (ScrollView) v.findViewById(R.id.layout_login);
        mLayoutNoFamily = (ScrollView) v.findViewById(R.id.layout_no_family);

        mImageUser = (ImageView)v.findViewById(R.id.image_people);
        mImageStatus = (ImageView)v.findViewById(R.id.image_status);
        mUserName = (TextView) v.findViewById(R.id.name_people);
        mStatusUpdate = (TextView)v.findViewById(R.id.txt_status_update);

        mEditName = (FormEditText)v.findViewById(R.id.edit_name);
        mEditMobile = (FormEditText)v.findViewById(R.id.edit_mobile);
        mFancyCoverFlow = (FancyCoverFlow) v.findViewById(R.id.fancyCoverFlow);
        final TextView txtUserType = (TextView)v.findViewById(R.id.txt_user_type);
        Button btnRegister = (Button)v.findViewById(R.id.btn_register);

        Button btnAddFamilyMember = (Button)v.findViewById(R.id.btn_add_familiy);

        mUserInfo = new Gson().fromJson(MemoryServices.getUserInfo(getActivity()),FamilyMember.class);
        LinearLayout layoutUser = (LinearLayout)v.findViewById(R.id.layout_user);
        layoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelpDialog();
            }
        });

        mPeopleItems = new ArrayList();
        FamilyInfo familyInfo = new Gson().fromJson(MemoryServices.getFamilyInfo(getActivity()),FamilyInfo.class);
        for(FamilyMember familyMember :familyInfo.getFamilyMembers()){
            mPeopleItems.add(familyMember);
        }
        FamilyItemAdapter familyItemAdapter = new FamilyItemAdapter(getActivity(), mPeopleItems);
        mAnimPeopleListAdapter = new SwingBottomInAnimationAdapter(familyItemAdapter);
        mAnimPeopleListAdapter.setAbsListView(mPeopleList);
        mPeopleList.setAdapter(mAnimPeopleListAdapter);

        if (MemoryServices.getUserID(getActivity()).equals("")) {
            mLayoutLogin.setVisibility(View.VISIBLE);
            mLayoutNoFamily.setVisibility(View.GONE);
            mLayoutList.setVisibility(View.GONE);
        } else {
            mLayoutLogin.setVisibility(View.GONE);
            mLayoutList.setVisibility(View.VISIBLE);
            setUserInfo();
            if(mPeopleItems.size()==0){
                mLayoutNoFamily.setVisibility(View.VISIBLE);
            }else{
                mLayoutNoFamily.setVisibility(View.GONE);
            }
        }

        final String[] userTypes= getResources().getStringArray(R.array.user_types_array);

        mFancyCoverFlow.setAdapter(new FancyCoverFlowSampleAdapter());
        mFancyCoverFlow.setUnselectedAlpha(1.0f);
        mFancyCoverFlow.setUnselectedSaturation(0.0f);
        mFancyCoverFlow.setUnselectedScale(0.5f);
        mFancyCoverFlow.setSpacing(10);
        mFancyCoverFlow.setMaxRotation(0);
        mFancyCoverFlow.setSelection(2);
        mFancyCoverFlow.setScaleDownGravity(1f);
        mFancyCoverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtUserType.setText(userTypes[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mFancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);

        Typeface titleFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSansLight.ttf");
        mUserName.setTypeface(titleFont);
        mStatusUpdate.setTypeface(titleFont);
        mEditName.setTypeface(titleFont);
        mEditMobile.setTypeface(titleFont);
        ((TextView)v.findViewById(R.id.txt_user_type)).setTypeface(titleFont);
        ((TextView)v.findViewById(R.id.txt_who_am_i)).setTypeface(titleFont);
        ((TextView)v.findViewById(R.id.txt_login_instructions)).setTypeface(titleFont);
        ((TextView)v.findViewById(R.id.txt_header_no_family)).setTypeface(titleFont);
        ((TextView)v.findViewById(R.id.txt_no_family_instructions)).setTypeface(titleFont);
        btnRegister.setTypeface(titleFont);
        btnAddFamilyMember.setTypeface(titleFont);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(onFieldsValidation()){
                    mIsDialogActive = true;
                    mLoadingDialog = new TransparentProgressDialog(getActivity(), R.drawable.ic_loading);
                    mLoadingDialog.show();
                    mLoadingDialog.tv.setText(getString(R.string.creating_account));
                    WebServices.userRegister(mEditMobile.getText().toString(),mEditName.getText().toString(),
                            String.valueOf(mFancyCoverFlow.getSelectedItemPosition()+1),MemoryServices.getPushToken(getActivity()), mRegisterHandler);
                }
                //Toast.makeText(getBaseContext(),String.valueOf(mFancyCoverFlow.getSelectedItemPosition()),Toast.LENGTH_SHORT).show();
            }
        });
        btnAddFamilyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFamilyMemberDialog();
            }
        });
        return  v;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!MemoryServices.getUserID(getActivity()).equals("")){

            mUserInfo = new Gson().fromJson(MemoryServices.getUserInfo(getActivity()),FamilyMember.class);
            setUserInfo();
            WebServices.getFamilyMembers(MemoryServices.getUserID(getActivity()),mGetFamilyMembersHandler);

        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public boolean onFieldsValidation() {
        FormEditText[] allFields    = { mEditName, mEditMobile};


        boolean allValid = true;
        for (FormEditText field: allFields) {
            allValid = field.testValidity() && allValid;
        }

        if (allValid) {
            // YAY
            return  true;
        } else {
            // EditText are going to appear with an exclamation mark and an explicative message.
            return  false;
        }
    }
    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.family, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                if(!MemoryServices.getUserID(getActivity()).equals("")) {
                    showAddFamilyMemberDialog();
                }else{
                    Toast.makeText(getActivity(),getActivity().getString(R.string.register_first),Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void showAddFamilyMemberDialog()
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View promptsView = inflater.inflate(R.layout.prompt_add_family_member, null);
        TextView headerAbandon = (TextView)promptsView.findViewById(R.id.header_add_family);
        Button btnAddFamily = (Button)promptsView.findViewById(R.id.btn_add_familiy);
        final FormEditText editMobile = (FormEditText)promptsView.findViewById(R.id.edit_mobile);

        Typeface titleFont= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSansLight.ttf");
        headerAbandon.setTypeface(titleFont);
        btnAddFamily.setTypeface(titleFont);
        editMobile.setTypeface(titleFont);
        alertDialogBuilder.setView(promptsView);
        btnAddFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editMobile.testValidity()) {
                    mIsDialogActive = true;
                    mLoadingDialog = new TransparentProgressDialog(
                            getActivity(), R.drawable.ic_loading);
                    mLoadingDialog.show();
                    if (mLoadingDialog != null) {
                        mLoadingDialog.tv.setText(getString(R.string.adding_family_member));
                    }
                    WebServices.addFamilyMember(
                            MemoryServices.getUserID(getActivity()), editMobile.getText().toString(), mAddFamilyHandler);
                }
            }
        });
        mAddFamilyAlertDialog = alertDialogBuilder.create();
        // show it
        mAddFamilyAlertDialog.show();
    }

    private void setUserInfo(){
        int type = Integer.valueOf(mUserInfo.getType());
        switch (type){
            case 1:
                mImageUser.setImageResource(R.drawable.ic_man);
                break;
            case 2:
                mImageUser.setImageResource(R.drawable.ic_woman);
                break;
            case 3:
                mImageUser.setImageResource(R.drawable.ic_children);
                break;
            case 4:
                mImageUser.setImageResource(R.drawable.ic_pregnant);
                break;
            case 5:
                mImageUser.setImageResource(R.drawable.ic_handicap);
                break;
        }
        int status = Integer.valueOf(mUserInfo.getStatus());
        switch (status){
            case 1:
                mImageStatus.setImageResource(R.drawable.ic_status_trapped);
                break;
            case 2:
                mImageStatus.setImageResource(R.drawable.ic_status_ok);
                break;
            case 3:
                mImageStatus.setImageResource(R.drawable.ic_status_medical_aid);
                break;
        }
        mUserName.setText(mUserInfo.getName());
        mStatusUpdate.setText("");
        mStatusUpdate.setVisibility(View.GONE);
    }
    private Handler mRegisterHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String response = bundle != null ? bundle.getString("response") : "";
            String mobile = bundle != null ? bundle.getString("mobile") : "";
            String name = bundle != null ? bundle.getString("name") : "";
            String type = bundle != null ? bundle.getString("type") : "";
            //Check if dialog is active
            if(mIsDialogActive) {
                mIsDialogActive = false;
                mLoadingDialog.dismiss();
                //Check response
                if ((response.equals("")) || response.equals("no connection")) {
                    //No internet access
                } else {

                    if (response.equals("NOK1")) {
                        Toast.makeText(getActivity(),
                                getString(R.string.invalid_parameters), Toast.LENGTH_SHORT).show();
                    } else if (response.equals("NOK2")) {
                        Toast.makeText(getActivity(),
                                getString(R.string.invalid_error), Toast.LENGTH_SHORT).show();
                    } else if (response.equals("NOK3")) {
                        Toast.makeText(getActivity(),
                                getString(R.string.username_already_registered), Toast.LENGTH_SHORT).show();
                    }  else {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String userID = jsonObject.getString("userID");
                            MemoryServices.setUserID(getActivity(),userID);
                            mUserInfo.setMobile(mobile);
                            mUserInfo.setStatus("2");
                            mUserInfo.setType(type);
                            mUserInfo.setName(name);
                            setUserInfo();
                            MemoryServices.setUserInfo(getActivity(),new Gson().toJson(mUserInfo));
                            mLayoutLogin.setVisibility(View.GONE);
                            mLayoutNoFamily.setVisibility(View.VISIBLE);
                            mLayoutList.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };
    private Handler mAddFamilyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String response = bundle != null ? bundle.getString("response") : "";
            if(mIsDialogActive) {
                mIsDialogActive = false;
                mLoadingDialog.dismiss();
                if ((response.equals("")) || response.equals("no connection")) {
                /*There is an issue with the response, no data received*/
                    Toast.makeText(getActivity(),
                            getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();

                } else {
                    if (response.equals("NOK1")) {
                        Toast.makeText(getActivity(),
                                getString(R.string.invalid_parameters), Toast.LENGTH_SHORT).show();
                    } else if (response.equals("NOK2")) {
                        Toast.makeText(getActivity(),
                                getString(R.string.mobile_not_found), Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            mPeopleItems.clear();
                            FamilyInfo familyInfo = new Gson().fromJson(response,FamilyInfo.class);
                            for(FamilyMember familyMember : familyInfo.getFamilyMembers()){
                                mPeopleItems.add(familyMember);
                            }
                            mAnimPeopleListAdapter.notifyDataSetChanged();
                            mLayoutNoFamily.setVisibility(View.GONE);
                            mLayoutList.setVisibility(View.VISIBLE);
                            mAddFamilyAlertDialog.dismiss();
                            MemoryServices.setFamilyInfo(getActivity(),response);
                        } catch (JsonSyntaxException e) {

                        }

                    }
                }
            }
        }
    };
    private Handler mGetFamilyMembersHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String response = bundle != null ? bundle.getString("response") : "";

                if ((response.equals("")) || response.equals("no connection")) {
                /*There is an issue with the response, no data received*/

                } else {
                    if (response.equals("NOK1")) {
                        Toast.makeText(getActivity(),
                                getString(R.string.invalid_parameters), Toast.LENGTH_SHORT).show();
                    } else if (response.equals("NOK2")) {
                        Toast.makeText(getActivity(),
                                getString(R.string.mobile_not_found), Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            mPeopleItems.clear();
                            FamilyInfo familyInfo = new Gson().fromJson(response,FamilyInfo.class);
                            for(FamilyMember familyMember : familyInfo.getFamilyMembers()){
                                mPeopleItems.add(familyMember);
                            }
                            mAnimPeopleListAdapter.notifyDataSetChanged();
                            if(mPeopleItems.size()==0){
                                mLayoutList.setVisibility(View.VISIBLE);
                                mLayoutNoFamily.setVisibility(View.VISIBLE);
                            }else{
                                mLayoutNoFamily.setVisibility(View.GONE);
                            }
                            MemoryServices.setFamilyInfo(getActivity(),response);
                        } catch (JsonSyntaxException e) {

                        }

                    }
                }
            }

    };
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
                    mUserInfo.setStatus(type);
                    MemoryServices.setUserInfo(getActivity(),new Gson().toJson(mUserInfo));
                    setUserInfo();
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
