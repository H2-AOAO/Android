package kr.sesac.aoao.android.common

import android.app.Application
import kr.sesac.aoao.android.dino.model.response.DinoLvResponse
import kr.sesac.aoao.android.common.model.ItemResponse

class GlobalVarApp : Application() {
    val itemList = listOf(
        ItemResponse(id = 1, name = "item_love", price = 30, exp = 120),
        ItemResponse(id = 2, name = "item_food",price = 25, exp = 105),
        ItemResponse(id = 3, name = "item_medicine",price = 35, exp = 130),
        ItemResponse(id = 4, name = "item_play",price = 25, exp = 105),
        ItemResponse(id = 5, name = "item_set",price = 100, exp = 0)
    )

    val dinoList = listOf(
        DinoLvResponse(lv = 1, name = "동글동글 공룡알", exp = 1024),
        DinoLvResponse(lv = 2, name = "아기 다이노", exp = 2048),
        DinoLvResponse(lv = 3, name = "질풍노도 다이노", exp = 4096),
        DinoLvResponse(lv = 4, name = "위풍당당 다이노", exp = 9192),
        DinoLvResponse(lv = 5, name = "완성", exp = 1),
    )
}