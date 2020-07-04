package com.example.homecookhelper.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class AgricWrapper(
    @Json(name="list")
    val list: List<AgricEntity>
)

//요청변수
//var API_KEY		String
//var TYPE 		String//xml? or json
//var API_URL		String

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