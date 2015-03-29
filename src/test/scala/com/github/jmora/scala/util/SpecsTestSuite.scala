package com.github.jmora.scala.util

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SpecsTestSuite extends Specification {

  val bpt = new SpecsBoilerplateTests

  "The general test suite" in {
    stringToHtmlLinkFragments2("run the tests for the boilerplate") ~ (bpt, "includes boilerplate Tests")
  }
}
