package com.github.jmora.scala.util

import java.io.Closeable
import org.scalatest.FlatSpec
import com.github.jmora.scala.util.idioms._

class IdiomsSpec extends FlatSpec {

  class ShitHappensException extends Exception {

  }

  class Counter() {
    var count = 0
  }

  class closeableTest(tell: Boolean, counter: Counter) extends Closeable {
    def close(): Unit = {
      counter.count += 1
      if (tell)
        throw new ShitHappensException()
    }
  }

  "using2" should "work" in {
    val counter = new Counter()
    using(new closeableTest(false, counter), new closeableTest(false, counter)) { (c1, c2) =>

    }
    assert(counter.count == 2)
  }
  it should "work no matter what" in {
    val counter = new Counter()
    intercept[ShitHappensException] {
      using(new closeableTest(true, counter), new closeableTest(true, counter)) { (c1, c2) =>
        throw new ShitHappensException()
      }
    }
    assert(counter.count == 2)
  }

  "using3" should "work" in {
    val counter = new Counter()
    using(new closeableTest(false, counter), new closeableTest(false, counter), new closeableTest(false, counter)) {
      (c1, c2, c3) =>
    }
    assert(counter.count == 3)
  }
  it should "work no matter what" in {
    val counter = new Counter()
    intercept[ShitHappensException] {
      using(new closeableTest(true, counter), new closeableTest(true, counter), new closeableTest(true, counter)) {
        (c1, c2, c3) =>
          throw new ShitHappensException()
      }
    }
    assert(counter.count == 3)
  }

}
