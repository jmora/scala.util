package com.github.jmora.scala.util.data.collection

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import com.github.jmora.scala.util.boilerplate._
import scala.collection.AbstractIterator

class PrefetchIterator[A](val inner: Iterator[A])(implicit timeout: Duration = Duration.Inf) extends AbstractIterator[A] {

  def prefetch = this // for extension purposes

  private var hd: Option[Future[A]] = if (inner.hasNext) Some(Future { inner.next }) else None

  def hasNext(): Boolean = hd.isDefined

  def next(): A = {
    val r: A = {
      if (hd.isEmpty)
        throw new java.util.NoSuchElementException("next on empty iterator")
      Await.result(hd.get, timeout)
    }
    hd = if (inner.hasNext) Some(Future { inner.next }) else None
    r
  }

}
