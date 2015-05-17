package com.github.jmora.scala.util.data.collection

import scala.collection.mutable.ArrayBuffer

// I'm not sure this should be called Iterator, nor composable
class ComposableIterator[+A] extends scala.collection.AbstractIterator[A] {

  type C <: A

  val inner = new ArrayBuffer[Iterator[C]]

  def compose = this

  def +:[B >: A](e: B) = Seq(e).iterator +: inner

  def :+[B >: A](e: B) = inner :+ Seq(e).iterator

  def ++[B >: A](that: ComposableIterator[B]) = inner ++ that.inner

  def ++[B >: A](that: () => Iterator[B]) = inner :+ LazyIterator(that)

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

}
