package com.github.jmora.scala.util.data.collection.impl

import com.github.jmora.scala.util.data.collection.ProactiveIterator
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import com.github.jmora.scala.util.convenience.nothing

class PrefetchIterator[A](val inner: Iterator[A])(implicit timeout: Duration = Duration.Inf) extends ProactiveIterator[A] {

  private var hd: Option[Future[A]] = if (inner.hasNext) Some(Future { inner.next }) else None

  def hasNext(): Boolean = hd.isDefined

  def next(): A = {
    val r: A = if (hd.isDefined) Await.result(hd.get, timeout) else nothing
    hd = if (inner.hasNext) Some(Future { inner.next }) else None
    r
  }

  // not to be mixed with the public methods...
  override def lazyHasNext(): Boolean = inner.hasNext
  override def lazyNext(): A = inner.next

}
