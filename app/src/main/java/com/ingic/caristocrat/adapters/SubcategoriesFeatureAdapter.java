package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentSubcategoriesDetailsBinding;
import com.ingic.caristocrat.fragments.FullImageFragment;
import com.ingic.caristocrat.fragments.MainDetailPageFragment;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.webhelpers.models.Media;
import com.ingic.caristocrat.webhelpers.models.News;

import java.util.ArrayList;

/**
 *  on 8/3/2018.
 */
public class SubcategoriesFeatureAdapter extends PagerAdapter {
    MainActivity mainActivityContext;
    FragmentSubcategoriesDetailsBinding fragmentSubcategoriesDetailsBinding;
    LayoutInflater layoutInflater;
    ArrayList<News> arrayList, fullNewsList;
    boolean fromMainDetail;

    public SubcategoriesFeatureAdapter(MainActivity mainActivityContext, FragmentSubcategoriesDetailsBinding fragmentSubcategoriesDetailsBinding, boolean fromMainDetail) {
        this.mainActivityContext = mainActivityContext;
        this.fragmentSubcategoriesDetailsBinding = fragmentSubcategoriesDetailsBinding;
        this.layoutInflater = (LayoutInflater) mainActivityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = new ArrayList<>();
        this.fullNewsList = new ArrayList<>();
        this.fromMainDetail = fromMainDetail;
    }

    public void add(News news) {
        this.arrayList.add(news);
    }

    public void addAll(ArrayList<News> arrayList) {
//        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    public void addFullNewsList(ArrayList<News> arrayList) {
//        this.fullNewsList.clear();
        this.fullNewsList.addAll(arrayList);
    }

    public ArrayList<News> getFeaturedNews() {
        return arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View rootView = layoutInflater.inflate(R.layout.layout_subcategory_details_featured, container, false);

        RelativeLayout rlFeaturedMain = rootView.findViewById(R.id.rlFeaturedMain);
        ImageView ivFeaturedBackground = rootView.findViewById(R.id.ivFeaturedBackground);
        TextView tvArticleTitle = rootView.findViewById(R.id.tvArticleTitle);
        TextView tvFeaturedThisWeek = rootView.findViewById(R.id.tvFeaturedThisWeek);
        TextView tvFeatured = rootView.findViewById(R.id.tvFeatured);
        if (fromMainDetail) {
            tvFeatured.setVisibility(View.VISIBLE);
            if (arrayList.get(position).getMedia().size() > 0) {
                if (arrayList.get(position).getMedia().get(0).getResourceId() != 0) {
                    ivFeaturedBackground.setImageResource(arrayList.get(position).getMedia().get(0).getResourceId());
                } else {
                    if (!Utils.getYouTubeId(arrayList.get(position).getMedia().get(0).getFileUrl()).equals("error")) {
                        UIHelper.setImageWithGlide(mainActivityContext, ivFeaturedBackground, "http://img.youtube.com/vi/" + Utils.getYouTubeId(arrayList.get(position).getMedia().get(0).getFileUrl()) + "/0.jpg");
                    } else {
                        UIHelper.setImageWithGlide(mainActivityContext, ivFeaturedBackground, arrayList.get(position).getMedia().get(0).getFileUrl());
                    }
                }
            }

            if (arrayList.get(position).getHeadline() != null) {
                tvArticleTitle.setText(Utils.compressText(arrayList.get(position).getHeadline(), AppConstants.FeaturedNewsTitleLimit));
            } else {
                tvFeaturedThisWeek.setVisibility(View.GONE);
                tvArticleTitle.setVisibility(View.GONE);
            }

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (arrayList.get(position).getMedia().get(0).getResourceId() == 0 && fromMainDetail) {
                        MainDetailPageFragment mainDetailPageFragment = new MainDetailPageFragment();
                        mainDetailPageFragment.setCategoryId(arrayList.get(position).getId());
                        ArrayList<News> similarListings = new ArrayList<>();
                        similarListings.addAll(fullNewsList);
                        for (int pos = 0; pos < fullNewsList.size(); pos++) {
                            if (fullNewsList.get(pos).getId() == arrayList.get(position).getId()) {
                                similarListings.remove(fullNewsList.get(pos));
                                break;
                            }
                        }
                        mainDetailPageFragment.setSimilarListing(similarListings);
                        /*
                        if (arrayList.get(position).getMedia().get(0).getMediaType() == AppConstants.MediaType.IMAGE) {
                            mainDetailPageFragment.setImageUrl(arrayList.get(position).getMedia().get(0).getFileUrl());
                        } else if (arrayList.get(position).getMedia().get(0).getMediaType() == AppConstants.MediaType.VIDEO) {
                            mainDetailPageFragment.setImageUrl("http://img.youtube.com/vi/" + Utils.getYouTubeId(arrayList.get(position).getMedia().get(0).getFileUrl()) + "/0.jpg");
                        }
                        */
                        mainActivityContext.replaceFragment(mainDetailPageFragment, MainDetailPageFragment.class.getSimpleName(), true, false);
                    }
                }
            });
        } else {
            if (arrayList.get(position).getMedia().size() > 0) {
                UIHelper.setImageWithGlideNoPlaceHolder(mainActivityContext, ivFeaturedBackground, arrayList.get(position).getMedia().get(0).getFileUrl());
                tvFeaturedThisWeek.setVisibility(View.GONE);
                tvArticleTitle.setVisibility(View.GONE);
                ivFeaturedBackground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FullImageFragment fullImageFragment = new FullImageFragment();
                        ArrayList<Media> mediaArrayList = new ArrayList<>();
                        for (int pos = 0; pos < arrayList.size(); pos++) {
                            mediaArrayList.add(arrayList.get(pos).getMedia().get(0));
                        }
                        fullImageFragment.setArrayList(mediaArrayList);
                        fullImageFragment.setCurrentPosition(position);
                        mainActivityContext.replaceFragment(fullImageFragment, FullImageFragment.class.getSimpleName(), true, false);
                    }
                });
            }
        }
        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
