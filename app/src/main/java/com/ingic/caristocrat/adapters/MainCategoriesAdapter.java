package com.ingic.caristocrat.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentHomeBinding;
import com.ingic.caristocrat.databinding.LayoutMainCategoryBinding;
import com.ingic.caristocrat.helpers.ItemTouchHelperAdapter;
import com.ingic.caristocrat.helpers.ItemTouchHelperViewHolder;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.OnStartDragListener;
import com.ingic.caristocrat.webhelpers.models.Category;

import java.util.ArrayList;
import java.util.Collections;

/**
 *  on 8/2/2018.
 */
public class MainCategoriesAdapter extends RecyclerView.Adapter<MainCategoriesAdapter.Holder> implements ItemTouchHelperAdapter {
    MainActivity mainActivityContext;
    FragmentHomeBinding homeFragmentHomeBinding;
    LayoutMainCategoryBinding binding;
    private OnStartDragListener onStartDragListener;
    ArrayList<Category> arrayList;

    public MainCategoriesAdapter(MainActivity mainActivityContext, OnStartDragListener onStartDragListener, FragmentHomeBinding homeFragmentHomeBinding) {
        this.mainActivityContext = mainActivityContext;
        this.homeFragmentHomeBinding = homeFragmentHomeBinding;
        this.onStartDragListener = onStartDragListener;
        this.arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mainActivityContext), R.layout.layout_main_category, parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        binding.tvArticleTitle.setText(arrayList.get(position).getName());
        binding.tvArticleShortDescription.setText(arrayList.get(position).getSubtitle());
        if (arrayList.get(position).getMedia().size() > 0) {
//            if(arrayList.get(position).getSlug().equals(AppConstants.MainCategoriesType.AUTO_LIFE)){
//                binding.ivMainCategoryBackground.setImageDrawable(mainActivityContext.getResources().getDrawable(R.drawable.auto_life));
//            }
            UIHelper.setImageWithGlide(mainActivityContext, binding.ivMainCategoryBackground, arrayList.get(position).getMedia().get(0).getFileUrl());
        }
        if (mainActivityContext.getPreferenceHelper().getLoginStatus() && arrayList.get(position).getUnreadCount() > 0) {
            binding.tvNewArticlesCount.setText((arrayList.get(position).getUnreadCount() == 1) ? arrayList.get(position).getUnreadCount() + " " + mainActivityContext.getResources().getString(R.string.new_article) : arrayList.get(position).getUnreadCount() + " " + mainActivityContext.getResources().getString(R.string.new_articles));
//            binding.tvNewArticlesCount.setText(arrayList.get(position).getUnreadCount() + " " + mainActivityContext.getResources().getString(R.string.new_articles));
            binding.tvNewArticlesCount.setVisibility(View.VISIBLE);
        } else {
            binding.tvNewArticlesCount.setVisibility(View.GONE);
        }
//        binding.ivMainCategoryBackground.setImageResource(arrayList.get(position).getBackgroundImage());
//        if(arrayList.get(position).getNewArticleCount() > 0){
//            binding.tvNewArticlesCount.setText(arrayList.get(position).getNewArticleCount() + " " + mainActivityContext.getResources().getString(R.string.new_articles));
//            binding.tvNewArticlesCount.setVisibility(View.VISIBLE);
//        }
       /* if(position != 0 && position % 2 != 0){
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            params1.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            binding.llArticleDescriptionTitile.setLayoutParams(params1);
        }*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addAll(ArrayList<Category> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
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
//        notifyItemChanged(fromPosition);
//        notifyItemChanged(toPosition);
        mainActivityContext.getPreferenceHelper().putCategoriesList(null);
        mainActivityContext.getPreferenceHelper().putCategoriesList(arrayList);
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public static class Holder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        LayoutMainCategoryBinding binding;
        static MainActivity context;

        Holder(LayoutMainCategoryBinding binding) {
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
