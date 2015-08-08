package yanhao.com.soucecode.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

/**
 * Author: yanhao(amosbake@gmail.com)
 * Date : 2015-08-03
 * Time: 11:34
 */
public class GsonRequest<T> extends Request<T> {
    private static final String TAG = "GsonRequest";
    /**
     * Gson parser
     */
    private final Gson mGson;

    /**
     * Class type for the response
     */
    private final Class<T> mClass;


    /**
     * Callback for response delivery
     */
    private final Response.Listener<T> mListener;
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(json, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
    /**
     * @param method
     * 		Request type.. Method.GET etc
     * @param url
     * 		path for the requests
     * @param objectClass
     * 		expected class type for the response. Used by gson for serialization.
     * @param listener
     * 		handler for the response
     * @param errorListener
     * 		handler for errors
     */
    public GsonRequest(int method
            , String url
            , Class<T> objectClass
            , Response.Listener<T> listener
            , Response.ErrorListener errorListener) {

        super(method, url, errorListener);
        this.mClass = objectClass;
        this.mListener = listener;
        mGson = new Gson();

    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}
