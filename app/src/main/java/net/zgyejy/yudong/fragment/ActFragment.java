package net.zgyejy.yudong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.activity.HomeActivity;
import net.zgyejy.yudong.activity.WebReadActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActFragment extends Fragment {

    @BindView(R.id.btn_h5)
    Button btnH5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_act, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_h5)
    public void onClick() {
        String url = "http://www.rabbitpre.com/m/jNuqM2NAs?lc=2&sui=Nt7zCVao&from=singlemessage&isappinstalled=0#from=share";
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        ((HomeActivity)getActivity()).openActivity(WebReadActivity.class,bundle);
    }
}
