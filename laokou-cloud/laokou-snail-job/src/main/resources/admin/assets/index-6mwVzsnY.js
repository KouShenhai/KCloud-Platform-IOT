import{d,cu as p,cw as w,cs as k,z as r,i as v,o as _,c as g,h as y,cy as S,cv as $,$ as h}from"./index-BztLELzb.js";import{_ as x}from"./workflow.vue_vue_type_script_setup_true_lang-D1cRPPJl.js";import"./Grid-DDhjb_3_.js";import"./DescriptionsItem-gUlfoQM0.js";import"./index-B-_kZKhv.js";const R=d({name:"workflow_form_copy",__name:"index",setup(T){const s=p(),c=w(),n=k(),a=r(!1),l=String(c.query.id),e=r({workflowName:`Workflow ${new Date().getTime()}`,workflowStatus:1,blockStrategy:1,description:void 0,executorTimeout:60}),u=async()=>{a.value=!0;const{data:t,error:o}=await S(l);o||(e.value=t),a.value=!1};v(()=>{s.clear(),s.setType(0),u()});const i=async()=>{var o;const{error:t}=await $(e.value);t||((o=window.$message)==null||o.info(h("common.addSuccess")),n.push("/workflow/task"))},f=()=>{n.push("/workflow/task")};return(t,o)=>(_(),g(y(x),{modelValue:e.value,"onUpdate:modelValue":o[0]||(o[0]=m=>e.value=m),spinning:a.value,onSave:i,onCancel:f},null,8,["modelValue","spinning"]))}});export{R as default};
