package com.github.jmora.scala.util.data.future

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scala.compat.Platform
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeoutException
import com.github.jmora.scala.util.data.future.Lazy.Lazy
import com.github.jmora.scala.util.data.future.Possibly.Possibly

@RunWith(classOf[JUnitRunner])
class PossiblyTests extends Specification {
  val refTime: Long = 1000
  def passtime(): Boolean = { Thread.sleep(refTime); true }

  "Possibly" should {
    "seem to be free" in {
      val initialTime: Long = Platform.currentTime
      val possibleValue = Possibly { passtime }
      val secondTime: Long = Platform.currentTime
      passtime()
      val thirdTime: Long = Platform.currentTime
      val r = possibleValue.value
      val overheadTime = Platform.currentTime - thirdTime + secondTime - initialTime
      overheadTime must beLessThan(refTime)
    }
    "return the right value" in {
      val r = Possibly { passtime }.value
      (r must beSuccessfulTry) && (r.get must beTrue)
    }
    "fail with timeout when implicit" in {
      implicit val timeout: Duration = Duration.create(1, scala.concurrent.duration.MILLISECONDS)
      val r = Possibly { passtime }.value
      (r must beFailedTry) && (r.get must throwAn[TimeoutException])
    }
  }

}
