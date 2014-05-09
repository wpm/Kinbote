package com.github.wpm.kinbote

import org.scalatest.FunSuite
import com.github.wpm.kinbote.example.{PronounDetector, RegularExpressionTokenizer}

class StandoffAnnotationTest extends FunSuite {
  test("JSON serialization") {
    val annotator = AnnotatorChain(RegularExpressionTokenizer(), PronounDetector())
    val a = annotator.annotate("He loves himself")
  }
}
