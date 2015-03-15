package com.github.jmora.scala.util.data.future

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.util.Try

import com.github.jmora.scala.util.data.future.Lazy.Lazy

object Possibly {
  type Possibly[+T] = Lazy[Try[T]]
  def apply[T](body: => T)(implicit timeout: Duration = Duration.Inf): Possibly[T] = {
    val futureValue = Future { Try { body } }
    Lazy { Try { Await.result(futureValue, timeout) }.flatten }
  }
}
