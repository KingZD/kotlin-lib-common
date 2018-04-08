package com.zed.http;


import com.zed.common.util.LogUtils;
import com.zed.http.listener.SwitchUrlListener;
import com.zed.http.rx.RxSchedulers;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by ZD on 2017/9/2.
 */

public class ZedObservable<T> extends Observable<T> implements Disposable {
    private final Call<T> originalCall;
    SwitchUrlListener listener;

    private int maxRetries = 0;
    private int retryDelayMillis = 0;

    public ZedObservable(Call<T> originalCall) {
        this.originalCall = originalCall;
    }

    public ZedObservable(Call<T> originalCall, SwitchUrlListener listener, int maxRetries, int retryDelayMillis) {
        this.originalCall = originalCall;
        this.listener = listener;
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    public void execOnThread(Observer<? super T> observer, LifecycleTransformer<T> transformer) {
        compose(transformer)
                .retryWhen(new RetryWithDelay(maxRetries, retryDelayMillis))
                .compose(RxSchedulers.<T>compose())
                .subscribe(observer);

    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
// Since Call is a one-shot type, clone it for each new observer.
        Call<T> call = originalCall;

        boolean terminated = false;
        try {
            Response<T> response = call.clone().execute();
            if (!call.isCanceled()) {
                observer.onNext(response.body());
            }
            if (!call.isCanceled()) {
                terminated = true;
                observer.onComplete();
            }
        } catch (Throwable t) {
            if (listener != null)
                listener.responseState(t);
            Exceptions.throwIfFatal(t);
            if (terminated) {
                RxJavaPlugins.onError(t);
            } else if (!call.isCanceled()) {
                try {
                    observer.onError(t);
                } catch (Throwable inner) {
                    Exceptions.throwIfFatal(inner);
                    RxJavaPlugins.onError(new CompositeException(t, inner));
                }
            }
        }
    }


    @Override
    public void dispose() {
        originalCall.cancel();
        LogUtils.e(originalCall.request().url().toString() + " cancel");
    }

    @Override
    public boolean isDisposed() {
        return originalCall.isCanceled();
    }

    private class RetryWithDelay implements
            Function<Observable<? extends Throwable>, Observable<?>> {

        private final int maxRetries;
        private final int retryDelayMillis;
        private int retryCount;

        public RetryWithDelay(int maxRetries, int retryDelayMillis) {
            this.maxRetries = maxRetries;
            this.retryDelayMillis = retryDelayMillis;
        }

        @Override
        public Observable<?> apply(Observable<? extends Throwable> attempts) {
            return attempts
                    .flatMap(new Function<Throwable, Observable<?>>() {
                        @Override
                        public Observable<?> apply(Throwable throwable) {
                            if (++retryCount <= maxRetries) {
                                // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                                LogUtils.e("get error, it will try after " + retryDelayMillis
                                        + " millisecond, retry count " + retryCount);
                                return Observable.timer(retryDelayMillis,
                                        TimeUnit.SECONDS);
                            }
                            // Max retries hit. Just pass the error along.
                            return Observable.error(throwable);
                        }
                    });
        }
    }
}
