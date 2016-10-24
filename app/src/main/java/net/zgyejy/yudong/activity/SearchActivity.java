package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;

import net.zgyejy.yudong.R;

public class SearchActivity extends AppCompatActivity {
    private SearchView mySearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mySearchView = (SearchView) findViewById(R.id.mySearchView);
    }
}
