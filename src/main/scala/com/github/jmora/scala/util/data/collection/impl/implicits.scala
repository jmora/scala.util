package com.github.jmora.scala.util.data.collection.impl

import scala.language.implicitConversions
import com.github.jmora.scala.util.data.collection.ProactiveIterator

object implicits {

  implicit def iterator2Proactive[A](it: Iterator[A]): ProactiveIterator[A] = new PrefetchIterator(it)

}
