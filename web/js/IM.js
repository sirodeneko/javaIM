
Array.prototype.remove = function(from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest);
};

var app = new Vue({
    el: '#app',
    data: {
        isFocus: 0,
        chatMain: {
            groupName: "奇怪的聊天室"
        },
        msgs: [],
        websock: null,
        createRoomForm: "",
        toMsg: {
            type: "",
            content: ""
        },
        me: {
            id: 99,
            nickname: "小白",
            avatar: "https://s1.hdslb.com/bfs/static/jinkela/space/asserts/icon-auth.png"
        },
        //重连计数
        recount:0,
        //聊天室列表 默认聊天室的排序就是这个 存放聊天室唯一id
        listRooms: [],
        //聊天室列表map，聊天室唯一id:聊天室消息
        Rooms: new Map(),
        showMoreBox:false,
        showSearchBox:false,
        searchText:"",
        searchResult:[],
        joinRoom:{
            id: 0,
            name:"呀嚯",
            master:0,
        },
        showJoinBox:false,
        showAddBox:false,
        createText:"",
    },
    created() {
        this.initWebSocket();
        this.load();
    },
    destroyed() {
        this.websock.close(); //离开路由之后断开websocket连接
    },
    methods: {
        load(){
            // 拉取个人信息
            axios.get("http://"+window.location.host+"/api/me")
                .then(res => {
                    //console.log(res);
                    this.me.id=res.data.User.id;
                    this.me.nickname=res.data.User.name;
                    this.loadRooms(res);
                }).catch((e)=>{
                    console.log(e);
                });
        },
        pushImg(e){
            console.log(e.target.files[0]);
            let file=e.target.files[0];
            console.log(file.type);
            const isPNG = (file.type === 'image/png' || file.type === 'image/jpeg' || file.type === 'image/gif');
            const isLt2M = file.size / 1024 / 1024 < 5;
            if (!isPNG) {
                this.$notify({
                    title: '警告',
                    message: '只能上传图片',
                    type: 'warning'
                });
            }else if (!isLt2M) {
                this.$notify({
                    title: '警告',
                    message: '图片太大了',
                    type: 'warning'
                });
            }else {
                axios.get("http://"+window.location.host+"/api/putImg?fileName="+file.name).then((res) => {
                    const oReq = new XMLHttpRequest();
                    oReq.open('PUT', res.data.img.put, true);
                    oReq.send(file);
                    oReq.onload = () => {
                        this.putImg(res.data.img.get);
                    };
                }).catch((error) => {
                    this.$notify.error({
                        title: '图片发送失败',
                        message: error,
                    });
                    console.log(error);
                });
            }
        },
        putImg(e){
            let actions={
                type: "Pic",
                content: e,
                to:this.listRooms[this.isFocus],
            };
            this.websocketsend(JSON.stringify(actions));
        },
        loadRooms(res){
            // 拉取个人聊天室
            axios.get("http://"+window.location.host+"/api/myGroups",{params:{id:this.me.id}})
                .then(res => {
                    for(let i=0; i<res.data.groups.length;i++){
                        this.listRooms.push(res.data.groups[i].id);
                        this.Rooms.set(res.data.groups[i].id,res.data.groups[i]);
                        this.Rooms.get(res.data.groups[i].id).msgs=[];
                    }
                    this.msgs=this.Rooms.get(this.listRooms[0]).msgs;
                }).catch((e)=>{
                    this.$notify.error({
                        title: '错误',
                        message: "拉取聊天室失败"+e,
                        duration: 0,
                    });
            });
        },
        changeRoom(index) {
            this.isFocus = index;
            this.chatMain.groupName = this.Rooms.get(this.listRooms[index]).name;
            this.msgs = this.Rooms.get(this.listRooms[index]).msgs;
            this.scrollToBottom();
            //console.log(this.isFocus);
        },
        putMsg() {
            if (this.toMsg.content !== "") {
                let actions = this.toMsg;
                actions.type = "Msg";
                actions.to=this.listRooms[this.isFocus];
                this.websocketsend(JSON.stringify(actions));
                this.toMsg.content = "";
            }
        },
        getMsg(data) {
            //console.log(this.listRooms[0]);
            let msg = document.getElementById('im-c-msg');
            let f = 0;
            if(msg.scrollTop+msg.clientHeight >= msg.scrollHeight-3){
                f=1;
                //console.log(1);
            }
            this.Rooms.get(data.to).msgs.push(data);
            // 经测试，this.msgs=this.Rooms.get(data.to).msgs为地址拷贝，故不需要再次写入
            // if (this.listRooms[this.isFocus]==data.to){
            //     this.msgs.push(data);
            // }
            //console.log(data);
            //console.log(this.msgs);
            this.$nextTick(() => {
                //console.log(3);
                if(f==1){
                    msg.scrollTop = msg.scrollHeight-msg.clientHeight; // 滚动高度
                    //console.log(2);
                }

            })
        },
        showMore(){
            this.showMoreBox=true;
            document.addEventListener('click', this.handleBodyClick, false)
        },
        hideMore () {
            this.showMoreBox = false;
            document.removeEventListener('click', this.handleBodyClick, false)
        },
        exitRoom(){
            // 删除个人聊天室
            axios.post("http://"+window.location.host+"/api/ExitGroup","to="+this.listRooms[this.isFocus])
                .then(res => {
                    this.Rooms.delete(this.listRooms[this.isFocus]);
                    this.listRooms.remove(this.isFocus);
                    this.changeRoom(0);
                    this.$notify({
                        title: '成功',
                        message: '退出成功',
                        type: 'success'
                    });
                }).catch((e)=>{
                    this.$notify.error({
                        title: '错误',
                        message: "退出聊天室失败"+e,
                    });
            });
        },
        putSearchText(){
            // 查询聊天室
            this.showSearch();
            axios.post("http://"+window.location.host+"/api/SearchGroup","name="+this.searchText)
                .then(res => {
                    this.searchResult=res.data.groups;
                    //console.log(res.data.groups);
                    //console.log(this.searchResult);
                }).catch((e)=>{
                    this.$notify.error({
                        title: '错误',
                        message: "查询失败"+e,
                    });
                this.searchResult=[];
            });
        },
        showSearch(){
            //console.log("5555");
            if(!this.showSearchBox){
                this.showSearchBox=true;
                document.addEventListener('click', this.handleBodyClick2, false);
            }
            this.searchResult=[];
        },
        hideSearch(){
            this.showSearchBox=false;
            document.addEventListener('click', this.handleBodyClick2, false);
        },
        joinTheRoom(item){
            this.hideSearch();
            this.joinRoom=item;
            this.showJoinBox=true;
            //console.log(item);
        },
        addARoom(){
            this.showAddBox=true;
        },
        joinRoomOK(){
            axios.post("http://"+window.location.host+"/api/JoinGroup","to="+this.joinRoom.id)
                .then(res => {
                    if(res.data.code==200){
                        this.listRooms.push(res.data.groups.id);
                        this.Rooms.set(res.data.groups.id,res.data.groups);
                        this.Rooms.get(res.data.groups.id).msgs=[];
                        console.log(this.Rooms);
                        this.$notify({
                            title: '成功',
                            message: '加入成功',
                            type: 'success'
                        });
                    }else{
                        this.$notify.error({
                            title: '错误',
                            message: "加入失败:"+res.data.err,
                        });
                    }

                }).catch((e)=>{
                    this.$notify.error({
                        title: '错误',
                        message: "加入失败"+e,
                    });
            }).finally((e)=>{this.showJoinBox=false;});
        },
        createRoom(){
            axios.post("http://"+window.location.host+"/api/CreateGroup","name="+this.createText)
                .then(res => {
                    if(res.data.code==200){
                        this.listRooms.push(res.data.groups.id);
                        this.Rooms.set(res.data.groups.id,res.data.groups);
                        this.Rooms.get(res.data.groups.id).msgs=[];
                        console.log(this.Rooms);
                        this.$notify({
                            title: '成功',
                            message: '创建成功',
                            type: 'success'
                        });
                    }else{
                        this.$notify.error({
                            title: '错误',
                            message: "创建失败失败:"+res.data.err,
                        });
                    }

                }).catch((e)=>{
                this.$notify.error({
                    title: '错误',
                    message: "创建失败失败"+e,
                });
            }).finally((e)=>{this.showAddBox=false;});
        },
        //滚动到底部
        scrollToBottom(){
            let msg = document.getElementById('im-c-msg');
            //msg.scrollTop = msg.scrollHeight-msg.clientHeight;
            this.$nextTick(() => {
                msg.scrollTop = msg.scrollHeight-msg.clientHeight; // 滚动高度
            })
        },
        initWebSocket() {
            //初始化weosocket

            const wsuri = "ws://" + window.location.host + "/ws";
            this.websock = new WebSocket(wsuri);
            this.websock.onmessage = this.websocketonmessage;
            this.websock.onopen = this.websocketonopen;
            this.websock.onerror = this.websocketonerror;
            this.websock.onclose = this.websocketclose;
        },
        websocketonopen() {
            this.recount=0;
            //连接建立之后执行send方法发送数据
            let actions = {
                type: "Ping"
            };
            //对象转json字符串
            this.websocketsend(JSON.stringify(actions));
        },
        websocketonerror() {
            this.websocketClosed("websocketc出现错误");
        },
        websocketonmessage(e) {
            //数据接收
            //字符串转json
            const redata = JSON.parse(e.data);
            //console.log(redata);
            switch (redata.type) {
                case "Pong":
                    break;
                case "Msg":
                    this.getMsg(redata);
                    //console.log(redata.content);
                    break;
                case "Pic":
                    this.getMsg(redata);
                    //console.log(redata.content);
                    break;
                case "Err":
                    this.websocketClosed(redata.content);
                    console.log(redata.content);
                    break;
                case "Other":
                    this.websocketOther();
                    break;
            }
        },
        websocketClosed(err){
            this.$notify.error({
                title: '错误',
                message: err,
                duration: 0,
            });
            console.log(err+"断开链接");
        },
        websocketOther(){
            this.$alert('您在其他地方上线', '错误', {
                confirmButtonText: '退出',
                callback: action => {
                    //关闭窗口后的回调
                    window.location.replace("http://"+window.location.host+"/index");
                }
            });
            console.log("在其他地方上线");
        },
        websocketsend(Data) {
            //数据发送
            this.websock.send(Data);
            if(this.websock.readyState==3){
                this.websocketClosed("发送失败数据失败");
            }
        },
        websocketclose(e) {
            //关闭
            console.log("断开连接");
            this.websocketClosed("连接close");
        },
        handleBodyClick(e) {
            //console.log("666");
            if (!this.$refs.More1.contains(e.target)&&!this.$refs.More2.contains(e.target)) {
                this.hideMore();
            }
        },
        handleBodyClick2(e){
            if (!this.$refs.SearchBox1.contains(e.target)&&!this.$refs.SearchBox2.contains(e.target)) {
                this.hideSearch();
            }
        }

    },
    mounted() {
        this.timer = setInterval(this.websocketonopen, 30000);

    },
    beforeDestroy() {
        clearInterval(this.timer);
    }
});
