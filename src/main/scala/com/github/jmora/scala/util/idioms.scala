package com.github.jmora.scala.util

import java.io.Closeable

import scala.language.implicitConversions

object idioms {

  def using[T <% Closeable, R](resource: T)(block: (T => R)): R = {
    try {
      block(resource)
    } finally {
      resource.close()
    }
  }

  def using[T <% Closeable, U <% Closeable, R](resource1: T, resource2: U)(block: ((T, U) => R)): R = {
    try {
      block(resource1, resource2)
    } finally {
      try {
        resource1.close
      } finally {
        resource2.close
      }
    }
  }

  // pushing ugliness to the depths of the library
  def using[T <% Closeable, U <% Closeable, V <% Closeable, R](resource1: T, resource2: U, resource3: V)(block: ((T, U, V) => R)): R = {
    try {
      block(resource1, resource2, resource3)
    } finally {
      try {
        resource1.close
      } finally {
        try {
          resource2.close
        } finally {
          resource3.close
        }
      }
    }
  }

  //TODO: this could handle the "using" use cases, and be moved to implicits...
  // or implicits could be moved here, call it sugar, then put boilerplate here too...
  case class AnyWrap[A](wrapped: A) {
    def |>[B](f: A => B): B = f(wrapped)
  }

  // TODO: so you are going to put implicits here now?
  implicit def wrapped[A](a: A): AnyWrap[A] = AnyWrap(a)

}
