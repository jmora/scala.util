package com.github.jmora.scala.util.data.collection

import scala.collection.AbstractIterator

trait ProactiveIterator[+A] extends scala.collection.Iterator[A] {

  def prefetch() = this // for pimping purposes
  def pro() = this
  protected def lazyNext(): A
  protected def lazyHasNext(): Boolean

  def iter(): Iterator[A] =
    ((self: ProactiveIterator[A]) => new AbstractIterator[A] {
      def hasNext: Boolean = self.hasNext
      def next: A = self.next
    })(this)

  // I don't think I'll ever do it, but I should use tasks for this:
  // https://github.com/scala/scala/blob/2.11.x/src/library/scala/collection/parallel/Tasks.scala
  // def pmap[B](f: A => B) = {}

}
