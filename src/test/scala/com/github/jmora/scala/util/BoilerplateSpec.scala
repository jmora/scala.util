package com.github.jmora.scala.util

import org.scalatest.WordSpec
import com.github.jmora.scala.util.boilerplate._
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeoutException

class BoilerplateSpec extends WordSpec {
  val refTime: Long = 1000
  def passtime(): Boolean = { Thread.sleep(refTime); true }

  "Lazy values" when {
    "not needed" should {
      "be immediate" in {
        val (value, time) = Time { Lazy { passtime } }
        assert(time < refTime)
      }
    }
    "needed" should {
      "perform the computation" in {
        val lazyValue = Lazy { passtime }
        val (value, time) = Time { lazyValue.value }
        assert(time >= refTime)
      }
    }
  }

  "Possibly" when {
    "given enough time" should {
      "seem to be free" in {
        val (possibleValue, time1) = Time { Possibly { passtime } }
        passtime()
        val (r, time2) = Time { possibleValue.value }
        assert((time1 + time2) < refTime)
      }
      "return the right value" in {
        val r = Possibly { passtime }.value
        assert(r.isSuccess && r.get)
      }
    }
    "not given enough time" should {
      "fail with timeout" in {
        intercept[TimeoutException] {
          implicit val timeout: Duration = Duration.create(10, scala.concurrent.duration.MILLISECONDS)
          val r = Possibly { passtime }.value
          assert(r.isFailure || r.isSuccess)
          assert(r.get)
        }
      }
    }
  }
}
