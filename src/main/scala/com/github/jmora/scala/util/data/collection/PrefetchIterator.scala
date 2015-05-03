package com.github.jmora.scala.util.data.collection

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import com.github.jmora.scala.util.boilerplate._

class PrefetchIterator[A](inner: => Iterator[A])(implicit timeout: Duration = Duration.Inf) extends ProactiveIterator[A] {

  private var hd: Future[Option[A]] = Future { if (inner.hasNext) Some(inner.next) else None }
  private var attempted = false
  private var ahd: Option[A] = None
  private var lazyFirst = true

  def hasNext(): Boolean = {
    if (!attempted) {
      attempted = true
      ahd = hd
    }
    ahd.isDefined
  }

  def next(): A = {
    if (!attempted)
      ahd = hd
    hd = Future { if (inner.hasNext) Some(inner.next) else None }
    attempted = false
    if (ahd.isEmpty)
      throw new java.util.NoSuchElementException("next on empty iterator")
    ahd.get
  }

  // not to be mixed with the public methods...
  override protected def lazyHasNext(): Boolean = if (lazyFirst) this.hasNext else inner.hasNext
  override protected def lazyNext(): A =
    if (lazyFirst) {
      lazyFirst = false
      ahd = hd
      if (ahd.isEmpty)
        throw new java.util.NoSuchElementException("next on empty iterator")
      ahd.get
    } else inner.next

}
