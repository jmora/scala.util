package com.github.jmora.scala.util

import scala.language.implicitConversions
import com.github.jmora.scala.util.data.collection.ProactiveIterator
import com.github.jmora.scala.util.data.collection.PrefetchIterator
import com.github.jmora.scala.util.data.collection.ComposableIterator

object implicits {

  implicit def iterator2Proactive[A](it: Iterator[A]): ProactiveIterator[A] = new PrefetchIterator(it)

  implicit def iterator2Composable[A](iterator: Iterator[A]): ComposableIterator[A] = {
    val r = new ComposableIterator
    r ++ iterator
    r
  }

}
