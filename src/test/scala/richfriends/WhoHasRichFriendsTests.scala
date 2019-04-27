package richfriends

import org.scalatest._
import Matchers._
import peopleDomain._
import richfriends.WhoHasRichFriends.PersonWithFriendsCash

class WhoHasRichFriendsTests extends SparkWordSpecBase {

  val defaultInput = List("david, 10, petr, josef, andrea",
    "andrea, 50, josef, martin",
    "petr, 20, david, josef",
    "josef, 5, andrea, petr, david",
    "martin, 100, josef, andrea, david")


  "WhoHasRichFriends" when {
    "findLuckyGuy" should {
      "find the users with the richest friends" when {
        "default input" in {
          //Arrange
          val input = FriendInputParser.parseFriendStrings(defaultInput)

          //Act
          val res = WhoHasRichFriends.findLuckyGuy(input, sparkSession)

          //Assert
          res should contain theSameElementsAs Seq(PersonWithFriendsCash(Person("andrea"),105))
        }
        "2 users tied" in {
          //Arrange
          val input = FriendInputParser.parseFriendStrings(
            List("david, 11, david, andrea",
            "andrea, 11, david, andrea")
          )

          //Act
          val res = WhoHasRichFriends.findLuckyGuy(input, sparkSession)

          //Assert
          res should contain theSameElementsAs Seq(
            PersonWithFriendsCash(Person("andrea"),22),
            PersonWithFriendsCash(Person("david"),22)
          )
        }
      }
    }
  }
}
