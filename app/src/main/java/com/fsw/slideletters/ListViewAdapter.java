package com.fsw.slideletters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fsw.slidelettersmenu.CharacterParser;

import java.util.List;

/**
 * @author Foin
 * @version 1.0
 * @time 2017/4/22
 * @desc 列表数据的适配器
 */
public class ListViewAdapter extends BaseAdapter {

    private Activity context;

    private List<String> list;

    private CharacterParser characterParser;


    public ListViewAdapter(Activity context, List<String> list) {
        this.context = context;
        this.list = list;
        characterParser = CharacterParser.getInstance();
    }

    /**
     * @param section
     * @return
     * @desc 此方法用于根据滑动选择的字母在列表中检索以此字母开头的数据中的第一条数据的下标
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i);
            String pinyin = characterParser.getSelling(sortStr);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.charAt(0) == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        if (position >= list.size()) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_choose_linkman, null);
            holder = new ViewHolder();
            holder.personName = (TextView) convertView.findViewById(R.id.person_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = list.get(position);
        if (!TextUtils.isEmpty(name)) {
            holder.personName.setText(name);
        }
        return convertView;
    }

    class ViewHolder {
        TextView personName;
    }

}
