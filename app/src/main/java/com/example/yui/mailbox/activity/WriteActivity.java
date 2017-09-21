package com.example.yui.mailbox.activity;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yui.mailbox.R;
import com.example.yui.mailbox.adapter.ContactAdapter;
import com.example.yui.mailbox.adapter.ImageShowAdapter;
import com.example.yui.mailbox.base.BaseActivity;
import com.example.yui.mailbox.utils.EmailUtil;
import com.example.yui.mailbox.utils.SizeHelper;
import com.example.yui.mailbox.view.SelectImageWindow;


public class WriteActivity extends BaseActivity implements View.OnFocusChangeListener{

    private RelativeLayout mRelativeLayout ;
    private SelectImageWindow selectPicWindow;

    private int[] list = {R.drawable.puzzle,R.drawable.puzzle,R.drawable.puzzle,R.drawable.puzzle,
            R.drawable.puzzle,R.drawable.puzzle,R.drawable.puzzle,R.drawable.puzzle};
    private ImageShowAdapter mImageAdapter;
    private RecyclerView mRecyclerView;

    private String recordAddress;

    //抄送/密送栏状态，false表示失去焦点
    private boolean STATE_CS = false;
    private boolean STATE_MS = false;
    private boolean LAYOUT_STATE = false;

    private EditText address, ms, cs, project, content;
    private ImageView addAddress, addMs, addCs;
    private TextView txt;
    private Toolbar toolbar;
    private WindowManager.LayoutParams params;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        // 获取参数
        SizeHelper.prepare(this);
        ContactAdapter.setMark(1);

        initTitleBar();
        initView();
        initResponseLayout();
        initRecyclerView();
    }

    private void initResponseLayout() {
        Intent intent = getIntent();
        if (intent != null){
            int type = intent.getIntExtra("type", -1);
            switch (type){
                case 0:
                    recordAddress = intent.getStringExtra("address");
                    String name_txt = intent.getStringExtra("name");
                    String subject_txt = intent.getStringExtra("subject");

                    address.setEnabled(false);
                    project.setEnabled(false);

                    if (!name_txt.isEmpty()){
                        address.setText(name_txt);
                    } else {
                        address.setText(recordAddress);
                    }
                    project.setText("回复：" + subject_txt);
                    break;
                case 1:
                    project.setText("转发：" + intent.getStringExtra("subject"));
                    project.setEnabled(false);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContactAdapter.setMark(0);
    }

    private void initRecyclerView() {
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
//
//        mRecyclerView.setLayoutManager(layoutManager);
//        mImageAdapter = new ImageShowAdapter(list);
//
//        mRecyclerView.setAdapter(mImageAdapter);
    }

    private void initTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
    }

    private void initView() {
        mRelativeLayout = (RelativeLayout)findViewById(R.id.temp_layout1);
        txt = (TextView)findViewById(R.id.txt2);

        mRecyclerView = (RecyclerView) findViewById(R.id.rec_list);

        address = (EditText)findViewById(R.id.address);
        ms = (EditText)findViewById(R.id.ms);
        cs = (EditText)findViewById(R.id.cs);
        project = (EditText)findViewById(R.id.project);
        content = (EditText)findViewById(R.id.content);

        addAddress = (ImageView)findViewById(R.id.add_address);
        addMs = (ImageView)findViewById(R.id.add_ms);
        addCs = (ImageView)findViewById(R.id.add_cs);

        address.setOnFocusChangeListener(this);
        ms.setOnFocusChangeListener(this);
        cs.setOnFocusChangeListener(this);
        project.setOnFocusChangeListener(this);
        content.setOnFocusChangeListener(this);
    }

    private void initPopWindow() {
        selectPicWindow = new SelectImageWindow(this, null);
//        设置Popupwindow显示位置（从底部弹出）
        selectPicWindow.showAtLocation(findViewById(R.id.write_main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha=0.7f;
        getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        selectPicWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = getWindow().getAttributes();
                params.alpha=1f;
                getWindow().setAttributes(params);
            }
        });
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b){
            switch (view.getId()){
                case R.id.address:
                    addAddress.setVisibility(View.VISIBLE);
                    break;
                case R.id.ms:
                    if (!(STATE_CS || STATE_MS) && !LAYOUT_STATE){
                        perfectAnimate(mRelativeLayout,0 ,SizeHelper.fromDp(50), "密    送");
                        LAYOUT_STATE = true;
                    }
                    STATE_MS = true;
                    addMs.setVisibility(View.VISIBLE);
                    break;
                case R.id.cs:
                    STATE_CS = true;
                    addCs.setVisibility(View.VISIBLE);
                    break;
            }
            if (!(STATE_CS || STATE_MS) && LAYOUT_STATE){
                perfectAnimate(mRelativeLayout, SizeHelper.fromDp(50),0 , "抄送/密送");
                LAYOUT_STATE = false;
            }
        } else {
            switch (view.getId()){
                case R.id.address:
                    addAddress.setVisibility(View.INVISIBLE);
                    break;
                case R.id.ms:
                    STATE_MS = false;
                    addMs.setVisibility(View.INVISIBLE);
                    break;
                case R.id.cs:
                    STATE_CS = false;
                    addCs.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    private void perfectAnimate(final View target, final int start, final int end, String text){
        if (!TextUtils.isEmpty(cs.getText()) || !TextUtils.isEmpty(ms.getText()))
            return;

        txt.setText(text);

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

    public void onClick(View v){

        switch (v.getId()) {
            case R.id.add_address:
                Intent intent;
                intent = new Intent(this, SelectContactActivity.class);
                startActivity(intent);
                break;
            case R.id.add_cs:
                Intent intent1;
                intent1 = new Intent(this, SelectContactActivity.class);
                startActivity(intent1);
                break;
            case R.id.add_ms:
                Intent intent2;
                intent2 = new Intent(this, SelectContactActivity.class);
                startActivity(intent2);
                break;
            case R.id.add_file:
                initPopWindow();
                break;
            case R.id.cancel_btn:
                Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.send_btn:
                if (address.getText().toString().isEmpty() ||
                        project.getText().toString().isEmpty() ||
                        content.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "收件人、标题和内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        try {
                            String content1 = "大风大浪解放东路开发阶段联发科记得来";
                            EmailUtil.autoSendMail(getApplicationContext(),
                                    address.getText().toString().trim(),
                                    project.getText().toString().trim(),
                                    content.getText().toString().trim());
                        } catch (Exception e){
                            e.printStackTrace();
                            message.what = 1;
                            mHandler.sendMessage(message);
                        }
                        message.what = 2;
                        mHandler.sendMessage(message);
                    }
                }).start();

                Toast.makeText(this, "已发送！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.finish_btn:
                Toast.makeText(this, "完成（1）", Toast.LENGTH_SHORT).show();
                selectPicWindow.dismiss();
                break;
            case R.id.cancel_pop_btn:
                selectPicWindow.dismiss();
                break;
            default:
                break;
        }
    }
}
