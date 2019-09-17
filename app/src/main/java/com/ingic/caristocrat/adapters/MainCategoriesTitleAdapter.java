package com.ingic.caristocrat.adapters;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.interfaces.OnItemClickListener;
import com.ingic.caristocrat.webhelpers.models.Category;

import java.util.ArrayList;

/**
 *  on 8/7/2018.
 */
public class MainCategoriesTitleAdapter extends RecyclerView.Adapter<MainCategoriesTitleAdapter.Holder> {
    MainActivity mainActivityContext;
    ArrayList<Category> arrayList;
    int selectedItem = -1;
    TextView previousTextView;
    ArticlesAdapter articlesAdapter;
    OnItemClickListener onItemClickListener;

    public MainCategoriesTitleAdapter(MainActivity mainActivityContext, OnItemClickListener onItemClickListener) {
        this.mainActivityContext = mainActivityContext;
        this.arrayList = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MainCategoriesTitleAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivityContext).inflate(R.layout.layout_main_category_title, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        holder.categoryName.setText(arrayList.get(position).getName());
        if (position == 0) {
            setSubCategoriesTab(holder.categoryName);
            onItemClickListener.onItemClick(position, arrayList.get(0).getName(), false);
        }

        holder.categoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (articlesAdapter != null) {
                    setSubCategoriesTab(holder.categoryName);
                    onItemClickListener.onItemClick(position, arrayList.get(position).getName(), true);
                }
            }
        });
    }

    private void setSubCategoriesTab(TextView currentTextView) {
        currentTextView.setBackground(mainActivityContext.getResources().getDrawable(R.drawable.black_border_item));
        currentTextView.setTextColor(ContextCompat.getColor(mainActivityContext, R.color.colorWhite));
        if (previousTextView != null && previousTextView != currentTextView) {
            previousTextView.setBackground(mainActivityContext.getResources().getDrawable(R.drawable.border_item));
            previousTextView.setTextColor(ContextCompat.getColor(mainActivityContext, R.color.colorBlack));
        }
        previousTextView = currentTextView;
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }

    public void setArticlesAdapter(ArticlesAdapter articlesAdapter) {
        this.articlesAdapter = articlesAdapter;
    }

    public void add(Category category) {
        this.arrayList.add(category);
    }

    public void add(int index, Category category) {
        this.arrayList.add(index, category);
    }

    public void addAll(ArrayList<Category> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public Holder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
