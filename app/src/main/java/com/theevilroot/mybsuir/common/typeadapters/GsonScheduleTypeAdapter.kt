package com.theevilroot.mybsuir.common.typeadapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.theevilroot.mybsuir.common.data.*

object GsonScheduleTypeAdapter : TypeAdapter<ScheduleType>() {
    override fun write(out: JsonWriter, value: ScheduleType) {
        out.value(value.stringValue)
    }

    override fun read(reader: JsonReader): ScheduleType {
        val string = reader.nextString()

        return setOf(Lecture, Practice, Lab)
            .firstOrNull { it.stringValue == string }
            ?: UnknownType(string)
    }
}