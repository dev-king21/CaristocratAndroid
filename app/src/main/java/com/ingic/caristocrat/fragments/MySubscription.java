package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.MySubscriptionAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentMySubscriptionBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.Subscription;
import com.ingic.caristocrat.webhelpers.models.User;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class MySubscription extends BaseFragment {
    FragmentMySubscriptionBinding binding;
    User user;

    public static MySubscription Instance() {
        return new MySubscription();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_subscription, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = preferenceHelper.getUser();
        getAllSubscription();
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showTitlebar(mainActivityContext);
        titlebar.setTitle("My Subscriptions");
        titlebar.showBackButton(mainActivityContext, false);
    }

    private void getAllSubscription() {
        if (mainActivityContext.showLoader()) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", user.getId());
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.GET_ALL_SUBSCRIPTION, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
                @Override
                public void onSuccess(ApiArrayResponse apiArrayResponse) {
                    ArrayList<Subscription> list = (ArrayList<Subscription>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Subscription.class);
                    Log.i("Subscription", list.toString());
                    mainActivityContext.hideLoader();


                    SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();


                    HashMap<String, ArrayList<Subscription>> hashMap = new HashMap<>();

                    for (Subscription sub : list) {
                        ArrayList<Subscription> temp = hashMap.get(sub.getType());
                        if (temp != null) {
                            temp.add(sub);
                            hashMap.put(sub.getType(), temp);
                        } else {
                            ArrayList<Subscription> temp2 = new ArrayList<>();
                            temp2.add(sub);
                            hashMap.put(sub.getType(), temp2);
                        }
                    }


                    Set set = hashMap.entrySet();
                    Iterator iterator = set.iterator();

                    while (iterator.hasNext()) {
                        Map.Entry mentry = (Map.Entry) iterator.next();
                        System.out.print("key is: " + mentry.getKey() + " & Value is: ");
                        System.out.println(mentry.getValue());
                        sectionAdapter.addSection(new MySubscriptionAdapter(mentry.getKey().toString(), (List<Subscription>) mentry.getValue(), mainActivityContext));

                    }
                    binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerview.setAdapter(sectionAdapter);


                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            });
        }
    }

}
