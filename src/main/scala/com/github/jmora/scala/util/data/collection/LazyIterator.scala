package com.github.jmora.scala.util.data.collection

object LazyIterator {
  def apply[A](closure: () => Iterator[A]): Iterator[A] = {
    lazy val inner = closure.apply
    new scala.collection.AbstractIterator[A] {
      def hasNext: Boolean = inner.hasNext
      def next(): A = inner.next
    }
  }
}
