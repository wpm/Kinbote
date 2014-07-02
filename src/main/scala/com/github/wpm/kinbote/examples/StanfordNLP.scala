package com.github.wpm.kinbote.examples

import com.github.wpm.kinbote.DocumentAnalysis.LabeledHyperEdge
import com.github.wpm.kinbote._
import edu.arizona.sista.processors.corenlp.CoreNLPProcessor
import edu.arizona.sista.processors.struct.DirectedGraphEdgeIterator

/**
 * Annotator based on the Stanford NLP tools.
 */
class StanfordNLP extends Annotator {
  type StanfordSentence = edu.arizona.sista.processors.Sentence

  protected val stanfordNLPProcessor = new CoreNLPProcessor()

  def apply(document: Document, analysis: DocumentAnalysis = DocumentAnalysis()): DocumentAnalysis = {
    val doc = stanfordNLPProcessor.annotate(document)
    (analysis /: doc.sentences.zipWithIndex) {
      case (as, (sentence, n)) =>
        val tokens = sentenceTokens(document, sentence)
        val tags = sentenceAnnotations(sentence.tags, PartOfSpeech)
        val lemmas = sentenceAnnotations(sentence.lemmas, Lemma)
        val entities = sentenceAnnotations(sentence.entities, Entity)

        // For each token, create a hyper edge with all its properties. Do not add entities tagged as "O" (non-named).
        val tokenInfo = (for (p <- List(tags, lemmas, entities); t <- p) yield t).transpose
        val tokenEdges = tokenInfo.zip(tokens).map {
          case (ti, t) => LabeledHyperEdge(t, ti.toSet - Entity("O"))
        }
        // For each sentence, create an edge for all the tokens it contains.
        val sEdge = LabeledHyperEdge(Sentence(n), tokens.toSet)
        // For each dependency, create a labeled edge from the modifier to the head.
        val depEdges = for (dependencies <- sentence.dependencies.toSeq;
                            depEdge <- new DirectedGraphEdgeIterator[String](dependencies))
        yield LabeledHyperEdge(tokens(depEdge._2), Set(tokens(depEdge._1)), Some(depEdge._3))

        as.addEdge(sEdge).addEdges(tokenEdges).addEdges(depEdges)
    }
  }

  protected def sentenceTokens(document: Document, sentence: StanfordSentence): Seq[Token] =
    sentence.startOffsets.zip(sentence.endOffsets).map {
      case (start, end) => Token(document.substring(start, end), start, end)
    }

  protected def sentenceAnnotations[A <: Annotation](as: Option[Array[String]], f: (String) => A): Option[Seq[A]] =
    for (a <- as) yield a.map(f).toSeq
}

object StanfordNLP {
  def apply() = new StanfordNLP()

  def main(args: Array[String]) {
    val a = StanfordNLP()
    val document = """Thomas Pynchon was born in Glen Cove, New York.
                     |He attended Cornell University.""".stripMargin
    val analysis = a(document)
    println(analysis.toDot)
  }
}
