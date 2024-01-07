package com.example.applemarket

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.applemarket.R

enum class InteractionMessage(
    @StringRes val message: Int,
){
    //Common
    CONFIRM(R.string.common_confirm),
    CANCEL(R.string.common_cancel),

    //Dialog
    DELETEPRODUCT(R.string.dialog_del_product),
    DELETEMESSAGE(R.string.dialog_del_again),

    //Notification
    NOTICEKEYWORD(R.string.notice_keyword),
    NOTICEKEYWORDINFO(R.string.notice_keyword_info),
    NOTICEKEYWORDMESSAGE(R.string.notice_keyword_message),

    //Snack bar
    SNACKPLUS(R.string.snack_plus_list)
}

enum class InteractionImage(
    @DrawableRes val img: Int
) {
    CHAT(R.drawable.img_main_chat_16dp)
}