package peopleDomain

object FriendInputParser {
  case class FriendInputParseResult(user: PersonWithCash, friends: Seq[Person])

  def parseFriendStrings(input: List[String]): Seq[FriendInputParseResult] = {
    input.map(row => row.split(", ").toList).map(parseRow)
  }

  private def parseRow(strings: List[String]): FriendInputParseResult = {
    strings match {
      case user :: cash :: friends => FriendInputParseResult(PersonWithCash(Person(user), BigDecimal(cash)), friends.map(Person))
      case _ => throw new InternalError
    }
  }
}
