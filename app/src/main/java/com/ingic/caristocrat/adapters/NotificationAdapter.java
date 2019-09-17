package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.helpers.DateFormatHelper;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.models.NotificationWrapper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * on 10/4/2018.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    ArrayList<NotificationWrapper> notifications;
    Context context;

    public NotificationAdapter(Context context, ArrayList<NotificationWrapper> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.notifiaction.setText((!UIHelper.isEmptyOrNull(notifications.get(position).getMessage()) ? notifications.get(position).getMessage() : "") + " " + (!UIHelper.isEmptyOrNull(notifications.get(position).getCarName()) ? notifications.get(position).getCarName() : ""));
        holder.tvCarTitle.setText(context.getResources().getString(R.string.car_title_colon) + " " + notifications.get(position).getCarName());
        holder.date.setText(!UIHelper.isEmptyOrNull(notifications.get(position).getCreatedAt()) ? DateFormatHelper.changeServerToNotificationsDateFormat(notifications.get(position).getCreatedAt()).trim() : "");
        holder.model.setText(context.getResources().getString(R.string.model_colon) + " " + (!UIHelper.isEmptyOrNull(notifications.get(position).getModelYear()) ? notifications.get(position).getModelYear().substring(0, 4) : "-"));
        holder.chasis.setText(context.getResources().getString(R.string.chassis_colon) + " " + (!UIHelper.isEmptyOrNull(notifications.get(position).getChassis()) ? notifications.get(position).getChassis() : "-"));

        if (!UIHelper.isEmptyOrNull(notifications.get(position).getImageUrl()))
            UIHelper.setImageWithGlide(context, holder.image, notifications.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void addAll(ArrayList<NotificationWrapper> data) {
        notifications.clear();
        notifications.addAll(data);
    }

    public void remove(int position) {
        notifications.remove(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView notifiaction, model, chasis, date, tvCarTitle;
        ImageView image;

        MyViewHolder(View itemView) {
            super(itemView);
            notifiaction = (TextView) itemView.findViewById(R.id.tvTradeIn);
            model = (TextView) itemView.findViewById(R.id.tvModel);
            chasis = (TextView) itemView.findViewById(R.id.tvChasis);
            image = (ImageView) itemView.findViewById(R.id.ivTradeIn);
            date = (TextView) itemView.findViewById(R.id.tvTradeDate);
            tvCarTitle = (TextView) itemView.findViewById(R.id.tvCarTitle);
        }
    }
}
