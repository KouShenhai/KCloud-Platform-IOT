(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-147fb2ec"],{"17d1":function(t,e,n){"use strict";n.r(e);var o=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a-drawer",{attrs:{width:"35%","label-col":4,"wrapper-col":14,visible:t.open},on:{close:t.onClose}},[n("a-divider",{attrs:{orientation:"left"}},[n("b",[t._v(t._s(t.formTitle))])]),n("a-form-model",{ref:"form",attrs:{model:t.form,rules:t.rules}},[n("a-form-model-item",{attrs:{label:"租户名称",prop:"name"}},[n("a-input",{attrs:{placeholder:"请输入"},model:{value:t.form.name,callback:function(e){t.$set(t.form,"name",e)},expression:"form.name"}})],1),n("a-form-model-item",{attrs:{label:"租户套餐",prop:"packageId"}},[n("a-select",{attrs:{placeholder:"请选择"},model:{value:t.form.packageId,callback:function(e){t.$set(t.form,"packageId",e)},expression:"form.packageId"}},t._l(t.packageOption,(function(e,o){return n("a-select-option",{key:o,attrs:{value:e.value}},[t._v(" "+t._s(e.label)+" ")])})),1)],1),n("a-form-model-item",{attrs:{label:"租户数据源",prop:"sourceId"}},[n("a-select",{attrs:{placeholder:"请选择"},model:{value:t.form.sourceId,callback:function(e){t.$set(t.form,"sourceId",e)},expression:"form.sourceId"}},t._l(t.sourceOption,(function(e,o){return n("a-select-option",{key:o,attrs:{value:e.value}},[t._v(" "+t._s(e.label)+" ")])})),1)],1),n("div",{staticClass:"bottom-control"},[n("a-space",[n("a-button",{attrs:{type:"primary",loading:t.submitLoading},on:{click:t.submitForm}},[t._v(" 保存 ")]),n("a-button",{attrs:{type:"dashed"},on:{click:t.cancel}},[t._v(" 取消 ")])],1)],1)],1)],1)},r=[],a=(n("d3b7"),n("af80")),i=n("ae91"),u=n("9157"),c=n("5bd3"),s={name:"CreateForm",components:{},data:function(){return{accessToken:"",submitLoading:!1,formTitle:"",form:{id:void 0,name:void 0,packageId:void 0,sourceId:""},packageOption:[],sourceOption:[],open:!1,rules:{name:[{required:!0,message:"租户名称不能为空",trigger:"blur"}],packageId:[{required:!0,message:"请选择套餐",trigger:"blur"}],sourceId:[{required:!0,message:"请选择数据源",trigger:"blur"}]}}},filters:{},created:function(){this.getPackageOption(),this.getSourceOption()},computed:{},watch:{},methods:{token:function(){var t=this;Object(c["a"])().then((function(e){t.accessToken=e.data.token}))},getSourceOption:function(){var t=this;Object(i["e"])().then((function(e){t.sourceOption=e.data}))},getPackageOption:function(){var t=this;Object(u["e"])().then((function(e){t.packageOption=e.data}))},onClose:function(){this.open=!1},cancel:function(){this.open=!1,this.reset()},reset:function(){this.form={id:void 0,name:void 0,packageId:void 0,sourceId:""}},handleAdd:function(){this.reset(),this.token(),this.open=!0,this.formTitle="租户新增"},handleUpdate:function(t){var e=this;this.reset();var n=t.id;Object(a["c"])(n).then((function(t){e.form=t.data,e.open=!0,e.formTitle="租户修改"}))},submitForm:function(){var t=this;this.$refs.form.validate((function(e){if(!e)return!1;if(t.submitLoading=!0,void 0!==t.form.id){var n={tenantCO:t.form};Object(a["g"])(n).then((function(){t.$message.success("修改成功",3),t.open=!1,t.$emit("ok")})).finally((function(){t.submitLoading=!1}))}else{var o={tenantCO:t.form};Object(a["e"])(o,t.accessToken).then((function(){t.$message.success("新增成功",3),t.open=!1,t.$emit("ok")})).catch((function(){t.token()})).finally((function(){t.submitLoading=!1}))}}))}}},d=s,l=n("2877"),f=Object(l["a"])(d,o,r,!1,null,null,null);e["default"]=f.exports},"5bd3":function(t,e,n){"use strict";n.d(e,"a",(function(){return r}));var o=n("b775");function r(){return Object(o["b"])({url:"/admin/v1/tokens",method:"get"})}},9157:function(t,e,n){"use strict";n.d(e,"d",(function(){return r})),n.d(e,"b",(function(){return a})),n.d(e,"c",(function(){return i})),n.d(e,"f",(function(){return u})),n.d(e,"a",(function(){return c})),n.d(e,"e",(function(){return s}));var o=n("b775");function r(t){return Object(o["b"])({url:"/admin/v1/packages/list",method:"post",data:t})}function a(t){return Object(o["b"])({url:"/admin/v1/packages/"+t,method:"get"})}function i(t,e){return Object(o["b"])({url:"/admin/v1/packages",method:"post",data:t,headers:{"Content-Type":"application/json;charset=UTF-8","Request-Id":e}})}function u(t){return Object(o["b"])({url:"/admin/v1/packages",method:"put",data:t})}function c(t){return Object(o["b"])({url:"/admin/v1/packages/"+t,method:"delete"})}function s(){return Object(o["b"])({url:"/admin/v1/packages/option-list",method:"get"})}},ae91:function(t,e,n){"use strict";n.d(e,"d",(function(){return r})),n.d(e,"b",(function(){return a})),n.d(e,"c",(function(){return i})),n.d(e,"f",(function(){return u})),n.d(e,"a",(function(){return c})),n.d(e,"e",(function(){return s}));var o=n("b775");function r(t){return Object(o["b"])({url:"/admin/v1/sources/list",method:"post",data:t})}function a(t){return Object(o["b"])({url:"/admin/v1/sources/"+t,method:"get"})}function i(t,e){return Object(o["b"])({url:"/admin/v1/sources",method:"post",data:t,headers:{"Content-Type":"application/json;charset=UTF-8","Request-Id":e}})}function u(t){return Object(o["b"])({url:"/admin/v1/sources",method:"put",data:t})}function c(t){return Object(o["b"])({url:"/admin/v1/sources/"+t,method:"delete"})}function s(){return Object(o["b"])({url:"/admin/v1/sources/option-list",method:"get"})}},af80:function(t,e,n){"use strict";n.d(e,"f",(function(){return r})),n.d(e,"c",(function(){return a})),n.d(e,"e",(function(){return i})),n.d(e,"g",(function(){return u})),n.d(e,"a",(function(){return c})),n.d(e,"d",(function(){return s})),n.d(e,"b",(function(){return d}));var o=n("b775");function r(t){return Object(o["b"])({url:"/admin/v1/tenants/list",method:"post",data:t})}function a(t){return Object(o["b"])({url:"/admin/v1/tenants/"+t,method:"get"})}function i(t,e){return Object(o["b"])({url:"/admin/v1/tenants",method:"post",data:t,headers:{"Content-Type":"application/json;charset=UTF-8","Request-Id":e}})}function u(t){return Object(o["b"])({url:"/admin/v1/tenants",method:"put",data:t})}function c(t){return Object(o["b"])({url:"/admin/v1/tenants/"+t,method:"delete"})}function s(){return Object(o["b"])({url:"/admin/v1/tenants/id",method:"get"})}function d(t){return Object(o["b"])({url:"/admin/v1/tenants/"+t+"/download-datasource",method:"get",responseType:"blob"})}}}]);