package com.github.jmora.scala.util.elaborate.tests

class KnapsackBacktrackTest {

  case class Knapsack(elements: Seq[(String, Int)], capacity: Int, inside: Seq[(String, Int)] = Seq())

  def available(k: Knapsack) = k.capacity - k.inside.map(_._2).sum

  val isFinalKS: Knapsack => Boolean = k => k.elements.forall(_._2 > available(k))

  val children: Knapsack => Iterator[Knapsack] = k =>
    k.elements.iterator.filter(_._2 > available(k)).map(x => k.copy(inside = k.inside :+ x))

  val convert = identity _

}
