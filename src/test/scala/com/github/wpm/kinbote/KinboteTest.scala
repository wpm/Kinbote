package com.github.wpm.kinbote

import org.scalatest.FunSuite
import Example._
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import com.github.wpm.kinbote.DocumentAnalysis.LabeledHyperEdge

class KinboteTest extends FunSuite {
  val document = "Pat saw Kim. She nodded."
  val analysis = AnnotatorChain(WhitespaceTokenizer(), SentenceSplitter())(document)
  val s0Tokens = List(Token("Pat", 0, 3), Token("saw", 4, 7), Token("Kim", 8, 11), Token(".", 11, 12))
  val s1Tokens = List(Token("She", 13, 16), Token("nodded", 17, 23), Token(".", 23, 24))
  val tokens = s0Tokens.toSet ++ s1Tokens.toSet
  val sentences = Set() + Sentence(0) + Sentence(1)

  test("Tokens should sort by offset") {
    val actualOrder = (Set() ++ s0Tokens ++ s1Tokens).toSeq.sorted
    assert(actualOrder === s0Tokens ++ s1Tokens)
  }

  test("Sentences should sort by sequence number") {
    val actualOrder = sentences.toSeq.sorted
    assert(actualOrder === Seq(Sentence(0), Sentence(1)))
  }

  test("Tokenizer/Sentence splitter should tokenize") {
    val actualTokens = analysis.get[Token]()
    assert(actualTokens === tokens)
  }

  test("Tokenizer/Sentence splitter should find 2 sentences") {
    val actualSentences = analysis.get[Sentence]()
    assert(actualSentences === sentences, s"\nIn $analysis")
  }

  test("Tokenizer/Sentence splitter should create token and sentence annotations") {
    val expectedAnalysis = DocumentAnalysis(tokens ++ sentences, Set() +
      LabeledHyperEdge(Sentence(0), Set() ++ s0Tokens) +
      LabeledHyperEdge(Sentence(1), Set() ++ s1Tokens))
    assert(analysis.nodes === expectedAnalysis.nodes)
    assert(analysis.edges === expectedAnalysis.edges)
    assert(analysis === expectedAnalysis)
  }

  test("Document analysis should be serializable as AVRO") {
    val outputStream = new ByteArrayOutputStream()
    analysis.avro(outputStream)
    val inputStream = new ByteArrayInputStream(outputStream.toByteArray)
    val roundTrip = DocumentAnalysis(inputStream)
    assert(roundTrip === analysis)
  }

  test("Document analysis should be serializable as Json") {
    val roundTrip = DocumentAnalysis(analysis.toJson)
    assert(roundTrip === analysis)
  }
}
