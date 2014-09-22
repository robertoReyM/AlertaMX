package com.smartplace.alerta.init;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartplace.alerta.MainActivity;
import com.smartplace.alerta.R;

public class GuideActivity extends Activity {

    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getActionBar().hide();

        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(new vpAdapter());

        Button btnGotIt = (Button)findViewById(R.id.btn_got_it);
        Typeface titleFont= Typeface.createFromAsset(getAssets(), "fonts/OpenSansLight.ttf");
        btnGotIt.setTypeface(titleFont);
        btnGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to main activity
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                        /*delete activity from back stack*/
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                GuideActivity.this.startActivity(intent);
                finish();
            }
        });

        final ImageView imageDot1  = (ImageView)findViewById(R.id.image_dot_1);
        final ImageView imageDot2  = (ImageView)findViewById(R.id.image_dot_2);
        final ImageView imageDot3  = (ImageView)findViewById(R.id.image_dot_3);
        final ImageView imageDot4  = (ImageView)findViewById(R.id.image_dot_4);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                switch(i){
                    case 0:
                        imageDot1.setBackgroundResource(R.drawable.rounded_dot_selected);
                        imageDot2.setBackgroundResource(R.drawable.rounded_dot_unselected);
                        imageDot3.setBackgroundResource(R.drawable.rounded_dot_unselected);
                        imageDot4.setBackgroundResource(R.drawable.rounded_dot_unselected);
                        break;
                    case 1:
                        imageDot1.setBackgroundResource(R.drawable.rounded_dot_unselected);
                        imageDot2.setBackgroundResource(R.drawable.rounded_dot_selected);
                        imageDot3.setBackgroundResource(R.drawable.rounded_dot_unselected);
                        imageDot4.setBackgroundResource(R.drawable.rounded_dot_unselected);
                        break;
                    case 2:
                        imageDot1.setBackgroundResource(R.drawable.rounded_dot_unselected);
                        imageDot2.setBackgroundResource(R.drawable.rounded_dot_unselected);
                        imageDot3.setBackgroundResource(R.drawable.rounded_dot_selected);
                        imageDot4.setBackgroundResource(R.drawable.rounded_dot_unselected);
                        break;
                    case 3:
                        imageDot1.setBackgroundResource(R.drawable.rounded_dot_unselected);
                        imageDot2.setBackgroundResource(R.drawable.rounded_dot_unselected);
                        imageDot3.setBackgroundResource(R.drawable.rounded_dot_unselected);
                        imageDot4.setBackgroundResource(R.drawable.rounded_dot_selected);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.guide, menu);
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
    private class vpAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == ((LinearLayout)o);
        }
        public void finishUpdate (ViewGroup container)
        {

        }
        public void startUpdate(ViewGroup container) {
         /* compiled code */
        }
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater)container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v =null;
            switch (position)            {

                case 0: {
                    v = inflater.inflate(R.layout.page_guide_1, null);
                    ImageView image = (ImageView) v.findViewById(R.id.image_guide);
                    TextView txtTitle = (TextView)v.findViewById(R.id.txt_header);
                    TextView txtDescription = (TextView)v.findViewById(R.id.txt_description);
                    image.setImageResource(R.drawable.ic_guide_1);
                    txtTitle.setText(R.string.title_guide_1);
                    txtDescription.setText(R.string.description_guide_1);
                    Typeface titleFont= Typeface.createFromAsset(getAssets(), "fonts/OpenSansLight.ttf");
                    txtTitle.setTypeface(titleFont);
                    txtDescription.setTypeface(titleFont);
                }
                    break;
                case 1:
                {
                    v = inflater.inflate(R.layout.page_guide_1, null);
                    ImageView image = (ImageView) v.findViewById(R.id.image_guide);
                    TextView txtTitle = (TextView)v.findViewById(R.id.txt_header);
                    TextView txtDescription = (TextView)v.findViewById(R.id.txt_description);
                    image.setImageResource(R.drawable.ic_guide_2);
                    txtTitle.setText(R.string.title_guide_2);
                    txtDescription.setText(R.string.description_guide_2);
                    Typeface titleFont= Typeface.createFromAsset(getAssets(), "fonts/OpenSansLight.ttf");
                    txtTitle.setTypeface(titleFont);
                    txtDescription.setTypeface(titleFont);
                }
                    break;
                case 2:{
                    v = inflater.inflate(R.layout.page_guide_1, null);
                    ImageView image = (ImageView) v.findViewById(R.id.image_guide);
                    TextView txtTitle = (TextView)v.findViewById(R.id.txt_header);
                    TextView txtDescription = (TextView)v.findViewById(R.id.txt_description);
                    image.setImageResource(R.drawable.ic_guide_3);
                    txtTitle.setText(R.string.title_guide_3);
                    txtDescription.setText(R.string.description_guide_3);
                    Typeface titleFont= Typeface.createFromAsset(getAssets(), "fonts/OpenSansLight.ttf");
                    txtTitle.setTypeface(titleFont);
                    txtDescription.setTypeface(titleFont);
                }
                    break;
                case 3:{
                    v = inflater.inflate(R.layout.page_guide_1, null);
                    ImageView image = (ImageView) v.findViewById(R.id.image_guide);
                    TextView txtTitle = (TextView)v.findViewById(R.id.txt_header);
                    TextView txtDescription = (TextView)v.findViewById(R.id.txt_description);
                    image.setImageResource(R.drawable.ic_guide_4);
                    txtTitle.setText(R.string.title_guide_4);
                    txtDescription.setText(R.string.description_guide_4);
                    Typeface titleFont= Typeface.createFromAsset(getAssets(), "fonts/OpenSansLight.ttf");
                    txtTitle.setTypeface(titleFont);
                    txtDescription.setTypeface(titleFont);
                }
                    break;
            }
            ((ViewPager)container).addView(v,0);
            return v;
        }

        public android.os.Parcelable saveState() {
            return null;
        }
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView((LinearLayout)object);
        }
    }
}
