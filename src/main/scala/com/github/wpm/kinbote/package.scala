package com.github.wpm

package object kinbote {
  type Document = String

  type Offset = Int

  trait Annotation

  type Annotator = (Document, StandoffAnnotation) => StandoffAnnotation

  /**
   * A sequence of annotator objects to apply to a document
   *
   * Each annotator may make reference to the annotations created by the previous annotators.
   * @param annotators sequence of annotators
   */
  case class AnnotatorChain(annotators: Annotator*) {
    def annotate(document: Document): StandoffAnnotation = (StandoffAnnotation() /: annotators)((as, a) => a(document, as))
  }
}
