(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["user"],{"7c18":function(t,e,a){},af80:function(t,e,a){"use strict";a.d(e,"f",(function(){return o})),a.d(e,"c",(function(){return r})),a.d(e,"e",(function(){return s})),a.d(e,"g",(function(){return i})),a.d(e,"a",(function(){return c})),a.d(e,"d",(function(){return u})),a.d(e,"b",(function(){return l}));var n=a("b775");function o(t){return Object(n["b"])({url:"/admin/v1/tenants/list",method:"post",data:t})}function r(t){return Object(n["b"])({url:"/admin/v1/tenants/"+t,method:"get"})}function s(t,e){return Object(n["b"])({url:"/admin/v1/tenants",method:"post",data:t,headers:{"Content-Type":"application/json;charset=UTF-8","Request-Id":e}})}function i(t){return Object(n["b"])({url:"/admin/v1/tenants",method:"put",data:t})}function c(t){return Object(n["b"])({url:"/admin/v1/tenants/"+t,method:"delete"})}function u(){return Object(n["b"])({url:"/admin/v1/tenants/id",method:"get"})}function l(t){return Object(n["b"])({url:"/admin/v1/tenants/"+t+"/download-datasource",method:"get",responseType:"blob"})}},c309:function(t,e,a){"use strict";a("7c18")},ca002:function(t,e,a){"use strict";function n(){var t=new Date,e=t.getHours();return e<9?"早上好":e<=11?"上午好":e<=13?"中午好":e<20?"下午好":"晚上好"}a.d(e,"a",(function(){return n}))},d524:function(t,e,a){"use strict";a.r(e);var n=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"main"},[a("a-form-model",{ref:"form",staticClass:"user-layout-login",attrs:{id:"formLogin",model:t.form,rules:t.rules}},[a("a-form-model-item",{attrs:{prop:"tenantId"}},[a("a-select",{attrs:{size:"large",placeholder:"请选择租户"},model:{value:t.form.tenantId,callback:function(e){t.$set(t.form,"tenantId",e)},expression:"form.tenantId"}},[a("a-select-option",{key:"0",attrs:{value:"0"}},[t._v("老寇云集团")]),t._l(t.tenantOptions,(function(e,n){return a("a-select-option",{key:n+1,attrs:{value:e.value}},[t._v(" "+t._s(e.label)+" ")])}))],2)],1),a("a-form-model-item",{attrs:{prop:"username"}},[a("a-input",{attrs:{autocomplete:"off","allow-clear":"",size:"large",placeholder:"请输入账号"},model:{value:t.form.username,callback:function(e){t.$set(t.form,"username",e)},expression:"form.username"}},[a("a-icon",{style:{color:"rgba(0,0,0,.25)"},attrs:{slot:"prefix",type:"user"},slot:"prefix"})],1)],1),a("a-form-model-item",{attrs:{prop:"password"}},[a("a-input-password",{attrs:{autocomplete:"off","allow-clear":"",size:"large",placeholder:"请输入密码"},model:{value:t.form.password,callback:function(e){t.$set(t.form,"password",e)},expression:"form.password"}},[a("a-icon",{style:{color:"rgba(0,0,0,.25)"},attrs:{slot:"prefix",type:"lock"},slot:"prefix"})],1)],1),a("a-row",{attrs:{gutter:16}},[a("a-col",{staticClass:"gutter-row",attrs:{span:16}},[a("a-form-model-item",{attrs:{prop:"code"}},[a("a-input",{attrs:{"allow-clear":"",size:"large",type:"text",autocomplete:"off",placeholder:"请输入验证码"},model:{value:t.form.captcha,callback:function(e){t.$set(t.form,"captcha",e)},expression:"form.captcha"}},[a("a-icon",{style:{color:"rgba(0,0,0,.25)"},attrs:{slot:"prefix",type:"security-scan"},slot:"prefix"})],1)],1)],1),a("a-col",{staticClass:"gutter-row",attrs:{span:8}},[a("img",{staticClass:"getCaptcha",attrs:{alt:"验证码",src:t.codeUrl},on:{click:t.getCode}})])],1),a("a-form-item",[a("a-button",{staticClass:"login-button",attrs:{size:"large",type:"primary",htmlType:"submit",loading:t.logining,disabled:t.logining},on:{click:t.handleSubmit}},[t._v("确定")])],1),a("a-form-item",[a("table",{staticClass:"account-table"},[a("thead",[a("tr",[a("th",{staticClass:"account-table"},[t._v("账号（老寇云）")]),a("th",{staticClass:"account-table"},[t._v("账号（阿里）")])])]),a("tbody",[a("tr",[a("td",{staticClass:"account-table"},[t._v("admin/admin123")]),a("td",{staticClass:"account-table"},[t._v("tenant/tenant123")])]),a("tr",[a("td",{staticClass:"account-table"},[t._v("test/test123")]),a("td",{staticClass:"account-table"},[t._v("-")])]),a("tr",[a("td",{staticClass:"account-table"},[t._v("laok5/test123")]),a("td",{staticClass:"account-table"},[t._v("-")])])])])]),a("div",{staticClass:"user-login-other"},[a("a",{staticClass:"sso",attrs:{href:t.ssoUri}},[t._v("单点登录")])])],1)],1)},o=[],r=a("5530"),s=(a("ac1f"),a("841c"),a("1276"),a("d3b7"),a("25f0"),a("5319"),a("5880")),i=a("ca002"),c=a("d2eb"),u=a("af80"),l=a("9816"),d={name:"Login",components:{},data:function(){return{publicKey:"",codeUrl:"",tenantOptions:[],form:{tenantId:"0",username:"",password:"",captcha:"",uuid:""},rules:{username:[{required:!0,message:"请输入账号",trigger:"blur"}],password:[{required:!0,message:"请输入密码",trigger:"blur"}],captcha:[{required:!0,message:"请输入验证码",trigger:"blur"}]},logining:!1,ssoUri:"",uri:""}},created:function(){this.checkLogin(),this.getSsoUri(),this.getPublicKey(),this.getTenant(),this.getTenantId()},mounted:function(){},methods:Object(r["a"])(Object(r["a"])({getTenantId:function(){var t=this;Object(u["d"])().then((function(e){t.form.tenantId=e.data}))},checkLogin:function(){var t=this,e=decodeURIComponent(window.location.search),a=!0;this.uri=a?"https://vue.laokou.org":window.location.protocol+"//127.0.0.1:"+window.location.port;var n=!0;if(e.length>0){e=e.substring(1);for(var o=e.split("?"),r=0;r<o.length;r++){var s=o[r].split("=");if("code"===s[0]){var i={auth_type:1,grant_type:"authorization_code",code:s[1],redirect_uri:this.uri};this.Login(i).then((function(){return t.loginSuccess()})).catch((function(){})).finally((function(){n=!1}))}}}n&&this.requestFailed()},getSsoUri:function(){var t=!0;this.ssoUri=t?"https://vue.laokou.org:1111/oauth2/authorize?client_id=95TxSsTPFA3tF12TBSMmUVK0da&client_secret=FpHwIfw4wY92dO&response_type=code&scope=password mail mobile&redirect_uri=https://vue.laokou.org":"http://127.0.0.1:5555/auth/oauth2/authorize?client_id=95TxSsTPFA3tF12TBSMmUVK0da&client_secret=FpHwIfw4wY92dO&response_type=code&scope=password mail mobile&redirect_uri="+this.uri},getPublicKey:function(){var t=this;Object(c["f"])().then((function(e){t.publicKey=e.data}))},getTenant:function(){var t=this;Object(c["g"])().then((function(e){t.tenantOptions=e.data}))},getUUID:function(){return"xxxxxxxx-xxxx-9xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g,16*Math.random()|0).toString(16)},getCode:function(){var t=this;this.form.uuid=this.getUUID(),Object(c["a"])(this.form.uuid).then((function(e){t.codeUrl=e.data}))}},Object(s["mapActions"])(["Login","Logout"])),{},{handleSubmit:function(){var t=this;this.logining=!0,this.$refs.form.validate((function(e){if(e){var a=new l["a"];a.setPublicKey(t.publicKey);var n=encodeURIComponent(a.encrypt(t.form.username)),o=encodeURIComponent(a.encrypt(t.form.password)),r=t.form.uuid,s=t.form.captcha,i=t.form.tenantId,c={username:n,password:o,captcha:s,uuid:r,grant_type:"password",tenant_id:i,auth_type:0};t.Login(c).then((function(){return t.loginSuccess()})).catch((function(){return t.requestFailed()})).finally((function(){t.logining=!1}))}else setTimeout((function(){t.logining=!1}),600)}))},loginSuccess:function(){var t=this;this.$router.push({path:"/"}),setTimeout((function(){t.$notification.success({message:"欢迎",description:"".concat(Object(i["a"])(),"，欢迎回来")})}),1e3)},requestFailed:function(){this.form.captcha="",this.getCode()}})},f=d,p=(a("c309"),a("2877")),m=Object(p["a"])(f,n,o,!1,null,"84f62d60",null);e["default"]=m.exports}}]);