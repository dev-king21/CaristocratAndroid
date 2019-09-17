package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.models.ProfileOptions;

import java.util.ArrayList;

/**
 * on 10/4/2018.
 */
public class ProfileOptionsAdatpter extends RecyclerView.Adapter<ProfileOptionsAdatpter.MyViewHolder> {
    ArrayList<ProfileOptions> profileOptions;
    Context context;

    public ProfileOptionsAdatpter(Context context, ArrayList<ProfileOptions> profileOptions) {
        this.context = context;
        this.profileOptions = profileOptions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile_options, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.btnProfileOptions.setText(profileOptions.get(position).getName());
        if(position%2==0){
            holder.btnProfileOptions.setBackground(context.getResources().getDrawable(R.drawable.rounded_button_profile_complete_now));
            holder.btnProfileOptions.setTextColor(ContextCompat.getColor(context,R.color.colorBlack));
        }
        else {
            holder.btnProfileOptions.setBackground(context.getResources().getDrawable(R.drawable.rounded_button_join_the_club));
            holder.btnProfileOptions.setTextColor(ContextCompat.getColor(context,R.color.colorWhite));
        }

    }

    @Override
    public int getItemCount() {
        return profileOptions.size();
    }

    public void set(int position, ProfileOptions profileOptions){
        this.profileOptions.set(position, profileOptions);
    }

    public void addAll(ArrayList<ProfileOptions> data) {
        profileOptions.clear();
        profileOptions.addAll(data);

    }



    class MyViewHolder extends RecyclerView.ViewHolder {
      Button btnProfileOptions;

        public MyViewHolder(View itemView) {
            super(itemView);
            btnProfileOptions = (Button) itemView.findViewById(R.id.btnOptions);

        }
    }
}
