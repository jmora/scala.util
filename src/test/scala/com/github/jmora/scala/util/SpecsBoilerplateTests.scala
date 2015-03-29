package com.github.jmora.scala.util

import scala.compat.Platform
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import com.github.jmora.scala.util.boilerplate._
import org.specs2.runner.JUnitRunner
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeoutException

@RunWith(classOf[JUnitRunner])
class SpecsBoilerplateTests extends Specification {
  val refTime: Long = 1000
  def passtime(): Boolean = { Thread.sleep(refTime); true }
  // override def is =

  "Lazy" should {
    "not compute when not needed" in {
      val (value, time) = Time { Lazy { passtime } }
      time must beLessThan(refTime)
    }
    "compute when needed" in {
      val lazyValue = Lazy { passtime }
      val (value, time) = Time { lazyValue.value }
      time must beGreaterThanOrEqualTo(refTime)
    }
  }

  "Possibly" should {
    "seem to be free" in {
      val (possibleValue, time1) = Time { Possibly { passtime } }
      passtime()
      val (r, time2) = Time { possibleValue.value }
      (time1 + time2) must beLessThan(refTime)
    }
    "return the right value" in {
      val r = Possibly { passtime }.value
      (r must beSuccessfulTry) && (r.get must beTrue)
    }
    "fail with timeout when implicit" in {
      implicit val timeout: Duration = Duration.create(1, scala.concurrent.duration.MILLISECONDS)
      val r = Possibly { passtime }.value
      (r must beFailedTry) and (r.get must throwAn[TimeoutException])
    }
  }

}
