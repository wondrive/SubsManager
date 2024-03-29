package com.kftc.openbankingsample2.biz.center_auth.auth.authorize_account;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTabHost;

import com.example.subsmanager2.R;
import com.kftc.openbankingsample2.biz.center_auth.AbstractCenterAuthMainFragment;
import com.kftc.openbankingsample2.biz.center_auth.CenterAuthConst;
import com.kftc.openbankingsample2.biz.center_auth.auth.authorize.CenterAuthAuthorizeCase1ChildFragment;
import com.kftc.openbankingsample2.biz.center_auth.auth.authorize.CenterAuthAuthorizeCase3ChildFragment;
import com.kftc.openbankingsample2.biz.center_auth.http.CenterAuthApiRetrofitInterface;
import com.kftc.openbankingsample2.biz.center_auth.util.CenterAuthUtils;

/**
 * 계좌등록확인
 */
public class CenterAuthAuthorizeAccountFragment extends AbstractCenterAuthMainFragment {

    // context
    private Context context;

    // view
    private View view;

    // data
    private Bundle args;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        args = getArguments();
        if (args == null) args = new Bundle();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_auth_authorize_account, container, false);
        initView();
        return view;
    }

    void initView() {

        // 사용자인증 or 계좌등록확인 여부를 전달
        args.putString(CenterAuthConst.BUNDLE_KEY_OAUTH_TYPE, CenterAuthConst.BUNDLE_DATA_OAUTH_TYPE_AUTHORIZE_ACCOUNT);
        args.putString(CenterAuthConst.BUNDLE_KEY_URI, CenterAuthUtils.getSavedValueFromSetting(CenterAuthConst.CENTER_AUTH_BASE_URI) + CenterAuthApiRetrofitInterface.authorizeAccountUrl);

        FragmentTabHost tabHost = view.findViewById(android.R.id.tabhost);
        tabHost.setup(context, getChildFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("CASE 1"), CenterAuthAuthorizeCase1ChildFragment.class, args);
        //tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("CASE 2"), Delete_CenterAuthAuthorizeCase2ChildFragment.class, args);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("CASE 3"), CenterAuthAuthorizeCase3ChildFragment.class, args);

        tabHost.setOnTabChangedListener(tabId -> {
            Class<?> fragmentClass = null;
            switch(tabId){
                case "tab1":
                    fragmentClass = CenterAuthAuthorizeCase1ChildFragment.class;
                    ((TextView) view.findViewById(R.id.tvSubTitle)).setText(R.string.fragment_subtitle_auth_authorize_case1);
                    break;
                /*case "tab2":
                    fragmentClass = Delete_CenterAuthAuthorizeCase2ChildFragment.class;
                    ((TextView) view.findViewById(R.id.tvSubTitle)).setText(R.string.fragment_subtitle_auth_authorize_case2);
                    break;*/
                case "tab3":
                    fragmentClass = CenterAuthAuthorizeCase3ChildFragment.class;
                    ((TextView) view.findViewById(R.id.tvSubTitle)).setText(R.string.fragment_subtitle_auth_authorize_case3);
                    break;
            }
            startChildFragment(fragmentClass, args);
        });

    }
}
