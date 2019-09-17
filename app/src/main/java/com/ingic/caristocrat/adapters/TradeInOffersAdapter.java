package com.ingic.caristocrat.adapters;

import android.content.Context;
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
import com.ingic.caristocrat.dialogs.CallConsultantDialog;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.webhelpers.models.OfferDetail;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * on 10/13/2018.
 */
public class TradeInOffersAdapter extends RecyclerView.Adapter<TradeInOffersAdapter.MyViewHolder> {
    ArrayList<OfferDetail> offerDetails;
    MainActivity context;
    String currency;
    boolean expired;

    public TradeInOffersAdapter(Context context) {
        this.context = (MainActivity) context;
        this.offerDetails = new ArrayList<>();
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_tradeins_info, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tvBidNumber.setText(context.getResources().getString(R.string.bid_hash) + " " + (position + 1));

        if (offerDetails.get(position).getUser() != null) {
            if (offerDetails.get(position).getUser().getShowroom_details() != null) {
                if (offerDetails.get(position).getUser().getShowroom_details().getLogoUrl() != null) {
                    UIHelper.setImageWithGlide(context, holder.offerPic, offerDetails.get(position).getUser().getShowroom_details().getLogoUrl());
                }
                if (offerDetails.get(position).getUser().getShowroom_details().getName() != null) {
                    holder.name.setText(offerDetails.get(position).getUser().getShowroom_details().getName());
                    holder.name.setVisibility(View.VISIBLE);

                    holder.ibCallConsultant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (offerDetails.get(position).getUser().getShowroom_details().getPhone() != null) {
                                callConsultant(offerDetails.get(position).getUser().getShowroom_details().getName(), offerDetails.get(position).getUser().getShowroom_details().getPhone());
                            } else {
                                UIHelper.showToast(context, context.getResources().getString(R.string.phone_number_not_found), Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
            }
        }
//        offerDetails.get(position).setAmount(null); //////////
        if (offerDetails.get(position).getAmount() != null) {
            holder.price.setText((offerDetails.get(position).getCurrency() != null ? offerDetails.get(position).getCurrency() : context.getResources().getString(R.string.AED)) + " " + NumberFormat.getNumberInstance(Locale.US).format(offerDetails.get(position).getAmount()));
            holder.llOfferedPrice.setVisibility(View.VISIBLE);
        } else {
            holder.price.setVisibility(View.GONE);
            holder.llOfferedPrice.setVisibility(View.VISIBLE);
            if (expired) {
                holder.tvOfferedPricePrefixed.setText(context.getResources().getString(R.string.no_offer_available));
            } else {
                holder.tvOfferedPricePrefixed.setText(context.getResources().getString(R.string.waiting_for_offer));
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.offerDetails.size();
    }

    private void callConsultant(String name, final String number) {
        CallConsultantDialog callConsultantDialog = CallConsultantDialog.newInstance(context, null);
        callConsultantDialog.setConsultantNameAndPhone(context.getResources().getString(R.string.would_you_like) + " " + name, number);
        callConsultantDialog.show(context.getFragmentManager(), null);
    }

    public void addAll(ArrayList<OfferDetail> data) {
        this.offerDetails.clear();
        this.offerDetails.addAll(data);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llOfferedPrice;
        ImageView offerPic;
        TextView name, tvBidNumber, tvOfferedPricePrefixed, price;
        ImageButton ibCallConsultant;
        View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            offerPic = (ImageView) itemView.findViewById(R.id.rivProfilePic);
            name = (TextView) itemView.findViewById(R.id.tvName);
            tvBidNumber = (TextView) itemView.findViewById(R.id.tvBidNumber);
            tvOfferedPricePrefixed = (TextView) itemView.findViewById(R.id.tvOfferedPricePrefixed);
            price = (TextView) itemView.findViewById(R.id.tvOfferedPrice);
            ibCallConsultant = (ImageButton) itemView.findViewById(R.id.ibCallConsultant);
            view = (View) itemView.findViewById(R.id.view);
            llOfferedPrice = itemView.findViewById(R.id.llOfferedPrice);
        }
    }
}

