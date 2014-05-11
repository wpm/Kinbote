package com.github.wpm.kinbote


import spray.json._
import com.github.wpm.kinbote.Annotation._

object AnnotationJSONProtocol extends TypedJSONProtocol {

  implicit object AnnotationsFormat extends RootJsonFormat[Annotations] {
    override def write(obj: Annotations) = obj.g.toSeq.map(_.asInstanceOf[TypedJSONSerializable]).toJson

    override def read(json: JsValue) = Annotations(json.convertTo[Set[TypedJSONSerializable]].map(_.asInstanceOf[Annotation]))
  }

  implicit val tokenFormat = jsonFormat2(Token)
  implicit val partOfSpeechFormat = jsonFormat1(PartOfSpeech)

  override def readTypedJson(t: String, json: JsValue) = t match {
    case "Token" => Some(json.convertTo[Token])
    case "PartOfSpeech" => Some(json.convertTo[PartOfSpeech])
    case _ => None
  }
}
