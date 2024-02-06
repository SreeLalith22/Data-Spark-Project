package main;

import java.io.IOException;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class SparkHiveCreateTable {
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
		
		StructType schema = DataTypes.createStructType(new StructField[] {
	            DataTypes.createStructField("time_id",  DataTypes.DateType, true),
	            DataTypes.createStructField("ping_ms", DataTypes.DoubleType, true),
	            DataTypes.createStructField("temperature_c", DataTypes.IntegerType, true),
	            DataTypes.createStructField("humidity_p", DataTypes.IntegerType, true)
	    });
		
		Dataset<Row> df = spark.read().option("header", "true").schema(schema).csv("input/data.csv");

		spark.sql("drop table if exists project.weather");
		spark.sql("drop database if exists project");
		spark.sql("create database if not exists project");
		
		df.write().saveAsTable("project.weather");
        
		spark.stop();
	}
}
