package com.zed.http;

import com.zed.http.listener.SwitchUrlListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * Created by ZD on 2017/9/2.
 */

public class ZedObservableFactory extends CallAdapter.Factory {
    static SwitchUrlListener mListener;
    static int maxRetries;
    static int retryDelayMillis;

    public static ZedObservableFactory create() {
        return new ZedObservableFactory();
    }

    public static ZedObservableFactory create(SwitchUrlListener listener, int maxRetries, int retryDelayMillis) {
        mListener = listener;
        ZedObservableFactory.maxRetries = maxRetries;
        ZedObservableFactory.retryDelayMillis = retryDelayMillis;
        return new ZedObservableFactory();
    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        //GAResponse<T>
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        //response
//        Class<?> rawObservableType = getRawType(observableType);
        //GAResponse<T> 里面的 T
//        Type responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
        return new GACallAdapter(observableType);
    }

    public class GACallAdapter<R> implements CallAdapter<R, Object> {
        Type returnType;

        public GACallAdapter(Type returnType) {
            this.returnType = returnType;
        }

        @Override
        public Type responseType() {
            return returnType;
        }

        @Override
        public Object adapt(Call<R> call) {
            if (mListener != null)
                return new ZedObservable<>(call, mListener, maxRetries, retryDelayMillis);
            return new ZedObservable<>(call);
        }
    }
}
