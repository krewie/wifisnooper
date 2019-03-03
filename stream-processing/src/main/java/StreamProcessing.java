
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import kafka.serializer.StringDecoder;

import java.io.IOException;
import java.util.*;

public class StreamProcessing {

    public static void main(String[] args) throws IOException {

        String brokers = "localhost:9092,localhost:9093";
        String topics = "wifilogs";

        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("local[2]");
        sparkConf.setAppName("SparkStreaming");
        //sparkConf.set("spark.cassandra.connection.host", "127.0.0.1");

        // Create context with a 10 seconds batch interval
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(10));

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

        messages.foreachRDD(rdd -> {
            System.out.println("-- New RDD with "+rdd.partitions().size()+ " partitions and "+rdd.count()+" records");

            rdd.foreach(record -> {
                System.out.println("#1: "+record._1);
                System.out.println("#2: "+record._2);
            });
            return null;
        });

        //Start the computation
        jssc.start();
        jssc.awaitTermination();
    }

}
