package com.ingic.caristocrat.interfaces;

import com.ingic.caristocrat.webhelpers.models.Comments;

public interface OnBeginCommentEditingListener {
    void OnBeginCommentEditing(Comments comment, int position, boolean delete);
}
