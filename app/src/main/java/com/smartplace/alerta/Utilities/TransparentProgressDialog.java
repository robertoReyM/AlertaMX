package com.smartplace.alerta.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartplace.alerta.R;


/**
 * Created by Roberto on 14/05/2014.
 */
public class TransparentProgressDialog extends Dialog {

    private ImageView iv;
    public TextView tv;
    private OnProgressDialogListener mListener;

    public TransparentProgressDialog(Context context, int resourceIdOfImage) {
        super(context, R.style.TransparentProgressDialog);
        setCanceledOnTouchOutside(false);
        setTitle(null);
        setCancelable(true);
        try {
            mListener = (OnProgressDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnProgressDialogListener");
        }
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if(mListener!=null){
                    mListener.onDialogCancelled();
                }
            }
        });

        LayoutInflater inflater = getLayoutInflater();
        View promptView = inflater.inflate(R.layout.prompt_loading, null);
        iv = (ImageView)promptView.findViewById(R.id.image_loading);
        iv.setDrawingCacheEnabled(true);

        tv = (TextView)promptView.findViewById(R.id.txt_loading);
        Typeface titleFont= Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansLight.ttf");
        tv.setTypeface(titleFont);

        setContentView(promptView);
    }

    @Override
    public void show() {
        super.show();
        RotateAnimation anim = new RotateAnimation(0.0f, 359.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(2000);

        iv.startAnimation(anim);
    }

    public interface OnProgressDialogListener{
        public void onDialogCancelled();
    }
}
