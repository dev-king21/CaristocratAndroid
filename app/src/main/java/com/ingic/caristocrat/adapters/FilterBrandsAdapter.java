package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.models.FilterBrand;

import java.util.ArrayList;

public class FilterBrandsAdapter extends ArrayAdapter<FilterBrand> {
    private MainActivity mainActivityContext;
    private ArrayList<FilterBrand> arrayList;
    private int resource;

    public FilterBrandsAdapter(MainActivity mainActivityContext, int resource){
        super(mainActivityContext, resource);
        this.mainActivityContext = mainActivityContext;
        this.resource = resource;
        this.arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mainActivityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);

            viewHolder = new ViewHolder();
            viewHolder.llBrandsLogo = (LinearLayout) convertView.findViewById(R.id.llBrandsLogo);
            viewHolder.ivBrandLogo = (ImageView) convertView.findViewById(R.id.ivBrandLogo);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        UIHelper.setImageWithGlide(mainActivityContext, viewHolder.ivBrandLogo, arrayList.get(position).getIconPath());
        if(arrayList.get(position).isSelected()){
            viewHolder.ivBrandLogo.setBackground(mainActivityContext.getResources().getDrawable(R.drawable.image));
        }else{
            viewHolder.ivBrandLogo.setBackground(null);
        }
        viewHolder.ivBrandLogo.setImageResource(arrayList.get(position).getLogoResource());
        viewHolder.llBrandsLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayList.get(position).isSelected()){
                    arrayList.get(position).setSelected(false);
                }else{
                    arrayList.get(position).setSelected(true);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    public void add(FilterBrand filterBrand){
        this.arrayList.add(filterBrand);
    }

    public void addAll(ArrayList<FilterBrand> arrayList){
       this.arrayList.clear();
       this.arrayList.addAll(arrayList);
    }

    class ViewHolder{
        LinearLayout llBrandsLogo;
        ImageView ivBrandLogo;
    }
}
