package com.ingic.caristocrat.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.BaseActivity;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.activities.RegistrationActivity;
import com.ingic.caristocrat.adapters.CommentsAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentCommentsBottomSheetBinding;
import com.ingic.caristocrat.databinding.FragmentMainDetailPageBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.OnBeginCommentEditingListener;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.Comments;
import com.ingic.caristocrat.webhelpers.models.User;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class CommentsFragment extends BottomSheetDialogFragment implements View.OnClickListener, OnBeginCommentEditingListener {
    FragmentCommentsBottomSheetBinding binding;
    FragmentMainDetailPageBinding fragmentMainDetailPageBinding;
    //    CommentAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    MainActivity mainActivityContext;
    CommentsAdapter adapter;
    ArrayList<Comments> arrayList;
    User user;
    int newsId, commentsCount, editingCommentPosition;
    Comments comment;
    boolean editing;

    public CommentsFragment() {
    }

    @SuppressLint("ValidFragment")
    public CommentsFragment(MainActivity mainActivity, FragmentMainDetailPageBinding fragmentMainDetailPageBinding) {
        this.mainActivityContext = mainActivity;
        this.fragmentMainDetailPageBinding = fragmentMainDetailPageBinding;
    }

    public static CommentsFragment Instance(MainActivity mainActivity, FragmentMainDetailPageBinding fragmentMainDetailPageBinding) {
        return new CommentsFragment(mainActivity, fragmentMainDetailPageBinding);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comments_bottom_sheet, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        setListeners();
        user = mainActivityContext.getPreferenceHelper().getUser();
        if (mainActivityContext.showLoader()) {
            getComments(newsId);
        }
    }

    @Override
    public void onClick(View view) {
        UIHelper.hideSoftKeyboard(mainActivityContext, view);
        switch (view.getId()) {
            case R.id.tvClose:
                dismiss();
                break;
            case R.id.ibSend:
                if (!mainActivityContext.getPreferenceHelper().getLoginStatus()) {
                    launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_message));
                    return;
                }
                if (binding.etComment.getText().toString().trim().length() > 0) {
                    send();
                } else {
                    UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.write_comment_first), Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initAdapter() {

        adapter = new CommentsAdapter(mainActivityContext, binding, this);
        binding.lvComments.setAdapter(adapter);
        binding.lvComments.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
    }

    private void setListeners() {
        binding.tvClose.setOnClickListener(this);
        binding.ibSend.setOnClickListener(this);
    }

    private void send() {
        if (mainActivityContext.internetConnected()) {
            showLoader();
            binding.ibSend.setEnabled(false);
            if (!editing) {
                postComments(binding.etComment.getText().toString(), newsId);
            } else {
                postComments(binding.etComment.getText().toString(), comment.getNewsId());
            }
        }
    }

    private void getComments(int newsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("news_id", newsId);
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.GET_COMMENTS, binding.getRoot(), null, params, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                arrayList = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Comments.class);
                commentsCount = arrayList.size();
                if (commentsCount > 0) {
                    adapter.addAll(arrayList);
                    adapter.notifyDataSetChanged();
                    binding.lvComments.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.lvComments.setSelection(adapter.getCount() - 1);
                        }
                    });
                } else {
                    binding.frameLayout.setVisibility(View.GONE);
                    binding.tvNoComments.setVisibility(View.VISIBLE);
                }
                mainActivityContext.hideLoader();
            }

            @Override
            public void onError() {
                mainActivityContext.hideLoader();
            }
        });
    }

    private void postComments(final String comment, int newsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("parent_id", 0);
        params.put("news_id", newsId);
        params.put("comment_text", comment.trim());

        if (mainActivityContext.internetConnected()) {
            if (!editing) {
                WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.POST_COMMENTS, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        Comments comment = (Comments) JsonHelpers.convertToModelClass(apiResponse.getData(), Comments.class);
                        comment.setUser(mainActivityContext.getPreferenceHelper().getUser());
                        adapter.add(comment);
                        adapter.notifyDataSetChanged();
                        if (adapter.getCount() > 0) {
                            binding.tvNoComments.setVisibility(View.GONE);
                            binding.frameLayout.setVisibility(View.VISIBLE);
                        }
                        fragmentMainDetailPageBinding.tvComments.setText((adapter.getCount() == 0 || adapter.getCount() == 1) ? adapter.getCount() + " " + mainActivityContext.getResources().getString(R.string.comment) : adapter.getCount() + " " + mainActivityContext.getResources().getString(R.string.comments));
                        binding.lvComments.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.lvComments.setSelection(adapter.getCount() - 1);
                            }
                        });
                        binding.etComment.getText().clear();
                        binding.ibSend.setEnabled(true);
                        hideLoader();
                    }

                    @Override
                    public void onError() {
                        binding.ibSend.setEnabled(true);
                        hideLoader();
                    }
                }, null);
            } else {
                params.put("id", this.comment.getId());
                WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.COMMENT_EDIT, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        editing = false;
                        binding.etComment.setText("");
                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.comment_updated_successful), Toast.LENGTH_SHORT);
                        Comments editedcomment = (Comments) JsonHelpers.convertToModelClass(apiResponse.getData(), Comments.class);
                        adapter.setComment(editingCommentPosition, editedcomment);
                        adapter.notifyDataSetChanged();
                        binding.ibSend.setEnabled(true);
                        hideLoader();
                    }

                    @Override
                    public void onError() {
                        editing = false;
                        binding.ibSend.setEnabled(true);
                        hideLoader();
                    }
                }, null);
            }
        }
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    @Override
    public void OnBeginCommentEditing(Comments comment, int position, boolean delete) {
        this.comment = comment;
        this.editingCommentPosition = position;
        if (!delete) {
            this.editing = true;
            binding.etComment.setText(comment.getCommentText());
        } else {
            this.editing = false;
            UIHelper.showSimpleDialog(
                    mainActivityContext,
                    0,
                    mainActivityContext.getResources().getString(R.string.delete_comment),
                    mainActivityContext.getResources().getString(R.string.delete_comment_comfirmation),
                    mainActivityContext.getResources().getString(R.string.delete),
                    mainActivityContext.getResources().getString(R.string.cancel),
                    false,
                    false,
                    new SimpleDialogActionListener() {
                        @Override
                        public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                            if (positive) {
                                Map<String, Object> params = new HashMap<>();
                                params.put("id", comment.getId());
                                WebApiRequest.Instance(mainActivityContext)
                                        .request(AppConstants.WebServicesKeys.COMMENT_DELETE, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                                            @Override
                                            public void onSuccess(ApiResponse apiResponse) {
                                                adapter.deleteComment(position);
                                                adapter.notifyDataSetChanged();
                                                fragmentMainDetailPageBinding.tvComments.setText((adapter.getCount() == 0 || adapter.getCount() == 1) ? adapter.getCount() + " " + mainActivityContext.getResources().getString(R.string.comment) : adapter.getCount() + " " + mainActivityContext.getResources().getString(R.string.comments));
                                                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.comment_deleted_successful), Toast.LENGTH_SHORT);
                                                if (adapter.getCount() == 0) {
                                                    binding.frameLayout.setVisibility(View.GONE);
                                                    binding.tvNoComments.setVisibility(View.VISIBLE);
                                                }
                                                binding.etComment.setText("");
                                                mainActivityContext.hideLoader();
                                            }

                                            @Override
                                            public void onError() {
                                                mainActivityContext.hideLoader();
                                            }
                                        }, null);
                            }
                        }
                    }
            );
        }
    }

    private void showLoader() {
        binding.frameLayout.setVisibility(View.GONE);
        binding.frameLayoutProgressbar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        binding.frameLayoutProgressbar.setVisibility(View.GONE);
        binding.frameLayout.setVisibility(View.VISIBLE);
    }

    public void launchSigninRequirement(BaseActivity activityContext, String message) {
        UIHelper.showSimpleDialog(
                activityContext,
                0,
                activityContext.getResources().getString(R.string.require_signin),
                message,
                activityContext.getResources().getString(R.string.sign_in),
                activityContext.getResources().getString(R.string.cancel),
                false,
                false,
                new SimpleDialogActionListener() {
                    @Override
                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                        if (positive) {
                            dialog.dismiss();
                            Intent intent = new Intent(activityContext, RegistrationActivity.class);
                            intent.putExtra(AppConstants.FROM_GUEST, true);
                            activityContext.startActivity(intent);
//                            activityContext.startActivity(RegistrationActivity.class, false);
                        } else {
                            dialog.dismiss();
                        }
                    }
                }
        );
    }
}
