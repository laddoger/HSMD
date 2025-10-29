import request from '@/utils/request'

// 查询全局参数列表
export function listGparameters(query) {
  return request({
    url: '/environment/Gparameters/list',
    method: 'get',
    params: query
  })
}

// 查询全局参数详细
export function getGparameters(id) {
  return request({
    url: '/environment/Gparameters/' + id,
    method: 'get'
  })
}

// 新增全局参数
export function addGparameters(data) {
  return request({
    url: '/environment/Gparameters',
    method: 'post',
    data: data
  })
}

// 修改全局参数
export function updateGparameters(data) {
  return request({
    url: '/environment/Gparameters',
    method: 'put',
    data: data
  })
}

// 删除全局参数
export function delGparameters(id) {
  return request({
    url: '/environment/Gparameters/' + id,
    method: 'delete'
  })
}
