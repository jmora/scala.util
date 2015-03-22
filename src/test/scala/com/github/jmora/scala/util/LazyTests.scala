package com.github.jmora.scala.util

import scala.compat.Platform
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import com.github.jmora.scala.util.boilerplate._
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LazyTests extends Specification {
  val refTime: Long = 1000
  def passtime(): Boolean = { Thread.sleep(refTime); true }

  "Lazy" should {
    "not compute when not needed" in {
      val previousTime: Long = Platform.currentTime
      val lazyValue = Lazy { passtime }
      (Platform.currentTime - previousTime) must beLessThan(refTime)
    }
    "compute when needed" in {
      val lazyValue = Lazy { passtime }
      val previousTime: Long = Platform.currentTime
      val value = lazyValue.value
      (Platform.currentTime - previousTime) must beGreaterThanOrEqualTo(refTime)
    }
  }
}
