package main;

import java.io.IOException;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkHiveTesting {
	public static void main(String[] args) throws IOException {
		SparkConf sparkConf = new SparkConf()
		.setAppName("SparkHiveTest")
		.set("spark.sql.warehouse.dir", "hdfs://localhost/user/hive/warehouse")
		.set("hive.metastore.uris", "thrift://127.0.0.1:9083")
	    .set("spark.hadoop.javax.jdo.option.ConnectionUserName", "hive")
	    .set("spark.hadoop.javax.jdo.option.ConnectionPassword", "cloudera")
		.setMaster("local[*]");

		SparkSession spark = SparkSession
		  .builder()
		  .appName("SparkHiveTest")
		  .config(sparkConf)
		  .enableHiveSupport()
		  .getOrCreate();
		
//		Dataset<Row> weatherDF = spark.sql("select * from project.weather");
//		Dataset<Row> avgPerDayDF = spark.sql("select * from project.avgPerDay");
		Dataset<Row> avgPerMonthDF = spark.sql("select * from project.avgPerMonth");
		
		avgPerMonthDF.show();
        
		spark.stop();
	}
}
