package cn.lxycx.kuaicore.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.util.LinkedMultiValueMap;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.LinkedHashSet;

public class RequestWrapper extends HttpServletRequestWrapper {
    //参数字节数组
    private byte[] requestBody;
    //Http请求对象
    private HttpServletRequest request;
    private LinkedMultiValueMap multipartFiles;
    private LinkedHashSet multipartParameterNames;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.request = request;
    }

    public RequestWrapper(HttpServletRequest request, HttpServletRequest request1, LinkedMultiValueMap multipartFiles, LinkedHashSet multipartParameterNames) {
        super(request);
        this.request = request1;
        this.multipartFiles = multipartFiles;
        this.multipartParameterNames = multipartParameterNames;
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        /**
         * 每次调用此方法时将数据流中的数据读取出来，然后再回填到InputStream之中
         * 解决通过@RequestBody和@RequestParam（POST方式）读取一次后控制器拿不到参数问题
         */
        if (null == this.requestBody) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(request.getInputStream(), baos);
            this.requestBody = baos.toByteArray();
        }

        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() {
                return bais.read();
            }
        };
    }

    public byte[] getRequestBody() {
        try {
            if (null == this.requestBody){
                this.getInputStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestBody;
    }

    public void setRequestBody( byte[] data) {
        this.requestBody = data;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
