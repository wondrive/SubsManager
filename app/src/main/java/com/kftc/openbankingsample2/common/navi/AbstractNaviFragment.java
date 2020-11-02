package com.kftc.openbankingsample2.common.navi;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.subsmanager2.R;
import com.kftc.openbankingsample2.biz.main.OpenBankingMainActivity;

import timber.log.Timber;

public class AbstractNaviFragment extends Fragment {

    // context
    private Context context;
    protected OpenBankingMainActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = (OpenBankingMainActivity) getActivity();
    }

    public void startChildFragment(@NonNull Class fragmentClass) {
        startChildFragment(fragmentClass, R.id.flMenuSub);
    }

    public void startChildFragment(@NonNull Class fragmentClass, @IdRes int frameLayout) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (java.lang.InstantiationException | IllegalAccessException e) {
            Timber.e(e);
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(frameLayout, fragment);
            fragmentTransaction.commit();
        }
    }

}
