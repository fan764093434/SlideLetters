package com.fsw.slideletters;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.fsw.slidelettersmenu.SlideLettersMenu;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Foin
 * @version 1.0
 * @time 2017/4/22
 * @desc 首页，数据展示
 */
public class MainActivity extends AppCompatActivity {
    //数据、列表、适配器
    private ListView listView;
    private ListViewAdapter adapter;
    private List<String> arrays;
    //自定义字母控件
    private SlideLettersMenu slideBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //获取String.xml中的测试数据
        arrays = Arrays.asList(this.getResources().getStringArray(R.array.arrays));
        //将数据转成集合并根据拼音数字母排序
        Collections.sort(arrays, new PinyinComparator());
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ListViewAdapter(this, arrays);
        listView.setAdapter(adapter);
        slideBar = (SlideLettersMenu) findViewById(R.id.slide_bar);
        //滑动监听
        slideBar.setOnSlideListener(new SlideLettersMenu.OnSlideListener() {
            @Override
            public void onStartSlide(int index) {//开始滑动

            }

            @Override
            public void onSliding(int index) {//正在滑动

            }

            @Override
            public void onStopSlide(String index) {//滑动结束
                int position = adapter.getPositionForSection(index.charAt(0));
                if (position != -1) {
                    //指定ListVIEW展示选择的下标数据
                    listView.setSelection(position);
                }
            }
        });
    }

}
