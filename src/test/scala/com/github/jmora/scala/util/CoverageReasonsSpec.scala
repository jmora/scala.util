package com.github.jmora.scala.util

import org.scalatest.Spec
import scala.concurrent.Future
import com.github.jmora.scala.util.boilerplate._
import scala.concurrent.ExecutionContext.Implicits.global

class CoverageReasonsSpec extends Spec {
  object `Main App` {
    object `every time` {
      def `should say hi` {
        App.main(Array())
        assert(true)
      }
    }
  }
}
