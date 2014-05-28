package com.github.wpm.kinbote

case class DocumentAnalysis(nodes: Set[Annotation] = Set(), edges: Set[LabeledHyperEdge] = Set()) {
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
}


