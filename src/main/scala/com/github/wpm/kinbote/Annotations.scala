package com.github.wpm.kinbote

import scalax.collection.Graph
import scalax.collection.GraphEdge.DiHyperEdge

/**
 * Annotations are a graph of [[Annotation]] objects associated with a [[Document]].
 * @param g annotations of the document
 */
case class Annotations(g: Graph[Annotation, DiHyperEdge] = Graph[Annotation, DiHyperEdge]()) {
  def ++[A <: Annotation](edges: TraversableOnce[DiHyperEdge[A]]): Annotations =
    Annotations(g ++ edges.asInstanceOf[TraversableOnce[DiHyperEdge[Annotation]]])

  def addAnnotations[A <: Annotation](as: TraversableOnce[A]): Annotations = Annotations((g /: as)(_ + _))

  def get[T: reflect.ClassTag]() = g.nodes.map(_.value) collect { case a: T => a}
}

object Annotations {
  def apply(nodes: TraversableOnce[Annotation]): Annotations = Annotations() addAnnotations nodes
}