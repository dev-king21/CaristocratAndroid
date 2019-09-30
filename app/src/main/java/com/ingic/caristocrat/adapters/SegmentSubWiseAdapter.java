package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.webhelpers.models.CarBodyStyle;

import java.util.ArrayList;

public class SegmentSubWiseAdapter extends BaseExpandableListAdapter {
    private MainActivity mainActivityContext;
    private ArrayList<CarBodyStyle> arrayList;
    private int lastPosition = -1;

    public SegmentSubWiseAdapter(MainActivity mainActivityContext) {
        this.mainActivityContext = mainActivityContext;
        this.arrayList = new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return this.arrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.arrayList.get(groupPosition).getChildTypes().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.arrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.arrayList.get(groupPosition).getChildTypes().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = arrayList.get(groupPosition).getModel();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mainActivityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_compare_segment_wise_header, null);
        }

        TextView tvHeaderTitle = convertView.findViewById(R.id.tvSegmentName);
        tvHeaderTitle.setText(headerTitle);
        ImageView ivSegmentImage = convertView.findViewById(R.id.ivSegmentImage);
        ivSegmentImage.setVisibility(View.INVISIBLE);
        /*ImageView ivSegmentImage = convertView.findViewById(R.id.ivSegmentImage);
        UIHelper.setImageWithGlide(mainActivityContext, ivSegmentImage, arrayList.get(groupPosition).getUn_selected_icon());
        */

        ImageView ivHeaderExpandedArrow = convertView.findViewById(R.id.expandIcon);
        if (arrayList.get(groupPosition).isActive()) {
            ivHeaderExpandedArrow.setImageDrawable(mainActivityContext.getResources().getDrawable(R.drawable.minus));
        } else {
            ivHeaderExpandedArrow.setImageDrawable(mainActivityContext.getResources().getDrawable(R.drawable.addcar));
        }

//        lastPosition = AnimationNew.showListItem(activityContext, convertView.findViewById(R.id.rlHomeGroup), groupPosition, lastPosition, R.anim.down_from_top);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String headerTitle = arrayList.get(groupPosition).getChildTypes().get(childPosition).getVersion();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mainActivityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_compare_segment_wise_child, null);
        }

        TextView tvHeaderTitle = convertView.findViewById(R.id.tvSubSegmentName);
        tvHeaderTitle.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void addAll(ArrayList<CarBodyStyle> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }
}
