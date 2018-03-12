package com.example.ming.flowlayoutdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ming.flowlayoutlibrary.FlowLayout;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    FlowLayout flowLayout;
    RadioButton adjustSpace, adjustItem, gravityLeft, gravityCenter, gravityRight;
    RadioGroup radioGroup;
    public static final String[] datas = {
            "蒸羊羔", "蒸熊掌", "蒸鹿尾儿", "烧花鸭", "烧雏鸡儿", "烧子鹅 ",
            "卤煮咸鸭", "酱鸡", "腊肉", "松花", "小肚儿 ", "晾肉", "香肠",
            "什锦苏盘", "熏鸡", "白肚儿", "清蒸八宝猪", "江米酿鸭子", "罐儿野鸡",
            "罐儿鹌鹑", "卤什锦", "卤子鹅", "卤虾 ",
            "清蒸哈什蚂", "烩鸭腰儿", "烩鸭条儿", "清拌鸭丝儿", "黄心管儿", "焖白鳝"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flowLayout = (FlowLayout)findViewById(R.id.flowLayout);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        adjustSpace = (RadioButton)findViewById(R.id.bt_adjust_space);
        adjustItem = (RadioButton)findViewById(R.id.bt_adjust_item);
        gravityLeft = (RadioButton)findViewById(R.id.bt_gravity_left);
        gravityCenter = (RadioButton)findViewById(R.id.bt_gravity_center);
        gravityRight = (RadioButton)findViewById(R.id.bt_gravity_right);
        gravityLeft.setChecked(true);
        radioGroup.setOnCheckedChangeListener(this);
        initData();
    }

    private void initData(){
        if(datas == null || flowLayout == null){
            return;
        }
        for(int i=0; i<datas.length; i++){
            TextView textView = new TextView(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            textView.setPadding(24, 16, 24, 16);
            textView.setGravity(Gravity.CENTER);
            textView.setText(datas[i]);
            textView.setTextSize(COMPLEX_UNIT_SP, 16);
            textView.setTextColor(Color.BLACK);
            textView.setBackground(getResources().getDrawable(R.drawable.textview_background));
            flowLayout.addView(textView);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.bt_adjust_space:
                flowLayout.setAdjustMod(FlowLayout.ADJUST_SPACE);
                break;
            case R.id.bt_adjust_item:
                flowLayout.setAdjustMod(FlowLayout.ADJUST_ITEM);
                break;
            case R.id.bt_gravity_left:
                flowLayout.setAdjustMod(FlowLayout.ADJUST_NONE);
                flowLayout.setGravity(FlowLayout.GRAVITY_LEFT);
                break;
            case R.id.bt_gravity_center:
                flowLayout.setAdjustMod(FlowLayout.ADJUST_NONE);
                flowLayout.setGravity(FlowLayout.GRAVITY_CENTER);
                break;
            case R.id.bt_gravity_right:
                flowLayout.setAdjustMod(FlowLayout.ADJUST_NONE);
                flowLayout.setGravity(FlowLayout.GRAVITY_RIGHT);
                break;
            default:
                break;
        }
        flowLayout.requestLayout();
    }
}
