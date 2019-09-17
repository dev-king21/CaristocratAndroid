package com.ingic.caristocrat.dialogs;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.adapters.SelectRegionAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.LayoutSelectRegionDialogBinding;
import com.ingic.caristocrat.helpers.BasePreferenceHelper;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.DialogCloseListener;
import com.ingic.caristocrat.interfaces.JustReload;
import com.ingic.caristocrat.interfaces.OnRegionsSelectedListener;
import com.ingic.caristocrat.models.Region;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.PostRegion;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;

/**
 */
public class SelectCountryDialog extends DialogFragment implements View.OnClickListener {
    MainActivity context;
    LayoutSelectRegionDialogBinding binding;
    SelectRegionAdapter selectRegionAdapter;
    ArrayList<Region> regions = new ArrayList<>();
    ArrayList<Integer> selectedRegions = new ArrayList<>();
    ArrayList<Region> selectedRegionsObjects = new ArrayList<>();
    private DialogCloseListener dialogListener;
    LuxuryMarketSearchFilter filter;
    boolean fromFilter;
    OnRegionsSelectedListener onRegionsSelectedListener;
    JustReload loadData;

    public void setDialogListener(DialogCloseListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public void setFromFilter(boolean fromFilter) {
        this.fromFilter = fromFilter;
    }

    public static SelectCountryDialog newInstance(MainActivity context) {
        SelectCountryDialog fragment = new SelectCountryDialog();
        fragment.context = context;
        return fragment;
    }

    public static SelectCountryDialog newInstance(MainActivity context, JustReload loadData) {
        SelectCountryDialog fragment = new SelectCountryDialog();
        fragment.context = context;
        fragment.loadData = loadData;
        return fragment;
    }

    public void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }

    public ArrayList<Integer> getSelectedRegions() {
        return selectedRegions;
    }

    public ArrayList<Region> getSelectedRegionsObjects() {
        return selectedRegionsObjects;
    }

    public void setSelectedRegionsObjects(ArrayList<Region> selectedRegionsObjects) {
        this.selectedRegionsObjects = selectedRegionsObjects;
    }

    public void setOnRegionsSelectedListener(OnRegionsSelectedListener onRegionsSelectedListener) {
        this.onRegionsSelectedListener = onRegionsSelectedListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filter = LuxuryMarketSearchFilter.getInstance();
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_select_region_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (fromFilter) {
            binding.cbSelectCountry.setVisibility(View.GONE);
            binding.btnClear.setVisibility(View.VISIBLE);
        }
        if (context.showLoader()) {
            initAdapter();
        }
        setListeners();


    }

    private void setListeners() {
        binding.cbSelectCountry.setOnClickListener(this);
        binding.btnOk.setOnClickListener(this);
        binding.btnClear.setOnClickListener(this);
//        binding.btnCancel.setOnClickListener(this);
    }

    private void initAdapter() {
        if (fromFilter) {
            for (int i = 0; i < LuxuryMarketSearchFilter.getInstance().getSelectedRegions().size(); i++) {
                for (int j = 0; j < regions.size(); j++) {
                    if (LuxuryMarketSearchFilter.getInstance().getSelectedRegions().get(i).getId() == regions.get(j).getId()) {
                        regions.get(j).setIs_my_region(1);
                    }
                }
            }
        } else {
            for (int j = 0; j < regions.size(); j++) {
                regions.get(j).setIs_my_region(0);
            }
        }
        selectRegionAdapter = new SelectRegionAdapter(context, binding, regions, SelectCountryDialog.this);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
//                if(position == 0 || position==1)
//                   return 1;
//                else if(position == 2 || position==3)
//                    return 1;
//                else if (position ==4 )
//                    return 2;
//                else
//                    return 2;
                return (position + 1) % regions.size()  == 0 ? 2 : 1;
            }
        });

        binding.rvSelectCountry.setLayoutManager(layoutManager);
        binding.rvSelectCountry.setNestedScrollingEnabled(false);

        int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.dp24);
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(spacingInPixels);
        binding.rvSelectCountry.addItemDecoration(spacesItemDecoration);
        binding.rvSelectCountry.setAdapter(selectRegionAdapter);
//        ArrayList<Country> countryArrayList = new ArrayList<>();
//        countryArrayList.add(new Country(context.getResources().getString(R.string.usa), R.drawable.usa2, R.drawable.usa));
//        countryArrayList.add(new Country(context.getResources().getString(R.string.europe), R.drawable.europe, R.drawable.europe3));
//        countryArrayList.add(new Country(context.getResources().getString(R.string.dubai), R.drawable.dubai, R.drawable.dubai2));
//        countryArrayList.add(new Country(context.getResources().getString(R.string.ksa), R.drawable.ksa, R.drawable.ksa3));
//        countryArrayList.add(new Country(context.getResources().getString(R.string.egypt), R.drawable.egypt, R.drawable.egypt3));
//        selectRegionAdapter.addAll(countryArrayList);
        context.hideLoader();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (null != dialogListener) {
            dialogListener.onDismiss();
        }
        if (context != null)
            context.setTheme(R.style.AppTheme);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cbSelectCountry:
                break;
            case R.id.btnOk:
                if (selectedRegions.size() > 0 && selectedRegionsObjects.size() > 0) {
                    if (fromFilter) {
                        filter.setSelectedRegions(selectedRegionsObjects);
                        if (onRegionsSelectedListener != null) {
                            onRegionsSelectedListener.onRegionsSelected(selectedRegionsObjects);
                        }
                        dismiss();
                    } else {
//                        postRegions();
//                        if (onRegionsSelectedListener != null) {
//                            onRegionsSelectedListener.onRegionsSelected(selectedRegionsObjects);
//                        }
                        if (binding.cbSelectCountry.isChecked()) {
                            context.getPreferenceHelper().setBooleanPrefrence(BasePreferenceHelper.KEY_REGION_SELECTED, true);
                        }
                        filter.setSelectedRegions(selectedRegionsObjects);
                        filter.setFilterApply(true);
                        if(loadData != null){
                            loadData.load();
                        }
                        dismiss();
                    }
                } else {
                    UIHelper.showToast(context, context.getResources().getString(R.string.err_select_region), Toast.LENGTH_SHORT);
                }
                break;

            case R.id.btnClear:
                clear();
                break;
//            case R.id.btnCancel:
//                dismiss();
//                break;
        }
    }

    private void clear() {
        if (binding.btnClear.getText().equals(context.getResources().getString(R.string.clear))) {
            selectedRegions = new ArrayList<>();
            selectedRegionsObjects = new ArrayList<>();
            filter.getSelectedRegions().clear();
            selectRegionAdapter.clearAllSelections();
            binding.btnClear.setText(context.getResources().getString(R.string.close));
        } else if (binding.btnClear.getText().equals(context.getResources().getString(R.string.close))) {
            if (selectedRegionsObjects.size() == 0) {
                onRegionsSelectedListener.onRegionsSelected(new ArrayList<>());
            }
            dismiss();
        }
    }

    private void postRegions() {
        PostRegion postRegion = new PostRegion();
        postRegion.setRegion_id(selectedRegions);
        /*
        if (binding.cbSelectCountry.isChecked()){
            postRegion.setRegion_reminder(1);
        }
        */
        WebApiRequest.Instance(context).request(AppConstants.WebServicesKeys.POST_REGIONS, binding.getRoot(), postRegion, null, new WebApiRequest.WebServiceObjectResponse() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                if (binding.cbSelectCountry.isChecked())
                    context.getPreferenceHelper().setBooleanPrefrence(BasePreferenceHelper.KEY_REGION_SELECTED, true);
                dismiss();
            }

            @Override
            public void onError() {

            }
        }, null);
    }

    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.right = space;
            outRect.left = space;
        }
    }
}
