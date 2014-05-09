package com.github.wpm.kinbote

import scalax.collection.Graph
import scalax.collection.GraphEdge.DiEdge
import scalax.collection.GraphPredef._
import spray.json.{JsNumber, JsObject, JsArray}

/**
 * Standoff annotation for a document
 *
 * Document annotation is a directed graph of Annotation objects. The composition of these objects is left up to the
 * annotators that create them.
 * @param g graph of annotations
 */
case class StandoffAnnotation(g: Graph[Annotation, DiEdge] = Graph[Annotation, DiEdge]()) {

  def addNodes[A <: Annotation](nodes: TraversableOnce[Annotation]) = StandoffAnnotation((g /: nodes)(_ + _))

  def addEdge[A <: Annotation](edge: (A, A)) =
    StandoffAnnotation(g + (edge._1 ~> edge._2).asInstanceOf[Param[Annotation, DiEdge]])

  def addEdges[A <: Annotation](edges: TraversableOnce[(A, A)]) =
    StandoffAnnotation(g ++ edges.map(e => (e._1 ~> e._2).asInstanceOf[Param[Annotation, DiEdge]]))

  def annotations[T: reflect.ClassTag]() = g.nodes.map(_.value) collect {
    case a: T => a
  }

  /**
   * The annotation graph is serialized as a list of annotations and a list of pairs of indexes into the annotation
   * list.
   * @return JSON serialization of the annotation graph
   */
  def json: JsObject = {
    val as = g.nodes.map(_.value).toSeq
    val asIndex = as.view.zipWithIndex.toMap
    val jas = as.map(_.asInstanceOf[Annotation].json)
    val jedges = for (
      edge <- g.edges.toSeq;
      e1 = asIndex(edge._1.value);
      e2 = asIndex(edge._2.value);
      jedge = JsArray(JsNumber(e1), JsNumber(e2))
    ) yield jedge
    JsObject("annotations" -> JsArray(jas: _*), "relations" -> JsArray(jedges: _*))
  }

  override def toString: String = json.prettyPrint
}

object StandoffAnnotation {
  def apply[A <: Annotation](annotations: A*): StandoffAnnotation = new StandoffAnnotation().addNodes(annotations)

  def apply[A <: Annotation](nodes: TraversableOnce[A], edges: TraversableOnce[(A, A)]): StandoffAnnotation =
    new StandoffAnnotation().addNodes(nodes).addEdges(edges)
}