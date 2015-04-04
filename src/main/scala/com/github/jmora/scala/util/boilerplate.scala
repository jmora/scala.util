package com.github.jmora.scala.util

import scala.concurrent.duration.Duration
import scala.concurrent.Future
import scala.util.Try
import scala.concurrent.Await
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.util.Try
import scala.compat.Platform
import scala.language.implicitConversions

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

  // contrary to the previous companion objects, these are just functions that receive blocks
  // (or not even blocks), so they may be expressed as functions (just plain functions)
  // The point here is regularity and elegance, by now I'll delay that decision a bit more
  object Surely {
    def apply[T](futureValue: Future[T])(implicit timeout: Duration = Duration.Inf): T =
      Await.result(futureValue, timeout)

    def apply[T](body: => T): Lazy[T] = {
      val futureValue = Future { body }
      Lazy { Surely(futureValue) }
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

  implicit def future2Present[T](future: Future[T]): T = Await.result(future, Duration.Inf)

}
