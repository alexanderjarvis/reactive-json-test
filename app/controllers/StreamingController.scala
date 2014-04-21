package controllers

import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee._
import play.api.libs.json._

import play.extras.iteratees.JsonBodyParser._
import play.extras.iteratees.JsonIteratees._
import play.extras.iteratees.JsonEnumeratees._

trait StreamingController {

  // case class that we will fold the result of the parsing into
  case class Errors(id: Int = 0, errors: List[String] = Nil)

  // Map function that ignores the input, and returns an identity function to leave errors alone
  def ignore[A]: A => Errors => Errors = (_) => identity[Errors]

  // The parser
  val bodyParser = parser(
    // A JSON object enumerator, expecting keys, using the specified parsers for the values of each.
    // Each value gets mapped to a function, that will be used later to fold them into our Errors result.
    jsObject(
      // Handle the exportId as a number, and map it to a function that stores the id in Errors
      "importId" -> jsNumber.map(id => (e: Errors) => Errors(id.value.toInt, e.errors)),
      "importDate" -> jsNullOr(jsString).map(ignore),
      "importUser" -> jsNullOr(jsString).map(ignore),
      // Handle the items as an array, parsing the values as objects, then using enumeratee composition,
      // parse the item, import the item, and finally collect the errors and map them to the function
      // for folding into the Errors result
      "items" -> (jsArray(jsValues(jsSimpleObject)) ><> parseItem ><> importItem
      &>> Iteratee.getChunks[String].map(errorList => (e: Errors) => Errors(e.id, errorList)))
    // Fold the error functions into an Errors result
    ) &>> Iteratee.fold[Errors => Errors, Errors](Errors())((e, f) => f(e))
  )

  type Model

  def parseJson(json: JsObject): Option[Model]

  // Enumeratee that parses a JsObject into an item. Uses a simple mapping Enumeratee.
  def parseItem: Enumeratee[JsObject, Option[Model]] = Enumeratee.map { obj =>
    parseJson(obj)
  }

  // Enumeratee that imports items. Uses an input mapping enumeratee, and only passes a result
  // along if there is an error
  def importItem: Enumeratee[Option[Model], String] = Enumeratee.mapInput(_ match {
    case Input.El(Some(item)) =>
      // send item to writer actor here (not in this test example)
      Input.Empty
    case Input.El(None) => Input.El("An error")
    case other => other.map(_ => "")
  })

}