package com.example.subsmanager2.agric

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subsmanager2.entity.AgricEntity
import com.example.subsmanager2.entity.AgricWrapper
import com.example.subsmanager2.network.NetworkModule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.gildor.coroutines.okhttp.await


class AgricViewModel : ViewModel() {

    // JSON 라이브러리 모시(moshi) 플러그인
    val moshi by lazy {
        //data class를 JSON처럼 다룰 수 있도록 해주는 Moshi 플러그인
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    //resultList LiveData 선언
    private val resultList: MutableLiveData<List<AgricEntity>> = MutableLiveData()
    private val resultDetail: MutableLiveData<AgricEntity> = MutableLiveData()

    //resultList() getter 선언
    fun resultList(): LiveData<List<AgricEntity>> = resultList as MutableLiveData<List<AgricEntity>>
    fun resultDetail(): LiveData<AgricEntity> = resultDetail as MutableLiveData<AgricEntity>

    val errorHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("error", exception.message)
//        userNoticeMsg.postValue(exception.message)
    }

    /**/
    //List - Request 객체 생성 함수
    fun loadDataFromURL(
        selectMonth: String,
        selectAgric: String
    ) {
        /* Request 객체 생성 */
        val request = NetworkModule.makeHttprequest(
            /* 분류코드(소분류), 검색일자, 검색수량을 인자로 HttpUrl 객체 생성 함수를 호출하여
               HttpUrl 객체 생성 */
            NetworkModule.makeHttpUrl(agric = selectAgric, month = selectMonth)
        )
        Log.i("HTTP", request.toString())

        /* Coroutine을 이용하여 IO 스레드에서 경락가격정보서비스 서버에 요청  */
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            Log.i("AGRIC", request.url.toString())
            /* response(응답) 객체 - 경락가격정보 검색 요청
               - OkHTTPClient.newCall()의 인자로 Request 객체(request)를 전달하여 실행(요청)
             */
            val response = NetworkModule.client.newCall(request).await()
            /* String을 Moshi를 이용 JSON Body로 파싱 */
            val agricData = response.body?.string()?.let { mappingStringToNews(it) } ?: emptyList()

            //detailAgric = agricData.get(0)
            //resultList(LiveData) 저장
            resultList.postValue(agricData)
        }

    }//end of loadDataFromURL

    // Detail
    fun loadDetailFromURL(selectAgric: String) {
        val request = NetworkModule.makeHttprequest(NetworkModule.makeHttpUrl(agric = selectAgric, month = ""))
        Log.i("HTTP", request.toString())

        // Coroutine을 이용하여 IO 스레드에서 경락가격정보서비스 서버에 요청
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            Log.i("AGRIC", request.url.toString())
            //  - OkHTTPClient.newCall()의 인자로 Request 객체(request)를 전달하여 실행(요청)
            val response = NetworkModule.client.newCall(request).await()
            // String을 Moshi를 이용 JSON Body로 파싱
            val agricData = response.body?.string()?.let { mappingStringToNews(it) } ?: emptyList()
            //detailAgric = agricData.get(0)
            resultDetail.postValue(agricData.get(0))
        }
    }

    //Moshi를 이용 JSON Body로 파싱
    fun mappingStringToNews(jsonBody: String): List<AgricEntity> {
        // json 스트링을 데이터 클래스(AgricWrapper)에 맞게 자동으로 맵핑해주는 어댑터를 생성
        val json_tmp1 = JSONObject(jsonBody)
        val json_tmp2 = json_tmp1.getJSONObject("Grid_20171128000000000572_1")

        Log.i("아님 여기까지 ", "성공인가..?")

        val agricsStringToJsonAdapter = moshi.adapter(AgricWrapper::class.java)
        val agricsResponse = agricsStringToJsonAdapter.fromJson(json_tmp2.toString())
        Log.i(
            "AGRIC_JSON_LIST",
            "${agricsResponse}"
        )//row=[AgricEntity(...), ]

        return agricsResponse?.row ?: emptyList()
    }//end of mapppingStringToNews


    //    private val userNoticeMsg: MutableLiveData<String> = MutableLiveData()
//    fun userNoticeMsg(): LiveData<String> = userNoticeMsg

    //검색 결과를 SaveItem 테이블과 Fresh 테이블에 저장
    /*fun saveResult(context: Context, saveName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            /* SaveItem 테이블에 경락가격정보 저장
              - database Dao를 형태로 가져옵니다.
              - 먼저 저장 항목리스트를 만들어서 autoGenerated된 ID값을 받은다음,
              - freshdata(각각의 개별 데이터)를 리스트 ID값과 함께 저장해줍니다.
            */
            //DatabaseModule.getDatabase(context).agricDao().(
            //SaveItem(id = null, saveTitle = saveName)//saveName: "2020-06-15 사과 검색결과"
            //).run {
            //Fresh 테이블에 경락가격정보 저장
            resultList.value?.let { datas ->
                datas.forEach { it.saveId = this }
                Log.i("SAVERESULT", "datas: $datas")
                /* FreshData(id=null, saveId=1, lname=과일과채류, mname=수박..cprName=동화청과....)
                   FreshData(id=null, saveId=1, lname=과일과채류, mname=수박..cprName=서울청과....)
                   ...
                   ** Fresh table에
                     - id(자동발급), saveId(1), lname=과일과채류, mname=수박..cprName=동화청과...
                     - id(자동발급), saveId(1), lname=과일과채류, mname=수박..cprName=동화청과...
                     .... 저장 됨
                 */
                DatabaseModule.getDatabase(context).freshDao().????(datas)
            }
        }
        }
    }//end of saveResult
*/

}

