package org.awsm.rscore.appannie

import org.awsm.rscore.{XmlParser}
import org.awsm.rscommons.StatsResponse
import xml.{Node, NodeSeq, Elem}

/**
 * Created by: akirillov
 * Date: 10/23/12
 */

class AppAnnieXMLParser extends XmlParser{
  override def parse(source: Elem) = {
    val entries = (source \\ "tr" filter(p  =>  (p \ "@class").text.equals("ranks"))).toList

    //todo: check list and throw exception

    //after that we have such piece of html
    /*
     <tr class="ranks">
                            <td class="appstore">
                              <div>
                                <a href="/app/ios/cut-the-rope/ranking/history/#store_id=143573">
                                  Ghana
                                </a>
                              </div>
                            </td>
                            <td title="Rank #" class="rank down rank-level-500">

            408

                            </td>
     */

    def createPair(node: Node): Pair[String, String] = {
      ((node \\ "a" text).replaceAll("\\n", "").trim(), (node \\ "td" filter(p  =>  (p \ "@title").text.equals("Rank #"))).head.text.replaceAll("\\n", "").trim())
    }

    def createRanks(nodeList: List[Node]): List[Pair[String, String]] = nodeList match {
      case List() => List()
      case nodes => createPair(nodeList.head) :: createRanks(nodeList.tail)
    }
    
    val pairs = createRanks(entries)

    println(pairs)

    pairs
  }
}
