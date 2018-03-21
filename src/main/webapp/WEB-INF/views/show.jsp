<%--
  Created by IntelliJ IDEA.
  User: lucky
  Date: 2018/3/21
  Time: 13:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.js"></script>
    <script>
        $(function () {
            $("#upload").click(function () {
                $("#imgWait").show();
                var formData = new FormData();
                formData.append("file", document.getElementById("file1").files[0]);
                $.ajax({
                    url: "http://localhost:8080/Xuehai/user/upload",
                    type: "POST",
                    data: formData,
                    /**
                     *必须false才会自动加上正确的Content-Type
                     */
                    contentType: false,
                    /**
                     * 必须false才会避开jQuery对 formdata 的默认处理
                     * XMLHttpRequest会对 formdata 进行正确的处理
                     */
                    processData: false,
                    success: function (data) {
                        $("#img").attr("src","http://localhost:8080/Xuehai/" + data);
                    },
                    error: function () {
                        alert("上传失败！");
                        $("#imgWait").hide();
                    }
                });
            });
        });
    </script>
</head>

<body>
选择文件:<input type="file" id="file1" /><br />
<input type="button" id="upload" value="上传" />
<img id="img"/>
</body>
</html>
