package com.github.jmora.scala.util.data.collection

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import com.github.jmora.scala.util.boilerplate._
import scala.collection.AbstractIterator

class ProactiveIterator[A](inner: => Iterator[A])(implicit timeout: Duration = Duration.Inf) extends AbstractIterator[A] {

  lazy val linner = inner
  def pro = this
  private var futureHead: Future[Option[A]] = Future { if (linner.hasNext) Some(linner.next) else None }
  private var attempted = false
  private var actualHead: Option[A] = None

  def hasNext(): Boolean = {
    if (!attempted) {
      attempted = true
      actualHead = futureHead
    }
    actualHead.isDefined
  }

  def next(): A = {
    if (!attempted)
      actualHead = futureHead
    futureHead = Future { if (linner.hasNext) Some(linner.next) else None }
    attempted = false
    if (actualHead.isEmpty)
      throw new java.util.NoSuchElementException("next on empty iterator")
    actualHead.get
  }

}

