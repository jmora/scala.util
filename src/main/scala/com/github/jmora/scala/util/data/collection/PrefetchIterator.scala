package com.github.jmora.scala.util.data.collection

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import com.github.jmora.scala.util.boilerplate._

class PrefetchIterator[A](val inner: Iterator[A])(implicit timeout: Duration = Duration.Inf) extends ProactiveIterator[A] {

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

  // not to be mixed with the public methods...
  override protected def lazyHasNext(): Boolean = inner.hasNext || hd.isDefined
  override protected def lazyNext(): A =
    if (hd.isEmpty)
      inner.next
    else {
      val res = hd.get
      hd = None
      res
    }

}
