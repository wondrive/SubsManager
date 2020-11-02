package com.kftc.openbankingsample2.biz.center_auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.subsmanager2.MainActivity;
import com.example.subsmanager2.R;
import com.kftc.openbankingsample2.biz.center_auth.api.CenterAuthAPIFragment;
import com.kftc.openbankingsample2.biz.center_auth.auth.CenterAuthFragment;
import com.kftc.openbankingsample2.biz.main.HomeFragment;
import com.kftc.openbankingsample2.biz.main.OpenBankingMainActivity;

/**
 * 센터인증 메인화면
 */
public class CenterAuthHomeFragment extends AbstractCenterAuthMainFragment {

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
        view = inflater.inflate(R.layout.fragment_center_auth_home, container, false);
        initView();
        return view;
    }

    private void initView() {

        // 계좌등록
        view.findViewById(R.id.btnAuthToken).setOnClickListener(v -> startFragment(CenterAuthFragment.class, args, R.string.fragment_id_center_auth));

        // API 거래
        view.findViewById(R.id.btnAPICallMenu).setOnClickListener(v -> startFragment(CenterAuthAPIFragment.class, args, R.string.fragment_id_center_api_call));

    }

    @Override
    public void onBackPressed() {
        //startFragment(HomeFragment.class, args, R.string.fragment_id_home);
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }
}
