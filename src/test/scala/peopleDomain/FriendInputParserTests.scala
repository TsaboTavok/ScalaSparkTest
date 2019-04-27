package peopleDomain

import org.scalatest._
import Matchers._
import peopleDomain.FriendInputParser.FriendInputParseResult

class FriendInputParserTests extends WordSpec {

  "FriendInputParser" when {
    "empty input" should {
      "return empty seq" in {
        //Act
        val result = FriendInputParser.parseFriendStrings(List())

        //Assert
        result shouldBe empty

      }
    }
    "valid rows are parsed" should {
      "return unpivoted data" in {
        //Arrange
        val input = List("david, 10, petr, josef, andrea",
          "andrea, 50, josef, martin")

        //Act
        val result = FriendInputParser.parseFriendStrings(input)

        //Assert
        result should contain theSameElementsAs Seq(
          FriendInputParseResult(PersonWithCash(Person("david"), 10), Seq(Person("petr"), Person("josef"), Person("andrea"))),
          FriendInputParseResult(PersonWithCash(Person("andrea"), 50), Seq(Person("josef"), Person("martin")))
        )

      }
    }
    "invalid rows are parsed" should {
      "crash" when {
        "money is not a number" in {
          //Arrange
          val input = List("andrea, NotMoney, josef, martin",
            "andrea, 50, josef, martin")

          //Act && Assert
          assertThrows[NumberFormatException] { FriendInputParser.parseFriendStrings(input) }

        }
      }
      "input too short" in {
        //Arrange
        val input = List("andrea,",
          "andrea, 50, josef, martin")

        //Act && Assert
        assertThrows[InternalError] { FriendInputParser.parseFriendStrings(input) }

      }
    }
  }
}
