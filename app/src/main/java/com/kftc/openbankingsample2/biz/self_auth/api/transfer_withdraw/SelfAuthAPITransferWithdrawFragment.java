package com.kftc.openbankingsample2.biz.self_auth.api.transfer_withdraw;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.subsmanager2.R;
import com.kftc.openbankingsample2.biz.self_auth.AbstractSelfAuthMainFragment;
import com.kftc.openbankingsample2.biz.self_auth.SelfAuthConst;
import com.kftc.openbankingsample2.biz.self_auth.http.SelfAuthApiRetrofitAdapter;
import com.kftc.openbankingsample2.biz.self_auth.util.SelfAuthUtils;
import com.kftc.openbankingsample2.common.Scope;
import com.kftc.openbankingsample2.common.application.AppData;
import com.kftc.openbankingsample2.common.util.TwoString;
import com.kftc.openbankingsample2.common.util.Utils;
import com.kftc.openbankingsample2.common.util.view.KmUtilMoneyEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * 출금이체
 */
public class SelfAuthAPITransferWithdrawFragment extends AbstractSelfAuthMainFragment {

    // context
    private Context context;

    // view
    private View view;

    // data
    private Bundle args;
    private String cntrAccountType;
    private String transferPurpose;

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
        view = inflater.inflate(R.layout.fragment_self_auth_api_transfer_withdraw, container, false);
        initView();
        return view;
    }

    void initView() {

        // 핀테크이용번호 조회 or 계좌번호 조회 선택
        TableRow trFinTechUseNum = view.findViewById(R.id.trFinTechUseNum);
        TableRow trBankCode = view.findViewById(R.id.trBankCode);
        TableRow trAccountNum = view.findViewById(R.id.trAccountNum);
        TableRow trUserSeqNo = view.findViewById(R.id.trUserSeqNo);
        RadioGroup rgFintechAccountType = view.findViewById(R.id.rgFintechAccountType);
        RadioButton rbFintechNum = view.findViewById(R.id.rbFintechNum);
        rgFintechAccountType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbFintechNum:
                    trFinTechUseNum.setVisibility(View.VISIBLE);
                    trBankCode.setVisibility(View.GONE);
                    trAccountNum.setVisibility(View.GONE);
                    trUserSeqNo.setVisibility(View.GONE);
                    break;
                case R.id.rbAccountNum:
                    trFinTechUseNum.setVisibility(View.GONE);
                    trBankCode.setVisibility(View.VISIBLE);
                    trAccountNum.setVisibility(View.VISIBLE);
                    trUserSeqNo.setVisibility(View.VISIBLE);
                    break;
            }
        });

        // access_token : 가장 최근 토큰으로 기본 설정
        EditText etToken = view.findViewById(R.id.etToken);
        etToken.setText(AppData.getSelfAuthAccessToken(Scope.SA));

        // access_token : 기존 토큰에서 선택
        view.findViewById(R.id.btnSelectToken).setOnClickListener(v -> showTokenDialog(etToken, Scope.SA));

        // 은행거래고유번호(20자리)
        EditText etBankTranId = view.findViewById(R.id.etBankTranId);
        setRandomBankTranId(etBankTranId);
        view.findViewById(R.id.btnGenBankTranId).setOnClickListener(v -> setRandomBankTranId(etBankTranId));

        // 약정 계좌/계정 구분 : 드랍다운 메뉴
        List<TwoString> cntrAccountTypeMenuList = new ArrayList<>();
        cntrAccountTypeMenuList.add(new TwoString("N(계좌)", "N"));
        cntrAccountTypeMenuList.add(new TwoString("C(계정)", "C"));
        AppCompatSpinner spCntrAccountType = view.findViewById(R.id.spCntrAccountType);
        ArrayAdapter<TwoString> cntrAccountTypeAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, cntrAccountTypeMenuList);
        spCntrAccountType.setAdapter(cntrAccountTypeAdapter);
        spCntrAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TwoString twoString = cntrAccountTypeAdapter.getItem(position);
                if (twoString != null) {
                    cntrAccountType = twoString.getSecond();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 약정 계좌/계정 번호 : 설정값
        EditText etCntrAccountNum = view.findViewById(R.id.etCntrAccountNum);
        etCntrAccountNum.setText(SelfAuthUtils.getSavedValueFromSetting(SelfAuthConst.SELF_AUTH_CONTRACT_WITHDRAW_ACCOUNT_NUM));

        // 입금계좌인자내역
        EditText etDpsPrintContent = view.findViewById(R.id.etDpsPrintContent);

        // 핀테크이용번호 : 최근 계좌로 기본 설정
        // 계좌번호 : 최근 계좌로 기본 설정
        EditText etFintechUseNum = view.findViewById(R.id.etFintechUseNum);
        etFintechUseNum.setText(Utils.getSavedValue(SelfAuthConst.SELF_AUTH_FINTECH_USE_NUM));
        EditText etBankCode = view.findViewById(R.id.etBankCode);
        etBankCode.setText(Utils.getSavedValue(SelfAuthConst.SELF_AUTH_BANK_CODE));
        EditText etAccountNum = view.findViewById(R.id.etAccountNum);
        etAccountNum.setText(Utils.getSavedValue(SelfAuthConst.SELF_AUTH_ACCOUNT_NUM));

        // 핀테크이용번호 : 기존 계좌에서 선택
        // 계좌번호 : 기존 계좌에서 선택
        View.OnClickListener onClickListener = v -> showAccountDialog(etFintechUseNum, etBankCode, etAccountNum);
        view.findViewById(R.id.btnSelectFintechUseNum).setOnClickListener(onClickListener);
        view.findViewById(R.id.btnSelectBankAccount).setOnClickListener(onClickListener);

        // user_seq_no : 가장 최근 사용자 일련번호로 기본 설정
        EditText etUserSeqNo = view.findViewById(R.id.etUserSeqNo);
        etUserSeqNo.setText(Utils.getSavedValue(SelfAuthConst.SELF_AUTH_USER_SEQ_NO));

        // 거래금액
        KmUtilMoneyEditText moneyTranAmt = view.findViewById(R.id.moneyTranAmt);

        // 요청일시
        EditText etTranDtime = view.findViewById(R.id.etTranDtime);
        etTranDtime.setText(new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date()));

        // 요청고객성명
        EditText etReqClientName = view.findViewById(R.id.etReqClientName);
        etReqClientName.setText(Utils.getSavedValueOrDefault(SelfAuthConst.SELF_AUTH_REQ_CLIENT_NAME, etReqClientName.getText().toString()));

        // 요청고객계좌 개설기관 표준코드
        EditText etReqClientBankCode = view.findViewById(R.id.etReqClientBankCode);
        etReqClientBankCode.setText(Utils.getSavedValueOrDefault(SelfAuthConst.SELF_AUTH_REQ_CLIENT_BANK_CODE, etReqClientBankCode.getText().toString()));

        // 요청고객 계좌번호
        EditText etReqClientAccountNum = view.findViewById(R.id.etReqClientAccountNum);
        etReqClientAccountNum.setText(Utils.getSavedValueOrDefault(SelfAuthConst.SELF_AUTH_REQ_CLIENT_ACCOUNT_NUM, etReqClientAccountNum.getText().toString()));

        // 요청고객 회원번호
        EditText etReqClientNum = view.findViewById(R.id.etReqClientNum);

        // 이체용도
        List<TwoString> transferPurposeMenuList = new ArrayList<>();
        transferPurposeMenuList.add(new TwoString("TR(송금)", "TR"));
        transferPurposeMenuList.add(new TwoString("ST(결제)", "ST"));
        transferPurposeMenuList.add(new TwoString("RC(충전)", "RC"));
        AppCompatSpinner spTransferPurpose = view.findViewById(R.id.spTransferPurpose);
        ArrayAdapter<TwoString> transferPurposeAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, transferPurposeMenuList);
        spTransferPurpose.setAdapter(transferPurposeAdapter);
        spTransferPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TwoString twoString = transferPurposeAdapter.getItem(position);
                if (twoString != null) {
                    transferPurpose = twoString.getSecond();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 하위가맹점명
        EditText etSubFrncName = view.findViewById(R.id.etSubFrncName);

        // 하위가맹점번호
        EditText etSubFrncNum = view.findViewById(R.id.etSubFrncNum);

        // 하위가맹점 사업자등록번호
        EditText etSubFrncBusinessNum = view.findViewById(R.id.etSubFrncBusinessNum);

        // 최종수취고객성명
        EditText etRecvClientName = view.findViewById(R.id.etRecvClientName);

        // 최종수취고객은행
        EditText etRecvClientBankCode = view.findViewById(R.id.etRecvClientBankCode);

        // 최종수취고객계좌
        EditText etRecvClientAccountNum = view.findViewById(R.id.etRecvClientAccountNum);

        // 출금이체 요청
        view.findViewById(R.id.btnNext).setOnClickListener(v -> {

            // 직전내용 저장
            String accessToken = etToken.getText().toString().trim();
            Utils.saveData(SelfAuthConst.SELF_AUTH_ACCESS_TOKEN, accessToken);
            String fintechUseNum = etFintechUseNum.getText().toString();
            Utils.saveData(SelfAuthConst.SELF_AUTH_FINTECH_USE_NUM, fintechUseNum);
            String bankCode = etBankCode.getText().toString();
            Utils.saveData(SelfAuthConst.SELF_AUTH_BANK_CODE, bankCode);
            String accountNum = etAccountNum.getText().toString();
            Utils.saveData(SelfAuthConst.SELF_AUTH_ACCOUNT_NUM, accountNum);
            String userSeqNo = etUserSeqNo.getText().toString();
            Utils.saveData(SelfAuthConst.SELF_AUTH_USER_SEQ_NO, userSeqNo);
            String reqClientName = etReqClientName.getText().toString().trim();
            Utils.saveData(SelfAuthConst.SELF_AUTH_REQ_CLIENT_NAME, reqClientName);
            String reqClientBankCode = etReqClientBankCode.getText().toString().trim();
            Utils.saveData(SelfAuthConst.SELF_AUTH_REQ_CLIENT_BANK_CODE, reqClientBankCode);
            String reqClientAccountNum = etReqClientAccountNum.getText().toString().trim();
            Utils.saveData(SelfAuthConst.SELF_AUTH_REQ_CLIENT_ACCOUNT_NUM, reqClientAccountNum);

            // 요청전문
            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("bank_tran_id", etBankTranId.getText().toString());
            paramMap.put("cntr_account_type", cntrAccountType);
            paramMap.put("cntr_account_num", etCntrAccountNum.getText().toString());
            paramMap.put("dps_print_content", etDpsPrintContent.getText().toString());
            paramMap.put("tran_amt", moneyTranAmt.getTextString());     // 쉼표(,)를 제외하고 추출
            paramMap.put("tran_dtime", etTranDtime.getText().toString());
            paramMap.put("req_client_name", reqClientName);
            paramMap.put("req_client_bank_code", reqClientBankCode);
            paramMap.put("req_client_account_num", reqClientAccountNum);
            paramMap.put("req_client_num", etReqClientNum.getText().toString());
            paramMap.put("transfer_purpose", transferPurpose);
            paramMap.put("sub_frnc_name", etSubFrncName.getText().toString());
            paramMap.put("sub_frnc_num", etSubFrncNum.getText().toString());
            paramMap.put("sub_frnc_bussiness_num", etSubFrncBusinessNum.getText().toString());
            paramMap.put("recv_client_name", etRecvClientName.getText().toString());
            paramMap.put("recv_client_bank_code", etRecvClientBankCode.getText().toString());
            paramMap.put("recv_client_account_num", etRecvClientAccountNum.getText().toString());

            showProgress();

            // 핀테크이용번호 사용
            if (rbFintechNum.isChecked()) {
                paramMap.put("fintech_use_num", fintechUseNum);
                SelfAuthApiRetrofitAdapter.getInstance()
                        .transferWithdrawFinNum("Bearer " + accessToken, paramMap)
                        .enqueue(super.handleResponse("tran_amt", "이체완료!! 이체금액"));
            }

            // 계좌번호 사용
            else {
                paramMap.put("wd_bank_code_std", bankCode);
                paramMap.put("wd_account_num", accountNum);
                paramMap.put("user_seq_no", userSeqNo);
                SelfAuthApiRetrofitAdapter.getInstance()
                        .transferWithdrawAcntNum("Bearer " + accessToken, paramMap)
                        .enqueue(super.handleResponse("tran_amt", "이체완료!! 이체금액"));
            }
        });

        // 취소
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> onBackPressed());

    }

}
