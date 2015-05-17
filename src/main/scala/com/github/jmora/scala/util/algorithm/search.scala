package com.github.jmora.scala.util.algorithm

import scala.annotation.tailrec
import com.github.jmora.scala.util.data.collection._
import com.github.jmora.scala.util.implicits._

object search {

  // TODO: Hi future-me, first do the tests to check that things work, then wrt style there are a few things to consider:
  // 1. should these be functions or just a trait defined somewhere (e.g. searchable, as it will be common for others)
  // On the functional side => functions, on the OOP => trait, I think OOP wins this time
  // 2. return types, Iterator, GenTraversableOnce, A <% Something??
  // 3. decide what to do wrt tailrec, looks like the compiler is not very clever here.
  // 4. decide what to do wrt ComposableIterator (is unbounded nesting of iterators happening? is that a problem?)
  // 5. decide what to do wrt convert (seems just annoying here, return could be just A)
  // 6. decide what to do with "isGood" (I mean, I could do a filter of the children, but doesn't look like a good idea)
  // 7. BTW: the function that calls rename, should be a closure, it's wrong because I was trying to do the tailrec thing (I think you have already checked closures work as, well... closures)

  def backtracking[A, B](children: A => Iterator[A], isFinal: A => Boolean, convert: A => B, start: A): Iterator[B] = {
    def rename(pending: Iterator[A]): Iterator[A] = recbt(pending)

    @tailrec
    def recbt(pending: Iterator[A]): Iterator[A] =
      if (!pending.hasNext)
        pending
      else {
        val n = pending.next
        if (isFinal(n))
          Seq(n).iterator ++ LazyIterator(() => rename(pending))
        // Seq(n).iterator ++ LazyIterator { rename(pending) }
        else
          recbt(children(n).toIterator ++ pending)
      }

    recbt(Seq(start).iterator).map(convert)
  }

}
