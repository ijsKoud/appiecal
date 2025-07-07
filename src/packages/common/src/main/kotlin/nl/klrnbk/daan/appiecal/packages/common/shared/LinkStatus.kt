package nl.klrnbk.daan.appiecal.packages.common.shared

enum class LinkStatus(
    val status: String,
) {
    LINKED("linked"),
    NOT_LINKED("not-linked"),
    ;

    companion object {
        fun fromBoolean(isLinked: Boolean) = if (isLinked) LINKED else NOT_LINKED
    }
}
