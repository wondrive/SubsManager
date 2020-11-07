package com.kftc.openbankingsample2.biz.center_auth.api.account_transaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.subsmanager2.R;
import com.kftc.openbankingsample2.common.data.Transaction;
import com.kftc.openbankingsample2.common.util.Utils;
import com.kftc.openbankingsample2.common.util.view.recyclerview.KmRecyclerViewArrayAdapter;
import com.kftc.openbankingsample2.common.util.view.recyclerview.KmRecyclerViewHolder;

import java.util.ArrayList;

/**
 * 거래내역조회 어댑터, 송금인정보조회 어댑터
 */
public class CenterAuthAPIAccountTransactionResultAdapter extends KmRecyclerViewArrayAdapter<Transaction> {
    public CenterAuthAPIAccountTransactionResultAdapter(ArrayList<Transaction> itemList) {
        super(itemList);
    }

    @NonNull
    @Override
    public KmRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_center_auth_api_account_transaction_result, parent, false);
        return new ApiCallAccountTransactionResultViewHolder(v, this);
    }

    public static class ApiCallAccountTransactionResultViewHolder extends KmRecyclerViewHolder<Transaction> {

        private CenterAuthAPIAccountTransactionResultAdapter adapter;

        public ApiCallAccountTransactionResultViewHolder(View v, CenterAuthAPIAccountTransactionResultAdapter adapter) {
            super(v);
            this.adapter = adapter;
        }

        @Override
        public void onBindViewHolder(View view, int position, Transaction item, boolean isSelected, boolean isExpanded, boolean isEtc) {

            ((TextView) view.findViewById(R.id.tvTranDate)).setText(Utils.dateForm(item.getTran_date(), "-"));
            /*((TextView) view.findViewById(R.id.tvInoutType)).setText(item.getInout_type());*/

            // 거래금액
            ((TextView) view.findViewById(R.id.tvTranAmt)).setText(Utils.moneyForm(item.getTran_amt()));

            // 거래후잔액
            /*((TextView) view.findViewById(R.id.tvAfterBalanceAmt)).setText(Utils.moneyForm(item.getAfter_balance_amt()));*/

            // 통장인자내용
            TextView tvPrintContent = (TextView) view.findViewById(R.id.tvPrintContent);
            tvPrintContent.setText(item.getPrint_content());

            // 체크박스
            CheckBox cbSelect = (CheckBox)view.findViewById(R.id.cbSelect);
            cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //set your object's last status
                    cbSelect.setSelected(isChecked);    // setChecked와 setSelected 차이??
                    Toast.makeText(view.getContext(), tvPrintContent+" is "+cbSelect.isChecked(), Toast.LENGTH_SHORT).show();


                    // 리사이클러뷰 페이지 넘겨도 체크한거 유지되도록
                    //adapter.setSelected(position, cbSelect.isSelected());
                    if(cbSelect.isChecked()){

                    }
                }
            });
        }
    }
}
