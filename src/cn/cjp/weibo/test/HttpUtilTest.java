package cn.cjp.weibo.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class HttpUtilTest {

	public static void main(String[] args) throws Exception {

		testUpPic();
		
	}

	public static void testUpPic() throws Exception {
		String bound = "---------------------------20226268138071";
		File f = new File("C:\\Users\\REAL\\Desktop\\2.png");

		List<byte[]> bts = new ArrayList<byte[]>();

		String ts = "--"
				+ bound
				+ "\r\n"
				+ "Content-Disposition: form-data; name=\"type\""
				+ "\r\n\r\n"
				+ "json"
				+ "\r\n"
				+ "--"
				+ bound
				+ "\r\n"
				+ "Content-Disposition: form-data; name=\"pic\"; filename=\"2.png\""
				+ "\r\n" + "Content-Type: image/png" + "\r\n\r\n";
		bts.add(ts.getBytes());
		ts = "";

		// 写入图片流
		int size = (int) f.length();
		byte[] data = new byte[size];
		FileInputStream fis = new FileInputStream(f);
		fis.read(data, 0, size);
		fis.close();
		bts.add(data);

		ts += "\r\n--" + bound + "--\r\n";
		bts.add(ts.getBytes());
		ts = "";

		HttpUtil httpUtil = new HttpUtil();

		String str = httpUtil.upload(bound, bts);

		System.out.println(str);
	}

	public static void testPubWeibo() {
		HttpUtil httpUtil = new HttpUtil();

		String str = httpUtil
				.pubWeibo("content=Test&picId=da66c124jw1ekz8udekw0j211m0aedhv%2Cda66c124jw1ekz8udekw0j211m0aedhx");

		System.out.println(str);
	}

}
