package com.example.yui.mailbox.activity;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.yui.mailbox.R;
import com.example.yui.mailbox.base.BaseActivity;
import com.example.yui.mailbox.utils.SizeHelper;


public class AddContactActivity extends BaseActivity {

    private RelativeLayout layout;
    private EditText editText;

    private boolean EDITTEXT_STATE = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_contact);

        SizeHelper.prepare(this);
        initView();
    }

    private void initView() {

        layout = (RelativeLayout)findViewById(R.id.layout_5);
        editText = (EditText)findViewById(R.id.mail_address);

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
            case R.id.layout_6:
                if (!EDITTEXT_STATE){
                    perfectAnimate(layout, 0, SizeHelper.fromDp(50));
                    layout.setClickable(false);
                }
                break;
            case R.id.add_btn_4:
                if (EDITTEXT_STATE){
                    editText.setText(null);
                    perfectAnimate(layout, SizeHelper.fromDp(50),0);
                    layout.setClickable(true);
                }
                break;
            default:
                break;
        }
    }
}
