package net.zgyejy.yudong.live;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import net.zgyejy.yudong.live.controller.EmojiManager;
import net.zgyejy.yudong.live.ui.message.BaseMsgView;
import net.zgyejy.yudong.live.ui.message.GiftMessage;
import net.zgyejy.yudong.live.ui.message.GiftMsgView;
import net.zgyejy.yudong.live.ui.message.InfoMsgView;
import net.zgyejy.yudong.live.ui.message.TextMsgView;
import net.zgyejy.yudong.util.SharedUtil;

import java.util.ArrayList;
import java.util.HashMap;
import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.ISendMessageCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 融云初始化
 * Created by Administrator on 2017/1/9 0009.
 */

public class LiveKit {
    /**
     * 事件代码
     */
    public static final int MESSAGE_ARRIVED = 0;
    public static final int MESSAGE_SENT = 1;

    /**
     * 事件错误代码
     */
    public static final int MESSAGE_SEND_ERROR = -1;

    /**
     * 消息类与消息UI展示类对应表
     */
    private static HashMap<Class<? extends MessageContent>, Class<? extends BaseMsgView>> msgViewMap = new HashMap<>();

    /**
     * 事件监听者列表
     */
    private static ArrayList<Handler> eventHandlerList = new ArrayList<>();

    /**
     * 当前登录用户id
     */
    private static UserInfo currentUser;

    /**
     * 当前聊天室房间id
     */
    private static String currentRoomId = "10001";

    /**
     * 消息接收监听者
     */
    private static RongIMClient.OnReceiveMessageListener onReceiveMessageListener = new RongIMClient.OnReceiveMessageListener() {
        @Override
        public boolean onReceived(Message message, int i) {
            handleEvent(MESSAGE_ARRIVED, message.getContent());
            return false;
        }
    };

    /**
     * 初始化方法，在整个应用程序全局只需要调用一次，建议在Application 继承类中调用。
     * <p/>
     * <strong>注意：</strong>其余方法都需要在这之后调用。
     *
     * @param context Application类的Context
     * @param appKey  融云注册应用的AppKey
     */
    public static void init(Context context, String appKey) {
        RongIMClient.init(context, appKey);
        //EmojiManager.init(context);

        RongIMClient.setOnReceiveMessageListener(onReceiveMessageListener);

        registerMessageType(GiftMessage.class);//自定义消息类型
        registerMessageView(TextMessage.class, TextMsgView.class);
        /*registerMessageView(VoiceMessage.class,);*/
        registerMessageView(InformationNotificationMessage.class, InfoMsgView.class);
        registerMessageView(GiftMessage.class, GiftMsgView.class);
    }

    /**
     * 设置当前登录用户，通常由注册生成，通过应用服务器来返回。
     *
     * @param user 当前用户
     */
    public static void setCurrentUser(UserInfo user) {
        currentUser = user;
    }

    /**
     * 获得当前登录用户。
     *
     * @return
     */
    public static UserInfo getCurrentUser() {
        return currentUser;
    }

    /**
     * 注册自定义消息。
     *
     * @param msgType 自定义消息类型
     */
    public static void registerMessageType(Class<? extends MessageContent> msgType) {
        try {
            RongIMClient.registerMessageType(msgType);
        } catch (AnnotationNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册消息展示界面类。
     *
     * @param msgContent 消息类型
     * @param msgView    对应的界面展示类
     */
    public static void registerMessageView(Class<? extends MessageContent> msgContent, Class<? extends BaseMsgView> msgView) {
        msgViewMap.put(msgContent, msgView);
    }

    /**
     * 获取注册消息对应的UI展示类。
     *
     * @param msgContent 注册的消息类型
     * @return 对应的UI展示类
     */
    public static Class<? extends BaseMsgView> getRegisterMessageView(Class<? extends MessageContent> msgContent) {
        return msgViewMap.get(msgContent);
    }

    /**
     * 连接融云服务器，在整个应用程序全局，只需要调用一次，需在 {@link #init(Context, String)} 之后调用。
     * </p>
     * 如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。
     *
     * @param token    从服务端获取的用户身份令牌（Token）
     * @param callback 连接回调
     */
    public static void connect(String token, final RongIMClient.ConnectCallback callback) {
        RongIMClient.connect(token, callback);
    }

    /**
     * 断开与融云服务器的连接，并且不再接收 Push 消息。
     */
    public static void logout() {
        RongIMClient.getInstance().logout();
    }

    /**
     * 加入聊天室。如果聊天室不存在，sdk 会创建聊天室并加入，如果已存在，则直接加入。加入聊天室时，可以选择拉取聊天室消息数目。
     *
     * @param roomId          聊天室 Id
     * @param defMessageCount 默认开始时拉取的历史记录条数
     * @param callback        状态回调
     */
    public static void joinChatRoom(String roomId, int defMessageCount, final RongIMClient.OperationCallback callback) {
        currentRoomId = roomId;
        RongIMClient.getInstance().joinChatRoom(currentRoomId, defMessageCount, callback);
    }

    /**
     * 退出聊天室，不在接收其消息。
     */
    public static void quitChatRoom(final RongIMClient.OperationCallback callback) {
        RongIMClient.getInstance().quitChatRoom(currentRoomId, callback);
    }

    private static final String TAG = "LiveKit";

    /**
     * 向当前聊天室发送消息。
     * </p>
     * <strong>注意：</strong>此函数为异步函数，发送结果将通过handler事件返回。
     *
     * @param msgContent 消息对象
     */
    public static void sendMessage(final MessageContent msgContent) {
        if (currentUser == null) {
            throw new RuntimeException("currentUser should not be null.");
        }
        msgContent.setUserInfo(currentUser);
        Message msg = Message.obtain(currentRoomId, Conversation.ConversationType.CHATROOM, msgContent);
        RongIMClient.getInstance().sendMessage(msg,null,null, new IRongCallback.ISendMessageCallback(){
            @Override
            public void onAttached(Message message) {
                Log.i(TAG, "onSuccess: +++++++++++++++++++++++发送onAttached" );
            }
            @Override
            public void onSuccess(Message message) {
                handleEvent(MESSAGE_SENT, msgContent);
                Log.i(TAG, "onSuccess: +++++++++++++++++++++++发送消息成功！" );
            }
            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                handleEvent(MESSAGE_SEND_ERROR, errorCode.getValue(), 0, msgContent);
                Log.i(TAG, "onError: ++++++++++++++++++++++++++++++发送失败！");
            }
        });
        /*RongIMClient.getInstance().sendMessage(msg, null, null, new RongIMClient.SendMessageCallback() {
            @Override
            public void onSuccess(Integer integer) {
                handleEvent(MESSAGE_SENT, msgContent);
            }

            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                handleEvent(MESSAGE_SEND_ERROR, errorCode.getValue(), 0, msgContent);
            }
        }, new RongIMClient.ResultCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
            }
        });*/
    }

    /**
     * 添加IMLib 事件接收者。
     *
     * @param handler
     */
    public static void addEventHandler(Handler handler) {
        if (!eventHandlerList.contains(handler)) {
            eventHandlerList.add(handler);
        }
    }

    /**
     * 移除IMLib 事件接收者。
     *
     * @param handler
     */
    public static void removeEventHandler(Handler handler) {
        eventHandlerList.remove(handler);
    }

    private static void handleEvent(int what) {
        for (Handler handler : eventHandlerList) {
            android.os.Message m = android.os.Message.obtain();
            m.what = what;
            handler.sendMessage(m);
        }
    }

    private static void handleEvent(int what, Object obj) {
        for (Handler handler : eventHandlerList) {
            android.os.Message m = android.os.Message.obtain();
            m.what = what;
            m.obj = obj;
            handler.sendMessage(m);
        }
    }

    private static void handleEvent(int what, int arg1, int arg2, Object obj) {
        for (Handler handler : eventHandlerList) {
            android.os.Message m = android.os.Message.obtain();
            m.what = what;
            m.arg1 = arg1;
            m.arg2 = arg2;
            m.obj = obj;
            handler.sendMessage(m);
        }
    }
}
