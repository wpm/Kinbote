package com.github.wpm.kinbote

import spray.json._
import com.github.wpm.kinbote.linguistics._
import com.github.wpm.kinbote.linguistics.ReflexivePronoun
import com.github.wpm.kinbote.linguistics.Verb
import com.github.wpm.kinbote.linguistics.Pronoun
import com.github.wpm.kinbote.linguistics.Token
import scala.Some

object AnnotationJsonProtocol extends DefaultJsonProtocol {

  /**
   * An annotation is serialized as a JSON object with a name string field indicating its class name. Optional values
   * are omitted if they are None.
   */
  implicit object AnnotationJsonFormat extends RootJsonFormat[Annotation] {
    def write(a: Annotation) = {
      val fvs: Seq[(String, Any)] = Seq(("name", a.getClass.getSimpleName)) ++
        a.getClass.getDeclaredFields.map(field => {
          field setAccessible true
          (field.getName, field.get(a))
        })
      val jfvs = for ((f, v) <- fvs.toSeq if v != None;
                      jv = v match {
                        case Some(x) => JsString(x.toString)
                        case x: Int => JsNumber(x)
                        case x => JsString(x.toString)
                      }) yield f -> jv
      JsObject(jfvs: _*)
    }

    def read(value: JsValue) = {
      val fields = value.asJsObject.fields
      fields("name").convertTo[String] match {
        case "Token" => Token(fields("start").convertTo[Int], fields("end").convertTo[Int])
        case "Verb" => Verb(Person.unapply(fields), Number.unapply(fields))
        case "Pronoun" => Pronoun(Person.unapply(fields), Number.unapply(fields), Gender.unapply(fields))
        case "ReflexivePronoun" => ReflexivePronoun(Person.unapply(fields), Number.unapply(fields), Gender.unapply(fields))
        case _ => throw new Exception(s"Unrecognized annotation $value")
      }
    }
  }

  /**
   * The annotation graph is serialized as a list of annotations and a list of pairs of indexes into the annotation
   * list.
   */
  implicit object StandoffAnnotationJsonFormat extends RootJsonFormat[StandoffAnnotation] {
    override def read(json: JsValue): StandoffAnnotation = {
      val fields = json.asJsObject.getFields("annotations", "relations")
      val nodes = fields(0).convertTo[Seq[Annotation]]
      val relations = fields(1).convertTo[Seq[Seq[Int]]]
      val edges = for (relation <- relations) yield nodes(relation(0)) -> nodes(relation(1))
      StandoffAnnotation(nodes, edges)
    }

    override def write(obj: StandoffAnnotation): JsValue = {
      val as = obj.g.nodes.map(_.value).toSeq
      val asIndex = as.view.zipWithIndex.toMap
      val jas = as.toJson
      val jedges = for (
        edge <- obj.g.edges.toSeq;
        e1 = asIndex(edge._1.value);
        e2 = asIndex(edge._2.value);
        jedge = JsArray(JsNumber(e1), JsNumber(e2))
      ) yield jedge
      JsObject("annotations" -> jas, "relations" -> JsArray(jedges: _*))
    }
  }

}
