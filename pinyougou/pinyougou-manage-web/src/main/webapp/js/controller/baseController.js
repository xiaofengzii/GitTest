app.controller("baseController",function ($scope,$http,brandService) {

    $scope.paginationConf={
        currentPage:1,
        totalItems:10,//总记录数
        itemsPerPage:10,//页大小
        perPageOptions:[10,20,30,40,50],//可选择的每页大小
        onChange:function () {//当参数发生变化后触发
            $scope.reloadList();
        }
    };

    $scope.reloadList=function () {
        $scope.search($scope.paginationConf.currentPage,
            $scope.paginationConf.itemsPerPage);
    };




    $scope.selectedIds = [];
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {
            $scope.selectedIds.push(id);
        } else {
            var index = $scope.selectedIds.indexOf(id);
            // 删除位置，删除个数
            $scope.selectedIds.splice(index, 1);
        }
    };

    $scope.searchEntity={};
    $scope.search = function (page,rows) {
        brandService.search($scope.searchEntity,page,rows).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });
    }

})
