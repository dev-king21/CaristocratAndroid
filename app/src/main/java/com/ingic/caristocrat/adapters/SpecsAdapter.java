package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.ItemClickListener;
import com.ingic.caristocrat.interfaces.SectionStateChangeListener;
import com.ingic.caristocrat.models.Item;
import com.ingic.caristocrat.models.MyCarAttributes;
import com.ingic.caristocrat.models.Section;

import java.util.ArrayList;

/**
 *
 */
public class SpecsAdapter extends RecyclerView.Adapter<SpecsAdapter.ViewHolder> {

    //data array
    private ArrayList<Object> mDataArrayList;

    //context
    private final Context mContext;

    //listeners
    private final ItemClickListener mItemClickListener;
    private final SectionStateChangeListener mSectionStateChangeListener;

    //view type
    private static final int VIEW_TYPE_SECTION = R.layout.item_section_specs;
    private static final int VIEW_TYPE_ITEM = R.layout.layout_specifications_item; //TODO : change this

    public SpecsAdapter(Context context, ArrayList<Object> dataArrayList,
                        final GridLayoutManager gridLayoutManager, ItemClickListener itemClickListener,
                        SectionStateChangeListener sectionStateChangeListener) {
        mContext = context;
        mItemClickListener = itemClickListener;
        mSectionStateChangeListener = sectionStateChangeListener;
        mDataArrayList = dataArrayList;

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return isSection(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
    }

    private boolean isSection(int position) {
        return mDataArrayList.get(position) instanceof Section;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false), viewType);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        switch (holder.viewType) {
            case VIEW_TYPE_ITEM:
                final MyCarAttributes item = (MyCarAttributes) mDataArrayList.get(position);
                if (item.getAttr_id() == -1) {
                    if (!UIHelper.isEmptyOrNull(item.getAttr_name())) {
                        holder.itemDesc.setVisibility(View.VISIBLE);
                        holder.llSpecLayout.setVisibility(View.GONE);
                        holder.itemDesc.setText(!UIHelper.isEmptyOrNull(item.getAttr_name()) ? item.getAttr_name() : "");
                    }
                } else {
                    holder.itemDesc.setVisibility(View.GONE);
                    holder.llSpecLayout.setVisibility(View.VISIBLE);
                    holder.itemKey.setText(!UIHelper.isEmptyOrNull(item.getAttr_name()) ? item.getAttr_name() : "");
/*
                    item.setAttr_option(null);
                    item.setValue(null);
*/
                    if (item.getAttr_option() != null) {
                        holder.itemValue.setText(item.getAttr_option());
                    } else if (item.getValue() != null) {
                        holder.itemValue.setText(item.getValue().endsWith(".0") ? item.getValue().replace(".0", "") : item.getValue());
                    } else {
                        holder.itemValue.setText("-");
                    }
//                    else if (item.getValue() != null){
//                        holder.itemValue.setText(item.getValue().endsWith(".0") ? item.getValue().replace(".0", "") : item.getValue());
//                    }
                    UIHelper.setImageWithGlide(mContext, holder.ivSpecsImage, item.getAttrIcon());
                    if (item.getCategoryKey() != null) {
                        holder.itemValue.setText(!UIHelper.isEmptyOrNull(item.getAttr_option()) ? item.getAttr_option() : "");
                        if (item.getCategoryKey().equals(AppConstants.MainCategoriesType.LUXURY_NEW_CARS)) {
                            holder.ivSpecsImage.setVisibility(View.GONE);
                        }
                    }
                    holder.itemKey.setSelected(true);
                    holder.itemValue.setSelected(true);
                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemClickListener.itemClicked(item);
                        }
                    });
                }
                break;
            case VIEW_TYPE_SECTION:
                final Section section = (Section) mDataArrayList.get(position);
                holder.tvSection.setText(section.getName());
                holder.tvSection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //mItemClickListener.itemClicked(section);
                        holder.sectionToggleButton.setChecked(!holder.sectionToggleButton.isChecked());
                    }
                });
                holder.sectionToggleButton.setChecked(section.isExpanded);
                holder.sectionToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mSectionStateChangeListener.onSectionStateChanged(section, isChecked);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isSection(position))
            return VIEW_TYPE_SECTION;
        else return VIEW_TYPE_ITEM;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        //common
        View view;
        int viewType;

        //for section
        TextView tvSection;
        CheckBox sectionToggleButton;
        View bottomView;

        //for item
        TextView itemKey;
        TextView itemValue;
        TextView itemDesc;
        ImageView ivSpecsImage;
        LinearLayout llSpecLayout;

        public ViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            this.view = view;
            if (viewType == VIEW_TYPE_ITEM) {
                itemKey = (TextView) view.findViewById(R.id.tvKey);
                itemValue = (TextView) view.findViewById(R.id.tvValue);
                itemDesc = (TextView) view.findViewById(R.id.tvDesc);
                ivSpecsImage = (ImageView) view.findViewById(R.id.ivSpecsImage);
                llSpecLayout = (LinearLayout) view.findViewById(R.id.llSpecsChildLayout);
            } else {
                tvSection = (TextView) view.findViewById(R.id.tvSection);
                sectionToggleButton = (CheckBox) view.findViewById(R.id.toggle_button_section);
                bottomView = (View) view.findViewById(R.id.view);
            }
        }
    }
}
