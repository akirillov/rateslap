package org.awsm.rscore.appannie

import org.xml.sax.InputSource
import org.awsm.rscore.{ParserResponse, XmlParser}
import org.awsm.rscommons.StatsResponse
import xml.Elem

/**
 * Created by: akirillov
 * Date: 10/23/12
 */

class AppAnnieXMLParser extends XmlParser{
  override def parse(source: Elem) = {



    new ParserResponse(new StatsResponse("2012-10-21",  "My AWESOME App",  "Third world country",  "-1000"), null)
  }
}
