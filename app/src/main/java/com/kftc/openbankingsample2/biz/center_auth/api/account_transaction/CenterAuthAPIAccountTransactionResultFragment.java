package com.kftc.openbankingsample2.biz.center_auth.api.account_transaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.subsmanager2.R;
import com.example.subsmanager2.dao.SubsDao;
import com.example.subsmanager2.entity.SubsEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.kftc.openbankingsample2.biz.center_auth.AbstractCenterAuthMainFragment;
import com.kftc.openbankingsample2.biz.center_auth.CenterAuthConst;
import com.kftc.openbankingsample2.biz.center_auth.CenterAuthHomeFragment;
import com.kftc.openbankingsample2.biz.center_auth.api.CenterAuthAPIFragment;
import com.kftc.openbankingsample2.biz.center_auth.http.CenterAuthApiRetrofitAdapter;
import com.kftc.openbankingsample2.common.data.ApiCallAccountTransactionResponse;
import com.kftc.openbankingsample2.common.data.ResMsg;
import com.kftc.openbankingsample2.common.data.Transaction;
import com.kftc.openbankingsample2.common.util.Utils;
import com.kftc.openbankingsample2.common.util.view.recyclerview.KmRecyclerViewDividerHeight;

import java.lang.reflect.Array;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 거래내역조회 결과, 송금인정보조회 결과
 */
public class CenterAuthAPIAccountTransactionResultFragment extends AbstractCenterAuthMainFragment {

    // context
    private Context context;

    // view
    private View view;
    private RecyclerView recyclerView;
    private CenterAuthAPIAccountTransactionResultAdapter adapter;
    private TextView tvTotalRecordCnt;

    // data
    private Bundle args;
    private ApiCallAccountTransactionResponse result;
    private boolean isNextPage = false;         // 다음페이지 존재여부(1페이지당 25건만 조회)
    private String beforInquiryTraceInfo = "";  // 직전조회 추적정보
    private HashMap<String, String> paramMap;
    private String accessToken;
    private int totalRecordCnt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        args = getArguments();
        if (args == null) args = new Bundle();

        result = args.getParcelable("result");                                      // 공유변수 받아오기 (응답받은 변수)
        paramMap = (HashMap<String, String>) args.getSerializable("request");      // 공유변수 받아오기 (요청할 변수)
        accessToken = args.getString(CenterAuthConst.BUNDLE_KEY_ACCESS_TOKEN, "");  // 공유변수 args에서 토큰 추출
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_center_auth_api_account_transaction_result, container, false);
        initView();
        return view;
    }

    void initView() {
        // 상단 정보
        totalRecordCnt = result.getPageRecordCntInt();
        tvTotalRecordCnt = view.findViewById(R.id.tvTotalRecordCnt);
        tvTotalRecordCnt.setText(String.format("%s건", Utils.moneyForm(totalRecordCnt)));

        // 거래내역(반복부)
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new KmRecyclerViewDividerHeight(30));

        view.findViewById(R.id.btnNext).setOnClickListener(v -> goNext());          // 저장
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> onBackPressed()); //

        initData();
    }

    void initData() {

        // 다음 데이터가 더 있는지 확인
        beforInquiryTraceInfo = result.getBefor_inquiry_trace_info();
        isNextPage = result.isNextPage();

        // 리사이클러뷰에 어댑터 설정
        adapter = new CenterAuthAPIAccountTransactionResultAdapter(result.getRes_list());

        // 바닥까지 스크롤하면 다음 데이터 요청
        adapter.setBottomReachedListener(position -> {
            if (isNextPage /*&& !TextUtils.isEmpty(beforInquiryTraceInfo)*/) {
                requestMoreDate();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    // 거래내역조회 요청
    void requestMoreDate() {

        paramMap.put("bank_tran_id", getRandomBankTranId());    // 거래 고유번호 랜덤 입력
        paramMap.put("befor_inquiry_trace_info", beforInquiryTraceInfo);    // 직전정보 조회(다음 데이터 더 있는지 확인용)

        showProgress();
        CenterAuthApiRetrofitAdapter.getInstance()
                .accountTrasactionListFinNum("Bearer " + accessToken, paramMap)
                .enqueue(handleResponse);
    }

    // 결과처리
    Callback<Map> handleResponse = new Callback<Map>() {
        @Override
        public void onResponse(Call<Map> call, Response<Map> response) {
            hideProgress();

            // http ok(200) 아닐경우
            if (!response.isSuccessful()) {
                handleHttpFailure(response);
                return;
            }

            String responseJson = new Gson().toJson(response.body());

            // json 안에 있는 응답코드를 확인
            ResMsg resMsg = new Gson().fromJson(responseJson, ResMsg.class);
            if (!resMsg.isSuccess()) {
                handleApiFailure(resMsg, responseJson);
                return;
            }

            // 응답메시지 파싱
            ApiCallAccountTransactionResponse nextResult =
                    new Gson().fromJson(responseJson, ApiCallAccountTransactionResponse.class);
            isNextPage = nextResult.isNextPage();
            beforInquiryTraceInfo = nextResult.getBefor_inquiry_trace_info();


            // 반복부 파싱
            ArrayList<Transaction> transactionList = nextResult.getRes_list();
            if (transactionList != null) {
                for (Transaction transaction : transactionList) {
                    adapter.addItem(transaction);
                }
                totalRecordCnt += nextResult.getPageRecordCntInt();
            }

            tvTotalRecordCnt.setText(String.format("%s건", Utils.moneyForm(totalRecordCnt)));
        }

        @Override
        public void onFailure(Call<Map> call, Throwable t) {
            hideProgress();
            showAlert("통신실패", "서버 접속에 실패하였습니다.", t.getMessage());
        }
    };

    void goNext() {
        //ArrayList<Transaction> SelectedList = adapter.get(); //new ArrayList<String>();

        //Toast.makeText(view.getContext(), "선택항목 : " + SelectedList.toString(), Toast.LENGTH_LONG);
        Log.d("선택항목", "getSelectedList : "+adapter.getSelectedList().toString());



        // 수정
        // startFragment(CenterAuthAPIFragment.class, null, R.string.fragment_id_center_api_call);
        savePlan();         // result 를 plan에 저장
        activity.finish();  // 구독앱 리스트 호출
    }

    public void onBackPressed() {
        startFragment(CenterAuthHomeFragment.class, null, R.string.fragment_id_center);
    }

    void savePlan() {
        SimpleDateFormat dayFormat = new SimpleDateFormat("d", Locale.KOREA);

        SubsDao subsDao = new SubsDao();
        ArrayList<SubsEntity> subsList = new ArrayList<>();

        // 일단 전체 저장 해보기
        for (Transaction item : adapter.getItemList()) {

            SubsEntity subs = new SubsEntity();
            subs.setSubsName(item.getPrint_content());
            subs.setSubsCustomName(item.getPrint_content());
            subs.setFee(item.getTran_amt());
            //subs.setFeeDate(item.getTran_date());
            subs.setFeeDate(item.getTran_date().substring(6));
            subs.setAlarmYN(false);
            subs.setAlarmDday("1주 전");
            subs.setUserId(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            subsList.add(subs);
        }


        subsDao.insertSubsList(subsList);


        // 선택 저장
        ArrayList<Transaction> SelectedList = adapter.getSelectedItemList(); //new ArrayList<String>();
        Toast.makeText(getContext(), "선택항목 : " + SelectedList.toString(), Toast.LENGTH_LONG);
        for (Transaction item : SelectedList){
            // Firebase 저장

        }



        // 체크된 것만 저장
    }
}
