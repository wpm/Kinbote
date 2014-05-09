package com.github.wpm.kinbote

import org.scalatest.FunSuite
import com.github.wpm.kinbote.example.{PronounDetector, RegularExpressionTokenizer}
import AnnotationJsonProtocol._

class StandoffAnnotationTest extends FunSuite {
  test("JSON serialization") {
    val annotator = AnnotatorChain(RegularExpressionTokenizer(), PronounDetector())
    val a = annotator.annotate("He loves himself")
    assert(a === a.toJson.convertTo[StandoffAnnotation])
  }
}
