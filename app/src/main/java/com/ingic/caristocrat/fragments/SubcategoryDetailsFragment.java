package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.ArticlesAdapter;
import com.ingic.caristocrat.adapters.SubcategoriesFeatureAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentSubcategoriesDetailsBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.RecyclerTouchListener;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.ClickListenerRecycler;
import com.ingic.caristocrat.models.Article;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.News;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 */
public class SubcategoryDetailsFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    FragmentSubcategoriesDetailsBinding binding;
    LinearLayoutManager linearLayoutManager;
    SubcategoriesFeatureAdapter featureAdapter;
    ArticlesAdapter adapter;
    ArrayList<News> featuredArticleArrayList;
    ArrayList<Article> arrayList;
    ArrayList<News> news = new ArrayList<>(), loadedNews;
    int categoryId;
    String title = "";
    ArrayList<News> nonFeaturedNews = new ArrayList<>();

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;
    int LIMIT = 10, OFFSET = 0, TOTAL_RECORDS = 0;
    boolean ON_CALL = false;

    public SubcategoryDetailsFragment() {
    }

    public static SubcategoryDetailsFragment Instance() {
        return new SubcategoryDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subcategories_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvNoDataLayout = binding.noDataLayout.findViewById(R.id.noDataText);
        if (title != null) {
            tvNoDataLayout.setText("There are no " + title.toLowerCase() + " to Show.");
        }

        initAdapter();

        setListeners();

        if (news.size() == 0) {
            if (mainActivityContext.showLoader()) {
                getCategorieDetail();
            } else {
                hideView();
            }
        } else {
            featureAdapter.addFullNewsList(news);
            for (int position = 0; position < news.size(); position++) {
                if (news.get(position).getIsFeatured() == 0) {
                    nonFeaturedNews.add(news.get(position));
                    adapter.add(news.get(position));
                } else {
                    featureAdapter.add(news.get(position));
                }
            }
            if (adapter.getItemCount() > 0) {
                adapter.notifyDataSetChanged();
                visibleView();
            }
            if (featureAdapter.getCount() > 0) {
                featureAdapter.notifyDataSetChanged();

                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage > featureAdapter.getCount() - 1) {
                            currentPage = 0;
                        }
                        binding.viewpager.setCurrentItem(currentPage++);
                    }
                };

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, DELAY_MS, PERIOD_MS);
            } else {
                binding.rlSlider.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showTitlebar(mainActivityContext);
        titlebar.setTitle(title);
        titlebar.showBackButton(mainActivityContext, false);
//        titlebar.showSearchButton(mainActivityContext).setOnClickListener(this);
        titlebar.showHomeButton(mainActivityContext);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibSearch:
                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_LONG);
                break;
        }
    }

    void setListeners() {
//        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                binding.swipeContainer.setRefreshing(false);
//            }
//        });

        binding.viewpager.setOnPageChangeListener(this);
    }

    public void visibleView() {
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.noDataLayout.setVisibility(View.GONE);
    }

    public void hideView() {
        binding.rlSlider.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.noDataLayout.setVisibility(View.VISIBLE);
    }

    private void initAdapter() {
        adapter = new ArticlesAdapter(mainActivityContext, binding, false);
        linearLayoutManager = new LinearLayoutManager(mainActivityContext);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setAdapter(adapter);
        featureAdapter = new SubcategoriesFeatureAdapter(mainActivityContext, binding, true);
      /*  linearLayoutManager = new LinearLayoutManager(mainActivityContext, LinearLayoutManager.HORIZONTAL, false);
        binding.rvNews.setLayoutManager(linearLayoutManager);*/
//        binding.rvNews.setNestedScrollingEnabled(false);
//        SnapHelper snapHelper=new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(binding.rvNews);
        binding.viewpager.setAdapter(featureAdapter);
        binding.circlePageIndicator.setViewPager(binding.viewpager);
        binding.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.recyclerView, new ClickListenerRecycler() {
            @Override
            public void onClick(View view, int position) {
                if (nonFeaturedNews != null && nonFeaturedNews.size() > 0) {
                    MainDetailPageFragment mainDetailPageFragment = new MainDetailPageFragment();
                    mainDetailPageFragment.setCategoryId(nonFeaturedNews.get(position).getId());
                    if (nonFeaturedNews.get(position).getMedia().size() > 0) {
                        mainDetailPageFragment.setImageUrl(nonFeaturedNews.get(position).getMedia().get(0).getFileUrl());
                    }
                    ArrayList<News> similarListing = new ArrayList<>();
                    similarListing.addAll(news);
                    similarListing.remove(nonFeaturedNews.get(position));
                    mainDetailPageFragment.setSimilarListing(similarListing);
                    mainActivityContext.replaceFragment(mainDetailPageFragment, MainDetailPageFragment.class.getSimpleName(), true, false);

                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItem == adapter.getItemCount() - 1) {
                    if (adapter.getItemCount() < TOTAL_RECORDS - 1) {
                        if (!ON_CALL) {
                            OFFSET = OFFSET + LIMIT;
                            ON_CALL = true;
                            if (mainActivityContext.showLoader()) {
                                getCategorieDetail();
                            }
                        }
                    }
                }
            }
        });
    }

    private void setFeaturedNews() {
        if (news.size() == 0) {
            binding.rlSlider.setVisibility(View.GONE);
        } else {
            if (featureAdapter != null)
                featureAdapter.addFullNewsList(news);

            featuredArticleArrayList = new ArrayList<News>();
            for (int i = 0; i < news.size(); i++) {
                if (news.get(i).getIsFeatured() == 1) {
                    featuredArticleArrayList.add(news.get(i));
                }
            }
            if (featuredArticleArrayList.size() > 0) {
                featureAdapter.addAll(featuredArticleArrayList);
                featureAdapter.notifyDataSetChanged();
                binding.circlePageIndicator.setViewPager(binding.viewpager);
                binding.rlSlider.setVisibility(View.VISIBLE);

                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
//                        Log.i("CAR_LOG", "running");
                        if (currentPage > featureAdapter.getCount() - 1) {
                            currentPage = 0;
                        }
                        binding.viewpager.setCurrentItem(currentPage++);
                    }
                };

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, DELAY_MS, PERIOD_MS);
            } else {
                binding.rlSlider.setVisibility(View.GONE);
            }
        }
    }

    private void getCategorieDetail() {
        if (timer != null) {
            timer.cancel();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("category_id", categoryId);
        params.put("limit", LIMIT);
        params.put("offset", OFFSET);
        WebApiRequest.Instance(mainActivityContext).request(
                AppConstants.WebServicesKeys.NEWS_V2, null,
                null,
                params,
                null,
                new WebApiRequest.WebServiceArrayResponse() {
                    @Override
                    public void onSuccess(ApiArrayResponse apiArrayResponse) {
                        TOTAL_RECORDS = apiArrayResponse.getTotal_count();
                        loadedNews = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), News.class);
                        news.addAll(loadedNews);
                        /*
                        if (news.size() > 0) {
                            binding.recyclerView.setVisibility(View.VISIBLE);
                            visibleView();

//                            nonFeaturedNews = new ArrayList<>();
                            for (int position = 0; position < news.size(); position++) {
                                if (news.get(position).getIsFeatured() == 0) {
                                    nonFeaturedNews.add(news.get(position));
                                }
                            }
                            adapter.addAll(nonFeaturedNews);

//                            adapter.addAll(news);
                            adapter.notifyDataSetChanged();
                        } else {
                            hideView();
                        }
                        setFeaturedNews();
                        */
                        if (loadedNews.size() > 0) {
                            featureAdapter.addFullNewsList(loadedNews);
                            for (int position = 0; position < loadedNews.size(); position++) {
                                if (loadedNews.get(position).getIsFeatured() == 0) {
                                    nonFeaturedNews.add(loadedNews.get(position));
                                    adapter.add(loadedNews.get(position));
                                } else {
                                    featureAdapter.add(loadedNews.get(position));
                                }
                            }
                            if (adapter.getItemCount() > 0) {
                                adapter.notifyDataSetChanged();
                                visibleView();
                            }
                            if (featureAdapter.getCount() > 0) {
                                featureAdapter.notifyDataSetChanged();

                                final Handler handler = new Handler();
                                final Runnable Update = new Runnable() {
                                    public void run() {
                                        if (currentPage > featureAdapter.getCount() - 1) {
                                            currentPage = 0;
                                        }
                                        binding.viewpager.setCurrentItem(currentPage++);
                                    }
                                };

                                timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        handler.post(Update);
                                    }
                                }, DELAY_MS, PERIOD_MS);
                            } else {
                                binding.rlSlider.setVisibility(View.GONE);
                            }
                        } else {
                            hideView();
                        }
                        mainActivityContext.hideLoader();
                        ON_CALL = false;
                    }

                    @Override
                    public void onError() {
                        mainActivityContext.hideLoader();
                        ON_CALL = false;
                    }
                }
        );
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        Log.i("testing", position + "");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
