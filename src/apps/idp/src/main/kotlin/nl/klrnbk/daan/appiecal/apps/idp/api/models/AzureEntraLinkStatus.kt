package nl.klrnbk.daan.appiecal.apps.idp.api.models

enum class AzureEntraLinkStatus(
    val status: String,
) {
    LINKED("linked"),
    NOT_LINKED("not-linked"),
    ;

    companion object {
        fun fromBoolean(isLinked: Boolean) = if (isLinked) LINKED else NOT_LINKED
    }
}
