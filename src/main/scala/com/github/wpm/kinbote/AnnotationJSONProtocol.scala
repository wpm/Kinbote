package com.github.wpm.kinbote


import spray.json._
import com.github.wpm.kinbote.Annotation._
import scalax.collection.GraphEdge.DiHyperEdge

object AnnotationJSONProtocol extends TypedJSONProtocol {

  implicit object AnnotationFormat extends RootJsonFormat[Annotation] {
    override def write(obj: Annotation) = obj.asInstanceOf[TypedJSONSerializable].toJson

    override def read(json: JsValue) = json.convertTo[TypedJSONSerializable].asInstanceOf[Annotation]
  }

  implicit object AnnotationsFormat extends RootJsonFormat[Annotations] {
    val NODES = "nodes"
    val EDGES = "edges"

    override def write(annotations: Annotations) = {
      val nodes = annotations.g.nodes.toSeq.map(_.value)
      val nodeIndex = nodes.view.zipWithIndex.toMap
      val edges = for (edge <- annotations.g.edges; ns = edge.map(n => nodeIndex(n.value))) yield ns
      JsObject(NODES -> nodes.toJson, EDGES -> edges.toJson)
    }

    override def read(json: JsValue) = {
      val fields = json.asJsObject.getFields(NODES, EDGES)
      val nodes = fields(0).convertTo[Seq[Annotation]]
      val edgeIndexes = fields(1).convertTo[Seq[Seq[Int]]]
      val edges = for (eis <- edgeIndexes; edge = eis.map(nodes(_))) yield DiHyperEdge[Annotation](edge)
      Annotations(nodes) ++ edges
    }
  }

  implicit val tokenFormat = jsonFormat2(Token)
  implicit val partOfSpeechFormat = jsonFormat1(PartOfSpeech)

  override def readTypedJson(t: String, json: JsValue) = t match {
    case "Token" => Some(json.convertTo[Token])
    case "PartOfSpeech" => Some(json.convertTo[PartOfSpeech])
    case _ => None
  }
}
