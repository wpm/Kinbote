package com.github.wpm.kinbote

import scala.util.matching.Regex
import com.github.wpm.kinbote.linguistics._

object ExampleAnnotators {
  /**
   * Tokenize a document using a regular expression.
   *
   * The default regular expression splits on whitespace and treats single-character punctuation marks as tokens.
   * @param delimiter regular expression that specifies the token delimiter
   * @param document document to tokenize
   * @param annotations incoming annotation set
   * @return annotation set with tokens added
   */
  def regularExpressionTokenizer(delimiter: Regex = "\\w+|[\"'().,?!]".r)
                                (document: Document,
                                 annotations: StandoffAnnotation = StandoffAnnotation()): StandoffAnnotation = {
    annotations.addNodes(delimiter.findAllMatchIn(document).map(m => Token(m.start, m.end)))
  }

  /**
   * Find pronouns in a document
   *
   * This requires the document to have been tokenized.
   *
   * @param document document to analyze
   * @param annotations incoming annotation set
   * @return annotation set with pronoun annotations pointing to tokens
   */
  def pronounDetector(document: Document, annotations: StandoffAnnotation): StandoffAnnotation = {
    import Person._
    import Number._
    import Gender._

    def tagPronoun(token: Token) = token.content(document).toLowerCase match {
      case "i" => Option(Pronoun(Option(first), Option(singular), None))
      case "we" => Option(Pronoun(Option(first), Option(plural), None))
      case "you" => Option(Pronoun(Option(second), None, None))
      case "he" => Option(Pronoun(Option(third), Option(singular), Option(masculine)))
      case "she" => Option(Pronoun(Option(third), Option(singular), Option(feminine)))
      case "they" => Option(Pronoun(Option(third), Option(plural), None))

      case "myself" => Option(ReflexivePronoun(Option(first), Option(singular), None))
      case "ourselves" => Option(ReflexivePronoun(Option(first), Option(plural), None))
      case "yourself" => Option(ReflexivePronoun(Option(second), Option(singular), None))
      case "yourselves" => Option(ReflexivePronoun(Option(second), Option(plural), None))
      case "himself" => Option(ReflexivePronoun(Option(third), Option(singular), Option(masculine)))
      case "herself" => Option(ReflexivePronoun(Option(third), Option(singular), Option(feminine)))
      case "themselves" => Option(ReflexivePronoun(Option(third), Option(plural), None))
      case _ => None
    }

    val tokens = annotations.annotations[Token]()
    val edges = for (token <- tokens;
                     pronoun <- tagPronoun(token);
                     edge = (pronoun, token))
    yield edge
    annotations.addEdges(edges)
  }
}
