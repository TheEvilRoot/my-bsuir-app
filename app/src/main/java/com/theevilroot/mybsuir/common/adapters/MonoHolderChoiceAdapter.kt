package com.theevilroot.mybsuir.common.adapters

import android.view.View
import androidx.annotation.LayoutRes

class MonoHolderChoiceAdapter <T, VH: ChoiceViewHolder<T>> (
        @LayoutRes
        layoutRes: Int,
        holderFactory: (View) -> VH,
        onSelectChanged: (Int, T?) -> Unit,
): ChoiceAdapter<T, VH, VH, VH>(
        layoutRes,
        layoutRes,
        holderFactory,
        holderFactory,
        onSelectChanged
)