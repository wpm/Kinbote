package com.github.wpm.kinbote

/**
 * Annotations are a graph of [[Annotation]] objects associated with a [[Document]].
 * @param g annotations of the document
 */
case class Annotations(g: Set[Annotation] = Set.empty) {
  def ++[A <: Annotation](as: TraversableOnce[A]): Annotations = Annotations(g ++ as)

  def get[T: reflect.ClassTag]() = g collect { case a: T => a}
}
