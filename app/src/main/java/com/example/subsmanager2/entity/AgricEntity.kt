package com.example.subsmanager2.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class AgricWrapper(
    @field:Json(name="row")
    val row: List<AgricEntity>
)

// 오픈뱅킹API에서 가져올 계좌 객체 ===========> 아직 변경 전이라 월별 제철농산물 API 임
@Entity(tableName = "Agric")
data class AgricEntity(
    @PrimaryKey
    @Json(name="IDNTFC_NO")         //품번
    var agricId:String,
    @Json(name="PRDLST_NM")         //품명
    var agricName:String,
    @Json(name="M_DISTCTNS")        //월 (ex. 7월)
    var month:String,
    @Json(name="M_DISTCTNS_ITM")    //월+a (ex. 7월+1,2,3 = 7~10월)
    var monthPlus:String,
    @Json(name="MTC_NM")            //주요산지
    var origin:String,
    @Json(name="EFFECT")            //효능
    var effect:String,
    @Json(name="PURCHASE_MTH")      //구입요령
    var purchaseMethod:String,
    @Json(name="COOK_MTH")          //조리법
    var cookMethod:String,
    @Json(name="TRT_MTH")           //손질요령
    var treatMehtod:String,
    @Json(name="IMG_URL")           //이미지URL
    var imgUrl:String
)