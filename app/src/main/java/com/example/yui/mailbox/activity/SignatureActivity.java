package com.example.yui.mailbox.activity;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.yui.mailbox.R;
import com.example.yui.mailbox.base.BaseActivity;
import com.example.yui.mailbox.utils.SizeHelper;


public class SignatureActivity extends BaseActivity {

    private ImageView img1, img2;
    private RelativeLayout layout;

    private Boolean EDITTEXT_STATE = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signature);

        SizeHelper.prepare(this);
        initView();
    }

    private void initView() {
        img1 = (ImageView)findViewById(R.id.temp_1);
        img2 = (ImageView)findViewById(R.id.temp_2);

        img1.setVisibility(View.VISIBLE);
        img2.setVisibility(View.INVISIBLE);

        layout = (RelativeLayout)findViewById(R.id.text_layout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void perfectAnimate(final View target, final int start, final int end){

        EDITTEXT_STATE = !EDITTEXT_STATE;

        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //   int currentValue = (Integer)valueAnimator.getAnimatedValue();

                float fraction = valueAnimator.getAnimatedFraction();
                target.getLayoutParams().height = mEvaluator.evaluate(fraction, start, end);
                target.requestLayout();
            }
        });

        valueAnimator.setDuration(500).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_layout_1:
                if (EDITTEXT_STATE){
                    img1.setVisibility(View.VISIBLE);
                    img2.setVisibility(View.INVISIBLE);
                    perfectAnimate(layout, SizeHelper.fromDp(50),0);
                }
                break;
            case R.id.btn_layout_2:
                if (!EDITTEXT_STATE){
                    img1.setVisibility(View.INVISIBLE);
                    img2.setVisibility(View.VISIBLE);
                    perfectAnimate(layout, 0, SizeHelper.fromDp(50));
                }
                break;
            default:
                break;
        }
    }
}
