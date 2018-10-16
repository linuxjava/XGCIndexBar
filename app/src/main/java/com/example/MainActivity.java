package com.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import xgc.free.indexbar.IndexBar;
import xgc.free.pinned.decoration.PinnedHeaderDecoration;
import xiao.free.decoration.HorizontalDividerItemDecoration;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private StationAdapter mAdapter;
    private IndexBar mIndexBar;
    private HashMap<String, Integer> indexMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIndexBar = findViewById(R.id.indexbar);
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new StationAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new PinnedHeaderDecoration().registerTypePinnedHeader(SData.ITEM_TYPE_SECTION
                , new PinnedHeaderDecoration.PinnedHeaderCreator() {
                    @Override
                    public boolean create(RecyclerView parent, int adapterPosition) {
                        return true;
                    }
                }));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(1).margin(10, 10).visibilityProvider(mAdapter).build());

        mIndexBar.setTextColor(0xFF4A4A4A);
        mIndexBar.setBgColor(0x7F767676);
        mIndexBar.setOverlayTextView((TextView) findViewById(R.id.text_char));
        mIndexBar.setIndexChangedListener(new IndexBar.IndexChangedListener() {
            @Override
            public void onIndexChanged(String index, int position) {
                int listPos = indexMap.get(index);
                mRecyclerView.getLayoutManager().scrollToPosition(listPos);
            }
        });

        List<SData> datas = new Gson().fromJson(TestData.DATA, new TypeToken<List<SData>>() {
        }.getType());
        Collections.sort(datas, new SectionComparator());
        for (int i=0; i< datas.size(); i++){
            if(datas.get(i).itemType == SData.ITEM_TYPE_SECTION){
                indexMap.put(datas.get(i).section.substring(0, 1), i);
            }else {
                datas.get(i).itemType = SData.ITEM_TYPE_STATION;
            }
        }

        mAdapter.setDatas(datas);
    }

    public class SectionComparator implements Comparator<SData> {

        @Override
        public int compare(SData l, SData r) {
            if (l == null || r == null) {
                return 0;
            }

            char lChar = l.section.charAt(0);
            char rChar = r.section.charAt(0);
            //
            if (lChar == rChar) {
                return 0;
            }
            return lChar < rChar ? -1 : 1;
        }
    }
}
