package io.javac.ManyBlue.manager;

import org.greenrobot.eventbus.EventBus;

import io.javac.ManyBlue.ManyBlue;
import io.javac.ManyBlue.bean.NotifyMessage;
import io.javac.ManyBlue.utils.LogUtils;

/**
 * Created by Pencilso on 2017/7/22.
 */

public class EventManager {
    private static final EventBus libraryEvent = EventBus.builder().build();

    /**
     * 蓝牙依赖内部的EventBus
     *
     * @return
     */
    public static EventBus getLibraryEvent() {
        return libraryEvent;
    }

    public static void removeAllEvent() {
        if (libraryEvent != null) libraryEvent.removeAllStickyEvents();
    }

    /**
     * 接收方 发送
     *
     * @param notifyMessage
     */
    public static void recePost(NotifyMessage notifyMessage) {
        if (ManyBlue.DEBUG)
            LogUtils.log("recePost:" + notifyMessage);
        libraryEvent.post(notifyMessage);
    }

    /**
     * 服务里面 发送
     *
     * @param notifyMessage
     */
    public static void servicePost(NotifyMessage notifyMessage) {
        if (ManyBlue.DEBUG)
            LogUtils.log("servicePost:" + notifyMessage);
        libraryEvent.post(notifyMessage);
    }
}

