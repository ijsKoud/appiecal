package nl.klrnbk.daan.appiecal.apps.calendar.helpers

import okhttp3.HttpUrl.Companion.toHttpUrl
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

fun extractVeventCalendars(xml: String): List<Pair<String, String>> {
    val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val doc = docBuilder.parse(InputSource(StringReader(xml)))

    val responses = nodeListToElementList(doc.getElementsByTagName("response"))
    val calendars =
        responses
            .mapNotNull { it as? Element }
            .mapNotNull { it ->
                val href = it.getElementsByTagName("href").item(0).textContent
                val name = it.getElementsByTagName("displayname").item(0).textContent

                val resourceType = it.getElementsByTagName("resourcetype").item(0) as? Element
                if (resourceType == null) return@mapNotNull null

                val isCalendar = resourceType.getElementsByTagName("calendar").length > 0
                if (!isCalendar) return@mapNotNull null

                val calenderComponentSet = it.getElementsByTagName("supported-calendar-component-set").item(0) as? Element
                if (calenderComponentSet == null) return@mapNotNull null

                val supportsEvents =
                    nodeListToElementList(
                        calenderComponentSet.getElementsByTagName("comp"),
                    ).any { node -> node.attributes.getNamedItem("name")?.textContent == "VEVENT" }

                if (!supportsEvents) return@mapNotNull null

                return@mapNotNull Pair(name, href)
            }

    return calendars
}

fun extractHrefFromPropfindResponse(xml: String): String? {
    val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val doc = docBuilder.parse(InputSource(StringReader(xml)))

    val nodes = doc.getElementsByTagName("href")
    val nodeList =
        nodeListToElementList(nodes)
            .filter { it.attributes.getNamedItem("xmlns")?.textContent == "DAV:" }

    if (nodeList.isEmpty()) return null
    return nodeList.firstOrNull()?.textContent
}

fun nodeListToElementList(nodes: NodeList): List<Node> = (0 until nodes.length).map { nodes.item(it) }

fun hrefToValidUrl(
    href: String,
    baseUrlOrUrl: String,
): String {
    if (href.startsWith("http", ignoreCase = true)) return href
    return baseUrlOrUrl.toHttpUrl().resolve(href).toString()
}
