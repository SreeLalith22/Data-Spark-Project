package main;

import java.io.IOException;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;

public class SparkHiveManipulations {
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
		
		spark.sql("drop table if exists project.avgPerDay");
		spark.sql("drop table if exists project.avgPerMonth");
		
		Dataset<Row> weatherDF = spark.sql("select * from project.weather");
		
		weatherDF = weatherDF.withColumn("month_id", functions.date_format(weatherDF.col("time_id"), "MMM"));
		weatherDF = weatherDF.withColumn("year_id", functions.date_format(weatherDF.col("time_id"), "YYYY"));
		
		Dataset<Row> avgPerDayDF = weatherDF.groupBy("time_id")
				.agg(functions.avg("temperature_c").cast(DataTypes.IntegerType).as("avgTemperatureC"),
						functions.avg("humidity_p").cast(DataTypes.IntegerType).as("avgHumidityP"));
		
		Dataset<Row> avgPerMonthDF = weatherDF.groupBy("month_id", "year_id")
				.agg(functions.avg("temperature_c").cast(DataTypes.IntegerType).as("avgTemperatureC"),
						functions.avg("humidity_p").cast(DataTypes.IntegerType).as("avgHumidityP"));
		
		avgPerDayDF.write().saveAsTable("project.avgPerDay");
		avgPerMonthDF.write().saveAsTable("project.avgPerMonth");
        
		spark.stop();
	}
}
