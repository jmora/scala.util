package com.github.jmora.scala.util.data.future

// Allows passing lazy values to different scopes
object Lazy {
  class Lazy[+T](body: => T) {
    lazy val value: T = body
    def get(): T = value
  }
  def apply[T](body: => T): Lazy[T] = new Lazy(body)
}
