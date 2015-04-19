package com.github.jmora.scala.util

import org.scalatest.Suites
import org.scalatest.Tag
import org.scalatest.ParallelTestExecution

class TestSuite extends Suites(
  new BoilerplateSpec,
  new CoverageReasonsSpec,
  new io.SinkSpec,
  new IdiomsSpec
)
object SlowTest extends Tag("com.github.jmora.tags.SlowTest")
