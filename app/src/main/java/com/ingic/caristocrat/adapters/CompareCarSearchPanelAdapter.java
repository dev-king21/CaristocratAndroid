package com.ingic.caristocrat.adapters;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.fragments.CompareCarSearchPanelFragment;
import com.ingic.caristocrat.helpers.DialogFactory;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;
import com.ingic.caristocrat.models.CompareCarPanel;
import com.ingic.caristocrat.models.Model;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompareCarSearchPanelAdapter extends RecyclerView.Adapter<CompareCarSearchPanelAdapter.Holder> {
    private MainActivity mainActivityContext;
    private CompareCarSearchPanelFragment compareCarSearchPanelFragment;
    private ArrayList<CompareCarPanel> arrayList;
    private static final int LAYOUT_ADD_NEW_CAR = R.layout.layout_compare_car_add_new;
    private static final int LAYOUT_COMPARE_CAR_SEARCH = R.layout.layout_compare_car_search_panel;
    private CompareCarPanel newCompareCarPanel;
    private LuxuryMarketSearchFilter filter;

    public CompareCarSearchPanelAdapter(MainActivity mainActivityContext, CompareCarSearchPanelFragment compareCarSearchPanelFragment, LuxuryMarketSearchFilter filter) {
        this.mainActivityContext = mainActivityContext;
        this.compareCarSearchPanelFragment = compareCarSearchPanelFragment;
        this.arrayList = new ArrayList<>();
        this.newCompareCarPanel = new CompareCarPanel();
        this.newCompareCarPanel.setMoreCar(true);
        this.filter = filter;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mainActivityContext).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        switch (holder.viewType) {
            case LAYOUT_COMPARE_CAR_SEARCH:
                holder.ivCarImage.setVisibility(View.INVISIBLE);
                holder.ivNoCar.setVisibility(View.VISIBLE);
                holder.tvSelectBrand.setText("");
                holder.tvSelectModel.setText("");
                holder.tvSelectYear.setText("");
                holder.tvSelectVersion.setText("");

                if (arrayList.get(position).getTradeCar() != null) {
                    if (arrayList.get(position).getTradeCar().getMedia() != null && arrayList.get(position).getTradeCar().getMedia().size() > 0) {
                        UIHelper.setImageWithGlide(mainActivityContext, holder.ivCarImage, arrayList.get(position).getTradeCar().getMedia().get(0).getFileUrl());
                        holder.ivCarImage.setVisibility(View.VISIBLE);
                        holder.ivNoCar.setVisibility(View.GONE);
                    }
                }

                holder.ibRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeCar(position);
                    }
                });

                if (arrayList.get(position).getSelectedBrand() != null) {
                    holder.tvSelectBrand.setText(arrayList.get(position).getSelectedBrand().getName());
                }

                if (arrayList.get(position).getSelectedModel() != null) {
                    holder.tvSelectModel.setText(arrayList.get(position).getSelectedModel().getName());
                }

                if (arrayList.get(position).getSelectedYear() > 0) {
                    holder.tvSelectYear.setText(arrayList.get(position).getSelectedYear() + "");
                }

                if (arrayList.get(position).getVersion() != null) {
                    holder.tvSelectVersion.setText(arrayList.get(position).getVersion().getName());
                }

                holder.tvSelectBrand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openBrandPicker(position);
                    }
                });

                holder.tvSelectModel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (arrayList.get(position).getSelectedBrand() == null) {
                            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.select_brand), Toast.LENGTH_SHORT);
                            return;
                        }

                        openModelPicker(arrayList.get(position).getSelectedBrand().getId(), position);
                    }
                });

                holder.tvSelectYear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (arrayList.get(position).getSelectedBrand() == null) {
                            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.select_brand), Toast.LENGTH_SHORT);
                            return;
                        }

                        if (arrayList.get(position).getSelectedModel() == null) {
                            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.select_model), Toast.LENGTH_SHORT);
                            return;
                        }

                        openYearPicker(arrayList.get(position).getSelectedModel().getId(), position);
                    }
                });

                holder.tvSelectVersion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (arrayList.get(position).getSelectedBrand() == null) {
                            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.select_brand), Toast.LENGTH_SHORT);
                            return;
                        }

                        if (arrayList.get(position).getSelectedModel() == null) {
                            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.select_model), Toast.LENGTH_SHORT);
                            return;
                        }

                        if (arrayList.get(position).getSelectedYear() == 0) {
                            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.select_year), Toast.LENGTH_SHORT);
                            return;
                        }

                        openVersionPicker(arrayList.get(position).getSelectedModel().getId(), arrayList.get(position).getSelectedYear(), position);
                    }
                });
                break;

            case LAYOUT_ADD_NEW_CAR:
                holder.llAddNewCar.setTag(position);
                holder.llAddNewCar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CompareCarPanel compareCarPanel = new CompareCarPanel();
                        compareCarPanel.setMoreCar(false);
                        set(position, compareCarPanel);
                        add(newCompareCarPanel);
                        notifyDataSetChanged();
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position).isMoreCar()) {
            return LAYOUT_ADD_NEW_CAR;
        } else {
            return LAYOUT_COMPARE_CAR_SEARCH;
        }
    }

    public void addAll(ArrayList<CompareCarPanel> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    public ArrayList<TradeCar> getSelectedCars() {
        ArrayList<TradeCar> selectedCars = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getTradeCar() != null) {
                selectedCars.add(arrayList.get(i).getTradeCar());
            }
        }
        return selectedCars;
    }

    public void add(CompareCarPanel compareCarPanel) {
        this.arrayList.add(compareCarPanel);
    }

    public void set(int position, CompareCarPanel compareCarPanel) {
        this.arrayList.set(position, compareCarPanel);
    }

    public void remove(int position) {
        this.arrayList.remove(position);
    }

    private void openBrandPicker(int position) {
        if (compareCarSearchPanelFragment.getBrands() != null && compareCarSearchPanelFragment.getBrands().size() > 0) {
            final ArrayList<String> brandNames = new ArrayList<>();
            for (int i = 0; i < compareCarSearchPanelFragment.getBrands().size(); i++) {
                brandNames.add(compareCarSearchPanelFragment.getBrands().get(i).getName());
            }
            DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    arrayList.get(position).setSelectedBrand(compareCarSearchPanelFragment.getBrands().get(i));
                    arrayList.get(position).setSelectedModel(null);
                    arrayList.get(position).setTradeCar(null);
                    arrayList.get(position).setSelectedYear(0);
                    arrayList.get(position).setVersion(null);
                    notifyItemChanged(position);
                }
            }, mainActivityContext.getResources().getString(R.string.brands), brandNames);
        } else {
            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.no_brand_found), Toast.LENGTH_SHORT);
        }
    }

    private void openModelPicker(int brandId, int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("brand_id", brandId);
        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.MODEL, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
                @Override
                public void onSuccess(ApiArrayResponse apiArrayResponse) {
                    ArrayList<Model> models = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Model.class);
                    if (models != null && models.size() > 0) {
                        final ArrayList<String> modelsNames = new ArrayList<>();
                        for (int i = 0; i < models.size(); i++) {
                            modelsNames.add(models.get(i).getName());
                        }
                        DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                arrayList.get(position).setSelectedModel(models.get(i));
                                arrayList.get(position).setTradeCar(null);
                                arrayList.get(position).setSelectedYear(0);
                                arrayList.get(position).setVersion(null);
                                notifyItemChanged(position);
                            }
                        }, mainActivityContext.getResources().getString(R.string.models), modelsNames);
                    } else {
                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.no_model_found), Toast.LENGTH_SHORT);
                    }
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            });
        }
    }

    private void openYearPicker(int modelId, int position) {
        if (mainActivityContext.showLoader()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("category_id", AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORIES);
            params.put("sort_by_year", 1);
            params.put("model_ids", modelId);

            if (filter.isFilterApply()) {
                //Set Selected Brands in Filter
                if (filter.getBrandsList().size() > 0) {
                    String brandsIds = "", modelsIds = "";
                    for (int pos = 0; pos < filter.getBrandsList().size(); pos++) {
                        if (pos == filter.getBrandsList().size() - 1) {
                            brandsIds += filter.getBrandsList().get(pos).getId();
                        } else {
                            brandsIds += filter.getBrandsList().get(pos).getId() + ",";
                        }

                        if (filter.getBrandsList().get(pos).getModels() != null) {
                            if (filter.getBrandsList().get(pos).getModels().size() > 0) {
                                for (int modelPos = 0; modelPos < filter.getBrandsList().get(pos).getModels().size(); modelPos++) {
                                    modelsIds += filter.getBrandsList().get(pos).getModels().get(modelPos).getId() + ",";
                                }
                            }
                        }
                    }
                    if (modelsIds.length() > 0) {
                        if (modelsIds.charAt(modelsIds.length() - 1) == ',') {
                            modelsIds = modelsIds.substring(0, modelsIds.length() - 1);
                        }
                    }
//                params.put("brand_ids", brandsIds);
                    params.put("model_ids", modelsIds);
                }

                //Set Version name in filter
                if (filter.getVersionName() != null) {
                    params.put("version", filter.getVersionName());
                }

                //Set Min Price in Filter
                if (filter.getMinPrice() != null) {
                    params.put("min_price", filter.getMinPrice());
                }

                //Set Max Price in Filter
                if (filter.getMaxPrice() != null) {
                    params.put("max_price", filter.getMaxPrice());
                }

                //Set Body Styles in Filter
                if (filter.getCarBodyStyles().size() > 0) {
                    boolean isSelected = false;
                    String carBodyTypeIds = "";
                    for (int pos = 0; pos < filter.getCarBodyStyles().size(); pos++) {
                        if (filter.getCarBodyStyles().get(pos).isSelected()) {
                            isSelected = true;
                            if (pos == filter.getCarBodyStyles().size() - 1) {
                                carBodyTypeIds += filter.getCarBodyStyles().get(pos).getId();
                            } else {
                                carBodyTypeIds += filter.getCarBodyStyles().get(pos).getId() + ",";
                            }
                        }
                    }
                    if (isSelected) {
                        params.put("car_type", carBodyTypeIds);
                    }
                }

                if (filter.getRating() >= 0) {
                    params.put("rating", filter.getRating());
                }
            }
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORIES, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
                @Override
                public void onSuccess(ApiArrayResponse apiArrayResponse) {
                    ArrayList<TradeCar> tradeCars = (ArrayList<TradeCar>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class);
                    if (tradeCars != null && tradeCars.size() > 0) {
                        final ArrayList<String> yearNames = new ArrayList<>();
                        for (int i = 0; i < tradeCars.size(); i++) {
                            yearNames.add(tradeCars.get(i).getYear() + "");
                        }
                        DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                arrayList.get(position).setSelectedYear(tradeCars.get(i).getYear());
                                arrayList.get(position).setVersion(null);
                                arrayList.get(position).setTradeCar(null);
                                notifyItemChanged(position);
                            }
                        }, mainActivityContext.getResources().getString(R.string.years), yearNames);
                    } else {
                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.no_car_found), Toast.LENGTH_SHORT);
                    }
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            });
        }
    }

    private void openVersionPicker(int modelId, int year, int position) {
        if (mainActivityContext.showLoader()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("category_id", AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORIES);
            params.put("sort_by_version", 1);
            params.put("model_ids", modelId);
            params.put("min_year", year);
            params.put("max_year", year);

            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORIES, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
                @Override
                public void onSuccess(ApiArrayResponse apiArrayResponse) {
                    ArrayList<TradeCar> tradeCars = (ArrayList<TradeCar>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class);
                    if (tradeCars != null && tradeCars.size() > 0) {
                        final ArrayList<String> versionNames = new ArrayList<>();
                        for (int i = 0; i < tradeCars.size(); i++) {
                            if (tradeCars.get(i).getVersion() != null) {
                                versionNames.add(tradeCars.get(i).getVersion().getName() + "");
                            }
                        }
                        DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                arrayList.get(position).setVersion(tradeCars.get(i).getVersion());
                                arrayList.get(position).setTradeCar(tradeCars.get(i));
                                notifyItemChanged(position);
                            }
                        }, mainActivityContext.getResources().getString(R.string.versions), versionNames);
                    } else {
                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.no_car_found), Toast.LENGTH_SHORT);
                    }
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            });
        }
    }

    private void removeCar(int position) {
        UIHelper.showSimpleDialog(
                mainActivityContext,
                0,
                mainActivityContext.getResources().getString(R.string.remove_car),
                mainActivityContext.getResources().getString(R.string.remove_car_confirmation),
                mainActivityContext.getResources().getString(R.string.delete),
                mainActivityContext.getResources().getString(R.string.cancel),
                false,
                false,
                new SimpleDialogActionListener() {
                    @Override
                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                        if (positive) {
                            remove(position);
                            notifyDataSetChanged();
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }
                    }
                }
        );
    }

    static class Holder extends RecyclerView.ViewHolder {
        LinearLayout llAddNewCar;
        ImageButton ibRemove;
        ImageView ivCarImage, ivNoCar;
        TextView tvSelectBrand, tvSelectModel, tvSelectYear, tvSelectVersion;
        View view;
        int viewType;

        public Holder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            this.view = itemView;
            if (viewType == LAYOUT_COMPARE_CAR_SEARCH) {
                ibRemove = itemView.findViewById(R.id.ibRemove);
                ivCarImage = itemView.findViewById(R.id.ivCarImage);
                ivNoCar = itemView.findViewById(R.id.ivNoCar);
                tvSelectBrand = itemView.findViewById(R.id.tvSelectBrand);
                tvSelectModel = itemView.findViewById(R.id.tvSelectModel);
                tvSelectYear = itemView.findViewById(R.id.tvSelectYear);
                tvSelectVersion = itemView.findViewById(R.id.tvSelectVersion);
            } else {
                llAddNewCar = itemView.findViewById(R.id.llAddNewCar);
            }
        }
    }
}
