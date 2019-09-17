package com.ingic.caristocrat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.databinding.LayoutItemRatingBinding;
import com.ingic.caristocrat.models.RatingAttribute;
import com.ingic.caristocrat.models.UserRating;

import java.util.ArrayList;

public class RatingAttributeAdapter extends RecyclerView.Adapter<RatingAttributeAdapter.Holder> {
    private LayoutItemRatingBinding binding;
    private MainActivity mainActivityContext;
    private ArrayList<RatingAttribute> arrayList;
    private UserRating userRating;
    private boolean ratingPost, rated;
    private static int LAYOUT_ITEM_RATING = R.layout.layout_item_rating;
    private static int LAYOUT_ITEM_RATING_POST = R.layout.layout_item_rating_post;

    public RatingAttributeAdapter(MainActivity mainActivityContext) {
        this.mainActivityContext = mainActivityContext;
        this.arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mainActivityContext).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        switch (holder.viewType) {
            case R.layout.layout_item_rating_post:
                holder.tvRatingAttributeTitle.setText(arrayList.get(position).getTitle());
                holder.ratingbar.setRating(arrayList.get(position).getRating());
                holder.ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        arrayList.get(position).setRating(rating);
                    }
                });
                if (rated) {
                    if (userRating != null) {
                        for (int pos = 0; pos < userRating.getRatingAttributes().size(); pos++) {
                            if (arrayList.get(position).getId() == userRating.getRatingAttributes().get(pos).getId()) {
                                holder.ratingbar.setRating(userRating.getRatingAttributes().get(pos).getRating());
                                holder.ratingbar.setIsIndicator(true);
                            }
                        }
                    }
                }
                break;

            case R.layout.layout_item_rating:
                holder.tvRatingAttributeTitle.setSelected(true);
                holder.tvRatingAttributeTitle.setText(arrayList.get(position).getTitle());
                holder.ratingbar.setRating(arrayList.get(position).getRating());
                break;
        }
        /*
        if (ratingPost) {
            binding.tvRatingAttributeTitle.setText(arrayList.get(position).getTitle());
            binding.ratingbar.setRating(arrayList.get(position).getRating());
            binding.ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    arrayList.get(position).setRating(rating);
                }
            });
        } else {
            binding.tvRatingAttributeTitle.setText(arrayList.get(position).getTitle());
            binding.ratingbar.setRating(arrayList.get(position).getRating());
        }
        */
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (ratingPost) {
            return LAYOUT_ITEM_RATING_POST;
        } else {
            return LAYOUT_ITEM_RATING;
        }
    }

    public void addAll(ArrayList<RatingAttribute> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    public ArrayList<RatingAttribute> getAll() {
        return this.arrayList;
    }

    public void setUserRating(UserRating userRating) {
        this.userRating = userRating;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    class Holder extends RecyclerView.ViewHolder {
        View view;
        int viewType;
        TextView tvRatingAttributeTitle;
        RatingBar ratingbar;

        Holder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            this.view = view;
            tvRatingAttributeTitle = itemView.findViewById(R.id.tvRatingAttributeTitle);
            ratingbar = itemView.findViewById(R.id.ratingbar);
        }
    }

    public void setRatingPost(boolean ratingPost) {
        this.ratingPost = ratingPost;
    }
}
