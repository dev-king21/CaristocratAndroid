package com.ingic.caristocrat.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.databinding.FragmentAutoLifeBinding;
import com.ingic.caristocrat.databinding.ItemAutoLifeBinding;
import com.ingic.caristocrat.fragments.AutoLifeFragment;
import com.ingic.caristocrat.helpers.ItemTouchHelperAdapter;
import com.ingic.caristocrat.helpers.ItemTouchHelperViewHolder;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.CategoryPositionsChangedListener;
import com.ingic.caristocrat.interfaces.OnStartDragListener;
import com.ingic.caristocrat.webhelpers.models.Category;

import java.util.ArrayList;
import java.util.Collections;

/**
 * on 8/3/2018.
 */
public class AutoLifeCategroriesAdapter extends RecyclerView.Adapter<AutoLifeCategroriesAdapter.Holder> implements ItemTouchHelperAdapter {
    MainActivity mainActivityContext;
    ArrayList<Category> arrayList;
    FragmentAutoLifeBinding fragmentAutoLifeBinding;
    private OnStartDragListener mDragStartListener;
    ItemAutoLifeBinding binding;
    CategoryPositionsChangedListener categoryPositionsChangedListener;

    public AutoLifeCategroriesAdapter(MainActivity mainActivityContext, FragmentAutoLifeBinding fragmentAutoLifeBinding, OnStartDragListener mDragStartListener, ArrayList<Category> autoLifeCategories, CategoryPositionsChangedListener categoryPositionsChangedListener) {
        this.mainActivityContext = mainActivityContext;
        this.fragmentAutoLifeBinding = fragmentAutoLifeBinding;
        this.mDragStartListener = mDragStartListener;
        this.arrayList = autoLifeCategories;
        this.categoryPositionsChangedListener = categoryPositionsChangedListener;
    }

    @NonNull
    @Override
    public AutoLifeCategroriesAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mainActivityContext), R.layout.item_auto_life, parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        binding.tvSubcategoryTitle.setText(arrayList.get(position).getName());
        binding.tvSubcategoryDescription.setText(arrayList.get(position).getDescription());
        if(arrayList.get(position).getMedia().size() > 0){
            UIHelper.setImageWithGlide(mainActivityContext, binding.ivSubCategoryItem, arrayList.get(position).getMedia().get(0).getFileUrl());
        }

        if (mainActivityContext.getPreferenceHelper().getLoginStatus() && arrayList.get(position).getUnreadCount() > 0) {
            binding.tvNewArticlesCount.setText((arrayList.get(position).getUnreadCount() == 1) ? arrayList.get(position).getUnreadCount() + " " + mainActivityContext.getResources().getString(R.string.new_article) : arrayList.get(position).getUnreadCount() + " " + mainActivityContext.getResources().getString(R.string.new_articles));
//            binding.tvNewArticlesCount.setText(arrayList.get(position).getUnreadCount() + " " + mainActivityContext.getResources().getString(R.string.new_articles));
            binding.tvNewArticlesCount.setVisibility(View.VISIBLE);
        }else{
            binding.tvNewArticlesCount.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addAll(ArrayList<Category> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(arrayList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(arrayList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        mainActivityContext.getPreferenceHelper().putSubCategoriesList(null);
        mainActivityContext.getPreferenceHelper().putSubCategoriesList(arrayList);
        categoryPositionsChangedListener.onCategoryPositionChanged(0, arrayList);
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public void markRead(int position){
        this.arrayList.get(position).setUnreadCount(0);
    }

    public static class Holder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        ItemAutoLifeBinding binding;
        static MainActivity context;

        Holder(ItemAutoLifeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public Holder(View itemView) {
            super(itemView);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(context.getResources().getColor(R.color.colorTransparentGray));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
