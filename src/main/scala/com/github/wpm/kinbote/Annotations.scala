package com.github.wpm.kinbote

import scalax.collection.Graph
import scalax.collection.GraphEdge.DiEdge

/**
 * Annotations are a graph of [[Annotation]] objects associated with a [[Document]].
 * @param g annotations of the document
 */
case class Annotations(g: Graph[Annotation, DiEdge] = Graph[Annotation, DiEdge]()) {
  def addAnnotations[A <: Annotation](as: TraversableOnce[A]): Annotations = Annotations((g /: as)(_ + _))

  def get[T: reflect.ClassTag]() = g collect { case a: T => a}
}

object Annotations {
  def apply(nodes: TraversableOnce[Annotation]): Annotations = Annotations() addAnnotations nodes
}