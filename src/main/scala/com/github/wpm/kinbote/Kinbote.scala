package com.github.wpm.kinbote

import scalax.collection.Graph
import scala.util.matching.Regex

/**
 * Text to be annotated
 */
case class Document(text: String) {
  def content(span: Span): String = text.substring(span.start, span.end)
}

/**
 * Information about a region of a document
 */
abstract class Annotation

/**
 * A region within a document
 */
case class Span(start: Int, end: Int) extends Annotation {
  assert(start <= end)

  def content(span: Span)(implicit document: Document): String = document.content(span)
}

case class AnnotatedDocument(document: Document,
                             annotations: Graph[Annotation, scalax.collection.GraphEdge.DiEdge] = Graph()) {
  def text = document.text

  def ++(as: Traversable[Annotation]) = AnnotatedDocument(document, (annotations /: as)(_ + _))
}

abstract class Annotator {
  def annotate(implicit document: AnnotatedDocument): AnnotatedDocument
}

case class RegularExpressionTokenizer(delimiter: Regex = "\\w+".r) extends Annotator {
  override def annotate(implicit document: AnnotatedDocument): AnnotatedDocument = {
    val tokens = for (m <- delimiter.findAllMatchIn(document.text)) yield Span(m.start, m.end)
    document ++ tokens.toSeq
  }
}


object Kinbote {
  implicit def StringToDocument(text: String) = Document(text)

  implicit def DocumentToAnnotatedDocument(implicit document: Document) = AnnotatedDocument(document)

  def main(args: Array[String]) {
    println(RegularExpressionTokenizer().annotate(AnnotatedDocument("The mouse ran")))
  }
}
