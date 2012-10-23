package org.awsm.rscore

import org.xml.sax.InputSource
import xml.Elem

/**
 * Created by: akirillov
 * Date: 10/23/12
 */

trait Parser {
  def parse(source: Elem): ParserResponse
}
