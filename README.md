### Spark Stream with Java
This repository processes data that includes temperature degrees,
humidity percentages, and ping times for sensor data originating from a room.
It divides the home sensor dataset into chunks and generates new files.
The Spark Streaming job is configured to monitor new file creations and subsequently applies an algorithm to identify
data surpassing a certain threshold.
The identified data is then stored in HBase, along with timestamped records of temperature, humidity, and sensor ping data.

### To run the hbase:
sudo service hbase-master start
sudo service hbase-regionserver start
hbase shell

### To run the project as it is:
-Run HBaseSaver.java program in Eclipse to create HBase table sensor_data. If this data exists it will delete it and create it again.
-Run Streaming.java program in Eclipse to start listening to new file creations . This application will process the data in 15 seconds intervals.Will check the threshold values for temperature and humidity values. 22 celsius degrees for temperature and 70 percent for humidity. Also this program will save the data which is above the threshold to hbase.
-Run FileSplitter.java program in an external console using the commands:
javac FileSplitter.java
java FileSplitter

Necessary jar files needs to be imported

### Jupyter Notebook
https://linuxhostsupport.com/blog/how-to-install-jupyter-on-centos-7/
hbase thrift start
pip install happybase
pip install plotly
pip install pandas
Connect to HBase from jupyter, transform the data and use on the plotly.

![diagram-bdt](https://github.com/utkuaysev/SparkSensorStreaming/assets/33395066/1463ed93-6bfe-4947-aed8-79fcd9e8d6bc)
