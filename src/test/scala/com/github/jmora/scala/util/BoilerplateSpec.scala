package com.github.jmora.scala.util

import java.util.concurrent.TimeoutException

import scala.concurrent.duration.Duration

import org.scalatest.WordSpec

import com.github.jmora.scala.util.boilerplate.Lazy
import com.github.jmora.scala.util.boilerplate.Possibly
import com.github.jmora.scala.util.boilerplate.Time
import com.github.jmora.scala.util.boilerplate.ints
import com.github.jmora.scala.util.boilerplate.naturals

class BoilerplateSpec extends WordSpec {

  val refTime: Long = 1000
  def passtime(): Boolean = { Thread.sleep(refTime); true }
  def immediate(): Boolean = { true }

  "Lazy values" when {
    "not needed" should {
      "be immediate" in {
        val (value, time) = Time { Lazy { passtime } }
        assert(time < refTime)
      }
    }
    "needed" should {
      "perform the computation" taggedAs (SlowTest) in {
        val lazyValue = Lazy { passtime }
        val (value, time) = Time { lazyValue.value }
        assert(time >= refTime)
      }
      "return the right value" in {
        assert(Lazy { immediate })
      }
    }

  }

  "Possibly" when {
    "given enough time" should {
      "seem to be free" taggedAs (SlowTest) in {
        val (possibleValue, time1) = Time { Possibly { passtime } }
        passtime()
        val (r, time2) = Time { possibleValue.value }
        assert((time1 + time2) < refTime)
      }
      "return the right value" in {
        val r = Possibly { immediate }.value
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

  "Iterables" when {
    "getting values" should {
      "provide them" in {
        assert(naturals.take(3).toVector == (0 to 2).toVector)
      }
    }
    "reusing them" should {
      "provide the same values" in {
        assert(ints.take(3).toVector == ints.take(3).toVector)
      }
    }
  }
}
