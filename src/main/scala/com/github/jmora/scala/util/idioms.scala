package com.github.jmora.scala.util

import java.io.Closeable

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

}
