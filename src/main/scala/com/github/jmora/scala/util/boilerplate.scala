package com.github.jmora.scala.util

import scala.collection.AbstractIterable
import scala.compat.Platform
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.language.implicitConversions
import scala.util.Try

object boilerplate {

  class Lazy[+T](body: => T) {
    lazy val value: T = body
    def get(): T = value
  }

  object Lazy {
    def apply[T](body: => T): Lazy[T] = new Lazy(body)
    implicit def lazy2Actual[T](lazyValue: Lazy[T]): T = lazyValue.value
  }

  type Possibly[+T] = Lazy[Try[T]]
  object Possibly {
    def apply[U](body: => U)(implicit timeout: Duration = Duration.Inf): Possibly[U] = {
      val futureValue = Future { Try { body } }
      Lazy { Try { Await.result(futureValue, timeout) }.flatten }
    }
  }

  // there may be something already done in the standard library for this
  // and it may change through versions of Scala...
  object Time {
    def apply[T](body: => T): (T, Long) = {
      val init = Platform.currentTime
      val r = body
      val time = Platform.currentTime - init
      (r, time)
    }
  }

  implicit def present2Future[T](present: => T): Future[T] = Future { present }
  implicit def future2Present[T](future: Future[T]): T = Await.result(future, Duration.Inf)

  object Iterable {
    def apply[T](giterator: => Iterator[T]): Iterable[T] =
      new AbstractIterable[T] {
        def iterator = giterator
      }
  }

  val ints = Iterable { Iterator.range(Int.MinValue, Int.MaxValue) }
  val naturals = Iterable { Iterator.range(0, Int.MaxValue) }

}
