package com.example.homecookhelper.network

import android.util.Log
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/* object 키워드로 NetworkModule 객체를 생성
   - 공공DB 서버에 요청할 OkHttp Request 객체 생성
 */
object NetworkModule {
    private val keyValue = "007c443a70fd3c24f487bbb043a1baea647dac71cc49866181e71cbec8805f47"  // API Key

    /* OKhttp client (실제로 네트워크를 호출하는 부분) 생성 */
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES) // connect timeout // 최근 연결 시간이 길어져 connectTimeout을 2분으로 지정
            .readTimeout(5, TimeUnit.MINUTES) // 최근 읽기 시간이 길어져 readTimeout을 2분으로 지정
            .build()
    }

    /* HttpUrl 객체 정의 함수 선언
       - HttpUrl.Builder()를 이용하여 HttpUrl 객체(URI 구조) 정의
       - 공공DB API 명세에 맞춰 선언
    */
    fun makeHttpUrl(agric: String, month: String): HttpUrl {    //scode: String, date: String, amount: String
        //http://211.237.50.150:7080/openapi/sample/xml/Grid_20171128000000000572_1/1/5

        var builder = HttpUrl.Builder()
            .scheme("http")
            .host("211.237.50.150")
            .port(7080)
            .addPathSegment("openapi")
            .addPathSegment(keyValue)
            .addPathSegment("json")
            .addPathSegment("Grid_20171128000000000572_1")
            .addPathSegment("1")
            .addPathSegment("30")
        if(agric != null && !agric.equals(""))
            builder.addQueryParameter("PRDLST_NM", agric)
        if(month != null && !month.equals(""))
            builder.addQueryParameter("M_DISTCTNS", month)
        return builder.build()
    }//end of makeHttpUrl

    /* OkHttp Request 생성 함수
       - Request.Builder()를 사용하여 요청을 위한 Request 생성
       - url: 요청서버 url
       - get/post: 전송방식
    */
    fun makeHttprequest(httpUrl: HttpUrl): Request {
        return Request.Builder()
            .url(httpUrl.toString())
            .get().build();
    }//end of makeHttprequest
}//end of NetworkModule