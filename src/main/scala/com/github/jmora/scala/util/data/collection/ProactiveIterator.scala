package com.github.jmora.scala.util.data.collection

import scala.collection.AbstractIterator
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import com.github.jmora.scala.util.boilerplate._

trait ProactiveIterator[+A] extends scala.collection.Iterator[A] {

  def prefetch() = this // for extension purposes
  def pro() = this
  protected def lazyNext(): A
  protected def lazyHasNext(): Boolean

  def iterator(): Iterator[A] =
    ((self: ProactiveIterator[A]) => new AbstractIterator[A] {
      def hasNext: Boolean = self.hasNext
      def next: A = self.next
    })(this)

  // I don't think I'll ever do it, but I should probably use tasks for this:
  // https://github.com/scala/scala/blob/2.11.x/src/library/scala/collection/parallel/Tasks.scala
  // the point here is to mix parallel processing and lazy processing in a map
  def pmap[B](f: A => B): Iterator[B] = {
    val following = new ArrayBuffer[Future[B]]
    for (i <- 1 to Runtime.getRuntime.availableProcessors)
      if (lazyHasNext)
        ((n: A) => following append (f(n)))(lazyNext)
    val self = this
    new AbstractIterator[B] {
      def hasNext: Boolean = !following.isEmpty
      def next: B = {
        if (self.lazyHasNext) {
          val n = self.lazyNext
          following append (f(n))
        }
        following remove 0
      }
    }
  }

}
