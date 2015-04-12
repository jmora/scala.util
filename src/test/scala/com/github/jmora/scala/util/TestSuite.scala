package com.github.jmora.scala.util

import org.scalatest.Suites
import org.scalatest.Tag

class TestSuite extends Suites(
  new BoilerplateSpec,
  new CoverageReasonsSpec
)
object SlowTest extends Tag("com.github.jmora.tags.SlowTest")
