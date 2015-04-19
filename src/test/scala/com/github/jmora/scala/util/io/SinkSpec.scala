package com.github.jmora.scala.util.io

import org.scalatest.FunSpec
import java.io.File
import scala.io.Source
import scala.io.Codec
import java.net.URI
import java.io.FileOutputStream

class SinkSpec extends FunSpec {
  val f = new File("ignoreme.txt")
  val text = Seq("Hello world!", "")
  def test(s: Sink) = {
    s.putLines(text)
    val lines = Source.fromFile(f).getLines()
    val res = lines sameElements text.iterator
    f.delete()
    res
  }

  val codec: String = "UTF8"

  describe("A file Sink") {

    describe("when something is put") {
      it("should contain that") {
        assert(test(Sink.toFile(f, codec)))
      }
    }
    it("can be created from a name") {
      assert(test(Sink.toFile(f.getName, codec)))
    }
    it("can be created from a URI") {
      assert(test(Sink.toFile(URI.create("file:///" + f.getAbsolutePath.replace("\\", "/")), codec)))
    }
  }

  describe("A OutputStream Sink") {
    it("should work as a file sink") {
      assert(test(Sink.toStream(new FileOutputStream(f), codec)))
    }
  }

}
