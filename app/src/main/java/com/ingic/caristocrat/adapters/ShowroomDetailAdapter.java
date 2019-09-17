package com.ingic.caristocrat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.helpers.RoundImageView;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.models.ShowroomDetails;
import com.ingic.caristocrat.models.TradeCar;

import java.util.ArrayList;

public class ShowroomDetailAdapter extends RecyclerView.Adapter<ShowroomDetailAdapter.Holder> {
    private MainActivity activityContext;
    private ArrayList<ShowroomDetails> arrayList;
    private TradeCar tradeCar;

    public ShowroomDetailAdapter(MainActivity activityContext, TradeCar tradeCar) {
        this.activityContext = activityContext;
        this.tradeCar = tradeCar;
        this.arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activityContext).inflate(R.layout.layout_showroom_detail, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        UIHelper.setImageWithGlide(activityContext, holder.rivProfilePic, arrayList.get(position).getLogoUrl());
        holder.tvConsultantName.setText(arrayList.get(position).getName());
        holder.tvAddress.setText(arrayList.get(position).getAddress());
        holder.ibCallConsultant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityContext.disableViewsForSomeSeconds(holder.ibCallConsultant);
                activityContext.IntrectionCall(tradeCar.getId(), AppConstants.Interaction.PHONE);
                activityContext.callConsultant(arrayList.get(position).getName(), arrayList.get(position).getPhone());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }

    public void addAll(ArrayList<ShowroomDetails> arrayList){
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        RoundImageView rivProfilePic;
        TextView tvConsultantName, tvAddress;
        ImageButton ibCallConsultant;

        public Holder(View view) {
            super(view);
            rivProfilePic = view.findViewById(R.id.rivProfilePic);
            tvConsultantName = view.findViewById(R.id.tvConsultantName);
            tvAddress = view.findViewById(R.id.tvAddress);
            ibCallConsultant = view.findViewById(R.id.ibCallConsultant);
        }
    }
}
