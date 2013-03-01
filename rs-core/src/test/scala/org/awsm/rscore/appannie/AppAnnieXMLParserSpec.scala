package org.awsm.rscore.appannie

import org.specs2.mutable._
import io.Source

/**
 * Created by: akirillov
 * Date: 2/18/13
 */

class AppAnnieXMLParserSpec extends Specification{



  "AppAnnie XML parser" should {
    "parse provided XML data correctly (1)" in {
      val source = Source.fromURL(getClass.getResource("testSet00.xml"))

      val xml = source .mkString
      source.close ()
      val parseResult = new AppAnnieXMLParser().parse(xml)

      parseResult must contain (("Jamaica","61"))
      parseResult must contain (("Austria","48"))
      parseResult must contain (("Sri Lanka","46"))
      parseResult must contain (("Sweden","59"))
      parseResult must contain  (("United Arab Emirates","156"))
    }
    "parse provided XML data correctly (2)" in {
      val source = Source.fromURL(getClass.getResource("testSet01.xml"))

      val xml = source .mkString
      source.close ()
      val parseResult = new AppAnnieXMLParser().parse(xml)

      parseResult must contain (("Mexico","13"))
      parseResult must contain (("Uzbekistan","113"))
      parseResult must contain (("Macau","221"))
      parseResult must contain (("Lithuania","55"))
      parseResult must contain (("Japan","199"))
    }
  }
}
