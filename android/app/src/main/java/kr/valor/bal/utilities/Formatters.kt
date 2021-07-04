package kr.valor.bal.utilities

fun Long.elapsedTimeFormatter(): String {
    val hour = (this / (1000 * 60 * 60)) % 24
    val min = (this / (1000 * 60)) % 60
    val sec = (this / 1000) % 60

    return String.format("%02d:%02d:%02d", hour, min, sec)
}