package com.github.wpm.kinbote


import spray.json._
import com.github.wpm.kinbote.Annotation._

object AnnotationJSONProtocol extends TypedJSONProtocol {

  implicit object AnnotationsFormat extends RootJsonFormat[Annotations] {
    val NODES = "nodes"
    val EDGES = "edges"

    override def write(annotations: Annotations) = {
      val nodes = annotations.g.nodes.map(_.value).toSeq
      val nodeIndex = nodes.view.zipWithIndex.toMap
      val edges = for (edge <- annotations.g.edges; ns = edge.map(n => nodeIndex(n.value))) yield ns
      JsObject(NODES -> nodes.map(_.asInstanceOf[TypedJSONSerializable]).toJson, EDGES -> edges.toJson)
    }

    override def read(json: JsValue) = {
      val nodes = json.asJsObject.getFields(NODES).head
      Annotations(nodes.convertTo[Set[TypedJSONSerializable]].map(_.asInstanceOf[Annotation]))
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
