package com.github.jmora.scala.util.data.collection

import scala.collection.mutable.ArrayBuffer

// I'm not sure this should be called Iterator, nor composable
class ComposableIterator[+A] extends scala.collection.AbstractIterator[A] {

  type C <: A

  val inner = new ArrayBuffer[Iterator[C]]

  def composable = this

  def +:[B >: A](e: B) = new SingleElementIterator(e) +: inner

  def :+[B >: A](e: B) = inner :+ new SingleElementIterator(e)

  def ++[B >: A](that: ComposableIterator[B]) = inner ++ that.inner

  def ++[B >: A](that: => Iterator[B]) = inner :+ new PrefetchIterator(that)

  def ++[B >: A](that: Iterator[B]) = inner :+ that

  def hasNext: Boolean = {
    while (!(inner.isEmpty || inner.head.hasNext))
      inner remove 0
    !inner.isEmpty
  }

  def next: A = {
    val r = inner.head.next
    while (!(inner.isEmpty || inner.head.hasNext))
      inner remove 0
    r
  }

  /* AUXILIARY CLASSES */

  private class SingleElementIterator[+D](val elem: D) extends scala.collection.AbstractIterator[D] {

    var fresh: Boolean = true

    def hasNext: Boolean = fresh

    def next: D = {
      fresh = false
      elem
    }

  }

}
