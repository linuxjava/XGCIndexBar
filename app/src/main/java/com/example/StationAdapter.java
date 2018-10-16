package com.example;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import xiao.free.decoration.FlexibleDividerDecoration;
import xiao.free.superadapter.recycview.MultiItemTypeAdapter;
import xiao.free.superadapter.recycview.base.ItemViewDelegate;
import xiao.free.superadapter.recycview.base.ViewHolder;

public class StationAdapter extends MultiItemTypeAdapter<SData> implements FlexibleDividerDecoration.VisibilityProvider{
    public StationAdapter(Context context) {
        super(context);

        addItemViewDelegate(SData.ITEM_TYPE_SECTION, new SectionHolder());
        addItemViewDelegate(SData.ITEM_TYPE_STATION, new StationHolder());
    }

    @Override
    public boolean shouldHideDivider(int position, RecyclerView parent) {
        if (position >= 0 && position < getItemCount()) {

            //SECTION item前一个item不显示分割线
            if(position + 1 < getItemCount() && getDatas().get(position + 1).itemType == SData.ITEM_TYPE_SECTION){
                return true;
            }
            //SECTION item不显示分割线
            if (getDatas().get(position).itemType == SData.ITEM_TYPE_SECTION) {
                return true;
            }
        }
        return false;
    }

    public static class SectionHolder implements ItemViewDelegate<SData> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_pinned_header;
        }

        @Override
        public boolean isForViewType(SData item, int position) {
            return item.itemType == SData.ITEM_TYPE_SECTION;
        }

        @Override
        public void convert(ViewHolder holder, SData sData, int position) {
            holder.setText(R.id.text_pinned_header, sData.section.substring(0, 1));
        }
    }

    public static class StationHolder implements ItemViewDelegate<SData> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_station_name;
        }

        @Override
        public boolean isForViewType(SData item, int position) {
            return item.itemType == SData.ITEM_TYPE_STATION;
        }

        @Override
        public void convert(ViewHolder holder, SData sData, int position) {
            holder.setText(R.id.text_station_name, sData.stationName);
        }
    }
}
