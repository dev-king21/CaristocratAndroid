package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.helpers.BasePreferenceHelper;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.models.LimitedEditionSpec;
import com.ingic.caristocrat.models.TradeCar;

import java.text.NumberFormat;
import java.util.Locale;

/**
 */

public class MyTableViewAdapter extends AbstractTableAdapter<LimitedEditionSpec, TradeCar, LimitedEditionSpec> {

    public MyTableViewAdapter(Context pContext) {
        super(pContext);
    }

    /**
     * This is sample CellViewHolder class
     * This viewHolder must be extended from AbstractViewHolder class instead of RecyclerView.ViewHolder.
     */
    class MyCellViewHolder extends AbstractViewHolder {

        public final TextView cell_textview;
//        public final LinearLayout ItemView;

        public MyCellViewHolder(View itemView) {
            super(itemView);
            cell_textview = (TextView) itemView.findViewById(R.id.tvCell);
//            ItemView = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }


    /**
     * This is where you create your custom Cell ViewHolder. This method is called when Cell
     * RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given type to
     * represent an item.
     *
     * @param viewType : This value comes from #getCellItemViewType method to support different type
     *                 of viewHolder as a Cell item.
     * @see #getCellItemViewType(int);
     */
    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        // Get cell xml layout
        View layout = LayoutInflater.from(m_jContext).inflate(R.layout.item_compare_cell,
                parent, false);
        // Create a Custom ViewHolder for a Cell item.
        return new MyCellViewHolder(layout);
    }

    /**
     * That is where you set Cell View Model data to your custom Cell ViewHolder. This method is
     * Called by Cell RecyclerView of the TableView to display the data at the specified position.
     * This method gives you everything you need about a cell item.
     *
     * @param holder     : This is one of your cell ViewHolders that was created on
     *                   ```onCreateCellViewHolder``` method. In this example we have created
     *                   "MyCellViewHolder" holder.
     * @param pValue     : This is the cell view model located on this X and Y position. In this
     *                   example, the model class is "Cell".
     * @param pXPosition : This is the X (Column) position of the cell item.
     * @param pYPosition : This is the Y (Row) position of the cell item.
     * @see #onCreateCellViewHolder(ViewGroup, int);
     */
    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object pValue, int
            pXPosition, int pYPosition) {
        LimitedEditionSpec cell = (LimitedEditionSpec) pValue;
        // Get the holder to update cell item text
        MyCellViewHolder viewHolder = (MyCellViewHolder) holder;
        if (cell.getName().equals(AppConstants.CarSpecsLuxuryNew.HEIGHT) || cell.getName().equals(AppConstants.CarSpecsLuxuryNew.WIDTH) || cell.getName().equals(AppConstants.CarSpecsLuxuryNew.LENGTH)) {
            viewHolder.cell_textview.setText(cell.getValue() + " MM");
        } else if (cell.getName().equals(AppConstants.CarSpecsLuxuryNew.TRUNK)) {
            viewHolder.cell_textview.setText(cell.getValue() + " L");
        } else if (cell.getName().equals(AppConstants.CarSpecsLuxuryNew.WEIGHT)) {
            viewHolder.cell_textview.setText(cell.getValue() + " KG");
        } else if (cell.getName().equals(AppConstants.CarSpecsLuxuryNew.DISPLACEMENT)) {
            viewHolder.cell_textview.setText(cell.getValue() + " CC");
        } else if (cell.getName().equals(AppConstants.CarSpecsLuxuryNew.MAX_SPEED)) {
            viewHolder.cell_textview.setText(cell.getValue() + " KM/H");
        } else if (cell.getName().equals(AppConstants.CarSpecsLuxuryNew.ACCELERATION)) {
            viewHolder.cell_textview.setText(cell.getValue() + " SEC");
        } else if (cell.getName().equals(AppConstants.CarSpecsLuxuryNew.TORQUE)) {
            viewHolder.cell_textview.setText(cell.getValue() + " NM");
        } else if (cell.getName().equals(AppConstants.CarSpecsLuxuryNew.FUEL_CONSUMBSION)) {
            viewHolder.cell_textview.setText(cell.getValue() + " L/100 KM");
        } else if (cell.getName().equals(AppConstants.CarSpecsLuxuryNew.EMISSION)) {
            viewHolder.cell_textview.setText(cell.getValue() + " gmCO2/KM");
        } else if (cell.getName().equals(AppConstants.CarSpecsLuxuryNew.WARRANTY) || cell.getName().equals(AppConstants.CarSpecsLuxuryNew.MAINTENANCE_PROGRAM)) {
            viewHolder.cell_textview.setText(cell.getValue());
        } else {
            viewHolder.cell_textview.setText(cell.getValue());
        }
        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.
        // It is necessary to remeasure itself.
//        viewHolder.ItemView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        if (cell.getIsHighlight() == 1) {
//            viewHolder.cell_textview.setBackgroundColor(m_jContext.getResources().getColor(R.color.colorSpecsHighlight));
            viewHolder.cell_textview.setBackgroundDrawable(m_jContext.getResources().getDrawable(R.drawable.cell_background));
        } else {
//            viewHolder.cell_textview.setBackgroundColor(m_jContext.getResources().getColor(R.color.colorLightWhite));
            viewHolder.cell_textview.setBackgroundDrawable(null);
        }
        viewHolder.cell_textview.setSelected(true);
        viewHolder.cell_textview.requestLayout();
    }

    /**
     * This is sample CellViewHolder class.
     * This viewHolder must be extended from AbstractViewHolder class instead of RecyclerView.ViewHolder.
     */
    class MyColumnHeaderViewHolder extends AbstractViewHolder {

        public final TextView column_header_textview;
//        public final LinearLayout column_header_container;

        public MyColumnHeaderViewHolder(View itemView) {
            super(itemView);
            column_header_textview = (TextView) itemView.findViewById(R.id.tvColumnHeader);
//            column_header_container = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }

    /**
     * This is where you create your custom Column Header ViewHolder. This method is called when
     * Column Header RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given
     * type to represent an item.
     *
     * @param viewType : This value comes from "getColumnHeaderItemViewType" method to support
     *                 different type of viewHolder as a Column Header item.
     * @see #getColumnHeaderItemViewType(int);
     */
    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        // Get Column Header xml Layout
        View layout = LayoutInflater.from(m_jContext).inflate(R.layout
                .item_compare_column_header, parent, false);
        // Create a ColumnHeader ViewHolder
        return new MyColumnHeaderViewHolder(layout);
    }

    /**
     * That is where you set Column Header View Model data to your custom Column Header ViewHolder.
     * This method is Called by ColumnHeader RecyclerView of the TableView to display the data at
     * the specified position. This method gives you everything you need about a column header
     * item.
     *
     * @param holder   : This is one of your column header ViewHolders that was created on
     *                 ```onCreateColumnHeaderViewHolder``` method. In this example we have created
     *                 "MyColumnHeaderViewHolder" holder.
     * @param pValue   : This is the column header view model located on this X position. In this
     *                 example, the model class is "ColumnHeader".
     * @param position : This is the X (Column) position of the column header item.
     * @see #onCreateColumnHeaderViewHolder(ViewGroup, int) ;
     */
    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object pValue, int
            position) {
        LimitedEditionSpec columnHeader = (LimitedEditionSpec) pValue;
        // Get the holder to update cell item text
        MyColumnHeaderViewHolder columnHeaderViewHolder = (MyColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.column_header_textview.setText(columnHeader.getName().replace("_", " ").toUpperCase());
        columnHeaderViewHolder.column_header_textview.setSelected(true);
        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.
        // It is necessary to remeasure itself.
//        columnHeaderViewHolder.column_header_container.getLayoutParams().width = LinearLayout
//                .LayoutParams.WRAP_CONTENT;
        columnHeaderViewHolder.column_header_textview.requestLayout();
    }

    /**
     * This is sample CellViewHolder class.
     * This viewHolder must be extended from AbstractViewHolder class instead of RecyclerView.ViewHolder.
     */
    class MyRowHeaderViewHolder extends AbstractViewHolder {

        public final TextView row_header_textview;
        public final TextView tvRowYear;
        public final ImageView row_header_imageView;

        public MyRowHeaderViewHolder(View itemView) {
            super(itemView);
            row_header_textview = (TextView) itemView.findViewById(R.id.tvRowHeader);
            tvRowYear = (TextView) itemView.findViewById(R.id.tvRowYear);
            row_header_imageView = (ImageView) itemView.findViewById(R.id.ivCompareCar);
        }
    }


    /**
     * This is where you create your custom Row Header ViewHolder. This method is called when
     * Row Header RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given
     * type to represent an item.
     *
     * @param viewType : This value comes from "getRowHeaderItemViewType" method to support
     *                 different type of viewHolder as a row Header item.
     * @see #getRowHeaderItemViewType(int);
     */
    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        // Get Row Header xml Layout
        View layout = LayoutInflater.from(m_jContext).inflate(R.layout
                .item_compare_row_header, parent, false);
        // Create a Row Header ViewHolder
        return new MyRowHeaderViewHolder(layout);
    }

    /**
     * That is where you set Row Header View Model data to your custom Row Header ViewHolder. This
     * method is Called by RowHeader RecyclerView of the TableView to display the data at the
     * specified position. This method gives you everything you need about a row header item.
     *
     * @param holder   : This is one of your row header ViewHolders that was created on
     *                 ```onCreateRowHeaderViewHolder``` method. In this example we have created
     *                 "MyRowHeaderViewHolder" holder.
     * @param pValue   : This is the row header view model located on this Y position. In this
     *                 example, the model class is "RowHeader".
     * @param position : This is the Y (row) position of the row header item.
     * @see #onCreateRowHeaderViewHolder(ViewGroup, int) ;
     */
    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object pValue, int
            position) {
        TradeCar rowHeader = (TradeCar) pValue;
        // Get the holder to update row header item text
        MyRowHeaderViewHolder rowHeaderViewHolder = (MyRowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(rowHeader.getName());

        BasePreferenceHelper preferenceHelper = new BasePreferenceHelper(m_jContext);
        if (preferenceHelper.getLoginStatus() && preferenceHelper.getUser() != null && preferenceHelper.getUser().getDetails() != null && preferenceHelper.getUser().getDetails().getUserRegionDetail() != null && preferenceHelper.getUser().getDetails().getUserRegionDetail().getCurrency() != null) {
            rowHeaderViewHolder.tvRowYear.setText(rowHeader.getCurrency() + " " + NumberFormat.getNumberInstance(Locale.US).format(rowHeader.getAmount()));
        } else {
            rowHeaderViewHolder.tvRowYear.setText(m_jContext.getResources().getString(R.string.aed) + " " + NumberFormat.getNumberInstance(Locale.US).format(rowHeader.getAmount()));
        }
        rowHeaderViewHolder.row_header_textview.setSelected(true);

        if (rowHeader.getMedia() != null) {
            if (rowHeader.getMedia().size() > 0) {
                UIHelper.setImageWithGlide(m_jContext, rowHeaderViewHolder.row_header_imageView, rowHeader.getMedia().get(0).getFileUrl());
            }
        }
    }


    @Override
    public View onCreateCornerView() {
        // Get Corner xml layout
        return LayoutInflater.from(m_jContext).inflate(R.layout.corner_layot, null);
//        return null;
    }

    @Override
    public int getColumnHeaderItemViewType(int pXPosition) {
        // The unique ID for this type of column header item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int pYPosition) {
        // The unique ID for this type of row header item
        // If you have different items for Row Header View by Y (Row) position,
        // then you should fill this method to be able create different
        // type of RowHeaderViewHolder on "onCreateRowHeaderViewHolder"
        return 0;
    }

    @Override
    public int getCellItemViewType(int pXPosition) {
        // The unique ID for this type of cell item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        return 0;
    }
}