app.service("brandService",function ($http) {
    this.findAll = function () {
        return  $http.get("../brand/findAll.do");
    };

    this.findOne = function (id) {
        return $http.post("../brand/findOne.do?id="+id);
    }

    this.add = function (entity) {
        return $http.post("../brand/add.do",entity);
    };
    this.update= function (entity) {
        return $http.post("../brand/update.do",entity);
    };

    this.delete=function (selectedIds) {
        return  $http.get("../brand/delete.do?ids="+selectedIds);
    };

    this.search = function (searchEntity,page,rows) {
        return  $http.post("../brand/search.do?page="+page+"&rows="+rows,searchEntity);
    };

});