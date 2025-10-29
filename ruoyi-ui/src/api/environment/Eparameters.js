import request from '@/utils/request'

// 查询环境参数列表
export function listEparameters(query) {
  return request({
    url: '/environment/Eparameters/list',
    method: 'get',
    params: query
  })
}

// 查询环境参数详细
export function getEparameters(globalEnvTemp) {
  return request({
    url: '/environment/Eparameters/' + globalEnvTemp,
    method: 'get'
  })
}

// 新增环境参数
export function addEparameters(data) {
  return request({
    url: '/environment/Eparameters',
    method: 'post',
    data: data
  })
}

// 修改环境参数
export function updateEparameters(data) {
  return request({
    url: '/environment/Eparameters',
    method: 'put',
    data: data
  })
}

// 删除环境参数
export function delEparameters(globalEnvTemp) {
  return request({
    url: '/environment/Eparameters/' + globalEnvTemp,
    method: 'delete'
  })
}
