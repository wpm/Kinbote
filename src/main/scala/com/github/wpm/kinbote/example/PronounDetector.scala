package com.github.wpm.kinbote.example

import com.github.wpm.kinbote._
import com.github.wpm.kinbote.linguistics._

/**
 * Find pronouns in a document
 *
 * This requires the document to have been tokenized.
 */
class PronounDetector extends Annotator {
  override def annotate(document: Document, annotations: StandoffAnnotation) = {
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

object PronounDetector {
  def apply() = new PronounDetector()
}