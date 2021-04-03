package com.theevilroot.mybsuir.common.typeadapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.theevilroot.mybsuir.common.data.SimpleTime
import com.theevilroot.mybsuir.common.data.SimpleTime.Companion.asSimpleTime

object GsonSimpleTimeAdapter : TypeAdapter<SimpleTime>()  {
    override fun write(out: JsonWriter, value: SimpleTime) {
        out.value(value.toString())
    }

    override fun read(`in`: JsonReader): SimpleTime {
        return `in`.nextString().asSimpleTime()
    }

}