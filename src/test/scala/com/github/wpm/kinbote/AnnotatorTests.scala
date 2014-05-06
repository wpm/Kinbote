package com.github.wpm.kinbote

import org.scalatest.FunSuite
import com.github.wpm.kinbote.linguistics._
import com.github.wpm.kinbote.example.{PronounDetector, RegularExpressionTokenizer}

import Person._
import Number._
import Gender._

class AnnotatorTests extends FunSuite {
  val document = "He loves himself"

  test("Regular expression tokenizer") {
    val actual = RegularExpressionTokenizer().annotate(document)
    val expected = StandoffAnnotation(Token(0, 2), Token(3, 8), Token(9, 16))
    assert(actual === expected)
  }

  test("Pronoun annotator chain") {
    val annotator = AnnotatorChain(RegularExpressionTokenizer(), PronounDetector())
    val actual = annotator.annotate(document)

    val he = Pronoun(Some(third), Some(singular), Some(masculine))
    val himself = ReflexivePronoun(Some(third), Some(singular), Some(masculine))

    val nodes = List(Token(3, 8))
    val edges = List(
      (he, Token(0, 2)),
      (himself, Token(9, 16))
    )
    val expected = StandoffAnnotation(nodes, edges)
    assert(actual === expected)

    val actualTokens = actual.annotations[Token]()
    val expectedTokens = Set(Token(0, 2), Token(3, 8), Token(9, 16))
    assert(actualTokens === expectedTokens)

    val actualPartOfSpeech = actual.annotations[PartOfSpeech]()
    val expectedPartOfSpeech = Set(he, himself)
    assert(actualPartOfSpeech === expectedPartOfSpeech)
  }
}
