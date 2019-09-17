package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.MyTableViewAdapter;
import com.ingic.caristocrat.databinding.FragmentCompareResultBinding;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.models.LimitedEditionSpec;
import com.ingic.caristocrat.models.TradeCar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * on 1/11/2019.
 */
public class CompareResultFragment extends BaseFragment {
    Titlebar mTitleBar;
    FragmentCompareResultBinding binding;
    ArrayList<TradeCar> tradeCars = new ArrayList<>();
    MyTableViewAdapter adapter;
    private List<TradeCar> mRowHeaderList = new ArrayList<>();
    private List<LimitedEditionSpec> mColumnHeaderList = new ArrayList<>();
    private List<List<LimitedEditionSpec>> mCellList = new ArrayList<>();
    boolean fromSearchPanel;
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public CompareResultFragment() {

    }

    public static CompareResultFragment Instance() {
        return new CompareResultFragment();
    }

    public void setTradeCars(ArrayList<TradeCar> tradeCars) {
        this.tradeCars.clear();
        this.tradeCars = tradeCars;
    }

    public void setFromSearchPanel(boolean fromSearchPanel) {
        this.fromSearchPanel = fromSearchPanel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_result, container, false);
        initAdapter();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (fromSearchPanel) {
            mTitleBar.resetTitlebar(mainActivityContext);
            mTitleBar.setTitle(mainActivityContext.getResources().getString(R.string.compare));
            mTitleBar.showBackButton(mainActivityContext, false);
//            mTitleBar.showFilter();

            mainActivityContext.getBinding().btnFilterAction.setVisibility(View.VISIBLE);
            mainActivityContext.getBinding().btnFilterAction.setText(mainActivityContext.getResources().getString(R.string.compare_cars));
//            mainActivityContext.getBinding().btnFilterAction.setOnClickListener(this);
            mainActivityContext.getBinding().llCommentsLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initAdapter() {
        adapter = new MyTableViewAdapter(mainActivityContext);
        binding.contentContainer.setAdapter(adapter);
        setData();
    }

    private void setData() {
        mRowHeaderList.addAll(tradeCars);
        LimitedEditionSpec data;

        data = new LimitedEditionSpec();
//        data.setName(mainActivityContext.getResources().getString(R.string.remaining_lifecycle));
//        data.setValue("");
//        mColumnHeaderList.add(data);

        if (tradeCars.get(0).getLimitedEditionSpecsArray().getDimensionsWeight() != null) {
            for (int j = 0; j < tradeCars.get(0).getLimitedEditionSpecsArray().getDimensionsWeight().size(); j++) {
                data = tradeCars.get(0).getLimitedEditionSpecsArray().getDimensionsWeight().get(j);
                mColumnHeaderList.add(data);

            }
        }
        if (tradeCars.get(0).getLimitedEditionSpecsArray().getSeatingCapacity() != null) {
            for (int j = 0; j < tradeCars.get(0).getLimitedEditionSpecsArray().getSeatingCapacity().size(); j++) {
                data = tradeCars.get(0).getLimitedEditionSpecsArray().getSeatingCapacity().get(j);
                mColumnHeaderList.add(data);
            }
        }
        if (tradeCars.get(0).getLimitedEditionSpecsArray().getDrivetrain() != null) {
            for (int j = 0; j < tradeCars.get(0).getLimitedEditionSpecsArray().getDrivetrain().size(); j++) {
                data = tradeCars.get(0).getLimitedEditionSpecsArray().getDrivetrain().get(j);
                mColumnHeaderList.add(data);

            }
        }
        if (tradeCars.get(0).getLimitedEditionSpecsArray().getEngine() != null) {
            for (int j = 0; j < tradeCars.get(0).getLimitedEditionSpecsArray().getEngine().size(); j++) {
                data = tradeCars.get(0).getLimitedEditionSpecsArray().getEngine().get(j);
                mColumnHeaderList.add(data);

            }
        }
        if (tradeCars.get(0).getLimitedEditionSpecsArray().getPerformance() != null) {
            for (int j = 0; j < tradeCars.get(0).getLimitedEditionSpecsArray().getPerformance().size(); j++) {
                data = tradeCars.get(0).getLimitedEditionSpecsArray().getPerformance().get(j);
                mColumnHeaderList.add(data);
            }
        }
        if (tradeCars.get(0).getLimitedEditionSpecsArray().getTransmission() != null) {
            for (int j = 0; j < tradeCars.get(0).getLimitedEditionSpecsArray().getTransmission().size(); j++) {
                data = tradeCars.get(0).getLimitedEditionSpecsArray().getTransmission().get(j);
                mColumnHeaderList.add(data);
            }
        }
        if (tradeCars.get(0).getLimitedEditionSpecsArray().getBrakes() != null) {
            for (int j = 0; j < tradeCars.get(0).getLimitedEditionSpecsArray().getBrakes().size(); j++) {
                data = tradeCars.get(0).getLimitedEditionSpecsArray().getBrakes().get(j);
                mColumnHeaderList.add(data);

            }
        }
        if (tradeCars.get(0).getLimitedEditionSpecsArray().getSuspension() != null) {
            for (int j = 0; j < tradeCars.get(0).getLimitedEditionSpecsArray().getSuspension().size(); j++) {
                data = tradeCars.get(0).getLimitedEditionSpecsArray().getSuspension().get(j);
                mColumnHeaderList.add(data);
            }
        }
        if (tradeCars.get(0).getLimitedEditionSpecsArray().getWheelsTyres() != null) {
            for (int j = 0; j < tradeCars.get(0).getLimitedEditionSpecsArray().getWheelsTyres().size(); j++) {
                data = tradeCars.get(0).getLimitedEditionSpecsArray().getWheelsTyres().get(j);
                mColumnHeaderList.add(data);
            }
        }
        if (tradeCars.get(0).getLimitedEditionSpecsArray().getFuel() != null) {
            for (int j = 0; j < tradeCars.get(0).getLimitedEditionSpecsArray().getFuel().size(); j++) {
                data = tradeCars.get(0).getLimitedEditionSpecsArray().getFuel().get(j);
                mColumnHeaderList.add(data);
            }
        }
        if (tradeCars.get(0).getLimitedEditionSpecsArray().getEmission() != null) {
            for (int j = 0; j < tradeCars.get(0).getLimitedEditionSpecsArray().getEmission().size(); j++) {
                data = tradeCars.get(0).getLimitedEditionSpecsArray().getEmission().get(j);
                mColumnHeaderList.add(data);

            }
        }
        if (tradeCars.get(0).getLimitedEditionSpecsArray().getWarrantyMaintenace() != null) {
            for (int j = 0; j < tradeCars.get(0).getLimitedEditionSpecsArray().getWarrantyMaintenace().size(); j++) {
                data = tradeCars.get(0).getLimitedEditionSpecsArray().getWarrantyMaintenace().get(j);
                mColumnHeaderList.add(data);
            }
        }

        LimitedEditionSpec remainingLifeCycleData = new LimitedEditionSpec();
        remainingLifeCycleData.setName(mainActivityContext.getResources().getString(R.string.remaining_lifecycle));
        remainingLifeCycleData.setValue("");
        mColumnHeaderList.add(remainingLifeCycleData);

        for (int i = 0; i < tradeCars.size(); i++) {
            ArrayList<LimitedEditionSpec> limitedEditionSpecs = new ArrayList<>();

            if (tradeCars.get(0).getLimitedEditionSpecsArray().getDimensionsWeight() != null) {
                limitedEditionSpecs.addAll(tradeCars.get(i).getLimitedEditionSpecsArray().getDimensionsWeight());
            }
            if (tradeCars.get(0).getLimitedEditionSpecsArray().getSeatingCapacity() != null) {
                limitedEditionSpecs.addAll(tradeCars.get(i).getLimitedEditionSpecsArray().getSeatingCapacity());
            }
            if (tradeCars.get(0).getLimitedEditionSpecsArray().getDrivetrain() != null) {
                limitedEditionSpecs.addAll(tradeCars.get(i).getLimitedEditionSpecsArray().getDrivetrain());
            }
            if (tradeCars.get(0).getLimitedEditionSpecsArray().getEngine() != null) {
                limitedEditionSpecs.addAll(tradeCars.get(i).getLimitedEditionSpecsArray().getEngine());
            }
            if (tradeCars.get(0).getLimitedEditionSpecsArray().getPerformance() != null) {
                limitedEditionSpecs.addAll(tradeCars.get(i).getLimitedEditionSpecsArray().getPerformance());
            }
            if (tradeCars.get(0).getLimitedEditionSpecsArray().getTransmission() != null) {
                limitedEditionSpecs.addAll(tradeCars.get(i).getLimitedEditionSpecsArray().getTransmission());
            }
            if (tradeCars.get(0).getLimitedEditionSpecsArray().getBrakes() != null) {
                limitedEditionSpecs.addAll(tradeCars.get(i).getLimitedEditionSpecsArray().getBrakes());
            }
            if (tradeCars.get(0).getLimitedEditionSpecsArray().getSuspension() != null) {
                limitedEditionSpecs.addAll(tradeCars.get(i).getLimitedEditionSpecsArray().getSuspension());
            }
            if (tradeCars.get(0).getLimitedEditionSpecsArray().getWheelsTyres() != null) {
                limitedEditionSpecs.addAll(tradeCars.get(i).getLimitedEditionSpecsArray().getWheelsTyres());
            }
            if (tradeCars.get(0).getLimitedEditionSpecsArray().getFuel() != null) {
                limitedEditionSpecs.addAll(tradeCars.get(i).getLimitedEditionSpecsArray().getFuel());
            }
            if (tradeCars.get(0).getLimitedEditionSpecsArray().getEmission() != null) {
                limitedEditionSpecs.addAll(tradeCars.get(i).getLimitedEditionSpecsArray().getEmission());
            }
            if (tradeCars.get(0).getLimitedEditionSpecsArray().getWarrantyMaintenace() != null) {
                limitedEditionSpecs.addAll(tradeCars.get(i).getLimitedEditionSpecsArray().getWarrantyMaintenace());
            }

            LimitedEditionSpec lifeCycle = new LimitedEditionSpec();
            if (tradeCars.get(i).getLife_cycle() != null) {
                Integer endYear = tradeCars.get(i).getLife_cycle().split("-")[1] != null ? Integer.valueOf(tradeCars.get(i).getLife_cycle().split("-")[1]) : null;
                Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);

                if (endYear != null) {
                    lifeCycle.setName("");
                    if (endYear - currentYear > 0) {
                        if (endYear - currentYear == 1) {
                            lifeCycle.setValue((endYear - currentYear) + " " + mainActivityContext.getResources().getString(R.string.year));
                        } else {
                            lifeCycle.setValue((endYear - currentYear) + " " + mainActivityContext.getResources().getString(R.string.years));
                        }
                    } else {
                        lifeCycle.setValue(mainActivityContext.getResources().getString(R.string.less_than_year));
                    }
                    lifeCycle.setIsHighlight(0);
                }
                ArrayList<LimitedEditionSpec> lifeCycleList = new ArrayList<>();
                lifeCycleList.add(lifeCycle);
                limitedEditionSpecs.addAll(lifeCycleList);
            } else {
                lifeCycle.setName("");
                lifeCycle.setValue("-");
                lifeCycle.setIsHighlight(0);
                ArrayList<LimitedEditionSpec> lifeCycleList = new ArrayList<>();
                lifeCycleList.add(lifeCycle);
                limitedEditionSpecs.addAll(lifeCycleList);
            }

            mCellList.add(limitedEditionSpecs);
        }
        adapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showBackButton(mainActivityContext, false);
        if (title != null) {
            titlebar.setTitle(title);
        } else {
            titlebar.setTitle(mainActivityContext.getResources().getString(R.string.results));
        }
        mTitleBar = titlebar;
        if (fromSearchPanel) {
            mainActivityContext.getBinding().btnFilterAction.setVisibility(View.GONE);
            mainActivityContext.getBinding().btnFilterAction.setText(mainActivityContext.getResources().getString(R.string.apply));
            mainActivityContext.getBinding().llCommentsLayout.setVisibility(View.GONE);
        }
    }
}
