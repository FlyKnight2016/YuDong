package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.widget.SearchView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends MyBaseActivity {
    private SearchView mySearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mySearchView = (SearchView) findViewById(R.id.mySearchView);
    }

    @OnClick(R.id.iv_search_back)
    public void onClick() {
        finish();
    }
}
