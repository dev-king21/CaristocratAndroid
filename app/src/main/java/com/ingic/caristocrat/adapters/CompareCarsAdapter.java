package com.ingic.caristocrat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.DataNotFoundListener;
import com.ingic.caristocrat.models.TradeCar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 */
public class CompareCarsAdapter extends RecyclerView.Adapter<CompareCarsAdapter.VH> {
    private final MainActivity activity;
    ArrayList<TradeCar> arrayList;
    DataNotFoundListener mlistener;
    SelectedCarsAdapter selectedCarsAdapter;

    public CompareCarsAdapter(MainActivity activity, ArrayList<TradeCar> arrayList, SelectedCarsAdapter selectedCarsAdapter) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.selectedCarsAdapter = selectedCarsAdapter;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_compare_cars, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        UIHelper.setImageWithGlide(activity, holder.ivCar, arrayList.get(position).getMedia().get(0).getFileUrl());
        holder.tvCarName.setText(!UIHelper.isEmptyOrNull(arrayList.get(position).getName()) ? arrayList.get(position).getName() : "");
        holder.tvCarPrice.setText((arrayList.get(position).getCurrency() == null ? activity.getCurrency() : arrayList.get(position).getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.UK).format(arrayList.get(position).getAmount()));
        if (arrayList.get(position).isSelected()) {
            holder.btnSeLectDeselect.setImageResource(R.drawable.minus);
        } else {
            holder.btnSeLectDeselect.setImageResource(R.drawable.add_with_round);
        }
        holder.btnSeLectDeselect.setOnClickListener((view) -> {
            if (arrayList.get(position).isSelected()) {
                holder.btnSeLectDeselect.setImageResource(R.drawable.add_with_round);
                selectedCarsAdapter.remove(arrayList.get(position));
                arrayList.get(position).setSelected(false);
                arrayList.remove(position);
                notifyDataSetChanged();
            } else {
                holder.btnSeLectDeselect.setImageResource(R.drawable.minus);
                selectedCarsAdapter.add(arrayList.get(position));
                arrayList.get(position).setSelected(true);
            }
        });
    }

    private void addCar(TradeCar entity) {
        boolean isExist = false;
        for (int i = 0; i < this.arrayList.size(); i++) {
            if (arrayList.get(i).getId() == entity.getId()) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            this.arrayList.add(entity);
            notifyDataSetChanged();
        }

    }

    public void clearAll() {
        if (arrayList != null){
            this.arrayList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
//        if (arrayList.size() == 0) {
//            mlistener.isDataFound(false);
//        }
//        else {
//            mlistener.isDataFound(true);
//        }
        return arrayList.size();
    }

    public ArrayList<TradeCar> getArrayList() {
        return arrayList;
    }

    public void addAll(ArrayList<TradeCar> data) {
        this.arrayList.clear();
        for (int j = 0; j < data.size(); j++) {
            boolean isExist = false;
            for (int i = 0; i < selectedCarsAdapter.getArrayList().size(); i++) {
                if (data.get(j).getId() == selectedCarsAdapter.getArrayList().get(i).getId()) {
                    isExist = true;
                    this.arrayList.add( selectedCarsAdapter.getArrayList().get(i));
                    break;

                }
            }
            if (!isExist) {
                this.arrayList.add(data.get(j));

            }

        }
        notifyDataSetChanged();

//        for (int i = 0; i <data.size() ; i++) {
//            boolean isExist = false;
//            for (int j = 0; j <this.arrayList.size() ; j++) {
//                if(this.arrayList.get(j).getId()==data.get(i).getId()) {
//                  isExist=true;
//                  break;
//                }
//            }
//            if(!isExist){
//                this.arrayList.add(data.get(i));
//                notifyDataSetChanged();
//            }


    }

    public void add(TradeCar tradeCar) {
        this.arrayList.add(tradeCar);
        notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder {
        ImageView ivCar;
        TextView tvCarName;
        TextView tvCarPrice;
        ImageView btnSeLectDeselect;

        public VH(View itemView) {
            super(itemView);

            ivCar = itemView.findViewById(R.id.ivCar);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvCarPrice = itemView.findViewById(R.id.tvCarPrice);
            btnSeLectDeselect = itemView.findViewById(R.id.btnSelectDeselect);

        }

    }

    public void setMlistener(DataNotFoundListener mlistener) {
        this.mlistener = mlistener;
    }
}

