package fuse4j.hadoopfs;
import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;


public class FuseHdfsClientTest {

	@Test
	public void testMain() throws Exception {
		
		try {
			FuseHdfsClient.main(new String[] {});
			Assert.fail("at least two arguments required");
		} catch (IllegalArgumentException e) {
			// good
		}
		
		try {
			FuseHdfsClient.main(new String[] {"ha"});
			Assert.fail("at least two arguments required");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	static Logger LOG = Logger.getLogger(FuseHdfsClientTest.class);
}
