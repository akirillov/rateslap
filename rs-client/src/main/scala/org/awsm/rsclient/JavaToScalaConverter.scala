package org.awsm.rsclient

import scala.collection.JavaConversions._

/**
 * Created by: akirillov
 * Date: 2/7/13
 */

object JavaToScalaConverter {

  def convertList[T](input: java.util.List[T]): List[T] = {
    input.toList
  }

  def convertSet[T](input: java.util.Set[T]): Set[T] = {
    input.toSet[T]
  }
}
