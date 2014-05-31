package com.github.wpm

package object kinbote {
  /**
   * A document
   */
  type Document = String

  /**
   * An offset into a document
   */
  type Offset = Int

  trait Annotator {
    def apply(document: Document, analysis: DocumentAnalysis): DocumentAnalysis
  }

  case class AnnotatorChain(annotators: Annotator*) extends Annotator {
    override def apply(document: Document, analysis: DocumentAnalysis = DocumentAnalysis()) =
      (analysis /: annotators) { case (as, a) => a.apply(document, as)}
  }

}
