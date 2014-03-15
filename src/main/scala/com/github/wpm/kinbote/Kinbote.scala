package com.github.wpm.kinbote

/**
 * Text to be annotated
 */
case class Document(text: String) {
  def region(span: Span): String = text.substring(span.begin, span.end)
}

/**
 * Information about a region of a document
 */
abstract class Annotation

/**
 * A region within a document
 */
case class Span(begin: Int, end: Int) extends Annotation {
  assert(begin <= end)
}

// TODO Use http://www.scala-graph.org, annotations:Graph
// If the edges are labeled this graph becomes isomorphic to an AVM.
/**
 * A document is described by a directed graph of annotations
 * @param document document
 * @param annotations annotations describing the document
 * @param edges edges in the annotation graph
 */
case class AnnotatedDocument(document: Document, annotations: Set[Annotation], edges: Set[Pair[Annotation, Annotation]]) {
  def +(annotation: Annotation): AnnotatedDocument = AnnotatedDocument(document, annotations + annotation, edges)

  def +(annotation: Annotation, currentAnnotation: Annotation) {
    assert(annotations.contains(currentAnnotation))
    AnnotatedDocument(document, annotations + annotation, edges + Pair(annotation, currentAnnotation))
  }
}


abstract class Annotator {
  def annotate(document: Document): Set[Pair[Span, Annotation]]
}


object Kinbote {
  def main(args: Array[String]) {
    println("Kinbote")
  }
}
