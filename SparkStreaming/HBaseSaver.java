import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
import java.util.UUID;


public class HBaseSaver {
	private static final String TABLE_NAME = "sensor_data";
	private static final String CF_RECORD_NAME = "r";
	private static final String COLUMN_PING= "ping";
	private static final String COLUMN_TEMPERATURE = "temperature";
	private static final String COLUMN_HUMIDITY= "humidity";
	private static final String COLUMN_TIME= "time";

	private static final Configuration config = HBaseConfiguration.create();

	public static void main(String[] args) throws IOException {
		try (Connection connection = ConnectionFactory.createConnection(config);
				Admin admin = connection.getAdmin()) {
			HTableDescriptor creatingTable = new HTableDescriptor(
					TableName.valueOf(TABLE_NAME));
			creatingTable.addFamily(new HColumnDescriptor(CF_RECORD_NAME));

			System.out.print("Creating table.... ");

			if (admin.tableExists(creatingTable.getTableName())) {
				admin.disableTable(creatingTable.getTableName());
				admin.deleteTable(creatingTable.getTableName());
			}
			admin.createTable(creatingTable);

			System.out.println(" Table created!");

		}
	}

	public static void save(double ping, double temperature, double humidity) throws IOException {
		TableName tableName = TableName.valueOf(TABLE_NAME);
		try (Connection connection = ConnectionFactory.createConnection(config);
				Admin admin = connection.getAdmin()) {

			try (Table table = connection.getTable(tableName)) {
				// Create a Put object for the row you want to update
				byte[] cf_record = Bytes.toBytes(CF_RECORD_NAME);
				byte[] rowKey = getCurrentDateTime().getBytes();
				byte[] uniqueKey = UUID.randomUUID().toString().getBytes();

				Put put = new Put(uniqueKey);

				byte[] pColumnQualifier = Bytes.toBytes(COLUMN_PING);
				byte[] pColumnValue = Bytes.toBytes(ping + "");
				byte[] tColumnQualifier = Bytes.toBytes(COLUMN_TEMPERATURE);
				byte[] tColumnValue = Bytes.toBytes(temperature + "");
				byte[] hColumnQualifier = Bytes.toBytes(COLUMN_HUMIDITY);
				byte[] hColumnValue = Bytes.toBytes(humidity + "");
				byte[] columnTime = Bytes.toBytes(COLUMN_TIME);
				byte[] valueTime = getCurrentDateTime().getBytes();

				put.addColumn(cf_record, pColumnQualifier,
						pColumnValue);
				put.addColumn(cf_record, tColumnQualifier,
						tColumnValue);
				put.addColumn(cf_record, hColumnQualifier,
						hColumnValue);
				put.addColumn(cf_record, columnTime,
						valueTime);

				// Put the data into the table
				table.put(put);

			}
			System.out.println("New column added successfully.");
		}
	}
	 public static String getCurrentDateTime() {
	        // Get the current date and time
	        LocalDateTime currentDateTime = LocalDateTime.now();

	        // Define the desired date and time format
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

	        // Format the current date and time using the specified format
	        return currentDateTime.format(formatter);
	    }
	 private static byte[] longToByteArray(long value) {
	        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	        buffer.putLong(value);
	        return buffer.array();
	    }
}
