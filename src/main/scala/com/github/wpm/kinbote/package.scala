package com.github.wpm


package object kinbote {
  type Document = String

  type Offset = Int

  /**
   * An annotator adds to a document's standoff annotations.
   */
  trait Annotator {
    def annotate(document: Document, annotations: StandoffAnnotation): StandoffAnnotation
  }

  /**
   * A sequence of annotators to apply to a document
   *
   * Each annotator may make reference to the annotations created by the previous annotators.
   * @param annotators sequence of annotators
   */
  case class AnnotatorChain(annotators: Annotator*) {
    def annotate(document: Document): StandoffAnnotation =
      (StandoffAnnotation() /: annotators)((as, a) => a.annotate(document, as))
  }
}
