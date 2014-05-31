package com.github.wpm.kinbote

import com.gensler.scalavro.types.AvroType
import java.io.{InputStream, OutputStream}
import scala.util.{Failure, Success}
import spray.json.JsValue
import DocumentAnalysis._

/**
 * Analysis of a {{Document}}. This is a labeled directed hypergraph of {{Annotation}} objects.
 * @param nodes nodes in the hypergraph
 * @param edges edges in the hypergraph
 */
case class DocumentAnalysis private(nodes: Set[Annotation] = Set(), edges: Set[LabeledHyperEdge] = Set()) {

  import DocumentAnalysis._

  def addEdge(e: LabeledHyperEdge): DocumentAnalysis = DocumentAnalysis(nodes + e.from ++ e.to, edges + e)

  def addEdges(es: TraversableOnce[LabeledHyperEdge]): DocumentAnalysis = (this /: es)(_.addEdge(_))

  def addAnnotations(ns: TraversableOnce[Annotation]): DocumentAnalysis = DocumentAnalysis(nodes ++ ns, edges)

  def get[T: reflect.ClassTag]() = nodes collect { case a: T => a}

  def toDot: String = {
    val nodeTable: Map[Annotation, Int] = Map() ++ nodes.zipWithIndex
    val ns = nodeTable.map { case (a, i) => s"""\tn$i [label = "$a"]"""}.mkString("\n")
    val es = edges.map(
      edge => s"""\tn${nodeTable(edge.from)} -> ${edge.to.map("n" + nodeTable(_)).mkString("{", ",", "}")}"""
    ).mkString("\n")
    s"digraph {\n$ns\n$es\n}"
  }

  def schema: JsValue = AVRO_TYPE.schema()

  def avro(outputStream: OutputStream) {
    AVRO_TYPE.io.write(this, outputStream)
  }

  def toJson: JsValue = AVRO_TYPE.io.writeJson(this)
}

object DocumentAnalysis {
  val AVRO_TYPE = AvroType[DocumentAnalysis]

  def apply() = new DocumentAnalysis()

  def apply(json: JsValue) = {
    AVRO_TYPE.io.readJson(json) match {
      case Success(analysis) => analysis
      case Failure(cause) => throw cause
    }
  }

  def apply(inputStream: InputStream) = AVRO_TYPE.io.read(inputStream) match {
    case Success(analysis) => analysis
    case Failure(cause) => throw cause
  }

  case class LabeledHyperEdge(from: Annotation, to: Set[Annotation], label: Option[String] = None) {
    override def toString = s"$label: $from -> ${to.mkString("{", ",", "}")}"
  }

}



