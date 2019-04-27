package richfriends

import org.apache.spark.sql.SparkSession

object SimpleApp {
  val input = List("david, 10, petr, josef, andrea",
    "andrea, 50, josef, martin",
    "petr, 20, david, josef",
    "josef, 5, andrea, petr, david",
    "martin, 100, josef, andrea, david")


  def main(args: Array[String]) {
    //Temp solution for my lack of local Env =)
    System.setProperty("hadoop.home.dir", "c:\\\\winutil\\")

    val sparkSession = SparkSession.builder
      .appName("Rich Application")
      .config("spark.master", "local")
      .getOrCreate()
    import sparkSession.implicits._

    val logData = input.toDS
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")
    sparkSession.stop()
  }
}