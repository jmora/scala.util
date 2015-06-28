package com.github.jmora.scala.util.io

import java.io.{ File => JFile, OutputStream, PrintStream }
import java.net.URI

import scala.collection.GenTraversableOnce
import scala.io.Codec

import com.github.jmora.scala.util.idioms._

trait Sink {
  def putLines(lines: GenTraversableOnce[String]): Unit
}

object Sink {

  class FileSink(val file: JFile, val codec: Codec) extends Sink {
    def putLines(lines: GenTraversableOnce[String]): Unit = {
      new JFile(file.getParent) |> { d => d.exists || d.mkdirs }
      using(new PrintStream(file, codec.name)) { ps =>
        lines foreach (ps println _)
        ps.flush
      }
    }
  }

  class StreamSink(val stream: OutputStream, val codec: Codec) extends Sink {
    def putLines(lines: GenTraversableOnce[String]): Unit =
      using(new PrintStream(stream, false, codec.name)) { ps =>
        lines foreach (ps println _)
        ps.flush
      }
  }

  def toFile(name: String)(implicit codec: Codec): Sink =
    new FileSink(new JFile(name), codec)

  def toFile(name: String, enc: String): Sink =
    toFile(name)(Codec(enc))

  def toFile(uri: URI)(implicit codec: Codec): Sink =
    new FileSink(new JFile(uri), codec)

  def toFile(uri: URI, enc: String): Sink =
    toFile(uri)(Codec(enc))

  def toFile(file: JFile)(implicit codec: Codec): Sink =
    new FileSink(file, codec)

  def toFile(file: JFile, enc: String): Sink =
    toFile(file)(Codec(enc))

  def toStream(stream: OutputStream)(implicit codec: Codec): Sink =
    new StreamSink(stream, codec)

  def toStream(stream: OutputStream, enc: String): Sink =
    toStream(stream)(Codec(enc))

}
