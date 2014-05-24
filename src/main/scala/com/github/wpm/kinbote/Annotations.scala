package com.github.wpm.kinbote

import scalax.collection.Graph
import scalax.collection.GraphEdge.DiHyperEdge
import scalax.collection.io.dot._
import scalax.collection.io.dot.Indent._
import scalax.collection.io.dot.DotAttr
import scalax.collection.io.dot.Spacing
import scalax.collection.io.dot.DotRootGraph
import scala.Some
import scalax.collection.io.dot.DotNodeStmt
import scalax.collection.io.dot.DotEdgeStmt

/**
 * Annotations are a graph of [[Annotation]] objects associated with a [[Document]].
 * @param g annotations of the document
 */
case class Annotations(g: Graph[Annotation, DiHyperEdge] = Graph[Annotation, DiHyperEdge]()) {
  def ++[A <: Annotation](edges: TraversableOnce[DiHyperEdge[A]]): Annotations =
    Annotations(g ++ edges.asInstanceOf[TraversableOnce[DiHyperEdge[Annotation]]])

  def +[A <: Annotation](edge: DiHyperEdge[A]): Annotations =
    Annotations(g + edge.asInstanceOf[DiHyperEdge[Annotation]])

  def addAnnotations[A <: Annotation](as: TraversableOnce[A]): Annotations = Annotations((g /: as)(_ + _))

  def get[T: reflect.ClassTag]() = g.nodes.map(_.value) collect { case a: T => a}

  def toDot(document: Option[Document] = None): String = {
    val root = DotRootGraph(directed = true, id = None)

    // Build table of all nodes and write it in the header.
    val nodeTable = Map() ++ g.nodes.toSeq.zipWithIndex.map { case (n, i) => n.value -> s"n$i"}

    def nodeTransformer(innerNode: Graph[Annotation, DiHyperEdge]#NodeT): Option[(DotGraph, DotNodeStmt)] = {
      val n = innerNode.value
      Some((root, DotNodeStmt(nodeTable(n), Seq(DotAttr("label", n.toString)))))
    }

    def edgeTransformer(innerEdge: Graph[Annotation, DiHyperEdge]#EdgeT): Option[(DotGraph, DotEdgeStmt)] = {
      val edge = innerEdge.edge
      val ends = edge.tail.size match {
        case 1 => nodeTable(edge.tail.head.value)
        case _ => edge.tail.map(n => nodeTable(n.value)).mkString("{", ",", "}")
      }
      Some(root, DotEdgeStmt(nodeTable(edge.from.value), ends))
    }
    val s = g.toDot(
      root,
      edgeTransformer,
      cNodeTransformer = Some(nodeTransformer),
      iNodeTransformer = Some(nodeTransformer),
      spacing = Spacing(TwoSpaces)
    )
    // Hack to get around the fact that Scala graph will not render multiple edge targets.
    // Changes n1 -> "{n2,n3}" to n1 -> {n2,n3}
    import scala.util.matching.Regex.Match
    val r = """(\w+\d*) -> "(.*)"""".r
    r.replaceAllIn(s, (m: Match) => "%s -> %s" format(m.group(1), m.group(2)))
  }
}

object Annotations {
  def apply[A <: Annotation](nodes: TraversableOnce[A]): Annotations = Annotations() addAnnotations nodes

  def apply[A <: Annotation](nodes: TraversableOnce[A], edges: TraversableOnce[DiHyperEdge[A]]): Annotations =
    Annotations(nodes) ++ edges
}