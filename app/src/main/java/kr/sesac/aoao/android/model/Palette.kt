package kr.sesac.aoao.android.model

import kr.sesac.aoao.android.R

enum class Palette(val colorCode: String, val layoutId: Int) {

    YELLOW("yellow", R.drawable.todo_checked_yellow),
    ORANGE("orange", R.drawable.todo_checked_orange),
    GREEN("green", R.drawable.todo_checked_green),
    SKY_BLUE("sky_blue", R.drawable.todo_checked_sky_blue),
    PINK("pink", R.drawable.todo_checked_pink),
    RED("red", R.drawable.todo_checked_red),
    PURPLE("purple", R.drawable.todo_checked_purple),
    BLUE("blue", R.drawable.todo_checked_blue),
    LIGHT_GREEN("light_green", R.drawable.todo_checked_light_green),
    MINT("mint", R.drawable.todo_checked_mint),
    ;

    companion object {

        fun from(colorCode: String) : Palette {
            return entries.first { palette: Palette -> palette.colorCode == colorCode }
        }
    }
}
