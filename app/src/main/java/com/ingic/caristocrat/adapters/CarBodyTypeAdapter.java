package com.ingic.caristocrat.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.webhelpers.models.CarBodyStyle;

import java.util.ArrayList;

public class CarBodyTypeAdapter extends RecyclerView.Adapter<CarBodyTypeAdapter.Holder> {
    private MainActivity mainActivity;
    //    private ArrayList<CarBodyStyle> arrayList;
    private int selectedPosition = -1;
    LuxuryMarketSearchFilter filter;

    public CarBodyTypeAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
//        this.arrayList = new ArrayList<>();
        this.filter = LuxuryMarketSearchFilter.getInstance();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_body_type, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        /*
        holder.ivCarTypeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unselectAll(arrayList);
                LuxuryMarketSearchFilter.getInstance().setCarType(arrayList.get(position).getId());
                notifyDataSetChanged();
//                arrayList.get(position).setSelected(true);
//                notifyDataSetChanged();
            }
        });
        */

        holder.tvCarTypeLabel.setText(LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(position).getName());
        setImage(position, holder.ivCarTypeIcon);
//        UIHelper.setImageWithGlide(mainActivity, holder.ivCarTypeIcon, LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(position).getUn_selected_icon());
        holder.ivCarTypeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                changeImage(holder.ivCarTypeIcon, holder.ivCarTypeIcon, position);
                if (LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(position).isSelected()) {
                    LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(position).setSelected(false);
                } else {
                    LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(position).setSelected(true);
                }
                setImage(position, holder.ivCarTypeIcon);
                filter.setFilter(true);
            }
        });

//        if (LuxuryMarketSearchFilter.getInstance().getCarType() != null) {
//            if (LuxuryMarketSearchFilter.getInstance().getCarType() == arrayList.get(position).getId()) {
//                holder.ivCarTypeIcon.setBackground(activityContext.getResources().getDrawable(R.drawable.body_style_selected));
//                if (arrayList.get(position).getName().equals(AppConstants.BodyStyles.SNORT_2_DOORS)) {
//                    holder.ivCarTypeIcon.setImageResource(R.drawable.body_style_two_doors_selected);
//                } else if (arrayList.get(position).getName().equals(AppConstants.BodyStyles.SUV)) {
//                    holder.ivCarTypeIcon.setImageResource(R.drawable.body_style_two_suv_selected);
//                } else if (arrayList.get(position).getName().equals(AppConstants.BodyStyles.CONVERTABLE)) {
//                    holder.ivCarTypeIcon.setImageResource(R.drawable.body_style_two_convertable_selected);
//                } else {
//                    holder.ivCarTypeIcon.setImageResource(R.drawable.body_style_four_doors_selected);
//                }
//            } else {
//                holder.ivCarTypeIcon.setBackground(activityContext.getResources().getDrawable(R.drawable.body_style_unselected));
//                if (arrayList.get(position).getName().equals(AppConstants.BodyStyles.SNORT_2_DOORS)) {
//                    holder.ivCarTypeIcon.setImageResource(R.drawable.body_style_two_doors_unselected);
//                } else if (arrayList.get(position).getName().equals(AppConstants.BodyStyles.SUV)) {
//                    holder.ivCarTypeIcon.setImageResource(R.drawable.body_style_two_suv_unselected);
//                } else if (arrayList.get(position).getName().equals(AppConstants.BodyStyles.CONVERTABLE)) {
//                    holder.ivCarTypeIcon.setImageResource(R.drawable.body_style_two_convertable_unselected);
//                } else {
//                    holder.ivCarTypeIcon.setImageResource(R.drawable.body_style_four_doors_unselected);
//                }
//            }
//        } else {
//            holder.ivCarTypeIcon.setBackground(activityContext.getResources().getDrawable(R.drawable.body_style_unselected));
//            if (arrayList.get(position).getName().equals(AppConstants.BodyStyles.SNORT_2_DOORS)) {
//                holder.ivCarTypeIcon.setImageResource(R.drawable.body_style_two_doors_unselected);
//            } else if (arrayList.get(position).getName().equals(AppConstants.BodyStyles.SUV)) {
//                holder.ivCarTypeIcon.setImageResource(R.drawable.body_style_two_suv_unselected);
//            } else if (arrayList.get(position).getName().equals(AppConstants.BodyStyles.CONVERTABLE)) {
//                holder.ivCarTypeIcon.setImageResource(R.drawable.body_style_two_convertable_unselected);
//            } else {
//                holder.ivCarTypeIcon.setImageResource(R.drawable.body_style_four_doors_unselected);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().size();
    }

    /*
        public void addAll(ArrayList<CarBodyStyle> arrayList) {
            this.arrayList.clear();
            this.arrayList.addAll(arrayList);
        }
    */
    public void unselectAll(ArrayList<CarBodyStyle> arrayList) {
        for (int pos = 0; pos < arrayList.size(); pos++) {
            arrayList.get(pos).setSelected(false);
        }
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        LinearLayout llCarBodyType;
        TextView tvCarTypeLabel;
        ImageView ivCarTypeIcon;

        Holder(View view) {
            super(view);
            llCarBodyType = view.findViewById(R.id.llCarBodyType);
            tvCarTypeLabel = view.findViewById(R.id.tvCarTypeLabel);
            ivCarTypeIcon = view.findViewById(R.id.ivCarTypeIcon);
        }
    }

    private void setImage(int pos, ImageView img) {
        if (LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(pos).isSelected()) {
            img.setBackground(mainActivity.getResources().getDrawable(R.drawable.body_style_selected));
            UIHelper.setImageWithGlide(mainActivity, img, LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(pos).getSelected_icon());
        } else {
            img.setBackground(mainActivity.getResources().getDrawable(R.drawable.body_style_unselected));
            UIHelper.setImageWithGlide(mainActivity, img, LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(pos).getUn_selected_icon());
        }
        /*
        switch (pos) {
            case 0:
                if (LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(0).isSelected())
                    img.setImageResource(R.drawable.body_style_four_doors_selected);
                else
                    img.setImageResource(R.drawable.body_style_four_doors_unselected);
                break;
            case 1:
                if (LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(1).isSelected())
                    img.setImageResource(R.drawable.body_style_two_doors_selected);
                else
                    img.setImageResource(R.drawable.body_style_two_doors_unselected);
                break;
            case 2:
                if (LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(2).isSelected())
                    img.setImageResource(R.drawable.body_style_two_suv_selected);
                else
                    img.setImageResource(R.drawable.body_style_two_suv_unselected);
                break;
            case 3:
                if (LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(3).isSelected())
                    img.setImageResource(R.drawable.body_style_two_convertable_selected);
                else
                    img.setImageResource(R.drawable.body_style_two_convertable_unselected);
                break;
        }
        */
    }

    @SuppressLint("NewApi")
    private void changeImage(ImageView img, ImageView imgBg, int pos) {

//unSelected to Selected
        if (img.getDrawable().getConstantState().equals(mainActivity.getDrawable(R.drawable.body_style_four_doors_unselected).getConstantState())) {
            img.setImageResource(R.drawable.body_style_four_doors_selected);
            imgBg.setBackground(mainActivity.getResources().getDrawable(R.drawable.body_style_selected));
            LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(pos).setSelected(true);
        } else if (img.getDrawable().getConstantState().equals(mainActivity.getDrawable(R.drawable.body_style_two_doors_unselected).getConstantState())) {
            img.setImageResource(R.drawable.body_style_two_doors_selected);
            imgBg.setBackground(mainActivity.getResources().getDrawable(R.drawable.body_style_selected));
            LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(pos).setSelected(true);
        } else if (img.getDrawable().getConstantState().equals(mainActivity.getDrawable(R.drawable.body_style_two_suv_unselected).getConstantState())) {
            LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(pos).setSelected(true);
            img.setImageResource(R.drawable.body_style_two_suv_selected);
            imgBg.setBackground(mainActivity.getResources().getDrawable(R.drawable.body_style_selected));
        } else if (img.getDrawable().getConstantState().equals(mainActivity.getDrawable(R.drawable.body_style_two_convertable_unselected).getConstantState())) {
            img.setImageResource(R.drawable.body_style_two_convertable_selected);
            imgBg.setBackground(mainActivity.getResources().getDrawable(R.drawable.body_style_selected));
            LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(pos).setSelected(true);
        }
//selected to unselected
        else if (img.getDrawable().getConstantState().equals(mainActivity.getDrawable(R.drawable.body_style_four_doors_selected).getConstantState())) {
            img.setImageResource(R.drawable.body_style_four_doors_unselected);
            imgBg.setBackground(mainActivity.getResources().getDrawable(R.drawable.body_style_unselected));
            LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(pos).setSelected(false);
        } else if (img.getDrawable().getConstantState().equals(mainActivity.getDrawable(R.drawable.body_style_two_doors_selected).getConstantState())) {
            img.setImageResource(R.drawable.body_style_two_doors_unselected);
            imgBg.setBackground(mainActivity.getResources().getDrawable(R.drawable.body_style_unselected));
            LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(pos).setSelected(false);
        } else if (img.getDrawable().getConstantState().equals(mainActivity.getDrawable(R.drawable.body_style_two_suv_selected).getConstantState())) {
            img.setImageResource(R.drawable.body_style_two_suv_unselected);
            imgBg.setBackground(mainActivity.getResources().getDrawable(R.drawable.body_style_unselected));
            LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(pos).setSelected(false);
        } else if (img.getDrawable().getConstantState().equals(mainActivity.getDrawable(R.drawable.body_style_two_convertable_selected).getConstantState())) {
            img.setImageResource(R.drawable.body_style_two_suv_unselected);
            imgBg.setBackground(mainActivity.getResources().getDrawable(R.drawable.body_style_unselected));
            LuxuryMarketSearchFilter.getInstance().getCarBodyStyles().get(pos).setSelected(false);
        }


    }
}
