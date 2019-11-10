    <%--
      Created by IntelliJ IDEA.
      User: 28919
      Date: 2019/11/9
      Time: 15:13
      To change this template use File | Settings | File Templates.
    --%>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core_1_1" %>
    <html>
    <head>
        <meta charset="UTF-8">
        <title>学生列表</title>
        <link rel="stylesheet" type="text/css" href="/easyui/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="/easyui/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="/easyui/css/demo.css">
        <script type="text/javascript" src="/easyui/jquery.min.js"></script>
        <script type="text/javascript" src="/easyui/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="/easyui/js/validateExtends.js"></script>
        <script type="text/javascript">
            $(function() {
                var clazzList = []
                $.ajax({
                    type: "get",
                    url: "names",
                    dataType:'json',
                    success: function(data){
                        if(data.type == "success"){
                            clazzList =  data.clazzList
                        }
                    }
                });
                console.log(clazzList);
                var table;
                //datagrid初始化
                $('#dataList').datagrid({
                    title:'学生列表',
                    iconCls:'icon-more',//图标
                    border: true,
                    collapsible:false,//是否可折叠的
                    fit: true,//自动大小
                    method: "post",
                    url:"get_list?t="+new Date().getTime(),
                    idField:'id',
                    singleSelect:false,//是否单选
                    pagination:true,//分页控件
                    rownumbers:true,//行号
                    sortName:'id',
                    sortOrder:'DESC',
                    remoteSort: false,
                    columns: [[
                        {field:'chk',checkbox: true,width:50},
                        {field:'id',title:'ID',width:50, sortable: true},
                        {field:'photo',title:'头像',width:100,
                            formatter:function(value,index,row){
                                return '<img src='+value+' width="100px" height="100px" />';
                            }
                        },
                        {field:'sn',title:'学号',width:150, sortable: true},
                        {field:'username',title:'姓名',width:150, sortable: true},
                        {field:'sex',title:'性别',width:150, sortable: true},
                        {field:'remark',title:'备注',width:150, sortable: true},
                        {field:'clazzId',title:'所属班级',width:150, sortable: true,
                            formatter:function(value,index,row){
                                console.log(clazzList);
                                for(var i=0;i<clazzList.length;i++){
                                    if(clazzList[i].id == value){
                                        return clazzList[i].name;
                                    }
                                }
                                return value;
                            }
                        },
                        {field:'remark',title:'备注',width:300},
                    ]],
                    toolbar: "#toolbar"
                });
                //设置分页控件
                var p = $('#dataList').datagrid('getPager');
                $(p).pagination({
                    pageSize: 10,//每页显示的记录条数，默认为10
                    pageList: [10,20,30,50,100],//可以设置每页记录条数的列表
                    beforePageText: '第',//页数文本框前显示的汉字
                    afterPageText: '页    共 {pages} 页',
                    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
                });
                //设置工具类按钮
                $("#add").click(function(){
                    table = $("#addTable");
                    $("#addDialog").dialog("open");
                });
                //修改
                $("#edit").click(function(){
                    table = $("#editTable");
                    var selectRows = $("#dataList").datagrid("getSelections");
                    if(selectRows.length != 1){
                        $.messager.alert("消息提醒", "请选择一条数据进行操作!", "warning");
                    } else{
                        $("#editDialog").dialog("open");
                    }
                });
                //删除
                $("#delete").click(function(){
                    var selectRows = $("#dataList").datagrid("getSelections");
                    var selectLength = selectRows.length;
                    if(selectLength == 0){
                        $.messager.alert("消息提醒", "请选择数据进行删除!", "warning");
                    } else{
                        var ids = [];
                        $(selectRows).each(function(i, row){
                            ids[i] = row.id;
                        });
                        $.messager.confirm("消息提醒", "是否确认删除？", function(r){
                            if(r){
                                $.ajax({
                                    type: "post",
                                    url: "delete",
                                    data: {ids: ids},
                                    dataType:'json',
                                    success: function(data){
                                        if(data.type == "success"){
                                            $.messager.alert("消息提醒","删除成功!","info");
                                            //刷新表格
                                            $("#dataList").datagrid("reload");
                                            $("#dataList").datagrid("uncheckAll");
                                        } else{
                                            $.messager.alert("消息提醒",data.msg,"warning");
                                            return;
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
    
                //设置添加窗口
                $("#addDialog").dialog({
                    title: "添加学生",
                    width: 450,
                    height: 400,
                    iconCls: "icon-add",
                    modal: true,
                    collapsible: false,
                    minimizable: false,
                    maximizable: false,
                    draggable: true,
                    closed: true,
                    buttons: [
                        {
                            text:'添加',
                            plain: true,
                            iconCls:'icon-user_add',
                            handler:function(){
                                var validate = $("#addForm").form("validate");
                                if(!validate){
                                    $.messager.alert("消息提醒","请检查你输入的数据!","warning");
                                    return;
                                } else{
                                    var data = $("#addForm").serialize();
                                    $.ajax({
                                        type: "post",
                                        url: "add",
                                        data: data,
                                        dataType:'json',
                                        success: function(data){
                                            if(data.type == "success"){
                                                $.messager.alert("消息提醒","添加成功!","info");
                                                console.log(data.src);
                                                //关闭窗口
                                                $("#addDialog").dialog("close");
                                                //清空原表格数据
                                                $("#add_username").textbox('setValue', "");
                                                $("#add_password").textbox('setValue', "");
                                                //重新刷新页面数据
                                                $('#dataList').datagrid("reload");
    
                                            } else{
                                                $.messager.alert("消息提醒",data.msg,"warning");
                                                return;
                                            }
                                        }
                                    });
                                }
                            }
                        },
                    ],
                    onClose: function(){
                        $("#add_name").textbox('setValue', "");
                        $("#add_remark").textbox('setValue', "");
                    }
                });
    
                //编辑学生信息
                //编辑学生信息
                $("#editDialog").dialog({
                    title: "修改学生信息",
                    width: 450,
                    height: 650,
                    iconCls: "icon-edit",
                    modal: true,
                    collapsible: false,
                    minimizable: false,
                    maximizable: false,
                    draggable: true,
                    closed: true,
                    buttons: [
                        {
                            text:'提交',
                            plain: true,
                            iconCls:'icon-edit',
                            handler:function(){
                                var validate = $("#editForm").form("validate");
                                if(!validate){
                                    $.messager.alert("消息提醒","请检查你输入的数据!","warning");
                                    return;
                                } else{

                                    var data = $("#editForm").serialize();

                                    $.ajax({
                                        type: "post",
                                        url: "edit",
                                        data: data,
                                        dataType:'json',
                                        success: function(data){
                                            if(data.type == "success"){
                                                $.messager.alert("消息提醒","修改成功!","info");
                                                //关闭窗口
                                                $("#editDialog").dialog("close");

                                                //重新刷新页面数据
                                                $('#dataList').datagrid("reload");
                                                $('#dataList').datagrid("uncheckAll");

                                            } else{
                                                $.messager.alert("消息提醒",data.msg,"warning");
                                                return;
                                            }
                                        }
                                    });
                                }
                            }
                        },
                    ],
                    onBeforeOpen: function(){
                        var selectRow = $("#dataList").datagrid("getSelected");
                        //设置值
                        $("#edit-id").val(selectRow.id);
                        $("#edit_username").textbox('setValue', selectRow.username);
                        $("#edit_sn").textbox('setValue', selectRow.sn);
                        $("#edit_clazzId").combobox('setValue', selectRow.clazzId);
                        $("#edit_sex").combobox('setValue', selectRow.sex);
                        $("#edit_password").textbox('setValue', selectRow.password);
                        $("#edit_remark").textbox('setValue', selectRow.remark);
                        $("#edit-photo-preview").attr("src",selectRow.photo);
                        $("#edit_photo").val(selectRow.photo);
                    }
                });
                //上传图片按钮
                $("#upload-btn").click(function(){
                    if($("#add-upload-photo").filebox("getValue") == ''){
                        $.messager.alert("消息提醒","请选择图片文件!","warning");
                        return;
                    }
                    $("#photoForm").submit();
                });
                $("#edit-upload-btn").click(function(){
                    if($("#edit-upload-photo").filebox("getValue") == ''){
                        $.messager.alert("消息提醒","请选择图片文件!","warning");
                        return;
                    }
                    $("#editPhotoForm").submit();
                });
                //搜索按钮
                $("#search-btn").click(function(){
                    $('#dataList').datagrid('reload',{
                        username:$("#search-name").textbox('getValue'),
                        clazzId:$("#search-grade-id").combobox('getValue')
                    });
                });

            });
            function uploaded(e){
                var data = $(window.frames["photo_target"].document).find("body pre").text();
                if(data == '')return;
                data = JSON.parse(data);
                if(data.type == "success"){
                    $.messager.alert("消息提醒","图片上传成功!","info");
                    console.log(data.src);
                    // $("#photo-preview").attr("src","xxxx.jpg");
                    $("#photo-preview").attr("src",data.src);
                    $("#edit-photo-preview").attr("src",data.src);
                    $("#add_photo").val(data.src);
                    $("#edit_photo").val(data.src);
                }else{
                    $.messager.alert("消息提醒",data.msg,"warning");
                }
            }
        </script>
    </head>
    <body>
    <!-- 数据列表 -->
    <table id="dataList" cellspacing="0" cellpadding="0">
    
    </table>
    <!-- 工具栏 -->
    <div id="toolbar">
        <c:if test="${userType == 1}">
            <div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a></div>
            <div style="float: left;" class="datagrid-btn-separator"></div>
        </c:if>
            <div style="float: left;"><a id="edit" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a></div>
        <c:if test="${userType == 1}">
            <div style="float: left;" class="datagrid-btn-separator"></div>
        </c:if>
    <c:if test="${userType == 1}">
        <div>
            <c:if test="${userType == 1}">
                <a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">删除</a>
            </c:if>
            学生名：<input id="search-name" class="easyui-textbox" />
            所属班级：
            <select id="search-grade-id" class="easyui-combobox" style="width: 150px;">
                <option value="">全部</option>
                <c:forEach items="${ clazzList}" var="clazz">
                    <option value="${clazz.id }">${clazz.name }</option>
                </c:forEach>
            </select>
            <a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
        </div>
    </c:if>
    </div>
    
    <!-- 添加窗口 -->
    <div id="addDialog" style="padding: 10px;">
        <form id="photoForm" method="post" enctype="multipart/form-data" action="upload_photo" target="photo_target">
            <table id="addTable1" cellpadding="8">
                <tr >
                    <td>预览头像:</td>
                    <td>
                        <img id="photo-preview" alt="照片" style="max-width: 100px; max-height: 100px;" title="照片" src="/photo/student.jpg" />
                    </td>
                </tr>
                <tr >
                    <td>学生头像:</td>
                    <td>
                        <input id="add-upload-photo" class="easyui-filebox" name="photo" data-options="prompt:'选择照片'" style="width:200px;">
                        <a id="upload-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">上传图片</a>
                    </td>
                </tr>
            </table>
        </form>
        <form id="addForm" method="post">
            <table id="addTable" cellpadding="8">
                <tr >
                    <td>学生名:</td>
                    <td>
                        <input type="hidden" value="/photo/student.jpg" id="add_photo" name="photo"/>
                        <input id="add_name"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="username" data-options="required:true, missingMessage:'请填写学生名'"  />
                    </td>
                </tr>
                <tr >
                    <td>密码:</td>
                    <td>
                        <input id="add_pass"  class="easyui-textbox" style="width: 200px; height: 30px;" type="password" name="password" data-options="required:true, missingMessage:'请填登录密码'"  />
                    </td>
                </tr>
                <tr >
                    <td>性别:</td>
                    <td>
                        <select id="add_sex" class="easyui-combobox" style="width: 150px;" name="sex">
                            <option value="男">男</option>
                            <option value="女">女</option>
                        </select>
                    </td>
                </tr>
                <tr >
                    <td>所属班级:</td>
                    <td>
                        <select id="add_clazzId" class="easyui-combobox" style="width: 150px;" name="clazzId">
<%--                            <option value="">全部</option>--%>
                            <c:forEach items="${ clazzList}" var="clazz">
                                <option value="${clazz.id }">${clazz.name }</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>备注:</td>
                    <td><input id="add_remark" style="width: 256px; height: 180px;" class="easyui-textbox" type="text" name="remark" data-options="multiline:true"  /></td>
                </tr>
            </table>
        </form>
    </div>
    
    <!-- 修改窗口 -->
    <div id="editDialog" style="padding: 10px">
        <form id="editPhotoForm" method="post" enctype="multipart/form-data" action="upload_photo" target="photo_target">
            <table id="editTable1" cellpadding="8">
                <tr >
                    <td>预览头像:</td>
                    <td>
                        <img id="edit-photo-preview" alt="照片" style="max-width: 100px; max-height: 100px;" title="照片" src="/photo/student.jpg" />
                    </td>
                </tr>
                <tr >
                    <td>学生头像:</td>
                    <td>
                        <input id="edit-upload-photo" class="easyui-filebox" name="photo" data-options="prompt:'选择照片'" style="width:200px;">
                        <a id="edit-upload-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">上传图片</a>
                    </td>
                </tr>
            </table>
        </form>
        <form id="editForm" method="post">
            <input type="hidden" name="id" id="edit-id">
            <table id="editTable2" cellpadding="8">
                <input id="edit_photo" type="hidden" name="photo" value="/photo/student.jpg"  />
                <input type="hidden" name="sn" />
                <tr >
                    <td>姓名:</td>
                    <td>
                        <input id="edit_username"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="username" data-options="required:true, missingMessage:'请填写学生姓名'"  />
                    </td>
                </tr>
                <tr >
                    <td>学号:</td>
                    <td>
                        <input id="edit_sn"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="sn"  readonly/>
                    </td>
                </tr>
                <tr >
                    <td>登录密码:</td>
                    <td>
                        <input id="edit_password"  class="easyui-textbox" style="width: 200px; height: 30px;" type="password" name="password" data-options="required:true, missingMessage:'请填写登录密码'"  />
                    </td>
                </tr>
                <tr >
                    <td>所属班级:</td>
                    <td>
                        <select id="edit_clazzId"  class="easyui-combobox" style="width: 200px;" name="clazzId" data-options="required:true, missingMessage:'请选择所属班级'">
                            <c:forEach items="${ clazzList}" var="clazz">
                                <option value="${clazz.id }">${clazz.name }</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr >
                    <td>性别:</td>
                    <td>
                        <select id="edit_sex"  class="easyui-combobox" style="width: 200px;" name="sex" data-options="required:true, missingMessage:'请选择学生性别'">
                            <option value="男">男</option>
                            <option value="女">女</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>备注:</td>
                    <td><input id="edit_remark" style="width: 256px; height: 180px;" class="easyui-textbox" type="text" name="remark" data-options="multiline:true"  /></td>
                </tr>
            </table>
        </form>
    </div>
    <!-- 提交表单处理iframe框架 -->
    <iframe id="photo_target" name="photo_target" onload="uploaded(this)"></iframe>
    </body>
    </html>
