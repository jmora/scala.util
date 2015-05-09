package com.github.jmora.scala.util

import scala.language.implicitConversions
import com.github.jmora.scala.util.data.collection.ProactiveIterator
import com.github.jmora.scala.util.data.collection.ComposableIterator
import com.github.jmora.scala.util.data.collection.ParallelIterator
import com.github.jmora.scala.util.data.collection.PrefetchIterator

object implicits {

  // implicit def iterator2Proactive[A](that: Iterator[A]): ProactiveIterator[A] = new ProactiveIterator(that)
  implicit def iterator2Proactive[A](that: => Iterator[A]): ProactiveIterator[A] = new ProactiveIterator(that)
  implicit def iterator2Parallel[A](that: Iterator[A]): ParallelIterator[A] = new ParallelIterator(that)
  implicit def iterator2Prefetch[A](that: Iterator[A]): PrefetchIterator[A] = new PrefetchIterator(that)

  implicit def iterator2Composable[A](that: Iterator[A]): ComposableIterator[A] = {
    val r = new ComposableIterator
    r ++ that
    r
  }

}
