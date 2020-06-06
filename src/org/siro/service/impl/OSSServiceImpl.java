package org.siro.service.impl;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import org.siro.config.OSSConfig;
import org.siro.entity.ImgGet;
import org.siro.service.OSSService;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.UUID;

@Service
public class OSSServiceImpl implements OSSService {

	private String endpoint=OSSConfig.endpoint;;
	private String accessKeyId = OSSConfig.accessKeyId;;
	private String accessKeySecret =OSSConfig.accessKeySecret;
	private String bucketName=OSSConfig.bucketName;

	private OSS oss;
	//private String securityToken = "";

	private void getOSSClient(){
		oss=new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
	}

	public static String getRandomName(String fileName){
		int index=fileName.lastIndexOf(".");
		String ext=fileName.substring(index);//获取后缀名
		return UUID.randomUUID().toString().replace("-","")+ext;
	}
	
	@Override
	public ImgGet getUrl(String fileName) {
		getOSSClient();
		String randomName="ImImg/"+getRandomName(fileName);
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, randomName, HttpMethod.PUT);
		Date expiration = new Date(new Date().getTime() + 30 * 1000);
		request.setExpiration(expiration);
		request.setContentType(URLConnection.guessContentTypeFromName(fileName));
		URL putUrl = oss.generatePresignedUrl(request);

		request = new GeneratePresignedUrlRequest(bucketName, randomName, HttpMethod.GET);
		request.setExpiration(expiration);
		URL getUrl = oss.generatePresignedUrl(request);

		ImgGet imgGet=new ImgGet();
		imgGet.setGet(getUrl.toString());
		imgGet.setName(randomName);
		imgGet.setPut(putUrl.toString());
		return imgGet;
	}
}
