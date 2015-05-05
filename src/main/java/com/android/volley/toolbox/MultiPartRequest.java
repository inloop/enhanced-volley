package com.android.volley.toolbox;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.NetworkResponse;
import com.android.volley.ProgressListener;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;

/**
 * A request for making a Multi Part request
 * 
 * @param <T> Response expected
 */
public abstract class MultiPartRequest<T> extends Request<T> implements ProgressListener {

	private static final String PROTOCOL_CHARSET = "utf-8";
	private ProgressListener mProgressListener;
	private Map<String, MultiPartParam> mMultipartParams = null;
	private Map<String, String> mFileUploads = null;
	public static final int TIMEOUT_MS = 30000;

	public MultiPartRequest(int method, String url, ErrorListener errorlistener) {

		super(method, url, errorlistener);
		mMultipartParams = new HashMap<String, MultiPartParam>();
		mFileUploads = new HashMap<String, String>();

	}

	/**
	 * Add a parameter to be sent in the multipart request
	 * 
	 * @param name
	 *            The name of the paramter
	 * @param contentType
	 *            The content type of the paramter
	 * @param value
	 *            the value of the paramter
	 * @return The Multipart request for chaining calls
	 */
	public MultiPartRequest<T> addMultipartParam(String name, String contentType, String value) {
		mMultipartParams.put(name, new MultiPartParam(contentType, value));
		return this;
	}

	/**
	 * Add a file to be uploaded in the multipart request
	 * 
	 * @param name
	 *            The name of the file key
	 * @param filePath
	 *            The path to the file. This file MUST exist.
	 * @return The Multipart request for chaining method calls
	 */
	public MultiPartRequest<T> addFile(String name, String filePath) {

		mFileUploads.put(name, filePath);
		return this;
	}

	@Override
	abstract protected Response<T> parseNetworkResponse(NetworkResponse response);
	
	@Override
	public void onProgress(long transferredBytes, long totalSize) {
		if(null != mProgressListener){
			mProgressListener.onProgress(transferredBytes, totalSize);
		}
	}

	/**
	 * A representation of a MultiPart parameter
	 */
	public static final class MultiPartParam {

		public String contentType;
		public String value;

		/**
		 * Initialize a multipart request param with the value and content type
		 * 
		 * @param contentType
		 *            The content type of the param
		 * @param value
		 *            The value of the param
		 */
		public MultiPartParam(String contentType, String value) {
			this.contentType = contentType;
			this.value = value;
		}
	}

	/**
	 * Get all the multipart params for this request
	 * 
	 * @return A map of all the multipart params NOT including the file uploads
	 */
	public Map<String, MultiPartParam> getMultipartParams() {
		return mMultipartParams;
	}

	/**
	 * Get all the files to be uploaded for this request
	 * 
	 * @return A map of all the files to be uploaded for this request
	 */
	public Map<String, String> getFilesToUpload() {
		return mFileUploads;
	}

	/**
	 * Get the protocol charset
	 */
	public String getProtocolCharset() {
		return PROTOCOL_CHARSET;
	}
}