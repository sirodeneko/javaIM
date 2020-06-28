<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2020/3/23
  Time: 14:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>IM</title>
    <%--    <script src="/js/vue.js"></script>--%>
    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/IM.css"/>
    <script src="https://cdn.bootcdn.net/ajax/libs/axios/0.19.2/axios.js"></script>
    <!-- 引入样式 -->
    <link rel="stylesheet" href="http://cdn.xiaosiro.cn/css/elementUI.css">
    <!-- 引入组件库 -->
    <script src="http://cdn.xiaosiro.cn/js/elementUI.js"></script>
</head>
<body>
<div id="app">
    <div class="im">
        <div class="main-im-s">
            <div class="maim-im">
                <div class="im-master">
                    <img
                            class="im-m-avater"
                            :src="me.avatar"
                            style="width: 60px; height:60px; border-radius:30px;"
                    />
                    <a href="/LoginOut">
                        <div style="    cursor: pointer;position: absolute;bottom: 20px;left: 24px;font-size: 16px;">
                            登出
                        </div>
                    </a>
                </div>
                <div class="im-m-list">
                    <div class="im-m-l-search" ref="SearchBox1">
                        <input class="im-m-l-s-input" type="text" placeholder="搜索群组"
                               v-model="searchText"
                               @keyup.enter="putSearchText"/>
                        <div class="im-m-l-add" @click="addARoom"></div>
                    </div>
                    <div class="im-m-l-s-result" ref="SearchBox2" v-show="showSearchBox">
                        <div class="im-m-l-s-result-list">
                            <div v-if="searchResult.length==0"
                                 style="height: 100px;width: 100%;text-align: center;line-height: 100px;font-size: 15px;">
                                没有查询到房间哦~~~
                            </div>
                            <div v-if="searchResult.length!=0">
                                <div class="im-m-l-s-result-list-item" v-for="(item, index) in searchResult"
                                     :key="index"
                                     @click="joinTheRoom(item)"
                                >
                                    {{item.name}}(id:{{item.id}})
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="im-m-l-list">
                        <div
                                :class="isFocus==index?'im-m-l-l-room-focus':'im-m-l-l-room' "
                                v-for="(item, index) in listRooms"
                                :key="item"
                                @click="changeRoom(index)"
                        >
                            <%--                            <img :src="item.avatar" alt style="width: 48px; height: 48px; border-radius: 24px;" />--%>
                            <div class="im-m-l-l-r-main">
                                <div class="im-m-l-r-m-name" style="color:#f7f7f7;font-size: 14px;">
                                    <p>{{Rooms.get(item).name}}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="im-char">
                    <div class="im-c-header">
                        <div><h2 style="font-size: 16px;color: #333;">{{chatMain.groupName}}</h2></div>
                        <div class="im-c-h-right" ref="More2"><img src="/img/more.svg" width="40px" height="40px"
                                                                   style="display: block" @click="showMore"></div>
                    </div>
                    <div class="im-c-message" id="im-c-msg">
                        <div
                                :class="item.user.id!=me.id?'im-c-m-msg-left':'im-c-m-msg-right'"
                                v-for="(item, index) in msgs"
                                :key="index"
                        >
                            <%--                            <img class="im-c-m-avater" :src="item.from_avatar" alt />--%>
                            <div :class="item.user.id!=me.id?'im-c-m-m-left':'im-c-m-m-right'">
                                <div :class="item.user.id!=me.id?'im-c-m-m-name-left':'im-c-m-m-name-right'">
                  <span
                          class="im-c-m-m-name-name"
                          style="margin-right: 0;color: #333;font-size: 13px;"
                  >{{item.user.name}}</span>
                                    <%--                                    <span--%>
                                    <%--                                            class="im-c-m-m-name-time"--%>
                                    <%--                                            style="color: #666;font-size: 12px;margin-left: 4px;margin-right: 4px;"--%>
                                    <%--                                    >{{timestampToTime(item.created_at)}}</span>--%>
                                </div>
                                <div style="display: flex;">
                                    <div
                                            v-if="item.type=='Msg'"
                                            :class="item.user.id!=me.id?'im-c-m-m-txt-left':'im-c-m-m-txt-right'"
                                    >
                                        <div style="    user-select: text;word-break: break-word;">{{item.content}}
                                        </div>
                                    </div>
                                    <div
                                            v-if="item.type=='Pic'"
                                            :class="item.user.id!=me.id?'im-c-m-m-txt-left':'im-c-m-m-txt-right'"
                                    >
                                        <img :src="item.content"
                                             style="max-width: 300px;max-height: 350px;border-radius: 3px;"/>
                                    </div>
                                    <%--                                    <div :class="item.user.id!=me.id?'im-c-m-m-3-left':'im-c-m-m-3-right'"></div>--%>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="im-c-charInput">
                        <div class="im-c-c-img-no"><input @change="pushImg($event)" type="file"
                                                          accept=".png,.jpg,.jpge,.gif"
                                                          style="opacity: 0;width: 35px;height: 35px;"></div>
                        <input
                                class="im-c-c-input"
                                type="text"
                                placeholder="聊点什么吧 (/≧▽≦/) "
                                v-model="toMsg.content"
                                @keyup.enter="putMsg"
                        />
                        <div class="im-c-c-img-yes" @click="putMsg"></div>
                    </div>
                    <div class="im-c-more" v-show="showMoreBox" ref="More1">
                        <div class="im-c-more-h">详细信息</div>
                        <div class="im-c-more-content">
                            <div class="im-c-more-c-block">
                                <p>功能</p>
                                <div class="im-c-more-c-block-button" @click="exitRoom">
                                    退出房间
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <%--遮罩--%>
        <div class="black_overlay" v-show="showJoinBox"></div>
        <div class="black_overlay" v-show="showAddBox"></div>
        <div class="im-add-room" v-show="showAddBox">
            <div class="im-add-room-h">
                创建房间
                <div class="im-join-room-x" @click="showAddBox=false"></div>
            </div>
            <div style="padding: 20px;">
                <div style="font-size: 15px">请输入房间名</div>
                <input type="text" class="im-add-room-input" v-model="createText">
                <div class="im-add-room-put" @click="createRoom">
                    创建
                </div>
            </div>

        </div>
        <div class="im-join-room" v-show="showJoinBox">
            <div class="im-join-room-x" @click="showJoinBox=false"></div>
            <div class="im-join-room-heard">
                {{joinRoom.name}}
            </div>
            <div class="im-join-room-foot">
                <div class="im-join-room-foot-button" @click="joinRoomOK">
                    加入房间
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="/js/IM.js"></script>
</body>

</html>
