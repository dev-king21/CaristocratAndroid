package com.ingic.caristocrat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.databinding.FragmentCommentsBottomSheetBinding;
import com.ingic.caristocrat.helpers.DateFormatHelper;
import com.ingic.caristocrat.helpers.RoundImageView;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.OnBeginCommentEditingListener;
import com.ingic.caristocrat.webhelpers.models.Comments;

import java.util.ArrayList;

/**
 */
public class CommentsAdapter extends BaseAdapter {
    private MainActivity mainActivity;
    private ArrayList<Comments> arrayList;
    private FragmentCommentsBottomSheetBinding fragmentCommentsBottomSheetBinding;
    OnBeginCommentEditingListener onBeginCommentEditingListener;

    public CommentsAdapter(MainActivity mainActivity, FragmentCommentsBottomSheetBinding fragmentCommentsBottomSheetBinding, OnBeginCommentEditingListener onBeginCommentEditingListener) {
        this.mainActivity = mainActivity;
        this.fragmentCommentsBottomSheetBinding = fragmentCommentsBottomSheetBinding;
        this.arrayList = new ArrayList<>();
        this.onBeginCommentEditingListener = onBeginCommentEditingListener;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mainActivity).inflate(R.layout.item_comment, viewGroup, false);
        }

        Comments comment = (Comments) getItem(position);

        TextView tvCommenterName = view.findViewById(R.id.tvCommenterName);
        TextView tvTime = view.findViewById(R.id.tvTime);
        TextView tvComment = view.findViewById(R.id.tvComment);
        RoundImageView ivCommenter = view.findViewById(R.id.ivCommenter);
        LinearLayout llCommentOptions = view.findViewById(R.id.llCommentOptions);
        TextView tvEditComment = view.findViewById(R.id.tvEditComment);
        TextView tvDeleteComment = view.findViewById(R.id.tvDeleteComment);

        if (comment.getUser() != null) {
            if (comment.getUser().getDetails() != null) {
                UIHelper.setUserImageWithGlide(mainActivity, ivCommenter, comment.getUser().getDetails().getImageUrl() == null ? "" : comment.getUser().getDetails().getImageUrl());
//                if (!comment.getUser().getDetails().getSocialLogin())
//
//                else
//                    UIHelper.setUserImageWithGlide(activityContext, ivCommenter, comment.getUser().getDetails().getImage() == null ? "" : comment.getUser().getDetails().getImage());
//                UIHelper.setUserImageWithGlide(activityContext, ivCommenter, comment.getUser().getDetails().getImageUrl());
            }
            tvCommenterName.setText(comment.getUser().getName());
        }

        tvTime.setText(DateFormatHelper.isCurrentDate(comment.getCreatedAt()) ? DateFormatHelper.changeServerToOurFormatTime(comment.getCreatedAt()) : DateFormatHelper.changeServerToOurFormatDate(comment.getCreatedAt()) + " " + mainActivity.getResources().getString(R.string.at) + " " + DateFormatHelper.changeServerToOurFormatTime(comment.getCreatedAt()));
        tvComment.setText(comment.getCommentText());

        if (mainActivity.getPreferenceHelper().getLoginStatus()) {
            if (comment.getUser().getId() == mainActivity.getPreferenceHelper().getUser().getId()) {
                llCommentOptions.setVisibility(View.VISIBLE);
                tvEditComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onBeginCommentEditingListener != null){
                            onBeginCommentEditingListener.OnBeginCommentEditing(arrayList.get(position), position, false);
                        }
                    }
                });
                tvDeleteComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onBeginCommentEditingListener != null){
                            onBeginCommentEditingListener.OnBeginCommentEditing(arrayList.get(position), position, true);
                        }
                    }
                });
            }else{
                llCommentOptions.setVisibility(View.GONE);
            }
        }

        return view;
    }

    public void addAll(ArrayList<Comments> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    public void setComment(int position, Comments comment){
        this.arrayList.set(position, comment);
    }

    public void deleteComment(int position){
        this.arrayList.remove(position);
    }

    public void add(Comments comment) {
        this.arrayList.add(comment);
    }
}
