package com.theevilroot.mybsuir.common

import android.text.Editable
import android.text.TextWatcher

class TextWatchWrapper (
    private val onBeforeTextChanged: (CharSequence, Int, Int, Int) -> Unit = { _, _, _, _ -> },
    private val onTextChanged: (CharSequence, Int, Int, Int) -> Unit = { _, _, _, _ -> },
    private val onAfterTextChanged: (Editable?) -> Unit = { }
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        onBeforeTextChanged(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        onTextChanged.invoke(s, start, before, count)
    }

    override fun afterTextChanged(s: Editable?) {
        onAfterTextChanged(s)
    }
}