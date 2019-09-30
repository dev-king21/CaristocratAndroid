package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.webhelpers.models.CarBodyStyle;

import java.util.ArrayList;

public class SegmentMainWiseAdapter extends BaseAdapter {
    private MainActivity mainActivityContext;
    private ArrayList<CarBodyStyle> arrayList;

    public SegmentMainWiseAdapter(MainActivity mainActivityContext) {
        this.mainActivityContext = mainActivityContext;
        this.arrayList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int groupPosition) {
        return this.arrayList.get(groupPosition);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int groupPosition, View convertView, ViewGroup parent) {
        String headerTitle = arrayList.get(groupPosition).getName();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mainActivityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_compare_segment_wise_header, null);
        }

        TextView tvHeaderTitle = convertView.findViewById(R.id.tvSegmentName);
        tvHeaderTitle.setText(headerTitle);

        ImageView ivSegmentImage = convertView.findViewById(R.id.ivSegmentImage);
        UIHelper.setImageWithGlide(mainActivityContext, ivSegmentImage, arrayList.get(groupPosition).getUn_selected_icon());

        ImageView ivHeaderExpandedArrow = convertView.findViewById(R.id.expandIcon);
        if (arrayList.get(groupPosition).isActive()) {
            ivHeaderExpandedArrow.setImageDrawable(mainActivityContext.getResources().getDrawable(R.drawable.uparrow));
        } else {
            ivHeaderExpandedArrow.setImageDrawable(mainActivityContext.getResources().getDrawable(R.drawable.downarrow));
        }

        return convertView;
    }

    public void addAll(ArrayList<CarBodyStyle> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }
}
