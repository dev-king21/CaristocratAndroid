package com.ingic.caristocrat.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.databinding.FragmentCommentsBottomSheetBinding;
import com.ingic.caristocrat.databinding.ItemCommentBinding;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.webhelpers.models.Comments;

import java.util.ArrayList;

/**
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holder> implements View.OnClickListener {
    MainActivity mainActivityContext;
    ArrayList<Comments> arrayList;
    FragmentCommentsBottomSheetBinding fragmentCommentsBottomSheetBinding;
    ItemCommentBinding binding;

    public CommentAdapter(MainActivity mainActivityContext, FragmentCommentsBottomSheetBinding fragmentCommentsBottomSheetBinding) {
        this.mainActivityContext = mainActivityContext;
        this.fragmentCommentsBottomSheetBinding = fragmentCommentsBottomSheetBinding;
        this.arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mainActivityContext), R.layout.item_comment, parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        binding.tvCommenterName.setText(arrayList.get(position).getUser().getName());
        binding.tvComment.setText(arrayList.get(position).getCommentText());
/*
       if(arrayList.get(position).getUser()!=null && arrayList.get(position).getUser().getDetails()!=null){
           binding.tvTime.setText((arrayList.get(position).getUser().getCreatedAt()==null)?"": DateFormatHelper.isCurrentDate(arrayList.get(position).getUser().getCreatedAt())?DateFormatHelper.getMinutesRemaining(arrayList.get(position).getUser().getCreatedAt()):DateFormatHelper.changeServerToOurFormatDate(arrayList.get(position).getUser().getCreatedAt()));
           binding.tvCommenterName.setText((arrayList.get(position).getUser().getName()==null)?"": arrayList.get(position).getUser().getName());
           UIHelper.setImageWithGlide(mainActivityContext,binding.ivCommenter, arrayList.get(position).getUser().getDetails().getImageUrl());
       }
*/
        binding.ivCommenter.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCommenter:
                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_LONG);
                break;
        }
    }

    public void addAll(ArrayList<Comments> comments) {
        this.arrayList.clear();
        this.arrayList.addAll(comments);
    }

    public void add(Comments comment) {
        this.arrayList.add(comment);
    }

    public static class Holder extends RecyclerView.ViewHolder {

        ItemCommentBinding binding;

        Holder(ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
