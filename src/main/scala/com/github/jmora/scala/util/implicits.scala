package com.github.jmora.scala.util

import scala.language.implicitConversions
import com.github.jmora.scala.util.data.collection.ProactiveIterator
import com.github.jmora.scala.util.data.collection.PrefetchIterator

object implicits {

  implicit def iterator2Proactive[A](it: Iterator[A]): ProactiveIterator[A] = new PrefetchIterator(it)

}
