package com.yeay.xpush.pusher.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import com.yeay.xpush.message.DingMessage;
import com.yeay.xpush.message.DingMessageType;
import com.yeay.xpush.message.DingPushType;
import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.config.DingPushConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

/**
 * @author yeay
 * @Description 阿里云短信推送器
 * @createTime 2022/03/14 14:24:34
 */
public class DingPusher implements Pusher<DingMessage, DingPushConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DingPusher.class);

    private static final String DING_TALK_SEND_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";

    private static final String DING_TALK_TOKEN_URL = "https://oapi.dingtalk.com/gettoken";

    private static final String TOKEN_CACHE_KEY = "accessToken";
    private static TimedCache<String, String> accessTokenTimedCache;
    private DingPushConfig config;
    private volatile DefaultDingTalkClient defaultDingTalkClient;
    private volatile DefaultDingTalkClient robotClient;

    public DingPusher(DingPushConfig config) {
        setConfig(config);

        initDefaultDingTalkClient();
        initRobotClient();
    }

    @Override
    public DingPushConfig getConfig() {
        return this.config;
    }

    @Override
    public void setConfig(DingPushConfig config) {
        this.config = config;
    }

    @Override
    public PushResponse send(DingMessage msg) {
        if (Objects.equals(msg.getPushType(), DingPushType.WORK)) {
            return sendWorkMsg(msg);
        } else {
            return sendRobotMsg(msg);
        }
    }

    private void initDefaultDingTalkClient() {
        if (defaultDingTalkClient == null) {
            synchronized (DingPusher.class) {
                if (defaultDingTalkClient == null) {
                    defaultDingTalkClient = new DefaultDingTalkClient(DING_TALK_SEND_URL);
                }
            }
        }
    }

    private void initRobotClient() {
        if (robotClient == null) {
            synchronized (DingPusher.class) {
                if (robotClient == null) {
                    String serverUrl = getConfig().getWebHook();

                    if (StringUtils.isNotEmpty(getConfig().getRobotSecret())) {
                        try {
                            serverUrl = calculateSign(getConfig().getRobotSecret(), serverUrl);
                        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
                            LOGGER.error("calculate robot sign fail!", e);
                            throw new IllegalArgumentException("robot secret config is error!", e);
                        }
                    }

                    robotClient = new DefaultDingTalkClient(serverUrl);
                }
            }
        }
    }

    private TimedCache<String, String> getAccessTokenTimedCache() {
        if (accessTokenTimedCache == null || StringUtils.isEmpty(accessTokenTimedCache.get(TOKEN_CACHE_KEY))) {
            synchronized (DingPusher.class) {
                if (accessTokenTimedCache == null || StringUtils.isEmpty(accessTokenTimedCache.get(TOKEN_CACHE_KEY))) {
                    DefaultDingTalkClient client = new DefaultDingTalkClient(DING_TALK_TOKEN_URL);

                    OapiGettokenRequest request = new OapiGettokenRequest();
                    String agentId = getConfig().getAgentId();
                    request.setAppkey(getConfig().getAppKey());
                    request.setAppsecret(getConfig().getAppSecret());
                    request.setHttpMethod("GET");

                    OapiGettokenResponse response = null;
                    try {
                        response = client.execute(request);
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                    accessTokenTimedCache = CacheUtil.newTimedCache((response.getExpiresIn() - 60) * 1000);
                    accessTokenTimedCache.put(TOKEN_CACHE_KEY, response.getAccessToken());
                }
            }
        }
        return accessTokenTimedCache;
    }

    private PushResponse sendWorkMsg(DingMessage dingMessage) {
        PushResponse pushResponse = new PushResponse();

        try {
            List<String> userIds = dingMessage.getUserIds();

            OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
            request.setUseridList(String.join(",", userIds));
            request.setAgentId(Long.valueOf(getConfig().getAgentId()));
            request.setToAllUser(false);

            OapiMessageCorpconversationAsyncsendV2Request.Msg msg = dingMessage.createMsg();
            request.setMsg(msg);

            OapiMessageCorpconversationAsyncsendV2Response response = defaultDingTalkClient
                    .execute(request, getAccessTokenTimedCache().get(TOKEN_CACHE_KEY));

            if (response.getErrcode() != 0) {
                LOGGER.warn("push message fail! {}", response.getErrmsg());
                pushResponse.setSuccess(false);
                pushResponse.setMessage(response.getErrmsg());
                return pushResponse;
            }
        } catch (Exception e) {
            LOGGER.warn("push message fail!", e);
            pushResponse.setSuccess(false);
            pushResponse.setMessage(e.getMessage());
            return pushResponse;
        }

        pushResponse.setSuccess(true);
        return pushResponse;
    }

    private PushResponse sendRobotMsg(DingMessage dingMessage) {
        PushResponse pushResponse = new PushResponse();

        try {
            DingTalkClient client = robotClient;
            OapiRobotSendRequest request = new OapiRobotSendRequest();

            if (Objects.equals(DingMessageType.TEXT, dingMessage.getMsgType())) {
                OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();

                if (getConfig().getRobotKeyWords() != null) {
                    String content = joinKeyWords(getConfig().getRobotKeyWords(), dingMessage.getContent());
                    text.setContent(content);
                } else {
                    text.setContent(dingMessage.getContent());
                }

                // set @user
                OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
                if (dingMessage != null && CollectionUtils.isNotEmpty(dingMessage.getMobiles())) {
                    at.setAtMobiles(dingMessage.getMobiles());
                } else {
                    at.setIsAtAll(true);
                }

                request.setMsgtype(DingMessageType.TEXT.getName());
                request.setText(text);
                request.setAt(at);
            } else if (Objects.equals(DingMessageType.LINK, dingMessage.getMsgType())) {
                OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
                link.setMessageUrl(dingMessage.getUrl());
                link.setPicUrl(dingMessage.getPicUrl());
                link.setTitle(dingMessage.getTitle());
                if (getConfig().getRobotKeyWords() != null) {
                    String content = joinKeyWords(getConfig().getRobotKeyWords(), dingMessage.getContent());
                    link.setText(content);
                } else {
                    link.setText(dingMessage.getContent());
                }

                request.setLink(link);
                request.setMsgtype(DingMessageType.LINK.getName());
            } else if (Objects.equals(DingMessageType.MARKDOWN, dingMessage.getMsgType())) {
                OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
                markdown.setTitle(dingMessage.getTitle());
                if (getConfig().getRobotKeyWords() != null) {
                    String content = joinKeyWords(getConfig().getRobotKeyWords(), dingMessage.getContent());
                    markdown.setText(content);
                } else {
                    markdown.setText(dingMessage.getContent());
                }

                request.setMsgtype(DingMessageType.MARKDOWN.getName());
                request.setMarkdown(markdown);
            } else if (Objects.equals(DingMessageType.ACTION_CARD, dingMessage.getMsgType())) {
                OapiRobotSendRequest.Actioncard actionCard = new OapiRobotSendRequest.Actioncard();
                actionCard.setTitle(dingMessage.getTitle());
                if (getConfig().getRobotKeyWords() != null) {
                    String content = joinKeyWords(getConfig().getRobotKeyWords(), dingMessage.getContent());
                    actionCard.setText(content);
                } else {
                    actionCard.setText(dingMessage.getContent());
                }
                actionCard.setSingleTitle(dingMessage.getBtnTxt());
                actionCard.setSingleURL(dingMessage.getBtnUrl());

                request.setMsgtype(DingMessageType.ACTION_CARD.getName());
                request.setActionCard(actionCard);
            }


            OapiRobotSendResponse response = client.execute(request);
            if (response.getErrcode() != 0) {
                LOGGER.warn("push message fail! {}", response.getErrmsg());
                pushResponse.setSuccess(false);
                pushResponse.setMessage(response.getErrmsg());
                return pushResponse;
            }
        } catch (Exception e) {
            LOGGER.warn("push message fail!", e);
            pushResponse.setSuccess(false);
            pushResponse.setMessage(e.getMessage());
            return pushResponse;
        }

        pushResponse.setSuccess(true);
        return pushResponse;
    }

    private String calculateSign(String appSecret, String serverUrl) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + appSecret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(appSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");

        serverUrl = String.format(serverUrl + "&timestamp=%s&sign=%s", timestamp, sign);

        return serverUrl;
    }

    private String joinKeyWords(List<String> keyWords, String content) {
        if (CollectionUtils.isNotEmpty(keyWords)) {
            return String.join(" ", keyWords) + " " + content;
        } else {
            return content;
        }
    }
}
