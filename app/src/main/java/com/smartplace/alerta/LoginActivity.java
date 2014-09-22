package com.smartplace.alerta;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.smartplace.alerta.coverflow.FancyCoverFlow;
import com.smartplace.alerta.coverflow.FancyCoverFlowSampleAdapter;


public class LoginActivity extends Activity {

    private FormEditText mEditName;
    private FormEditText mEditMobile;
    private FancyCoverFlow mFancyCoverFlow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getActionBar().hide();

        mEditName = (FormEditText)findViewById(R.id.edit_name);
        mEditMobile = (FormEditText)findViewById(R.id.edit_mobile);
        Button btnRegister = (Button)findViewById(R.id.btn_register);
        mFancyCoverFlow = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);
        final TextView txtUserType = (TextView)findViewById(R.id.txt_user_type);
        final String[] userTypes= getResources().getStringArray(R.array.user_types_array);

        mFancyCoverFlow.setAdapter(new FancyCoverFlowSampleAdapter());
        mFancyCoverFlow.setUnselectedAlpha(1.0f);
        mFancyCoverFlow.setUnselectedSaturation(0.0f);
        mFancyCoverFlow.setUnselectedScale(0.5f);
        mFancyCoverFlow.setSpacing(50);
        mFancyCoverFlow.setMaxRotation(0);
        mFancyCoverFlow.setSelection(1);
        mFancyCoverFlow.setScaleDownGravity(0.5f);
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

        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/OpenSansLight.ttf");
        mEditName.setTypeface(titleFont);
        mEditMobile.setTypeface(titleFont);
        btnRegister.setTypeface(titleFont);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(onFieldsValidation()){

                }


                //Toast.makeText(getBaseContext(),String.valueOf(mFancyCoverFlow.getSelectedItemPosition()),Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

}
