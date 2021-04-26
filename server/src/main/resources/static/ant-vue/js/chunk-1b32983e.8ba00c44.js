(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-1b32983e"],{"825d":function(e,t,a){"use strict";var r=a("984d"),s=a.n(r);s.a},"984d":function(e,t,a){},ac2a:function(e,t,a){"use strict";a.r(t);var r=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"main"},[a("a-form",{ref:"formLogin",staticClass:"user-layout-login",attrs:{id:"formLogin",form:e.form},on:{submit:e.handleSubmit}},[a("a-tabs",{attrs:{"active-key":e.customActiveKey,"tab-bar-style":{textAlign:"center",borderBottom:"unset"}},on:{change:e.handleTabClick}},[a("a-tab-pane",{key:"tab1",attrs:{tab:"账号密码登录"}},[e.isLoginError?a("a-alert",{staticStyle:{"margin-bottom":"24px"},attrs:{type:"error","show-icon":"",message:"账户或密码错误（admin/ant.design )"}}):e._e(),a("a-form-item",[a("a-input",{directives:[{name:"decorator",rawName:"v-decorator",value:["username",{rules:[{required:!0,message:"请输入帐户名或邮箱地址"},{validator:e.handleUsernameOrEmail}],validateTrigger:"change"}],expression:"[\n              'username',\n              {rules: [{ required: true, message: '请输入帐户名或邮箱地址' }, { validator: handleUsernameOrEmail }], validateTrigger: 'change'}\n            ]"}],attrs:{size:"large",type:"text",placeholder:"账户: admin"}},[a("a-icon",{style:{color:"rgba(0,0,0,.25)"},attrs:{slot:"prefix",type:"user"},slot:"prefix"})],1)],1),a("a-form-item",[a("a-input-password",{directives:[{name:"decorator",rawName:"v-decorator",value:["password",{rules:[{required:!0,message:"请输入密码"}],validateTrigger:"blur"}],expression:"[\n              'password',\n              {rules: [{ required: true, message: '请输入密码' }], validateTrigger: 'blur'}\n            ]"}],attrs:{size:"large",placeholder:"密码: admin or ant.design"}},[a("a-icon",{style:{color:"rgba(0,0,0,.25)"},attrs:{slot:"prefix",type:"lock"},slot:"prefix"})],1)],1)],1),a("a-tab-pane",{key:"tab2",attrs:{tab:"手机号登录"}},[a("a-form-item",[a("a-input",{directives:[{name:"decorator",rawName:"v-decorator",value:["mobile",{rules:[{required:!0,pattern:/^1[34578]\d{9}$/,message:"请输入正确的手机号"}],validateTrigger:"change"}],expression:"['mobile', {rules: [{ required: true, pattern: /^1[34578]\\d{9}$/, message: '请输入正确的手机号' }], validateTrigger: 'change'}]"}],attrs:{size:"large",type:"text",placeholder:"手机号"}},[a("a-icon",{style:{color:"rgba(0,0,0,.25)"},attrs:{slot:"prefix",type:"mobile"},slot:"prefix"})],1)],1),a("a-row",{attrs:{gutter:16}},[a("a-col",{staticClass:"gutter-row",attrs:{span:16}},[a("a-form-item",[a("a-input",{directives:[{name:"decorator",rawName:"v-decorator",value:["captcha",{rules:[{required:!0,message:"请输入验证码"}],validateTrigger:"blur"}],expression:"['captcha', {rules: [{ required: true, message: '请输入验证码' }], validateTrigger: 'blur'}]"}],attrs:{size:"large",type:"text",placeholder:"验证码"}},[a("a-icon",{style:{color:"rgba(0,0,0,.25)"},attrs:{slot:"prefix",type:"mail"},slot:"prefix"})],1)],1)],1),a("a-col",{staticClass:"gutter-row",attrs:{span:8}},[a("a-button",{staticClass:"getCaptcha",attrs:{tabindex:"-1",disabled:e.state.smsSendBtn},domProps:{textContent:e._s(e.state.smsSendBtn?e.state.time+" s":"获取验证码")},on:{click:function(t){return t.stopPropagation(),t.preventDefault(),e.getCaptcha(t)}}})],1)],1)],1)],1),a("a-form-item",[a("a-checkbox",{directives:[{name:"decorator",rawName:"v-decorator",value:["rememberMe",{valuePropName:"checked"}],expression:"['rememberMe', { valuePropName: 'checked' }]"}]},[e._v("自动登录")]),a("router-link",{staticClass:"forge-password",staticStyle:{float:"right"},attrs:{to:{name:"recover",params:{user:"aaa"}}}},[e._v(" 忘记密码 ")])],1),a("a-form-item",{staticStyle:{"margin-top":"24px"}},[a("a-button",{staticClass:"login-button",attrs:{size:"large",type:"primary","html-type":"submit",loading:e.state.loginBtn}},[e._v(" 确定 ")])],1),a("div",{staticClass:"user-login-other"},[a("span",[e._v("其他登录方式")]),a("a",[a("a-icon",{staticClass:"item-icon",attrs:{type:"alipay-circle"}})],1),a("a",[a("a-icon",{staticClass:"item-icon",attrs:{type:"taobao-circle"}})],1),a("a",[a("a-icon",{staticClass:"item-icon",attrs:{type:"weibo-circle"}})],1),a("router-link",{staticClass:"register",attrs:{to:{name:"register"}}},[e._v("注册账户")])],1)],1)],1)},s=[],i=(a("d3b7"),a("5530")),o=a("2f62"),n=a("7401"),c={name:"Login",data:function(){return{customActiveKey:"tab1",loginBtn:!1,loginType:0,isLoginError:!1,requiredTwoStepCaptcha:!1,stepCaptchaVisible:!1,form:this.$form.createForm(this),state:{time:60,loginBtn:!1,loginType:0,smsSendBtn:!1}}},methods:Object(i["a"])(Object(i["a"])({},Object(o["b"])(["Login","Logout"])),{},{handleUsernameOrEmail:function(e,t,a){var r=this.state,s=/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;s.test(t)?r.loginType=0:r.loginType=1,a()},handleTabClick:function(e){this.customActiveKey=e},handleSubmit:function(e){var t=this;e.preventDefault();var a=this.form.validateFields,r=this.state,s=this.customActiveKey,o=this.Login;r.loginBtn=!0;var n="tab1"===s?["username","password"]:["mobile","captcha"];a(n,{force:!0},(function(e,a){if(e)setTimeout((function(){r.loginBtn=!1}),600);else{console.log("login form",a);var s=Object(i["a"])({},a);delete s.username,s[r.loginType?"username":"email"]=a.username,s.password=a.password,o(s).then((function(e){return t.loginSuccess(e)})).catch((function(e){return t.requestFailed(e)})).finally((function(){r.loginBtn=!1}))}}))},getCaptcha:function(e){var t=this;e.preventDefault();var a=this.form.validateFields,r=this.state;a(["mobile"],{force:!0},(function(e,a){if(!e){r.smsSendBtn=!0;var s=window.setInterval((function(){r.time--<=0&&(r.time=60,r.smsSendBtn=!1,window.clearInterval(s))}),1e3),i=t.$message.loading("验证码发送中..",0);Object(n["a"])({mobile:a.mobile}).then((function(e){setTimeout(i,2500),t.$notification.success({message:"提示",description:"验证码获取成功，您的验证码为："+e.result.captcha,duration:8})})).catch((function(e){setTimeout(i,1),clearInterval(s),r.time=60,r.smsSendBtn=!1,t.requestFailed(e)}))}}))},stepCaptchaSuccess:function(){this.loginSuccess()},stepCaptchaCancel:function(){var e=this;this.Logout().then((function(){e.loginBtn=!1,e.stepCaptchaVisible=!1}))},loginSuccess:function(e){var t=this;console.log(e),this.$router.push({path:"/"}),setTimeout((function(){t.$notification.success({message:"欢迎",description:"欢迎回来"})}),1e3),this.isLoginError=!1},requestFailed:function(e){this.isLoginError=!0,this.$notification.error({message:"错误",description:((e.response||{}).data||{}).message||"请求出现错误，请稍后再试",duration:4})}})},l=c,u=(a("825d"),a("2877")),m=Object(u["a"])(l,r,s,!1,null,"5bed8ac8",null);t["default"]=m.exports}}]);