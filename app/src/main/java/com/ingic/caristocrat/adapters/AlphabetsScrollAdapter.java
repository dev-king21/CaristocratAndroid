package com.ingic.caristocrat.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.databinding.LayoutAlphabetBinding;
import com.ingic.caristocrat.models.EnglishAlphabet;

import java.util.ArrayList;

public class AlphabetsScrollAdapter extends RecyclerView.Adapter<AlphabetsScrollAdapter.Holder> {
    MainActivity mainActivityContext;
    LayoutAlphabetBinding binding;
    ArrayList<EnglishAlphabet> arrayList;

    public AlphabetsScrollAdapter(MainActivity mainActivityContext){
        this.mainActivityContext = mainActivityContext;
        this.arrayList = new ArrayList<EnglishAlphabet>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mainActivityContext), R.layout.layout_alphabet, parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        binding.tvAlphabet.setText(arrayList.get(position).getAlphabet());
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }

    public void add(EnglishAlphabet englishAlphabet){
        this.arrayList.add(englishAlphabet);
    }

    public void addAll(ArrayList<EnglishAlphabet> arrayList){
        this.arrayList.clear();
        this.arrayList = arrayList;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        LayoutAlphabetBinding binding;

        Holder(LayoutAlphabetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
