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

}
