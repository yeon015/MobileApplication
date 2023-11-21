package mobile.example.network.downloadhttp.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends Activity {

	EditText etUrl;
	TextView tvResult;
	ImageView imageView;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        etUrl = findViewById(R.id.etUrl);
        tvResult = findViewById(R.id.tvResult);
        imageView = findViewById(R.id.imageView);

/*        네트워크 제약 사항 적용을 해제하기 위해 사용
		실습 시 테스트용으로만 사용하며 추후 스레드 또는 AsyncTask 사용 방법으로 대체할 것		*/
		StrictMode.ThreadPolicy pol
				= new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
		StrictMode.setThreadPolicy(pol);

	}

	public void onClick(View v) {

		String address = etUrl.getText().toString();   //xml에서 url을 string 타입으로 얻어옴
//		String address = getResources().getString(R.string.image_url);

//		네트워크 사용 가능 여부 확인
		if (!isOnline()) {
			Toast.makeText(this, "Network is not available!", Toast.LENGTH_SHORT).show();
			return;
		}

		switch (v.getId()) {
			case R.id.btnDownload:
				if (!address.equals("")) {
					String result = downloadContents(address);   //주소 넣어줌.
					tvResult.setText(result);
				}
				break;
			case R.id.btnImgDownload:
//				이미지를 다운로드 한 후 readStreamToBitmap() 호출
				String imageAddress = getResources().getString(R.string.image_url);
				Bitmap bitmap = downloadImages(imageAddress);
				imageView.setImageBitmap(bitmap);
				break;
		}

	}


//	네트워크 사용 가능 여부 확인
	private boolean isOnline() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}


//	문자열 형태의 웹 주소를 입력으로 서버 응답을 문자열로 만들어 반환
	private String downloadContents(String address){		// 이미지일 경우 Bitmap 반환
		HttpsURLConnection conn = null;
		InputStream stream = null;
		String result = null;
		int responseCode = 200;

		try {
			URL url = new URL(address);
			conn = (HttpsURLConnection)url.openConnection();   //서버 url 설정

			//요청 설정
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);

			//요청 처리
			conn.connect();   //연결

			responseCode = conn.getResponseCode();  //서버에 요청 전달 후 응답 결과 받음
			if (responseCode != HttpsURLConnection.HTTP_OK) {
				throw new IOException("HTTP error code: " + responseCode);
			}

			stream = conn.getInputStream();   //stream 타입으로 데이터 반환
			result = readStream(stream);	// 이미지일 경우 readStreamToBitmap 사용. stream을 문자열로 바꿈
		} catch (MalformedURLException e) {
			Toast.makeText(this, "주소 오류", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NetworkOnMainThreadException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try { stream.close(); }
				catch (IOException e) { e.printStackTrace(); }
			}
			if (conn != null) conn.disconnect();
		}

		return result;
	}

	//이미지 변환
	//	문자열 형태의 웹 주소를 입력으로 서버 응답을 문자열로 만들어 반환
	private Bitmap downloadImages(String address){		// 이미지일 경우 Bitmap 반환
		HttpsURLConnection conn = null;
		InputStream stream = null;
		Bitmap result = null;
		int responseCode = 200;

		try {
			URL url = new URL(address);
			conn = (HttpsURLConnection)url.openConnection();   //서버 url 설정

			//요청 설정
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(5000);

			//GET
			conn.setRequestMethod("GET");
			conn.setDoInput(true);

			//POST
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			//conn.setRequestProperty();



			//요청 처리
			conn.connect();   //연결

			responseCode = conn.getResponseCode();  //서버에 요청 전달 후 응답 결과 받음
			if (responseCode != HttpsURLConnection.HTTP_OK) {
				throw new IOException("HTTP error code: " + responseCode);
			}

			stream = conn.getInputStream();   //stream 타입으로 데이터 반환
			result = readStreamToBitmap(stream);	// 이미지일 경우 readStreamToBitmap 사용. stream을 문자열로 바꿈
		} catch (MalformedURLException e) {
			Toast.makeText(this, "주소 오류", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NetworkOnMainThreadException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try { stream.close(); }
				catch (IOException e) { e.printStackTrace(); }
			}
			if (conn != null) conn.disconnect();
		}

		return result;
	}


	public String readStream(InputStream stream){  //stream을 문자열로 변환
		StringBuilder result = new StringBuilder();
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(stream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String readLine = bufferedReader.readLine();

			while (readLine != null) {
				result.append(readLine + "\n");
				readLine = bufferedReader.readLine();
			}

			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();
	}


	private Bitmap readStreamToBitmap(InputStream stream) {
		return BitmapFactory.decodeStream(stream);   //if 이미지의 크기가 너무 크면 사이즈 변환 작업이 필요함. 크기를 줄이는 작업 필요. (지금은 없음) 크기 바꾸고 Bitmap에 넣어야함
	}


}
