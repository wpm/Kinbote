package com.github.wpm

// TODO Serialization with Avro

// TODO Type structure that enforces Annotator chain contracts

package object kinbote {
  /**
   * A document
   */
  type Document = String

  /**
   * An offset into a document
   */
  type Offset = Int

  case class LabeledHyperEdge(from: Annotation, to: Set[Annotation], label: Option[String] = None) {
    override def toString = s"$label: $from -> ${to.mkString("{", ",", "}")}"
  }

  trait Annotator {
    def apply(document: Document, analysis: DocumentAnalysis): DocumentAnalysis
  }

  case class AnnotatorChain(annotators: Annotator*) extends Annotator {
    override def apply(document: Document, analysis: DocumentAnalysis = DocumentAnalysis()) =
      (analysis /: annotators) { case (as, a) => a.apply(document, as)}
  }

}
