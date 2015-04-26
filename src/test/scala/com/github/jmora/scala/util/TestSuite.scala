package com.github.jmora.scala.util

import org.scalatest.Suites
import org.scalatest.ParallelTestExecution
import com.github.jmora.scala.util.data.collection.IteratorSpec

class TestSuite extends Suites(
  new CoverageReasonsSpec,
  new io.SinkSpec,
  new IdiomsSpec,
  new IteratorSpec,
  new BoilerplateSpec
)

object SlowTest extends org.scalatest.Tag("com.github.jmora.tags.SlowTest")
