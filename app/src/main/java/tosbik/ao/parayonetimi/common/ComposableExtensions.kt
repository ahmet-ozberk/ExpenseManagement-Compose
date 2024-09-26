package tosbik.ao.parayonetimi.common


fun String.addThousandSeparator(): String {
    return this.reversed().chunked(3).joinToString(".").reversed()
}