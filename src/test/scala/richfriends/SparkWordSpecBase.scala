package richfriends

import org.scalatest._
import org.apache.spark.sql.SparkSession

class SparkWordSpecBase extends WordSpec with BeforeAndAfterAll {

  lazy val sparkSession: SparkSession = SparkSession.builder
    .appName("Test Spark Application")
    .config("spark.master", "local")
    .getOrCreate()

  override protected def beforeAll(): Unit = {
    //Temp solution for my lack of local Env =)
    System.setProperty("hadoop.home.dir", "c:\\\\winutil\\")
    sparkSession.sparkContext.setLogLevel("ERROR")

  }

  override protected def afterAll(): Unit = {
    sparkSession.stop()
  }
}
