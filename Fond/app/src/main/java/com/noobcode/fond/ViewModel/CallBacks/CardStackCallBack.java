package com.noobcode.fond.ViewModel.CallBacks;

import androidx.recyclerview.widget.DiffUtil;

import com.noobcode.fond.Model.CardStack;

import java.util.List;

public class CardStackCallBack extends DiffUtil.Callback {

  private List<CardStack> old, baru;

    public CardStackCallBack(List<CardStack> old, List<CardStack> baru) {
        this.old = old;
        this.baru = baru;
    }

    @Override
    public int getOldListSize() {
        return old.size();
    }

    @Override
    public int getNewListSize() {
        return baru.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition).getImageUrl() == baru.get(newItemPosition).getImageUrl();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition) == baru.get(newItemPosition);
    }
}
