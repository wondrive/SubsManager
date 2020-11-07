package com.kftc.openbankingsample2.biz.center_auth.api.account_transaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.subsmanager2.R;
import com.google.gson.Gson;
import com.kftc.openbankingsample2.biz.center_auth.AbstractCenterAuthMainFragment;
import com.kftc.openbankingsample2.biz.center_auth.CenterAuthConst;
import com.kftc.openbankingsample2.biz.center_auth.http.CenterAuthApiRetrofitAdapter;
import com.kftc.openbankingsample2.common.Scope;
import com.kftc.openbankingsample2.common.application.AppData;
import com.kftc.openbankingsample2.common.data.ApiCallAccountTransactionResponse;
import com.kftc.openbankingsample2.common.util.TwoString;
import com.kftc.openbankingsample2.common.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * 거래내역조회 요청
 */
public class CenterAuthAPIAccountTransactionRequestFragment extends AbstractCenterAuthMainFragment {

    // context
    private Context context;

    // view
    private View view;

    // data
    private Bundle args;
    private String inquiryType;     // 조회구분코드
    private String inquiryBase;     // 조회기준코드
    private String sortOrder;       // 정렬순서

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
        view = inflater.inflate(R.layout.fragment_center_auth_api_account_transaction_request, container, false);
        initView();
        return view;
    }

    void initView() {

        // access_token : 가장 최근 액세스 토큰으로 기본 설정
        EditText etToken = view.findViewById(R.id.etToken);
        etToken.setText(AppData.getCenterAuthAccessToken(Scope.INQUIRY));

        // access_token : 기존 토큰에서 선택
        view.findViewById(R.id.btnSelectToken).setOnClickListener(v -> showTokenDialog(etToken, Scope.INQUIRY));

        // 은행거래고유번호(20자리)
        EditText etBankTranId = view.findViewById(R.id.etBankTranId);
        setRandomBankTranId(etBankTranId);
        view.findViewById(R.id.btnGenBankTranId).setOnClickListener(v -> setRandomBankTranId(etBankTranId));

        // 핀테크이용번호 : 최근 계좌로 기본 설정
        EditText etFintechUseNum = view.findViewById(R.id.etFintechUseNum);
        etFintechUseNum.setText(Utils.getSavedValue(CenterAuthConst.CENTER_AUTH_FINTECH_USE_NUM));

        // 핀테크이용번호 : 기존 계좌에서 선택
        View.OnClickListener onClickListener = v -> showAccountDialog(etFintechUseNum);
        view.findViewById(R.id.btnSelectFintechUseNum).setOnClickListener(onClickListener);

        // 조회구분코드 드랍다운 메뉴
        List<TwoString> inquiryTypeMenuList = new ArrayList<>();
        inquiryTypeMenuList.add(new TwoString("A(All)", "A"));
        inquiryTypeMenuList.add(new TwoString("I(입금)", "I"));
        inquiryTypeMenuList.add(new TwoString("O(출금)", "O"));
        AppCompatSpinner spInquiryType = view.findViewById(R.id.spInquiryType);
        ArrayAdapter<TwoString> inquiryTypeAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, inquiryTypeMenuList);
        spInquiryType.setAdapter(inquiryTypeAdapter);
        spInquiryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TwoString twoString = inquiryTypeAdapter.getItem(position);
                if (twoString != null) {
                    inquiryType = twoString.getSecond();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 조회기준코드 드랍다운 메뉴
        List<TwoString> inquiryBaseMenuList = new ArrayList<>();
        inquiryBaseMenuList.add(new TwoString("D(일자)", "D"));
        inquiryBaseMenuList.add(new TwoString("T(시간)", "T"));
        AppCompatSpinner spInquiryBase = view.findViewById(R.id.spInquiryBase);
        ArrayAdapter<TwoString> inquiryBaseAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, inquiryBaseMenuList);
        spInquiryBase.setAdapter(inquiryBaseAdapter);
        spInquiryBase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TwoString twoString = inquiryBaseAdapter.getItem(position);
                if (twoString != null) {
                    inquiryBase = twoString.getSecond();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 조회시작일자
        EditText etFromDate = view.findViewById(R.id.etFromDate);
        etFromDate.setText(Utils.getDateString8(-60));

        // 조회시작시간
        EditText etFromTime = view.findViewById(R.id.etFromTime);

        // 조회종료일자
        EditText etToDate = view.findViewById(R.id.etToDate);
        etToDate.setText(Utils.getDateString8(0));

        // 조회종료시간
        EditText etToTime = view.findViewById(R.id.etToTime);

        // 정렬순서
        List<TwoString> sortOrderMenuList = new ArrayList<>();
        sortOrderMenuList.add(new TwoString("D(Descending)", "D"));
        sortOrderMenuList.add(new TwoString("A(Ascending)", "A"));
        AppCompatSpinner spSortOrder = view.findViewById(R.id.spSortOrder);
        ArrayAdapter<TwoString> sortOrderAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, sortOrderMenuList);
        spSortOrder.setAdapter(sortOrderAdapter);
        spSortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TwoString twoString = sortOrderAdapter.getItem(position);
                if (twoString != null) {
                    sortOrder = twoString.getSecond();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 요청일시
        EditText etTranDtime = view.findViewById(R.id.etTranDtime);
        etTranDtime.setText(new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date()));

        // 직전조회추적번호
        EditText etBeforInquiryTraceInfo = view.findViewById(R.id.etBeforInquiryTraceInfo);
        etBeforInquiryTraceInfo.setText("123");

        // 거래내역조회 요청
        view.findViewById(R.id.btnNext).setOnClickListener(v -> {

            // 직전내용 저장
            // 토큰 및 핀테크 이용번호 추출
            String accessToken = etToken.getText().toString().trim();
            Utils.saveData(CenterAuthConst.CENTER_AUTH_ACCESS_TOKEN, accessToken);
            String fintechUseNum = etFintechUseNum.getText().toString();
            Utils.saveData(CenterAuthConst.CENTER_AUTH_FINTECH_USE_NUM, fintechUseNum);

            // 현재 날짜, 한달 전 날짜 포맷
            SimpleDateFormat presentDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
            Calendar fromDateFormat = Calendar.getInstance();
            fromDateFormat.add(Calendar.MONTH, -1);

            // 요청전문
            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("bank_tran_id", etBankTranId.getText().toString());    // 은행거래고유번호
            paramMap.put("fintech_use_num", fintechUseNum);                     // 핀테크 이용 번호
            paramMap.put("inquiry_type", "O");//inquiryType);                          // 쿼리 타입
            paramMap.put("inout_type", "출금");
            paramMap.put("inquiry_base", inquiryBase);                   // 조회 코드
            paramMap.put("from_date", presentDateFormat.format(fromDateFormat.getTime()));//etFromDate.getText().toString().trim());  // 조회 시작 날짜
            paramMap.put("from_time", "000000");//etFromTime.getText().toString().trim());  //   "  시작 시간
            paramMap.put("to_date", presentDateFormat.format(new Date()));//etToDate.getText().toString().trim());      //   "  종료 날짜
            paramMap.put("to_time", "000000");//etToTime.getText().toString().trim());      //   "  종료 시간
            paramMap.put("sort_order", "D");//sortOrder);                              // 정렬 순서
            paramMap.put("tran_dtime", etTranDtime.getText().toString());       // 요청 일시
            paramMap.put("befor_inquiry_trace_info", etBeforInquiryTraceInfo.getText().toString()); // 직전 조회 추적정보

            showProgress();
            CenterAuthApiRetrofitAdapter.getInstance()  // 센터인증 객체
                    .accountTrasactionListFinNum("Bearer " + accessToken, paramMap)
                    .enqueue(super.handleResponse("page_record_cnt", "현재페이지 조회건수", responseJson -> {

                        // 성공하면 결과화면으로 이동
                        ApiCallAccountTransactionResponse result =
                                new Gson().fromJson(responseJson, ApiCallAccountTransactionResponse.class);
                        args.putParcelable("result", result);
                        args.putSerializable("request", paramMap);
                        args.putString(CenterAuthConst.BUNDLE_KEY_ACCESS_TOKEN, accessToken);
                        Log.i("조회 요청", paramMap.toString());
                        goNext();
                    })
            );
        });

        // 취소
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> onBackPressed());
    }

    void goNext() {
        startFragment(CenterAuthAPIAccountTransactionResultFragment.class, args, R.string.fragment_id_api_call_transaction);
    }

}
