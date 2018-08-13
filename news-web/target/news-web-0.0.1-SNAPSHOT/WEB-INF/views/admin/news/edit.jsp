<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglib.jsp"%>
<c:url var="formUrl" value="/ajax/news"/>
<html>
<head>
    <title>Chỉnh sửa bài viết</title>
</head>
<body>
<div class="main-content">
    <div class="main-content-inner">
        <div class="breadcrumbs" id="breadcrumbs">
            <script type="text/javascript">
                try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
            </script>
            <ul class="breadcrumb">
                <li>
                    <i class="ace-icon fa fa-home home-icon"></i>
                    <a href="#">Trang chủ</a>
                </li>
                <li class="active">Chỉnh sửa bài viết</li>
            </ul><!-- /.breadcrumb -->
        </div>
        <div class="page-content">
            <div class="row">
                <div class="col-xs-12">
                    <c:if test="${not empty messageResponse}">
                        <div class="alert alert-block alert-${alert}">
                            <button type="button" class="close" data-dismiss="alert">
                                <i class="ace-icon fa fa-times"></i>
                            </button>
                                ${messageResponse}
                        </div>
                    </c:if>
                    <form:form id="formEdit" commandName="model" >
                    	<div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right">Loại bài viết</label>
                            <div class="col-sm-9">
                                <form:select path="categoryCode" id="category">
                                	<form:option value="NONE" label="--- Select ---"/>
    								<form:options items="${model.categories}" />
								</form:select>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right">Tiêu đề bài viết</label>
                            <div class="col-sm-9">
                                <form:input path="title" id="title" name="title"/>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right">Thumbnail bài viết:</label>
                            <div class="col-sm-9">
                                <input type="file" name="file" id="uploadImage"/>
                            </div>
                        </div>
                        <br/>
                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right">Xem trước</label>
                            <div class="col-sm-9">
                                <c:if test="${not empty model.thumbnail}">
                                    <c:set var="image" value="/repository/${model.thumbnail}"/>
                                    <img src="<c:url value='${image}'/>" id="viewImage" width="150px" height="150px">
                                </c:if>
                                <c:if test="${empty model.thumbnail}">
                                    <img src="<c:url value='/image/no-image.png'/>" id="viewImage" width="150px" height="150px">
                                </c:if>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right">Nội dung bài viết</label>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-12">
                                <form:textarea path="description" name="description" cols="80" rows="10" id="newsDescription"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-12">
                            	<c:if test="${not empty model.id}">
                        			<input type="button" class="btn btn-white btn-warning btn-bold" value="Cập nhật bài viết" id="btnAddOrUpdateNews"/>
                        		</c:if>
                        		<c:if test="${empty model.id}">
                        			<input type="button" class="btn btn-white btn-warning btn-bold" value="Thêm mới bài viết" id="btnAddOrUpdateNews"/>
                        		</c:if>
                            </div>
                        </div>
                        <form:hidden path="id" id="newsId"/>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
	var editor = '';
	$(document).ready(function () {
	    editor = CKEDITOR.replace( 'newsDescription' );
	    CKFinder.setupCKEditor( editor, '${pageContext.request.contextPath}/ckfinder/' );
	    $('#uploadImage').change(function () {
            readURL(this, "viewImage");
        });
	});
	function readURL(input, imageId) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                $('#' +imageId).attr('src', reader.result);
            }
            reader.readAsDataURL(input.files[0]);
        }
    }
	$("#btnAddOrUpdateNews").click(function (event) {     
		event.preventDefault();
        var dataArray = {};
        dataArray["categoryCode"] = $('#category').val();
        dataArray["title"] = $('#title').val();
        dataArray["description"] = editor.getData();
        var files = $('#uploadImage')[0].files[0];
        if (files == undefined) {
            dataArray["imageName"] = "";
            if ($('#newsId').val() != "") {
                updateNews(dataArray, $('#newsId').val());
            }
        } else {
            dataArray["imageName"] = files.name;
            var reader = new FileReader();
            reader.onload = function (e) {
                dataArray["thumbnailBase64"] = e.target.result;
                if ($('#newsId').val() == "") {
                    addNews(dataArray);   
                } else {
                    updateNews(dataArray, $('#newsId').val());
                }
            };
            reader.readAsDataURL(files);
        }
    });
    function addNews(data) {
        $.ajax({
            url: '${formUrl}',
            type: 'POST',
            dataType: 'json',
            contentType:'application/json',
            data: JSON.stringify(data),
            success: function(res) {
                window.location.href = "<c:url value='/admin/news/"+res.id+"'/>";
            },
            error: function(res) {
                console.log(res);                	
            }
        });
    }
    function updateNews(data, id) {
        $.ajax({
            url: '${formUrl}/'+id,
            type: 'PUT',
            dataType: 'json',
            contentType:'application/json',
            data: JSON.stringify(data),
            success: function(res) {
                window.location.href = "<c:url value='/admin/news/"+res.id+"'/>";
            },
            error: function(res) {
                console.log(res);                	
            }
        });
    }
</script>
</body>
</html>
