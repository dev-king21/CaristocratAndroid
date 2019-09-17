package com.ingic.caristocrat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentSubcategoriesDetailsBinding;
import com.ingic.caristocrat.databinding.LayoutSubcategoryDetailsArticleItemBinding;
import com.ingic.caristocrat.fragments.LuxuryMarketDetailsFragment;
import com.ingic.caristocrat.fragments.MainDetailPageFragment;
import com.ingic.caristocrat.helpers.DateFormatHelper;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.News;

import java.util.ArrayList;

/**
 * on 8/3/2018.
 */
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.Holder> {
    MainActivity mainActivityContext;
    FragmentSubcategoriesDetailsBinding fragmentSubcategoriesDetailsBinding;
    LayoutSubcategoryDetailsArticleItemBinding binding;
    ArrayList<News> arrayList;
    ArrayList<TradeCar> tradeCars;
    boolean isCars;
    boolean fromProfile;
    private static final int LAYOUT_FAV_ITEM = R.layout.layout_subcategory_details_article_item;
    private static final int LAYOUT_CARS_ITEM = R.layout.layout_my_cars;

    public ArticlesAdapter(MainActivity mainActivityContext, FragmentSubcategoriesDetailsBinding fragmentSubcategoriesDetailsBinding, boolean fromProfile) {
        this.mainActivityContext = mainActivityContext;
        this.fragmentSubcategoriesDetailsBinding = fragmentSubcategoriesDetailsBinding;
        this.arrayList = new ArrayList<>();
        this.tradeCars = new ArrayList<>();
        this.fromProfile = fromProfile;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mainActivityContext).inflate(viewType, parent, false), viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isCars)
            return LAYOUT_CARS_ITEM;
        else
            return LAYOUT_FAV_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        switch (holder.viewType) {
            case LAYOUT_FAV_ITEM:
                if (arrayList.get(position).getIsFeatured() == 1) {
                    holder.tvFeatured.setVisibility(View.VISIBLE);
                } else {
                    holder.tvFeatured.setVisibility(View.GONE);
                }
                if (arrayList.get(position).getMedia() != null && arrayList.get(position).getMedia().size() > 0) {
                    if (!Utils.getYouTubeId(arrayList.get(position).getMedia().get(0).getFileUrl()).equals("error")) {
                        UIHelper.setImageWithGlide(mainActivityContext, holder.ivCarThumbnail, "http://img.youtube.com/vi/" + Utils.getYouTubeId(arrayList.get(position).getMedia().get(0).getFileUrl()) + "/0.jpg");
                    } else {
                        UIHelper.setImageWithGlide(mainActivityContext, holder.ivCarThumbnail, arrayList.get(position).getMedia().get(0).getFileUrl(), true);
                    }
                }

                holder.tvNewsDescription.setText((arrayList.get(position).getHeadline() == null) ? "" : Utils.compressText(arrayList.get(position).getHeadline(), AppConstants.NewsTitleLimit));
                holder.tvLikesCount.setVisibility(View.VISIBLE);
                holder.tvCommentsCount.setVisibility(View.VISIBLE);
                holder.tvDate.setVisibility(View.VISIBLE);
                holder.tvViewsCount.setText(arrayList.get(position).getViewsCount() + "");
                holder.tvLikesCount.setText(arrayList.get(position).getLikeCount() + "");
                holder.tvCommentsCount.setText(arrayList.get(position).getCommentsCount() + "");
                if (fromProfile && arrayList.get(position).isLiked())
                    holder.ivFav.setVisibility(View.VISIBLE);
                holder.tvDate.setText((arrayList.get(position).getCreatedAt() == null) ? "" : DateFormatHelper.changeServerToOurFormatDate(arrayList.get(position).getCreatedAt()));
                holder.rlSubCategoryDetailsNewsItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UIHelper.hideSoftKeyboard(mainActivityContext, view);
                        MainDetailPageFragment mainDetailPageFragment = new MainDetailPageFragment();
                        mainDetailPageFragment.setCategoryId(arrayList.get(position).getId());
                        mainDetailPageFragment.setImageUrl(arrayList.get(position).getMedia().get(0).getFileUrl());
                        ArrayList<News> similarListing = new ArrayList<>();
                        similarListing.addAll(arrayList);
                        similarListing.remove(position);
                        mainDetailPageFragment.setSimilarListing(similarListing);
                        mainActivityContext.replaceFragment(mainDetailPageFragment, MainDetailPageFragment.class.getSimpleName(), true, false);
                    }
                });
                break;
            case LAYOUT_CARS_ITEM:
                if (tradeCars.get(position).getModel() != null) {
                    holder.tradeIn.setText(tradeCars.get(position).getName());
//                        holder.tradeIn.setText(mainActivityContext.getResources().getString(R.string.here_are_top_5) + " " + (!UIHelper.isEmptyOrNull(tradeCars.get(position).getName()) ? tradeCars.get(position).getName().toLowerCase() : ""));
                } else {
                    holder.tradeIn.setText(tradeCars.get(position).getName());
                }
                holder.model.setText(mainActivityContext.getResources().getString(R.string.model) + " " + (!UIHelper.isEmptyOrNull(tradeCars.get(position).getModel().getName() + "") ? tradeCars.get(position).getModel().getName() : ""));
                holder.chasis.setText(mainActivityContext.getResources().getString(R.string.chassis) + " " + (!UIHelper.isEmptyOrNull(tradeCars.get(position).getChassis()) ? tradeCars.get(position).getChassis() : ""));
                if (tradeCars.get(position).getMedia() != null && tradeCars.get(position).getMedia().size() > 0)
                    UIHelper.setImageWithGlide(mainActivityContext, holder.image, tradeCars.get(position).getMedia().get(0).getFileUrl());

                holder.llMainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UIHelper.hideSoftKeyboard(mainActivityContext, view);
                        LuxuryMarketDetailsFragment luxuryMarketDetailsFragment = new LuxuryMarketDetailsFragment();
                        luxuryMarketDetailsFragment.setCurrentTradeCar(tradeCars.get(position));
//                        luxuryMarketDetailsFragment.setSimilarListings(tradeCars);
                        if (tradeCars.get(position).getCategory() != null) {
                            luxuryMarketDetailsFragment.setCategoryKey(tradeCars.get(position).getCategory().getSlug());
                        }
                        mainActivityContext.replaceFragment(luxuryMarketDetailsFragment, LuxuryMarketDetailsFragment.class.getSimpleName(), true, true);

                    }
                });

                break;
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (!isCars && arrayList != null)
            size = arrayList.size();
        if (isCars && tradeCars != null)
            size = tradeCars.size();
        return size;
    }

    public void add(News news) {
        this.arrayList.add(news);
    }

    public void addAll(ArrayList<News> arrayList) {
//        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    public void addAllCars(ArrayList<TradeCar> arrayList) {
        this.tradeCars.clear();
        this.tradeCars.addAll(arrayList);
    }

    public ArrayList<News> getNews() {
        return this.arrayList;
    }

    protected static class Holder extends RecyclerView.ViewHolder {
        //common
        View view;
        int viewType;

        TextView tvFeatured;
        TextView tradeIn;
        ImageView image;
        TextView model;
        TextView chasis;
        ImageView ivCarThumbnail;
        TextView tvNewsDescription;
        TextView tvLikesCount;
        TextView tvCommentsCount;
        TextView tvDate;
        TextView tvViewsCount;
        ImageView ivFav;
        RelativeLayout rlSubCategoryDetailsNewsItem;
        LinearLayout llMainLayout;

        FrameLayout flListLoadingMore;

        public Holder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            this.view = view;
            if (viewType == LAYOUT_CARS_ITEM) {
                tradeIn = (TextView) itemView.findViewById(R.id.tvTradeIn);
                model = (TextView) itemView.findViewById(R.id.tvModel);
                chasis = (TextView) itemView.findViewById(R.id.tvChasis);
                image = (ImageView) itemView.findViewById(R.id.ivTradeIn);
                llMainLayout = (LinearLayout) itemView.findViewById(R.id.llMainLayout);
            } else {
                tvNewsDescription = (TextView) view.findViewById(R.id.tvNewsDescription);
                tvLikesCount = (TextView) view.findViewById(R.id.tvLikesCount);
                tvCommentsCount = (TextView) view.findViewById(R.id.tvCommentsCount);
                tvDate = (TextView) view.findViewById(R.id.tvDate);
                tvViewsCount = (TextView) view.findViewById(R.id.tvViewsCount);
                ivFav = (ImageView) view.findViewById(R.id.ivFav);
                ivCarThumbnail = (ImageView) view.findViewById(R.id.ivArticleThumbnail);
                rlSubCategoryDetailsNewsItem = (RelativeLayout) view.findViewById(R.id.rlSubCategoryDetailsNewsItem);
                tvFeatured = view.findViewById(R.id.tvFeatured);
            }
        }
    }

    public void setCars(boolean cars) {
        isCars = cars;
    }
}
