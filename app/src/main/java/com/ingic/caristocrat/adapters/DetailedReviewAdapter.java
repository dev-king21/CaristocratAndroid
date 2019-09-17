package com.ingic.caristocrat.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.databinding.LayoutDetailedReviewBinding;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.models.UserRating;

import java.util.ArrayList;

public class DetailedReviewAdapter extends RecyclerView.Adapter<DetailedReviewAdapter.Holder> {
    private LayoutDetailedReviewBinding binding;
    private MainActivity mainActivityContext;
    private ArrayList<UserRating> arrayList;
    boolean fromLuxuryDetails;

    public DetailedReviewAdapter(MainActivity mainActivityContext) {
        this.mainActivityContext = mainActivityContext;
        this.arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mainActivityContext), R.layout.layout_detailed_review, parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (arrayList.get(position).getUser() != null) {
            UIHelper.setUserImageWithGlide(mainActivityContext, binding.ivProfile, arrayList.get(position).getUser().getImage_url() == null ? "" : arrayList.get(position).getUser().getImage_url());
            binding.tvRaterName.setText((arrayList.get(position).getUser().getUser_name() == null) ? "" : arrayList.get(position).getUser().getUser_name());
        }

        binding.ratingbar.setRating(arrayList.get(position).getOverAllRating());
        binding.tvOverAllRating.setText("(" + arrayList.get(position).getOverAllRating() + ")");

        int spacingInPixels = mainActivityContext.getResources().getDimensionPixelSize(R.dimen.dp4);
        UIHelper.SpacesItemDecorationAllSideEqual spacesItemDecorationAllSideEqual = new UIHelper.SpacesItemDecorationAllSideEqual(spacingInPixels);

        if (!fromLuxuryDetails) {
            RatingAttributeAdapter ratingAttributeAdapter = new RatingAttributeAdapter(mainActivityContext);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mainActivityContext, 2);
//            binding.recyclerview.addItemDecoration(spacesItemDecorationAllSideEqual);
            binding.recyclerview.setLayoutManager(gridLayoutManager);
            binding.recyclerview.setNestedScrollingEnabled(false);
            binding.recyclerview.setAdapter(ratingAttributeAdapter);

            ratingAttributeAdapter.addAll(arrayList.get(position).getRatingAttributes());
            ratingAttributeAdapter.notifyDataSetChanged();

            binding.tvReviewDetails.setText(arrayList.get(position).getReviewDetails());
        } else {
            binding.tvRaterName.setText((arrayList.get(position).getUser().getUser_name() == null) ? "" : arrayList.get(position).getUser().getUser_name() + " (" + arrayList.get(position).getOverAllRating() + ")");
            if (arrayList.get(position).getReviewDetails() == null) {
                binding.llUserRating.setVisibility(View.GONE);
            }
            binding.ratingbar.setVisibility(View.GONE);
            binding.recyclerview.setVisibility(View.GONE);
            binding.tvReviewDetails.setVisibility(View.GONE);
            binding.tvOverAllRating.setText(arrayList.get(position).getReviewDetails());
            binding.frameLayout.getLayoutParams().width = 100;
            binding.frameLayout.getLayoutParams().height = 100;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 8, 0, 8);
            binding.tvOverAllRating.setLayoutParams(params);
            binding.tvOverAllRating.setTextColor(mainActivityContext.getResources().getColor(R.color.colorBlack));
            params.setMargins(0, 12, 0, 12);
            binding.llReviewDetail.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addAll(ArrayList<UserRating> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    public ArrayList<UserRating> getAll() {
        return arrayList;
    }

    class Holder extends RecyclerView.ViewHolder {
        LayoutDetailedReviewBinding binding;

        Holder(LayoutDetailedReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public Holder(View itemView) {
            super(itemView);
        }
    }

    public void setFromLuxuryDetails(boolean fromLuxuryDetails) {
        this.fromLuxuryDetails = fromLuxuryDetails;
    }
}
