package com.github.wpm

import spray.json._
import scala.Some


package object kinbote {
  type Document = String

  type Offset = Int

  trait Annotation {
    def json: JsObject = {
      val fvs: Seq[(String, Any)] = Seq(("name", getClass.getSimpleName)) ++
        getClass.getDeclaredFields.map(field => {
          field setAccessible true
          (field.getName, field.get(this))
        })
      val jfvs = for ((f, v) <- fvs.toSeq if v != None;
                      jv = v match {
                        case Some(x) => JsString(x.toString)
                        case x: Int => JsNumber(x)
                        case x => JsString(x.toString)
                      }) yield f -> jv
      JsObject(jfvs: _*)
    }
  }

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
