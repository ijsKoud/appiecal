package nl.klrnbk.daan.appiecal.apps.calendar.constants

const val GET_PRINCIPAL_XML = """
<propfind xmlns="DAV:">
  <prop>
    <current-user-principal/>
  </prop>
</propfind>
"""

const val GET_CALENDAR_HOME_SET = """
<d:propfind xmlns:d="DAV:" xmlns:cal="urn:ietf:params:xml:ns:caldav" xmlns:cs="http://calendarserver.org/ns/">
  <d:prop>
    <cal:calendar-home-set/>
  </d:prop>
</d:propfind>
"""

const val GET_CALENDAR_LIST = """
<propfind xmlns="DAV:" xmlns:cs="http://calendarserver.org/ns/" xmlns:cal="urn:ietf:params:xml:ns:caldav">
  <prop>
    <displayname/>
    <resourcetype/>
    <cs:getctag/>
    <cal:supported-calendar-component-set/>
  </prop>
</propfind>
"""
