import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;

import scala.Tuple2;
import scala.Tuple3;

public class Streamer {
	private static final double TEMPERATURE_THRESHOLD = 17.0;
	private static final double HUMIDITY_THRESHOLD = 70.0;

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("SparkApp").setMaster(
				"local[*]");
		JavaStreamingContext jssc = new JavaStreamingContext(conf,
				new Duration(15000));

		// JavaDStream<String> lines = jssc.socketTextStream(
		// "localhost", 9999);
		JavaDStream<String> lines = jssc.textFileStream("input/parts");
		JavaDStream<Tuple3<Double, Double, Double>> sensorData = lines.map(
				line -> {
					String[] parts = line.replaceAll("\"", "").split(",");
					double ping = Double.parseDouble(parts[1]);
					double temperature = Double.parseDouble(parts[2]);
					double humidity = Double.parseDouble(parts[3]);
					return new Tuple3<>(ping, temperature, humidity);
				}).filter(
				sensor -> sensor._2() > TEMPERATURE_THRESHOLD
						|| sensor._3() > HUMIDITY_THRESHOLD);
		sensorData.print();
		sensorData.foreachRDD(rdd -> {
			List<Tuple3<Double, Double, Double>> tuples = rdd.collect();
			// Process or print the Double values
				for (Tuple3 tuple: tuples) {
					HBaseSaver.save(Double.parseDouble(tuple._1().toString()), 
							Double.parseDouble(tuple._2().toString()), Double.parseDouble(tuple._3().toString() ));
				}
			});
		jssc.start();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Stopping Spark Streaming Application...");
			jssc.stop(true, true);
			System.out.println("Spark Streaming Application stopped.");
		}));
		jssc.awaitTermination();

	}
}