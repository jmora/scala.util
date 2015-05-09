package com.github.jmora.scala.util.data.collection

import org.scalatest.FlatSpec
import com.github.jmora.scala.util.implicits._
import com.github.jmora.scala.util.boilerplate.Time
import com.github.jmora.scala.util.SlowTest

class IteratorSpec extends FlatSpec {

  val refTime: Long = 1000
  def passtime(e: Int): Int = { Thread.sleep(refTime); e }
  def immediate(): Boolean = { true }

  "A prefetched iterator" should "return the correct results" in {
    assert(Vector(1, 2, 3) == ((1 to 3).iterator.prefetch.prefetch.toVector))
  }
  it should "raise an exception if next is called on an empty iterator" in {
    intercept[java.util.NoSuchElementException] {
      Seq.empty[String].iterator.prefetch.next
    }
  }
  "A proactive iterator" should "return the correct results" in {
    assert(Vector(1, 2, 3) == ((1 to 3).iterator.pro.pro.toVector))
  }
  it should "raise an exception if next is called on an empty iterator" in {
    intercept[java.util.NoSuchElementException] {
      Seq.empty[String].iterator.pro.next
    }
  }
  it should "work properly with blocks" taggedAs (SlowTest) in {
    val pit = { Thread.sleep(refTime * 2); (1 to 3).iterator.map(passtime) }.pro
    Thread.sleep(refTime * 2)
    val (v, t) = Time { pit.next; pit.next }
    assert(t < refTime * 2 && v == 2)
  }
  //  // too slow, even for slowtests...
  // it should "seem to be free" taggedAs (SlowTest) in {
  //    val (r, t) = Time { (1 to 10).iterator.map(passtime).prefetch.map(passtime).pro.map(passtime).toVector }
  //    assert(t < refTime * 15)
  //  }

  "A parallel map" should "have no problems with empty iterations" in {
    assert(Vector() == ((1 until 1).iterator.pmap(x => x).toVector))
  }
  it should "have no problems with single elements" in {
    assert(Vector(1) == ((1 to 1).iterator.pmap(x => x).toVector))
  }
  it should "be fast" taggedAs (SlowTest) in {
    val (r, t) = Time { (1 to 6).iterator.pmap(passtime).toVector }
    assert(t < refTime * 2)
  }
  it should "return correct results" in {
    val end = Runtime.getRuntime.availableProcessors + 10
    println(s"  - BTW, the number of processors is $end")
    assert((1 to end).toVector == ((1 to end).iterator.pmap(x => x).toVector))
  }

}

