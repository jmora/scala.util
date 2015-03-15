package com.github.jmora.scala.util.data.future

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration

import com.github.jmora.scala.util.data.future.Lazy.Lazy

object Surely {
  def apply[T](body: => T)(implicit timeout: Duration = Duration.Inf): Lazy[T] = {
    val futureValue = Future { body }
    Lazy { Await.result(futureValue, timeout) }
  }
}
