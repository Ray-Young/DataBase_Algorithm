package hw3;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

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
import org.apache.hadoop.mapred.Partitioner;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class ImprovedTriangleCount {
	public static class GroupInputMapper extends MapReduceBase
			implements Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		private Set<String> set = new HashSet<String>();

		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {

			String line = value.toString();
			String[] strs = line.split("\\s+");
			int degree = Integer.parseInt(strs[strs.length - 1]);

			if (strs.length > 1) {
				String primary = strs[0];
				for (int i = 1; i < strs.length - 1; i++) {
					for (int j = i + 1; j < strs.length - 1; j++) {
						String[] tmp = { strs[i], strs[j], primary };
						Arrays.sort(tmp);
						word.set(Arrays.toString(tmp));
						if (!set.contains(word.toString())) {
							output.collect(word, new IntWritable(degree));
							set.add(word.toString());
						}
						output.collect(word, one);
					}
				}
			}
			System.out.println(degree);
		}
	}

	public static class DegreePartitioner extends MapReduceBase implements Partitioner<Text, IntWritable> {

		public int getPartition(Text key, Iterator<IntWritable> values, int numReduceTasks) {
			int degree = values.next().get();

			if (numReduceTasks == 0) {
				return 0;
			}

			if (degree <= 15) {
				return 0;
			} else {
				return 1 % numReduceTasks;
			}
		}

		public int getPartition(Text key, IntWritable value, int numReduceTasks) {
			if (numReduceTasks == 0) {
				return 0;
			}

			Random rd = new Random();
			return rd.nextInt(1) % numReduceTasks;
		}

	}

	public static class CountReducer extends MapReduceBase
			implements Reducer<Text, IntWritable, NullWritable, IntWritable> {
		private final static NullWritable nw = NullWritable.get();
		private int count = 0;

		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<NullWritable, IntWritable> output,
				Reporter reporter) throws IOException {

			int sum = 0;
			values.next();
			while (values.hasNext()) {
				sum += values.next().get();
			}
			if (sum > 1) {
				output.collect(nw, new IntWritable(count++));
			}
		}

	}

	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(ImprovedTriangleCount.class);
		conf.setJobName("Improved Triangle Count");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(NullWritable.class);

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(IntWritable.class);

		conf.setPartitionerClass(DegreePartitioner.class);

		conf.setMapperClass(GroupInputMapper.class);
		conf.setReducerClass(CountReducer.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
	}

}
