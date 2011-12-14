package fuse4j.hadoopfs;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.junit.Test;

import fuse.FuseFSDirFiller;


public class MyTest {

	@Test
	public void test() throws Exception {
		FuseHdfsClient hdfs = new FuseHdfsClient("hdfs://localhost:8020");
		FuseFSDirFiller filler = new FuseFSDirFiller();
		filler.setCharset(Charset.forName("utf-8"));
		int dir = hdfs.getdir("/", filler );
		
		LOG.info(dir);
		LOG.info(filler);
		
	}

	static Logger LOG = Logger.getLogger(MyTest.class);
}
