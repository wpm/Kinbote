package com.github.wpm.kinbote

import org.scalatest.FunSuite
import Example._

class KinboteTest extends FunSuite {
  val document = "Pat saw Kim. She nodded."
  val actualAnalysis = AnnotatorChain(WhitespaceTokenizer(), SentenceSplitter())(document)
  val expectedS0Tokens = Token("Pat", 0, 3) :: Token("saw", 4, 7) :: Token("Kim", 8, 11) :: Token(".", 11, 12) :: Nil
  val expectedS1Tokens = Token("She", 13, 16) :: Token("nodded", 17, 23) :: Token(".", 23, 24) :: Nil
  val expectedTokens = Set() ++ expectedS0Tokens ++ expectedS1Tokens
  val expectedSentences = Set() + Sentence(0) + Sentence(1)

  test("Tokenizer/Sentence splitter should tokenize") {
    val actualTokens = actualAnalysis.get[Token]()
    assert(actualTokens === expectedTokens)
  }

  test("Tokenizer/Sentence splitter should find 2 sentences") {
    val actualSentences = actualAnalysis.get[Sentence]()
    assert(actualSentences === expectedSentences, s"\nIn $actualAnalysis")
  }

  test("Tokenizer/Sentence splitter should create token and sentence annotations") {
    val expectedAnalysis = DocumentAnalysis(expectedTokens ++ expectedSentences, Set() +
      LabeledHyperEdge(Sentence(0), Set() ++ expectedS0Tokens) +
      LabeledHyperEdge(Sentence(1), Set() ++ expectedS1Tokens))
    assert(actualAnalysis.nodes === expectedAnalysis.nodes)
    assert(actualAnalysis.edges === expectedAnalysis.edges)
    assert(actualAnalysis === expectedAnalysis)
  }
}
