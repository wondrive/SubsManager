package com.kftc.openbankingsample2.biz.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.subsmanager2.R;

/**
 * 메인 Activity. 모든 프래그먼트는 이 메인 activity를 안에서 구현된다.
 */
public class OpenBankingMainActivity extends AbstractMainActivity {

    // context
    private Context context;

    // data
    private Bundle args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        args = getIntent().getExtras();
        if (args == null) args = new Bundle();

        Log.i("OpenBankinMain 태그 여깅깅", "activity: "+this + ", context:"+getApplicationContext()+", baseContext: "+getBaseContext());
        initView();
    }

    private void initView() {
        initData();
    }

    private void initData() {
        goNext();
    }

    private void goNext() {
        startFragment(MainFragment.class, args, R.string.fragment_id_main);
    }
}
