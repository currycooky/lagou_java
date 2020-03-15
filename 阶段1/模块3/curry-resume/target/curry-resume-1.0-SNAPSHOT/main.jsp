<%--
  Created by IntelliJ IDEA.
  User: curry
  Date: 2020/3/11
  Time: 18:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>首页</title>
    <link rel="stylesheet" href="layui.css">
</head>
<body>
<div class="layui-row layui-col-space10">
    <div class="layui-col-md6">
        <div class="layui-btn-group">
            <button type="button" class="layui-btn" onclick="loadData()">刷新</button>
            <button type="button" class="layui-btn" onclick="add()">新增</button>
        </div>
        <table class="layui-table">
            <thead>
            <tr>
                <th>姓名</th>
                <th>地址</th>
                <th>手机号</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${list}" var="item">
                <tr>
                    <th>${item.name}</th>
                    <th>${item.address}</th>
                    <th>${item.phone}</th>
                    <th>
                        <button class="layui-btn layui-btn-xs" onclick="edit(${item})">编辑</button>
                        <button class="layui-btn layui-btn-xs" onclick="del(${item.id})">删除</button>
                    </th>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="layui-col-md6">
        <p style="color: #2F4056;">新增或编辑</p>
        <form class="layui-form layui-form-pane" action="/resume/" method="post" id="addOrUpdateForm">
            <input id="id" type="hidden" value="">
            <div class="layui-form-item" pane>
                <label class="layui-form-label">姓名</label>
                <div class="layui-input-block">
                    <input type="text" id="name" name="name" required  lay-verify="required" placeholder="请输入姓名" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item" pane>
                <label class="layui-form-label">地址</label>
                <div class="layui-input-block">
                    <input type="text" id="address" name="address" required  lay-verify="required" placeholder="请输入地址" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item" pane>
                <label class="layui-form-label">手机号</label>
                <div class="layui-input-block">
                    <input type="text" id="phone" name="phone" required  lay-verify="required" placeholder="请输入手机号" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button>
                    <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                </div>
            </div>
        </form>
    </div>
</div>

</body>
<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"/>
<script>
    /**
     * 加载数据
     */
    function loadData() {
        alert('load...');
        $.get('/resume/');
    }

    function add() {
        $("#id").val("");
        $("#addOrUpdateForm").reset();
    }

    /**
     * 修改数据
     *
     * @param item
     */
    function edit(item) {
        $("#id").val(item.id);
        $("#name").val(item.name);
        $("#address").val(item.address);
        $("#phone").val(item.phone);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    function del(id) {
        $.ajax({
            url: '/resume/' + id,
            method: 'delete'
        });
    }
</script>
</html>
