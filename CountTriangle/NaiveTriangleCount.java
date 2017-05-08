package hw3;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class NaiveTriangleCount {

	public static class GroupInputMapper extends MapReduceBase
			implements Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {

			String line = value.toString();
			String[] strs = line.split("\\s+");
			if (strs.length > 1) {
				String primary = strs[0];
				for (int i = 1; i < strs.length - 1; i++) {
					for (int j = i + 1; j < strs.length - 1; j++) {
						String[] tmp = { strs[i], strs[j], primary };
						Arrays.sort(tmp);
						word.set(Arrays.toString(tmp));
						output.collect(word, one);
					}
				}
			}
		}
	}

	public static class CountReducer extends MapReduceBase
			implements Reducer<Text, IntWritable, NullWritable, IntWritable> {
		private final static NullWritable nw = NullWritable.get();
		private int count = 0;

		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<NullWritable, IntWritable> output,
				Reporter reporter) throws IOException {

			int sum = 0;
			while (values.hasNext()) {
				sum += values.next().get();
			}
			if (sum > 1) {
				output.collect(nw, new IntWritable(count++));
			}
		}

	}

	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(NaiveTriangleCount.class);
		conf.setJobName("trianglecount");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(NullWritable.class);

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(IntWritable.class);

		conf.setMapperClass(GroupInputMapper.class);
		conf.setReducerClass(CountReducer.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
	}
}
