import{d as l,cu as u,cs as m,i as d,z as i,o as f,c as p,h as w,cv as k,$ as _}from"./index-BztLELzb.js";import{_ as v}from"./workflow.vue_vue_type_script_setup_true_lang-D1cRPPJl.js";import"./Grid-DDhjb_3_.js";import"./DescriptionsItem-gUlfoQM0.js";import"./index-B-_kZKhv.js";const T=l({name:"workflow_form_add",__name:"index",setup(S){const a=u(),t=m();d(()=>{a.clear(),a.setType(0)});const e=i({workflowName:`WF-${new Date().getTime()}`,workflowStatus:1,blockStrategy:1,description:void 0,executorTimeout:60}),r=async()=>{var o;const{error:s}=await k(e.value);s||((o=window.$message)==null||o.info(_("common.addSuccess")),t.push("/workflow/task"))},n=()=>{t.push("/workflow/task")};return(s,o)=>(f(),p(w(v),{modelValue:e.value,"onUpdate:modelValue":o[0]||(o[0]=c=>e.value=c),onSave:r,onCancel:n},null,8,["modelValue"]))}});export{T as default};
