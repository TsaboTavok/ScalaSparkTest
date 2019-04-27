package richfriends

import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import peopleDomain.FriendInputParser.FriendInputParseResult
import peopleDomain._

object WhoHasRichFriends {
  case class PersonWithFriendsCash(person: Person, friendsCash: BigDecimal)

  def findLuckyGuy(input: Seq[FriendInputParseResult], sparkSession: SparkSession): Seq[PersonWithFriendsCash] = {
    import sparkSession.implicits._

    //Firstly data is transformed in to [Friend,FriendsCash,User].
    //Create 2 DataFrames, that will be partitioned by [Friend] and allowing an easy join.
    val cashDF = input.map(i=>(i.user.person.name,i.user.cash)).toDF("friend","cash")
    val friendsDF = input.flatMap(i=>i.friends.map(f=>(f.name,i.user.person.name))).toDF("friend","user")

    //Join to complete the transformation
    val joinedDF  = friendsDF.join(cashDF,"friend")

    //Effective aggregation to create a DataFrame [User, SumFriendsOfCash]
    //Persisted as is has to be used twice.
    val aggregated = joinedDF.groupBy("user").sum("cash").persist()

    //Very tempting to use window function here
    //However this seems to cause aggregation of all data on single node, so its a trap
    //So doing this in 2 steps

    //Find the biggest value of friendsCash, by doing a distributed aggregation
    val maxCashDf = aggregated.groupBy().max("sum(cash)")
    maxCashDf.explain()
    val maxCashRow = maxCashDf.first()

    //Find the friends with the cash value of Max
    val maxUsersDf = aggregated.filter(col("sum(cash)") === lit(maxCashRow(0)))
    maxUsersDf.explain()
    val max1 = maxUsersDf.take(1)(0)

    Seq(PersonWithFriendsCash(Person(max1(0).asInstanceOf[String]), BigDecimal.exact(max1(1).asInstanceOf[java.math.BigDecimal])))
  }
}
