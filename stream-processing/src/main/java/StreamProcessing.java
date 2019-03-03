
import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.rdd.CassandraTableScanJavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import kafka.serializer.StringDecoder;

import java.io.IOException;
import java.util.*;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

public class StreamProcessing {

    public static void main(String[] args) throws IOException {

        String brokers = "localhost:9092,localhost:9093";
        String topics = "wifilogs";

        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("local[2]");
        sparkConf.setAppName("SparkStreaming");
        sparkConf.set("spark.cassandra.connection.host", "127.0.0.1");

        // Create context with a 10 seconds batch interval

        /*
        JavaSparkContext jsc = new JavaSparkContext(SparkContext.getOrCreate());
        SparkContext.getOrCreate(sparkConf)

        SQLContext sql = new SQLContext(SparkContext.getOrCreate());
*/

        JavaSparkContext ctx = JavaSparkContext.fromSparkContext(SparkContext.getOrCreate(sparkConf));
        JavaStreamingContext jssc = new JavaStreamingContext(ctx, new Duration(10000));
        SQLContext sql = new SQLContext(ctx);

        HashSet<String> topicsSet = new HashSet<>(
                Arrays.asList(topics.split(",")));
        HashMap<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", brokers);

        // Create direct kafka stream with brokers and topics
        JavaPairInputDStream<String, String> messages =
                KafkaUtils.createDirectStream(
                        jssc,
                        String.class,
                        String.class,
                        StringDecoder.class,
                        StringDecoder.class,
                        kafkaParams,
                        topicsSet
                );

        messages.foreachRDD( rdd -> {
            DataFrame dataFrame = sql.read().json(rdd.map(x -> x._2));
            for (Row row : dataFrame.collect()) {

                CassandraTableScanJavaRDD<CassandraRow> previousCounts =
                        javaFunctions(ctx)
                        .cassandraTable("wifi_addressing", "addresses")
                                .select("occurances")
                        .where("mac = '"+row.getAs("mac")+"'");

                        long occurances = 0;

                        if(!previousCounts.isEmpty()) {
                            occurances = previousCounts.first().getLong("occurances") + 1;
                        }
                        //String type, String ssid, String mac, String chnl, String rssi)

                        System.out.println("Type: "+row.getAs("type")  .getClass());
                        System.out.println("ssid: "+row.getAs("ssid")  .getClass());
                        System.out.println("mac:  "+row.getAs("mac")   .getClass());
                        System.out.println("chnl: "+row.getAs("chnl")  .getClass());
                        System.out.println("rssi: "+row.getAs("rssi")  .getClass());

                        List<Device> devices = Arrays.asList(
                                new Device(
                                        row.getAs("type"),
                                        row.getAs("ssid"),
                                        row.getAs("mac"),
                                        row.getAs("chnl"),
                                        row.getAs("rssi"),
                                        occurances)
                        );

                        JavaRDD<Device> newRdd = ctx.parallelize(devices);
                        javaFunctions(newRdd)
                                .writerBuilder("wifi_addressing", "addresses", mapToRow(Device.class))
                                .saveToCassandra();
            }
            dataFrame.show();
            return null;
        });
/*        messages.foreachRDD(rdd -> {
            System.out.println("-- New RDD with "+rdd.partitions().size()+ " partitions and "+rdd.count()+" records");

            rdd.foreach(record -> {
                sql.read().json(record._2);
            });
            return null;
        });*/

        //Start the computation
        jssc.start();
        jssc.awaitTermination();
    }

}
