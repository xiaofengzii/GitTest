app.controller("brandController",function ($scope,$controller,brandService) {

    //继承baseController
    $controller("baseController",{$scope:$scope});

    $scope.findAll = function () {
        brandService.findAll().success(function (response) {
            $scope.list =response;
        }).error(function () {
            alert("读取数据错误");
        });
    };

    $scope.findOne = function (id) {
        brandService.findOne(id).success(function (response) {
            $scope.entity = response;
        });
    }

    $scope.save = function () {
        var obj;
        if ($scope.entity.id!=null){
            obj = brandService.update($scope.entity);
        }else {
            obj = brandService.add($scope.entity);
        }

        obj.success(function (response) {
            if (response.success){
                $scope.reloadList();
            }else {
                alert(response.message);
            }
        });
    };



    $scope.delete=function () {
        if ($scope.selectedIds.length<1){
            alert("请选择要删除的品牌");
            return ;
        }
        if (confirm("确定要删除选中的品牌?")){
            brandService.delete($scope.selectedIds).success(function (response) {
                if (response.success){
                    $scope.reloadList();
                    $scope.selectedIds = [];
                }else {
                    alert(response.message);
                }
            })
        }
    }
})
