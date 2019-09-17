package com.ingic.caristocrat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.models.BankInsuranceInformation;

import java.util.ArrayList;

/**
 *  Addi.
 */
public class BankAdapter extends RecyclerView.Adapter<BankAdapter.VH> {
    private final MainActivity activity;
    BannkInterface bankInterface;
    ArrayList<BankInsuranceInformation> arrayList;

    public BankAdapter(MainActivity activity, BannkInterface bankInterface) {
        this.activity = activity;
        this.bankInterface = bankInterface;
        this.arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_bank, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (arrayList.get(position).getMedia().size() > 0) {
            UIHelper.setImageWithGlide(activity, holder.ivLogo, arrayList.get(position).getMedia().get(0).getFileUrl());
        }

        holder.tvPercent.setText(arrayList.get(position).getRate() + " %");

        holder.btnRequestCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.disableViewsForSomeSeconds(holder.btnRequestCall);
                bankInterface.onBankItemRequestCallClick(arrayList.get(position));
            }
        });

        holder.btnCallNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.disableViewsForSomeSeconds(holder.btnCallNow);
                bankInterface.onBankItemCallNowClick(arrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addAll(ArrayList<BankInsuranceInformation> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    class VH extends RecyclerView.ViewHolder {
        ImageView ivLogo;
        TextView tvPercent;
        Button btnRequestCall, btnCallNow;

        public VH(View itemView) {
            super(itemView);

            ivLogo = itemView.findViewById(R.id.ivLogo);
            tvPercent = itemView.findViewById(R.id.tvPercent);
            btnRequestCall = itemView.findViewById(R.id.btnRequestCall);
            btnCallNow = itemView.findViewById(R.id.btnCallNow);
        }
    }

    public interface BannkInterface {
        void onBankItemRequestCallClick(BankInsuranceInformation bankInsuranceInformation);

        void onBankItemCallNowClick(BankInsuranceInformation bankInsuranceInformation);
    }
}
