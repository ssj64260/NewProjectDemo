package com.android.newprojectdemo.service;

import com.orhanobut.logger.Logger;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;


/**
 * 请求回调，处理错误码，可取消订阅
 */

public abstract class MyDisposableObserver<T> extends DisposableObserver<T> {

    @Override
    public void onNext(@NonNull T result) {
        onSuccess(result);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        onError(e.getMessage());
    }

    @Override
    public void onComplete() {

    }

    public void onTokenIncorrect() {

    }

    public void onError(String errorMsg) {
        Logger.e(errorMsg);
    }

    public abstract void onSuccess(@NonNull T result);
}
