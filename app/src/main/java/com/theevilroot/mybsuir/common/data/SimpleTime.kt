package com.theevilroot.mybsuir.common.data

class SimpleTime (val hours: Int, val minutes: Int, val seconds: Int = 0) {

    val totalMinutes: Int
        get() = hours * 60 + minutes

    fun between(other: SimpleTime): SimpleTime {
        if (other.totalMinutes < totalMinutes)
            return other.between(this)
        return (other.totalMinutes - totalMinutes).minutesAsSimpleTime()
    }

    override fun toString(): String {
        if (seconds == 0)
            return String.format("%02d:%02d", hours, minutes)
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun hashCode(): Int {
        return hours * 60 * 60 + minutes * 60 + seconds;
    }

    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (other !is SimpleTime) return false

        return other.hashCode() == hashCode()
    }

    companion object {

        val empty = SimpleTime(0, 0, 0)

        fun String.asSimpleTime(): SimpleTime {
            val parts = split(":")
            if (parts.size < 2 || parts.size > 3)
                return empty

            val hours = parts[0].toIntOrNull()
                    ?: return empty
            val mins = parts[1].toIntOrNull()
                    ?: return empty
            val seconds = parts.getOrNull(3)?.toIntOrNull()
                    ?: 0

            return SimpleTime(hours, mins, seconds)
        }

        fun Int.minutesAsSimpleTime(): SimpleTime {
            if (this <= 0) return empty
            return SimpleTime(this / 60, this % 60)
        }
    }

}