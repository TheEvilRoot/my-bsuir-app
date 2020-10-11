package com.theevilroot.mybsuir.common.data

import java.lang.RuntimeException

data class InternalException(val msg: String) : RuntimeException()