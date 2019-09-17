package com.ingic.caristocrat.adapters;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.BrandsListFilterActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.ActivityBrandsListFilterBinding;
import com.ingic.caristocrat.fragments.BrandsModelsListFragment;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.BrandModelsSelectedListener;
import com.ingic.caristocrat.interfaces.DialogCloseListener;
import com.ingic.caristocrat.interfaces.OnModelsSelectedListener;
import com.ingic.caristocrat.models.FilterBrand;

import java.util.ArrayList;

public class BrandsListFilterAdapter extends RecyclerView.Adapter<BrandsListFilterAdapter.Holder> {
    private ActivityBrandsListFilterBinding binding;
    private BrandsListFilterActivity activityContext;
    private ArrayList<FilterBrand> arrayList;
    private ArrayList<Integer> mSectionPositions;
    private DialogCloseListener listener;
    private OnModelsSelectedListener modelsSelectedListener;
    private LuxuryMarketSearchFilter filter;
    private Double width = 120.0;
    private int lastSelectedPosition = -1;
    private FilterBrand selectedBrand;
    private BrandModelsSelectedListener brandModelsSelectedListener;

    public BrandsListFilterAdapter(BrandsListFilterActivity activityContext, ActivityBrandsListFilterBinding binding, DialogCloseListener listener, OnModelsSelectedListener modelsSelectedListener) {
        this.activityContext = activityContext;
        this.binding = binding;
        this.arrayList = new ArrayList<>();
        this.listener = listener;
        this.modelsSelectedListener = modelsSelectedListener;
        this.filter = LuxuryMarketSearchFilter.getInstance();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_brands_filter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        if (selectedBrand != null) {
            if (selectedBrand.getId() == arrayList.get(position).getId()) {
                lastSelectedPosition = position;
            }
        }
        holder.radioButton.setChecked(lastSelectedPosition == position);
        holder.llBrandsLogo.getLayoutParams().width = width.intValue();
        holder.llBrandsLogo.getLayoutParams().height = width.intValue();
        if (arrayList.get(position).getMedia().size() > 0) {
            if (arrayList.get(position).getMedia().get(0) != null && arrayList.get(position).getMedia().get(0).getFileUrl() != null) {
                UIHelper.setImageWithGlide(activityContext, holder.ivBrandLogo, arrayList.get(position).getMedia().get(0).getFileUrl());
            }
        }

        if (lastSelectedPosition == position) {
            selectedBrand = arrayList.get(lastSelectedPosition);
            holder.llBrandsLogo.setBackground(activityContext.getResources().getDrawable(R.drawable.bordered_item_rounded_filtered_brands_selected));
        } else {
            holder.llBrandsLogo.setBackground(activityContext.getResources().getDrawable(R.drawable.bordered_item_rounded_filtered_brands));
        }

        holder.llBrandsLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViewsForSomeSeconds(holder.llBrandsLogo);
                selectedBrand = arrayList.get(position);
                lastSelectedPosition = position;
                BrandsModelsListFragment brandsModelsListFragment = new BrandsModelsListFragment(activityContext, arrayList.get(position), binding, listener, modelsSelectedListener);
                brandsModelsListFragment.setBrandModelsSelectedListener(brandModelsSelectedListener);
                activityContext
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(activityContext.getDockFrameLayoutId(), brandsModelsListFragment, BrandsModelsListFragment.class.getSimpleName())
                        .addToBackStack(activityContext.getSupportFragmentManager().getBackStackEntryCount() == 0 ? "firstFrag" : null)
                        .commit();
            }
        });


//
//        for (int pos = 0; pos < LuxuryMarketSearchFilter.getInstance().getBrandsList().size(); pos++) {
//            if (LuxuryMarketSearchFilter.getInstance().getBrandsList().get(pos).getId() == arrayList.get(position).getId()) {
//                holder.llBrandsLogo.setBackground(activityContext.getResources().getDrawable(R.drawable.bordered_item_rounded_filtered_brands_selected));
//                break;
//            }
//        }
//
//        holder.llBrandsLogo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean found = false;
//                int foundPos = -1;
//                for (int pos = 0; pos < LuxuryMarketSearchFilter.getInstance().getBrandsList().size(); pos++) {
//                    if (LuxuryMarketSearchFilter.getInstance().getBrandsList().get(pos).getId() == arrayList.get(position).getId()) {
//                        found = true;
//                        foundPos = pos;
//                        break;
//                    }
//                }
//                if (found) {
//                    LuxuryMarketSearchFilter.getInstance().removeBrand(foundPos);
//                    holder.llBrandsLogo.setBackground(activityContext.getResources().getDrawable(R.drawable.bordered_item_rounded_filtered_brands));
//                } else {
//                    LuxuryMarketSearchFilter.getInstance().addBrand(arrayList.get(position));
//                    holder.llBrandsLogo.setBackground(activityContext.getResources().getDrawable(R.drawable.bordered_item_rounded_filtered_brands_selected));
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }

    /*
        @Override
        public Object[] getSections() {
            List<String> sections = new ArrayList<>(26);
            mSectionPositions = new ArrayList<>(26);
            int a  = 0;
            for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
                sections.add(alphabet+"");
                mSectionPositions.add(a);
                a++;
            }

            return sections.toArray(new String[0]);
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            binding.recyclerView.getLayoutManager().scrollToPosition(12);
            Log.i("testing", "Section Index: " + sectionIndex);
            return mSectionPositions.get(sectionIndex);
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }
    */
    public void addAll(ArrayList<FilterBrand> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    public ArrayList<FilterBrand> getArrayList() {
        return arrayList;
    }

    public void filterList(ArrayList<FilterBrand> arrayList) {
        this.arrayList = arrayList;
    }

    public void disableViewsForSomeSeconds(View view) {
        view.setEnabled(false);
        view.setAlpha((float) 0.75);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
                view.setAlpha((float) 1.0);
            }
        }, AppConstants.SPLASH_DURATION);
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public FilterBrand getSelectedBrand() {
        return selectedBrand;
    }

    public void setSelectedBrand(FilterBrand selectedBrand) {
        this.selectedBrand = selectedBrand;
    }

    public void setBrandModelsSelectedListener(BrandModelsSelectedListener brandModelsSelectedListener) {
        this.brandModelsSelectedListener = brandModelsSelectedListener;
    }

    public class Holder extends RecyclerView.ViewHolder {
        RelativeLayout llBrandsLogo;
        ImageView ivBrandLogo;
        RadioButton radioButton;

        Holder(View view) {
            super(view);
            llBrandsLogo = view.findViewById(R.id.llBrandsLogo);
            ivBrandLogo = view.findViewById(R.id.ivBrandLogo);
            radioButton = view.findViewById(R.id.radio);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedBrand = null;
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
